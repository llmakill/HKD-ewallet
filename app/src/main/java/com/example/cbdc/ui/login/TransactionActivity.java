package com.example.cbdc.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbdc.CBDCWallet;
import com.example.cbdc.R;
import com.example.cbdc.Web3jUtils;
import com.example.cbdc.dao.Userinfo;
import com.example.cbdc.model.TokenModel;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class TransactionActivity extends Activity
{
    public static String TAG = ExchangeActivity.class.getSimpleName();

    private static String test_link="https://rinkeby.infura.io/v3/3d11b65a993f4ccb88405ed1c5f8a4ef";

    String bank_pwd ="liii245pp";//银行的口令
    String bank_privateKey="e0089dc37bbfe9c2a0dc107a09adb0df0ef6e25c5d092ab77d0493b7c9d42907";//银行私钥

    Token HKDContract;//载入的合约

    String transactionID;
    String from;
    String to;
    String transactionTime;
    String amount;
    String user_address;

    CBDCWallet Bank_Wallet;

    Web3j CBDCWeb3j;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        if (CBDCWeb3j == null) {
            CBDCWeb3j = Web3jUtils.initWeb3j(this, test_link);
            Toast.makeText(TransactionActivity.this, "Web3j加载完成！", Toast.LENGTH_SHORT).show();
        }


        final TextView transaction_text = findViewById(R.id.transaction_text);

        final TextView payee = findViewById(R.id.payee);
        final EditText shurupayee = findViewById(R.id.shurupayee);

        final TextView payment_num = findViewById(R.id.payment_num);
        final EditText shurupayment_num = findViewById(R.id.shurupayment_num);

        final TextView payment_password = findViewById(R.id.payment_password);
        final EditText shurupayment_password = findViewById(R.id.shurupayment_password);

        final Button btn_transfer = findViewById(R.id.btn_transfer);
        btn_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (Bank_Wallet == null) {
                    Toast.makeText(TransactionActivity.this, "钱包为null", Toast.LENGTH_SHORT).show();
                    return;
                }


                user_address = shurupayee.getText().toString();
                amount = shurupayment_num.getText().toString();
                String input_pwd = shurupayment_password.getText().toString();
                if (TextUtils.isEmpty(amount))
                {
                    Toast.makeText(TransactionActivity.this, "金额不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(input_pwd)) {
                    Toast.makeText(TransactionActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }


                sendTransaction(user_address, Double.valueOf(amount) * 1e18);
                Toast.makeText(TransactionActivity.this, "点击生效", Toast.LENGTH_SHORT).show();
            }
        });

        final ImageButton back = findViewById(R.id.huiqu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        import_bank();//导入银行的钱包
        load_contract();//导入合约
    }

    private static Single<Token> loadContract(CBDCWallet wallet, String addr, BigInteger gasPrice, BigInteger gasLimit) {
        Credentials credentials = Credentials.create(wallet.getKeyPair());
        Web3j web3j = Web3jFactory.build(new HttpService(test_link, createOkHttpClient(), false));
        return Single.fromCallable(() -> Token.load(addr, web3j, credentials, gasPrice, gasLimit))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void load_contract(){
        TokenModel token = new TokenModel("0x4f4c1eec1e864fb466513b273c1fc37de8e9fc40", "HKD", false);
        loadContract(Bank_Wallet, token.getAddr(), getGasPrice(), getGasLimit())
                .subscribe(new SingleObserver<Token>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Token token) {
                        //合约加载成功
                        Toast.makeText(TransactionActivity.this, "合约加载完成！", Toast.LENGTH_SHORT).show();
                        HKDContract = token;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TransactionActivity.this, "合约加载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //导入银行钱包
    private void import_bank(){
        try {
            Bank_Wallet = generateBip44WalletByPrivateKey(bank_privateKey, bank_pwd);
            Toast.makeText(this, "导入成功", Toast.LENGTH_SHORT).show();
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CBDCWallet generateBip44WalletByPrivateKey(String privateKey, String password)
            throws CipherException, IOException {
        BigInteger pk = Numeric.toBigIntNoPrefix(privateKey);
        byte[] privateKeyByte = pk.toByteArray();
        ECKeyPair keyPair = ECKeyPair.create(privateKeyByte);
        WalletFile walletFile = Wallet.createLight(password, keyPair);
        Log.i("TAG", "generateBip44Wallet: 地址 = " + Keys.toChecksumAddress(walletFile.getAddress()));
        return new CBDCWallet(walletFile, null,keyPair);
    }

    private BigInteger getGasPrice() {
        return new BigInteger("100000000000");
    }

    private BigInteger getGasLimit() {
        return BigInteger.valueOf(600000);
    }

    //发送交易请求
    private void sendTransaction(String toAddress, double value){
        if (HKDContract == null) {
            Toast.makeText(this, "智能合约未部署", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, "transferByContract:转账金额为 " + value);

        Single.fromCallable(()->HKDContract.transfer(toAddress, BigDecimal.valueOf(value).toBigInteger())
                .send())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TransactionReceipt>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onSuccess(TransactionReceipt transactionReceipt) {
                        Toast.makeText(TransactionActivity.this, "转账成功！", Toast.LENGTH_SHORT).show();
                        Intent transactionDetail = new Intent(TransactionActivity.this, TransactionDetailActivity.class);
                        transactionID = transactionReceipt.getTransactionHash();
                        transactionTime = transactionReceipt.getBlockHash();
                        from = transactionReceipt.getFrom();
                        to = toAddress;

                        transactionDetail.putExtra("transactionID", transactionID);
                        transactionDetail.putExtra("transactionTime", transactionTime);
                        transactionDetail.putExtra("from", from);
                        transactionDetail.putExtra("to", to);
                        transactionDetail.putExtra("price", amount);

                        startActivity(transactionDetail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TransactionActivity.this, "转账失败！", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.writeTimeout(30 * 1000, TimeUnit.SECONDS)
                .connectTimeout(30 * 1000, TimeUnit.SECONDS);
        configureLogging(builder);
        return builder.build();
    }

    private static void configureLogging(OkHttpClient.Builder builder) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String msg) {
                        Log.i("TAG", "log: " + msg);
                    }
                });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
    }
}

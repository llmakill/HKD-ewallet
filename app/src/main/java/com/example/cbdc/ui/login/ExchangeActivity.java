package com.example.cbdc.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cbdc.CBDCWallet;
import com.example.cbdc.R;
import com.example.cbdc.WalletUtils;
import com.example.cbdc.Web3jUtils;
import com.example.cbdc.model.TokenModel;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ExchangeActivity extends Activity implements View.OnClickListener
{
    public static String TAG = ExchangeActivity.class.getSimpleName();

    private static String test_link="https://rinkeby.infura.io/v3/3d11b65a993f4ccb88405ed1c5f8a4ef";
    //Excel传入的数据
    Double bank_account_balance=1000.0;//银行卡余额
    String user_address="0xa6433EA30698CB359640be508ce00A1aCD56D812";//用户的钱包地址
    String bank_pwd ="liii245pp";//银行的口令
    String bank_privateKey="5cdd4cbe170a2ac48733bdb7f7b8243661501e967eff079edd55e92834ddfc10";//银行私钥
    Double cur_balance=0.0;//当前的数字货币余额

    Token HKDContract;//载入的合约

    EditText exchange_amount;//兑换的数量
    EditText input_bank_pwd;//用户输入的银行密码

    CBDCWallet Bank_Wallet;
    String amount;

    Web3j CBDCWeb3j;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        if (CBDCWeb3j == null) {
            CBDCWeb3j = Web3jUtils.initWeb3j(this, test_link);
            Toast.makeText(ExchangeActivity.this, "Web3j加载完成！", Toast.LENGTH_SHORT).show();
        }

        final TextView payment_num = (TextView) findViewById(R.id.payment_num);
        exchange_amount = (EditText) findViewById(R.id.shurupayment_num);

        final TextView payment_password2 = (TextView) findViewById(R.id.payment_password2);
        input_bank_pwd = (EditText) findViewById(R.id.shurupayment_password2);

        Button exchange = (Button) findViewById(R.id.btn_duihuan);
        exchange.setOnClickListener(this);

        ImageButton outButton = (ImageButton) findViewById(R.id.huiqu2);
        outButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExchangeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        import_bank();//导入银行的钱包
        load_contract();//导入合约
    }

    //加载合约
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
                        Toast.makeText(ExchangeActivity.this, "合约加载完成！", Toast.LENGTH_SHORT).show();
                        HKDContract = token;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ExchangeActivity.this, "合约加载失败！", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v){
        if (Bank_Wallet == null) {
            Toast.makeText(this, "钱包为null", Toast.LENGTH_SHORT).show();
            return;
        }

        amount = exchange_amount.getText().toString();
        String input_pwd = input_bank_pwd.getText().toString();
        if (TextUtils.isEmpty(amount))
        {
            Toast.makeText(this, "金额不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(input_pwd)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Double.valueOf(amount) > bank_account_balance){
            Toast.makeText(this, "余额不足，兑换失败", Toast.LENGTH_SHORT).show();
            Intent i= new Intent(ExchangeActivity.this, ExchangeFail.class);//跳到兑换失败的页面
            startActivity(i);
            return;
        }
        sendTransaction(user_address, Double.valueOf(amount) * 1e18);
        Toast.makeText(ExchangeActivity.this, "点击生效", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ExchangeActivity.this, "兑换成功！", Toast.LENGTH_SHORT).show();
                        cur_balance += value;
                        bank_account_balance -= value;
                        Intent i= new Intent(ExchangeActivity.this, ExchangeSuccess.class);//跳到兑换成功的页面
                        i.putExtra("amount", value);
                        startActivity(i);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ExchangeActivity.this, "兑换失败！", Toast.LENGTH_SHORT).show();
                        Intent i= new Intent(ExchangeActivity.this, ExchangeFail.class);//跳到兑换失败的页面
                        startActivity(i);
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


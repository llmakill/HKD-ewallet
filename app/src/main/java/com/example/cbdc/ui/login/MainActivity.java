package com.example.cbdc.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cbdc.CBDCWallet;
import com.example.cbdc.R;
import com.example.cbdc.model.TokenModel;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.io.Serializable;
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

public class MainActivity extends Activity
{
    private static String test_link="https://rinkeby.infura.io/v3/3d11b65a993f4ccb88405ed1c5f8a4ef";
    //Excel传入的数据
    String bank_privateKey="e0089dc37bbfe9c2a0dc107a09adb0df0ef6e25c5d092ab77d0493b7c9d42907";//银行私钥
    String bank_pwd ="liii245pp";//银行的口令
    BigDecimal cur_balance;//当前的数字货币余额

    Token HKDContract;//载入的合约

    TextView t_main_remainText;

    CBDCWallet Bank_Wallet;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView t_main_toptext = (TextView) findViewById(R.id.main_toptext);
        final String main_toptext = t_main_toptext.getText().toString();

        TextView t_main_remain = (TextView) findViewById(R.id.main_remain);
        final String main_remain = t_main_remain.getText().toString();

        t_main_remainText = (TextView) findViewById(R.id.main_remainText);
        final String main_remainText = t_main_remainText.getText().toString();

        final Button exportButton = findViewById(R.id.exportButton);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transact = new Intent(MainActivity.this, ExportActivity.class);
                startActivity(transact);
            }
        });

        final ImageView icon1 = findViewById(R.id.icon1);
        final Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pay = new Intent(MainActivity.this, PayMethodActivity.class);
                startActivity(pay);
            }
        });

        final ImageView icon2 = findViewById(R.id.icon2);
        final Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qr = new Intent(MainActivity.this, QRCodeActivity.class);
                startActivity(qr);
            }
        });

        final ImageView icon3 = findViewById(R.id.icon3);
        final Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transact = new Intent(MainActivity.this, TransactionActivity.class);
                startActivity(transact);
            }
        });

        final ImageView icon4 = findViewById(R.id.icon4);
        final Button button4 = findViewById(R.id.button4);

        final ImageView exchange = findViewById(R.id.exchange);
        final Button exButton = findViewById(R.id.exButton);
        exButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChoosewalletActivity.class);
                startActivity(intent);
            }
        });

        final ImageView manage = findViewById(R.id.manage);

        import_bank();//导入银行的钱包
        load_contract();//导入合约

        final Button manageButton = findViewById(R.id.manageButton);
        manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccountManagementActivity.class);
                startActivity(intent);
            }
        });

        final Button outButton = findViewById(R.id.outButton);
        outButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
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
                        Toast.makeText(MainActivity.this, "合约加载完成！", Toast.LENGTH_SHORT).show();
                        HKDContract = token;
                        getBalance();//显示余额
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "合约加载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //导入银行钱包
    private void import_bank(){
        try {
            Bank_Wallet = generateBip44WalletByPrivateKey(bank_privateKey,bank_pwd);
            Toast.makeText(this, "导入成功", Toast.LENGTH_SHORT).show();
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //加载合约
    public static Single<Token> loadContract(CBDCWallet wallet, String addr, BigInteger gasPrice, BigInteger gasLimit) {
        Credentials credentials = Credentials.create(wallet.getKeyPair());
        Web3j web3j = Web3jFactory.build(new HttpService(test_link, createOkHttpClient(), false));
        return Single.fromCallable(() -> Token.load(addr, web3j, credentials, gasPrice, gasLimit))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    private BigInteger getGasPrice() {
        return BigInteger.valueOf(1000);
    }

    private BigInteger getGasLimit() {
        return BigInteger.valueOf(600000);
    }


    private void getBalance()
    {
        balanceOf(HKDContract, Bank_Wallet.getWalletFile().getAddress())
                .subscribe(mBalanceObserver);
    }

    private SingleObserver<BigInteger> mBalanceObserver = new SingleObserver<BigInteger>()
    {
        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onSuccess(BigInteger bigInteger) {
            cur_balance = new BigDecimal(bigInteger.toString());
            cur_balance = cur_balance.divide(new BigDecimal("1000000000000000000"));
            t_main_remainText.setText(cur_balance.toString());
        }

        @Override
        public void onError(Throwable e) {
            t_main_remainText.setText("余额查询有误！" + e.getMessage());
        }
    };

    public static Single<BigInteger> balanceOf(Token token, String addr)
    {
        return Single.fromCallable(() -> token.balanceOf(Keys.toChecksumAddress(addr))
                .send())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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

    public static CBDCWallet generateBip44WalletByPrivateKey(String privateKey, String password)
            throws CipherException, IOException {
        BigInteger pk = Numeric.toBigIntNoPrefix(privateKey);
        byte[] privateKeyByte = pk.toByteArray();
        ECKeyPair keyPair = ECKeyPair.create(privateKeyByte);
        WalletFile walletFile = Wallet.createLight(password, keyPair);
        Log.i("TAG", "generateBip44Wallet: 地址 = " + Keys.toChecksumAddress(walletFile.getAddress()));
        return new CBDCWallet(walletFile, null,keyPair);
    }
}

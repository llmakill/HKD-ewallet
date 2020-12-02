package com.example.cbdc.ui.login;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbdc.R;
import com.example.cbdc.api.CsvActivity;
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
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
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

public class LoginActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        TextView t_login_toptext = (TextView) findViewById(R.id.login_toptext);
        final String login_toptext = t_login_toptext.getText().toString();

        TextView t_login_appname = (TextView) findViewById(R.id.login_appname);
        final String login_appname = t_login_appname.getText().toString();

        TextView t_textView1 = (TextView) findViewById(R.id.textView1);
        final String textView1 = t_textView1.getText().toString();

        final EditText phonenumber = findViewById(R.id.phonenumber);

        TextView t_textView2 = (TextView) findViewById(R.id.textView2);
        final String textView2 = t_textView2.getText().toString();

        final EditText password = findViewById(R.id.password);

        final ImageView portrait = (ImageView) findViewById(R.id.portrait);

        final Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Userinfo> user_infos = new ArrayList<>();
                CsvActivity csvActivity = new CsvActivity();
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File path = cw.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                user_infos = csvActivity.readCsv(path);
                int num = user_infos.size();
                System.out.println(num);
                for (int i = 0; i < num; i++)
                {
                    System.out.println(user_infos.get(i).toString());
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        final Button regButton = findViewById(R.id.regButton);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });
    }
}
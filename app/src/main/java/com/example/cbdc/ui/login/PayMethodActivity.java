package com.example.cbdc.ui.login;

import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.content.Intent;
import android.app.Activity;
import android.view.Menu;
import android.view.View.OnClickListener;

import android.widget.Toast;

import com.example.cbdc.CBDCWallet;
import com.example.cbdc.R;
import com.kenai.jffi.Main;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.web3j.crypto.Keys;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class PayMethodActivity extends Activity {
    private Button fukuanma;
    private Button saoyisao;
    private ImageButton back;

    CBDCWallet Bank_Wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_method);

        final TextView payment_way = (TextView)findViewById(R.id.payment_way);

        fukuanma = (Button)findViewById(R.id.fukuanma);
        fukuanma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayMethodActivity.this, QRCodeActivity.class);
                startActivity(intent);
            }
        });

        saoyisao = (Button)findViewById(R.id.saoyisao);
        saoyisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickQrcodeAddress();
            }
        });

        back = (ImageButton)findViewById(R.id.huiqu10);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayMethodActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onClickQrcodeAddress(){
        //扫码
        RxPermissions rxPermissions = new RxPermissions(PayMethodActivity.this);
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean granted) {
                        if (granted) {
                            ZXingLibrary.initDisplayOpinion(getApplicationContext());
                            Intent intent = new Intent(PayMethodActivity.this, CaptureActivity.class);
                            startActivityForResult(intent, 100);
                        } else {
                            Toast.makeText(PayMethodActivity.this, "相机授权失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}







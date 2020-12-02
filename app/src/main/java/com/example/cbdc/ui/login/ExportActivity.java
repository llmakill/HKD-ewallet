package com.example.cbdc.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbdc.CBDCWallet;
import com.example.cbdc.DialogUtils;
import com.example.cbdc.R;
import com.example.cbdc.WalletUtils;
import com.example.cbdc.dao.Userinfo;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.utils.Numeric;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.ArrayList;

public class ExportActivity extends Activity
{
    String bank_pwd ="liii245pp";//银行的口令
    String bank_privateKey="e0089dc37bbfe9c2a0dc107a09adb0df0ef6e25c5d092ab77d0493b7c9d42907";//银行私钥

    CBDCWallet Bank_Wallet;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        import_bank();

        final TextView login_text = findViewById(R.id.login_text);

        final TextView walletadd = findViewById(R.id.walletadd);
        final TextView exportData = findViewById(R.id.exportData);
        exportData.setMovementMethod(ScrollingMovementMethod.getInstance());

        final Button mnemonic = findViewById(R.id.btn_daochuzhujici);
        mnemonic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExportActivity.this, "开始导出，请稍等。。。", Toast.LENGTH_SHORT).show();

                DialogUtils.enterPwd(ExportActivity.this,(pwd)->{
                    if (!pwd.equals(bank_pwd))
                    {
                        return;
                    }

                    if (!WalletUtils.checkPwd(pwd, Bank_Wallet.getWalletFile()))
                    {
                        Toast.makeText(ExportActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    exportData.setText(Bank_Wallet.getMnemonic());
                });
            }
        });

        final Button keystore = findViewById(R.id.btn_daochukeystore);
        keystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExportActivity.this, "开始导出，请稍等。。。", Toast.LENGTH_SHORT).show();

                DialogUtils.enterPwd(ExportActivity.this,(pwd)->{
                    if (!pwd.equals(bank_pwd))
                    {
                        return;
                    }

                    if (!WalletUtils.checkPwd(pwd, Bank_Wallet.getWalletFile()))
                    {
                        Toast.makeText(ExportActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    exportData.setText(Bank_Wallet.getKeyStoreJsonString());
                });
            }
        });

        final Button privateKey = findViewById(R.id.btn_daochusishi);
        privateKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExportActivity.this, "开始导出，请稍等。。。", Toast.LENGTH_SHORT).show();

                DialogUtils.enterPwd(ExportActivity.this,(pwd)->{
                    if (!pwd.equals(bank_pwd))
                    {
                        return;
                    }

                    if (!WalletUtils.checkPwd(pwd, Bank_Wallet.getWalletFile()))
                    {
                        Toast.makeText(ExportActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    exportData.setText(bank_privateKey);
                });
            }
        });

        final TextView daochudata = findViewById(R.id.daochudata);

        final Button btn_copydata = findViewById(R.id.btn_copydata);
        btn_copydata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String result = exportData.getText().toString();
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(ExportActivity.this, "数据不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText(null, result));
                Toast.makeText(ExportActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
            }
        });

        final ImageButton back = findViewById(R.id.huiqu8);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExportActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
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
}

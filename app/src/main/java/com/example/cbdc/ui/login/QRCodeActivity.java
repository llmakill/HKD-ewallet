package com.example.cbdc.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbdc.CBDCWallet;
import com.example.cbdc.R;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

public class QRCodeActivity extends Activity
{
    CBDCWallet Bank_Wallet;
    ImageView QRcode;
    String bank_pwd ="liii245pp";//银行的口令
    String bank_privateKey="5cdd4cbe170a2ac48733bdb7f7b8243661501e967eff079edd55e92834ddfc10";//银行私钥

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        final TextView title = findViewById(R.id.title);

        QRcode = findViewById(R.id.iv_qrcode);

        import_bank();
        showQrcode();

        final ImageButton back = findViewById(R.id.huiqu5);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRCodeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showQrcode() {
        if (Bank_Wallet == null) {
            Toast.makeText(this, "钱包为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String address = Keys.toChecksumAddress(Bank_Wallet.getWalletFile().getAddress());
        Bitmap qrcode = CodeUtils.createImage(address, 600, 600, null);
        QRcode.setImageBitmap(qrcode);
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

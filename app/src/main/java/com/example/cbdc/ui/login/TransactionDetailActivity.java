package com.example.cbdc.ui.login;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbdc.R;

import java.text.SimpleDateFormat;

public class TransactionDetailActivity extends Activity
{
    String transactionID;
    String from;
    String to;
    String transactionTime;
    String amount;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        Intent transaction = getIntent();

        transactionID = transaction.getStringExtra("transactionID");
        transactionTime = transaction.getStringExtra("transactionTime");
        from = transaction.getStringExtra("from");
        to = transaction.getStringExtra("to");
        amount = transaction.getStringExtra("price") + "HKD";

        final TextView transaction_detail = findViewById(R.id.transaction_detail);

        final TextView price = findViewById(R.id.price);
        price.setText(amount);

        final TextView payment_method = findViewById(R.id.payment_method);

        final TextView merchant_fullname = findViewById(R.id.merchant_fullname);
        final TextView TransactionID = findViewById(R.id.TransactionID);
        TransactionID.setText(transactionID);

        final TextView merchant_address = findViewById(R.id.merchant_address);
        final TextView User_Address = findViewById(R.id.User_Address);
        User_Address.setText(from);

        final TextView payment_time = findViewById(R.id.payment_time);
        final TextView Merchant_Address = findViewById(R.id.Merchant_Address);
        Merchant_Address.setText(to);

        final TextView trasaction_Time = findViewById(R.id.trasaction_Time);
        final TextView time = findViewById(R.id.time);
        time.setText(transactionTime);

        Button btn_copydata = findViewById(R.id.btn_copydata);
        btn_copydata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (TextUtils.isEmpty(transactionID)) {
                    Toast.makeText(TransactionDetailActivity.this, "数据不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText(null, transactionID));
                Toast.makeText(TransactionDetailActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
            }
        });

        final ImageButton back = findViewById(R.id.huiqu4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

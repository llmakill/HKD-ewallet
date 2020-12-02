package com.example.cbdc.ui.login;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cbdc.R;

public class ExchangeSuccess extends ExchangeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_success);

        Double fee;

        Intent exchange = getIntent();
        fee = exchange.getDoubleExtra("amount", 100.0);
        fee = fee / 1e18;

        final ImageView xiaarrow = findViewById(R.id.xiaarrow);

        final TextView trader1 = findViewById(R.id.trader1);

        final TextView yue = findViewById(R.id.yue);
        yue.setText(fee.toString());

        final TextView chengong = findViewById(R.id.yue);

        ImageButton back = (ImageButton) findViewById(R.id.huiqu5);
        back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent i=new Intent(ExchangeSuccess.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
    //还没加余额查询的部分
}
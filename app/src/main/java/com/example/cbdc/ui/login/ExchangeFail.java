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

public class ExchangeFail extends ExchangeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_fail);

        final TextView trader1 = findViewById(R.id.trader1);

        ImageButton back = (ImageButton) findViewById(R.id.huiqu6);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent i=new Intent(ExchangeFail.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
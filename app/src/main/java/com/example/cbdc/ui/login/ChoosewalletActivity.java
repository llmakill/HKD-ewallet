package com.example.cbdc.ui.login;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.content.Intent;
import android.app.Activity;
import android.view.Menu;
import android.view.View.OnClickListener;

import android.widget.Toast;

import com.example.cbdc.R;
import com.kenai.jffi.Main;


public class ChoosewalletActivity extends Activity {
    private Button cardselected1;
    private Button cardselected2;
    private ImageButton back;
    //private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosewallet);
        //cardselected = (Button ) findViewById(R.id.bankcard);
        cardselected1 = (Button) findViewById(R.id.btn_card1);
        cardselected2 = (Button) findViewById(R.id.btn_card2);
        back = (ImageButton) findViewById(R.id.huiqu13);

        cardselected1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent nextpage = new Intent(ChoosewalletActivity.this , ExchangeActivity.class);
                //启动
                startActivity(nextpage);
            }
        });
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent previouspage = new Intent(ChoosewalletActivity.this , MainActivity.class);
                //启动
                startActivity(previouspage);
            }
        });
    }
}








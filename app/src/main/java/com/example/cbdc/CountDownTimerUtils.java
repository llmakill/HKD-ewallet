package com.example.cbdc;

import android.graphics.Color;

import android.os.CountDownTimer;

import android.text.Spannable;

import android.text.SpannableString;

import android.text.style.ForegroundColorSpan;

import android.widget.Button;


import com.example.cbdc.R;

public class CountDownTimerUtils extends CountDownTimer{
    private Button mButton;



    public CountDownTimerUtils(Button button,long millisInFuture, long countDownInterval) {//控件，定时总时间,间隔时间

        super(millisInFuture, countDownInterval);

        this.mButton=button;



    }



    @Override

    public void onTick(long millisUntilFinished) {

        mButton.setClickable(false);//设置不可点击

        mButton.setText(millisUntilFinished/1000+"秒後可重新發送");//设置倒计时时间

        /* SpannableString spannableString=new SpannableString(bt_getcord.getText().toString());//获取按钮上的文字

        ForegroundColorSpan span=new ForegroundColorSpan(Color.RED);//设置文字颜色

        bt_getcord.setAllCaps(false);

        spannableString.setSpan(span,0,2,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);将倒计时的时间设置为红色

        bt_getcord.setText(spannableString);*/



    }



    @Override

    public void onFinish() {

        mButton.setClickable(true);//重新获得点击

        mButton.setText("重新獲取驗證碼");



    }

}

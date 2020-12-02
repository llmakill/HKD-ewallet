package com.example.cbdc.ui.login;

import com.example.cbdc.BankAccountDetails;
import com.example.cbdc.CountDownTimerUtils;
import com.example.cbdc.R;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddwalletcardActivity extends Activity {

    private CountDownTimerUtils mCountDownTimerUtils;

    @BindView(R.id.et_cardNum)
    public EditText mETCardNum;

    @BindView(R.id.et_phoneNum)
    public EditText mETPhoneNum;

    @BindView(R.id.et_verification_code)
    public EditText mETVerificationCode;

    @BindView((R.id.et_bank))
    public EditText mETbankName;

    @BindView((R.id.btn_getCode2))
    public Button mBTgetCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwalletcard);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_getCode2)
    public void onClickGet_code(){
        String phoneNum3 = mETPhoneNum.getText().toString();
        if (TextUtils.isEmpty(phoneNum3)) {
            Toast.makeText(this, "請輸入手機號", Toast.LENGTH_SHORT).show();
            return;}
        mCountDownTimerUtils =new CountDownTimerUtils(mBTgetCode,60000,1000);
        mCountDownTimerUtils.start();

    }

    @OnClick(R.id.ac_huiqu)
    public void onClickManageBackToMain() {
        Intent i = new Intent(AddwalletcardActivity.this , AccountManagementActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_add_card)
    public void onClickAdd_card(){
        String cardNum2;
        cardNum2 = mETCardNum.getText().toString();
        String phoneNum2;
        phoneNum2 = mETPhoneNum.getText().toString();
        String verificationCode = mETVerificationCode.getText().toString();
        String bankName2 = mETbankName.getText().toString();
        if (TextUtils.isEmpty(cardNum2)) {
            Toast.makeText(this, "請輸入卡號", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(phoneNum2)) {
            Toast.makeText(this, "請輸入手機號", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(verificationCode)) {
            Toast.makeText(this, "請輸入驗證碼", Toast.LENGTH_SHORT).show();
            return;
        }
        BankAccountDetails newCard = new BankAccountDetails("debt", cardNum2, phoneNum2,bankName2);


        Intent intent = new Intent();
        intent.putExtra("newBankAccount", newCard);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}

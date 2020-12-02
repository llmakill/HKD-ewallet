package com.example.cbdc.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cbdc.BankAccountDetails;
import com.example.cbdc.BankAccountsAdapter;
import com.example.cbdc.DividerItemDecoration;
import com.example.cbdc.R;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountManagementActivity extends Activity {
    @BindView(R.id.rv)
    RecyclerView mRecyclerView;
    BankAccountsAdapter mAdapter;
    List<BankAccountDetails> bankAccountDetailsList = new ArrayList<>();


    public static final int LINK_BANK_ACCOUNT_REQUEST_CODE = 1;
    public static final int BANK_ACCOUNT_DETAILS_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        ButterKnife.bind(this);
        init();
        ImageButton outButton = (ImageButton) findViewById(R.id.huiqu11);
        outButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountManagementActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//    }


    private void init() {
        mAdapter = new BankAccountsAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        bankAccountDetailsList.add(new BankAccountDetails("123", "0xa6433EA30698CB359640be508ce00A1aCD56D812", "212", "DC"));
        bankAccountDetailsList.add(new BankAccountDetails("123", "621663 4623234526122", "212", "PBOC(HK)"));
        mAdapter.setData(bankAccountDetailsList);
    }

    @OnClick(R.id.add_card)
    public void onClickCreateWallet() {
        Intent i = new Intent(AccountManagementActivity.this, AddwalletcardActivity.class);
        startActivityForResult(i, LINK_BANK_ACCOUNT_REQUEST_CODE);
    }

//    @OnClick(R.id.huiqu11)
//    public void onClickManageBackToMain() {
//        Intent i = new Intent(AccountManagementActivity.this , MainActivity.class);
//        startActivity(i);
////        AccountManagementActivity.this.finish();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LINK_BANK_ACCOUNT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                BankAccountDetails bankAccountDetails = bundle.getParcelable(
                        "newBankAccount");
                mAdapter.addBank(bankAccountDetails);
                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }
}


package com.example.cbdc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BankAccountsAdapter extends RecyclerView.Adapter<BankAccountsAdapter.ViewHolder>{
    @NonNull
    @Override
    public BankAccountsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_casual_list,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BankAccountDetails bankAccountDetails = mBankAccountDetailsList.get(position);
        holder.mTvBankName.setText(bankAccountDetails.getBankName());
        holder.mTvCardNum.setText(bankAccountDetails.getCardNum());
//        holder.imageViewAccount.setImageResource(R.drawable.ic_bank);
    }

    private List<BankAccountDetails> mBankAccountDetailsList;


//    @Inject
    public BankAccountsAdapter() {
        mBankAccountDetailsList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        if (mBankAccountDetailsList != null) {
            return mBankAccountDetailsList.size();
        } else {
            return 0;
        }
    }

    public void setData(List<BankAccountDetails> bankAccountDetailsList) {
        this.mBankAccountDetailsList = bankAccountDetailsList;
        notifyDataSetChanged();
    }

    public BankAccountDetails getBankDetails(int position) {
        return mBankAccountDetailsList.get(position);
    }

    public void addBank(BankAccountDetails bankAccountDetails) {
        mBankAccountDetailsList.add(bankAccountDetails);
        notifyDataSetChanged();
    }

    public void setBankDetails(int index, BankAccountDetails bankAccountDetails) {
        mBankAccountDetailsList.set(index, bankAccountDetails);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_casual_list_title)
        TextView mTvBankName;
        @BindView(R.id.tv_item_casual_list_subtitle)
        TextView mTvCardNum;
//        @BindView(R.id.iv_item_casual_list_icon)
//        ImageView imageViewAccount;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}

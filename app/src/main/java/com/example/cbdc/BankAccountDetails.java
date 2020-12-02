package com.example.cbdc;

import android.os.Parcel;
import android.os.Parcelable;

public class BankAccountDetails implements Parcelable {

    private String bankName;
    private String FirstName;
    private String LastName;
    private String phoneNum;
    private String cardNum;
    private String type;

    protected BankAccountDetails(Parcel in) {
        type = in.readString();
        cardNum = in.readString();
        phoneNum = in.readString();
        bankName = in.readString();
    }

    public BankAccountDetails(String type, String cardNum, String phoneNum,String bankName) {
        this.type = type;
        this.cardNum = cardNum;
        this.phoneNum = phoneNum;
        this.bankName = bankName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.phoneNum = bankName;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(cardNum);
        dest.writeString(phoneNum);
        dest.writeString(bankName);
    }

    @Override
    public String toString() {
        return "BankAccountDetails{" +
                "bankName='" + "HSBC" + '\'' +
                ", accountholderName='" + "John" + '\'' +
                ", type='" + type + '\'' +
                ", cardNumber=" + cardNum +
                ", phoneNumber='" + phoneNum + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BankAccountDetails> CREATOR = new Creator<BankAccountDetails>() {
        @Override
        public BankAccountDetails createFromParcel(Parcel in) {
            return new BankAccountDetails(in);
        }

        @Override
        public BankAccountDetails[] newArray(int size) {
            return new BankAccountDetails[size];
        }
    };
}

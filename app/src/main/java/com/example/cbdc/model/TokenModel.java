package com.example.cbdc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TokenModel implements Parcelable
{
    //合约地址
    public String mAddr;
    //代币名称
    public String mTitle;
    //是否是eth以太币
    public boolean isEth;

    public TokenModel(String mAddr, String mTitle, boolean isEth)
    {
        this.mAddr = mAddr;
        this.mTitle = mTitle;
        this.isEth = isEth;
    }

    protected TokenModel(Parcel in)
    {
        mAddr = in.readString();
        mTitle = in.readString();
        isEth = in.readInt() == 1;
    }

    public String getAddr() {
        return mAddr;
    }

    public void setAddr(String addr) {
        this.mAddr = addr;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public static final Creator<TokenModel> CREATOR = new Creator<TokenModel>()
    {
        @Override
        public TokenModel createFromParcel(Parcel in)
        {
            return new TokenModel(in);
        }

        @Override
        public TokenModel[] newArray(int size)
        {
            return new TokenModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mAddr);
        dest.writeString(mTitle);
        dest.writeInt(isEth ? 1 : 0);
    }
}

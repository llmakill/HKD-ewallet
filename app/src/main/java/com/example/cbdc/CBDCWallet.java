package com.example.cbdc;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletFile;

public class CBDCWallet extends Bip39Wallet {
    WalletFile walletFile;
    ECKeyPair mKeyPair;

    public CBDCWallet(WalletFile walletFile,String mnemonic) {
        super(null,mnemonic);
        this.walletFile = walletFile;
    }

    public CBDCWallet( WalletFile walletFile,String mnemonic, ECKeyPair mKeyPair) {
        super(null, mnemonic);
        this.walletFile = walletFile;
        this.mKeyPair = mKeyPair;
    }

    public CBDCWallet(String filename, String mnemonic) {
        super(filename, mnemonic);
    }

    public ECKeyPair getKeyPair() {
        return mKeyPair;
    }

    public void setKeyPair(ECKeyPair keyPair) {
        this.mKeyPair = keyPair;
    }

    public WalletFile getWalletFile() {
        return walletFile;
    }

    public void setWalletFile(WalletFile walletFile) {
        this.walletFile = walletFile;
    }

    public String getKeyStoreJsonString(){
        Gson gson = new Gson();
        JSONObject j = null;
        try {
            j = new JSONObject(gson.toJson(walletFile));
            j.remove("address");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return j.toString();
    }
    public String getMnemonic(){
        String mnemonic = "regular century seven hedgehog elder giggle security true fish pulp cement advance";
        return mnemonic;
    }
}

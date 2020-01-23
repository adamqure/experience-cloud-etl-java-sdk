package ParameterClasses.Abstracts;

import ParameterClasses.Classes.AuthToken;
import com.google.gson.annotations.*;


import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.util.Calendar;

public abstract class AuthInfoInterface
{
    protected String userName;
    protected String passWord;
    @SerializedName("secret")
    @Expose
    protected String rsaKey;
    @SerializedName("api_key")
    @Expose
    protected String apiKey;
    @SerializedName("token_type")
    @Expose
    protected String tokenType;
    @SerializedName("access_token")
    @Expose
    protected AuthToken accessToken;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken.getToken();
    }

    public void setAccessToken(AuthToken accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiration() {
        return this.accessToken.getExpiration();
    }

    public void setExpiration(String expiration) {
        this.accessToken.setExpiration(expiration);
    }

    public String getRsaKey() {
        return rsaKey;
    }

//    public Key convertKey() {
//        byte[] bytes = this.rsaKey.getBytes();
//        BigInteger exponent = new BigInteger(bytes);
//        try {
//
//        }
//        catch (InvalidKeyException e) {
//            e.printStackTrace();
//        }
//    }

    public void setRsaKey(String rsaKey) {
        this.rsaKey = rsaKey;
    }

}

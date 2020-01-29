package ParameterClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthToken
{
    @SerializedName("access_token")
    @Expose
    private String token;
    @SerializedName("expires_in")
    @Expose
    private String expiration;

    public AuthToken()
    {
        token = "";
        expiration = "";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}

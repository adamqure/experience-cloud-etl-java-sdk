package java.ParameterInterfaces;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public abstract class AuthInfoInterface
{
    protected String userName;
    protected String passWord;
    @SerializedName("api_key")
    @Expose
    protected String apiKey;
    @SerializedName("token_type")
    @Expose
    protected String tokenType;
    @SerializedName("access_token")
    @Expose
    protected String accessToken;
    protected Calendar expiration;
}

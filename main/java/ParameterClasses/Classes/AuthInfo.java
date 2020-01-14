package ParameterClasses.Classes;

import com.google.gson.annotations.*;

import ParameterClasses.Abstracts.AuthInfoInterface;
import java.util.Calendar;
import java.util.Date;

public class AuthInfo extends AuthInfoInterface
{
    @SerializedName("client_secret")
    @Expose
    private String clientSecret;
    @SerializedName("jwt_token")
    @Expose
    private String jwtToken;
    @SerializedName("ims_org")
    @Expose
    private String imsOrgId;

    public AuthInfo(String ims, String secret, String jwt)
    {
        imsOrgId = ims;
        clientSecret = secret;
        jwtToken = jwt;
    }

    public void addAuthToken(String token, String type, long timeToLive)
    {
        this.accessToken = token;
        this.expiration = Calendar.getInstance();
        /*This section is retrieving the Date for "now" from the Calendar, then
         * adding the time for which the token will be valid onto it, to get
         * the actual expiration date/time of the token*/
        long currentTime = this.expiration.getTime().getTime();
        this.expiration.setTime(new Date(currentTime + (timeToLive)));
    }
}

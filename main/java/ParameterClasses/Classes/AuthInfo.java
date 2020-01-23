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
    private String jwt;
    @SerializedName("ims_org")
    @Expose
    private String imsOrgId;
    @SerializedName("sub")
    @Expose
    private String subject;

    public AuthInfo(String ims, String secret, String jwt)
    {
        imsOrgId = ims;
        clientSecret = secret;
        this.jwt = jwt;
    }

    public void addAuthToken(AuthToken token)
    {
        this.accessToken = token;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getImsOrgId() {
        return imsOrgId;
    }

    public void setImsOrgId(String imsOrgId) {
        this.imsOrgId = imsOrgId;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }
}

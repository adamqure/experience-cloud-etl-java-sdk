package ParameterClasses;

import com.google.gson.annotations.*;

/**
 * A model class for storing the auth header information to be used in making requests
 */
public class AuthInfo {
    protected String username;
    protected String password;
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

    public AuthInfo(String ims, String secret, String jwt) {
        this.imsOrgId = ims;
        this.clientSecret = secret;
        this.jwt = jwt;
    }

    public void addAuthToken(AuthToken token) {
        this.accessToken = token;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getAccessToken()
    {
        if (accessToken == null) { return null; }
        else { return accessToken.getToken(); }
    }

    public String getRsaKey() {
        return rsaKey;
    }

    public String getClientSecret() {
        return clientSecret;
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

    public String getSubject() {
        return subject;
    }

    public String getExpiration()
    {
        return this.accessToken.getExpiration();
    }

    @Override
    public String toString() {
        return "AuthInfo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", rsaKey='" + rsaKey + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", accessToken=" + accessToken +
                ", clientSecret='" + clientSecret + '\'' +
                ", jwt='" + jwt + '\'' +
                ", imsOrgId='" + imsOrgId + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRsaKey(String rsaKey) {
        this.rsaKey = rsaKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setAccessToken(AuthToken accessToken) {
        this.accessToken = accessToken;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setImsOrgId(String imsOrgId) {
        this.imsOrgId = imsOrgId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

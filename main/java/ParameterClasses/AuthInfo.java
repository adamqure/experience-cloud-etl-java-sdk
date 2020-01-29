package ParameterClasses;

import com.google.gson.annotations.*;

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

    public String getAccessToken() {
        return accessToken.getToken();
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
}

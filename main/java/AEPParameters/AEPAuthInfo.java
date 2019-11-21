package java.AEPParameters;

import java.ParameterInterfaces.AuthInfoInterface;
import java.util.Calendar;
import java.util.Date;

public class AEPAuthInfo extends AuthInfoInterface
{
    private String client_secret;
    private String jwt_token;
    private String ims_org;

    public AEPAuthInfo(String ims, String secret, String jwt)
    {
        ims_org = ims;
        client_secret = secret;
        jwt_token = jwt;
    }

    public void addAuthToken(String token, String type, long timeToLive)
    {
        this.access_token = token;
        this.expiration = Calendar.getInstance();
        /*This section is retrieving the Date for "now" from the Calendar, then
         * adding the time for which the token will be valid onto it, to get
         * the actual expiration date/time of the token*/
        long currentTime = this.expiration.getTime().getTime();
        this.expiration.setTime(new Date(currentTime + (timeToLive * 1000)));
    }
}

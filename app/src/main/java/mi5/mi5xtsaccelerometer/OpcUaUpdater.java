package mi5.mi5xtsaccelerometer;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.SessionActivationException;
import com.prosysopc.ua.UserIdentity;
import com.prosysopc.ua.client.UaClient;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.transport.security.SecurityMode;

import java.net.URISyntaxException;
import java.util.Locale;

/**
 * Created by ow on 30.04.15.
 */
public class OpcUaUpdater {

    private String m_serverUri;
    private UaClient m_client;
    private String m_applicationName = "mi5";

    public OpcUaUpdater(String serverUri)
    {
        m_serverUri = serverUri;
        try {
            m_client = createClient();
        }
        catch (Exception e) {
        //
        }



    }


    private UaClient createClient() throws URISyntaxException, SessionActivationException
    {
        UaClient myClient = new UaClient(m_serverUri);
        // We dont care about security, no certificates!
        myClient.setSecurityMode(SecurityMode.NONE);

        // Create application description
        ApplicationDescription appDescription = new ApplicationDescription();
        appDescription.setApplicationName(new LocalizedText(m_applicationName, Locale.ENGLISH));
        appDescription.setApplicationUri("urn:localhost:UA:"+m_applicationName);
        appDescription.setProductUri("urn:prosysopc.com:UA:"+m_applicationName);
        appDescription.setApplicationType(ApplicationType.Client);

        // Create and set application identity
        ApplicationIdentity identity =  new ApplicationIdentity();
        identity.setApplicationDescription(appDescription);
        identity.setOrganisation("Prosys");
        myClient.setApplicationIdentity(identity);

        // Set locale
        myClient.setLocale(Locale.ENGLISH);

        // Set default timeout to 60 seconds
        myClient.setTimeout(60000);

        // Set anonymous user identity
        myClient.setUserIdentity(new UserIdentity());

        return myClient;
    }
}

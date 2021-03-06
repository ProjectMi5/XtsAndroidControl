package mi5.mi5xtsaccelerometer;

import android.os.AsyncTask;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.SessionActivationException;
import com.prosysopc.ua.UserIdentity;
import com.prosysopc.ua.client.UaClient;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.transport.security.SecurityMode;

import java.net.URISyntaxException;
import java.util.Locale;

/**
 * Created by ow on 30.04.15.
 */
public class OpcUaUpdater extends AsyncTask<String, Void, Boolean> {

    private String m_serverUri;
    private UaClient m_client;
    private String m_applicationName = "mi5";
    private String error;
    private Boolean m_connected = false;

    public OpcUaUpdater(String serverUri)
    {
        m_serverUri = serverUri;
    }

    protected Boolean doInBackground(String...strings)
    {
        try {
            this.m_client = createClient();
            this.m_client.connect();
            m_connected = true;
        }
        catch (Exception e) {
            //
            error = e.toString();
        }
        return true;
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
        appDescription.setProductUri("urn:Mi5:UA:"+m_applicationName);
        appDescription.setApplicationType(ApplicationType.Client);

        // Create and set application identity
        ApplicationIdentity identity =  new ApplicationIdentity();
        identity.setApplicationDescription(appDescription);
        identity.setOrganisation("Mi5");
        myClient.setApplicationIdentity(identity);

        // Set locale
        myClient.setLocale(Locale.ENGLISH);

        // Set default timeout to 60 seconds
        myClient.setTimeout(60000);

        // Set anonymous user identity
        myClient.setUserIdentity(new UserIdentity());

        return myClient;
    }

    public Boolean updateValues(float x, float y, float z)
    {
        Boolean returnVal = false;

    if (m_connected) {
        NodeId[] nodeIds = new NodeId[1];
        Object[] values = new Object[1];

        // TODO: Adjust values
        nodeIds[0] = new NodeId(4, "AirConditionerXml.Humidity");
        values[0] = (double) x;

        StatusCode[] returnStatus;
        try {
            returnStatus = m_client.writeValues(nodeIds, values);
        } catch (Exception e) {
            error = e.toString();
        }
    }
        return returnVal;
    }
}

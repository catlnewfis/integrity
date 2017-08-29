package cli;

import org.apache.log4j.Logger;

import com.mks.api.IntegrationPoint;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.Session;
import com.mks.api.response.APIException;

public class ConnectionFactory {

	private final static Logger log = Logger.getLogger(ConnectionFactory.class);

	private static ConnectionFactory instance;

	private static String host = "";

	private static int port = 7001;

	private static String user = "";

	private static String password = "";

	private static int majorVersion = 4;

	private static int minorVersion = 15;

	private static IntegrationPoint ipf;

	private ConnectionFactory() {
	}

	public static synchronized ConnectionFactory getInstance() {
		if (instance == null) {
			instance = new ConnectionFactory();
		}
		return instance;
	}

	@SuppressWarnings("deprecation")
	public Connection getConnection() throws APIException {
		if (ipf == null) {
			ipf = IntegrationPointFactory.getInstance().createIntegrationPoint(host, port, false, majorVersion,
					minorVersion);
		}
		Session session = ipf.createSession(user, password);
		session.setDefaultUsername(user);
		session.setAutoReconnect(true); // session失效允许自动连接
		log.info("Session Created.");
		Connection connection = Connection.getInstance(session);
		return connection;
	}

	public void setHost(String host) {
		ConnectionFactory.host = host;
	}

	public void setPort(int port) {
		ConnectionFactory.port = port;
	}

	public void setUser(String user) {
		ConnectionFactory.user = user;
	}

}

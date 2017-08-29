package com.catlbattery.alm.connect;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.catlbattery.alm.caches.Caches;
import com.catlbattery.alm.caches.Constants;
import com.mks.api.CmdRunner;
import com.mks.api.Command;
import com.mks.api.IntegrationPoint;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.Session;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;

public class ConnectionFactory {

	private static Log log = LogFactory.getLog(ConnectionFactory.class);

	private static ConnectionFactory instance;

	private static String host = Caches.environment.get(Constants.MKSSI_HOST);

	private static int port = 7001;

	private static String user = Caches.environment.get(Constants.MKSSI_USER);;

	private static int majorVersion = 4;

	private static int minorVersion = 15;

	private int count;

	private static IntegrationPoint ip;

	private ConnectionFactory() {
		Map<String, String> environment = Caches.environment;
		host = environment.get(Constants.MKSSI_HOST);
		String pot = environment.get(Constants.MKSSI_PORT);
		if (pot != null) {
			try {
				port = Integer.parseInt(pot);
			} catch (NumberFormatException e) {
				log.warn(e.getMessage());
			}
		}
		user = environment.get(Constants.MKSSI_USER);
	}

	public static synchronized ConnectionFactory getInstance() {
		if (instance == null) {
			instance = new ConnectionFactory();
		}
		return instance;
	}

	public Connection getConnection() throws APIException {
		if (ip == null) {
			ip = IntegrationPointFactory.getInstance().createLocalIntegrationPoint(majorVersion, minorVersion);
			;
		}
		ip.setAutoStartIntegrityClient(true);
		Session session = ip.getCommonSession();
		session.setAutoReconnect(true);
		session.setDefaultHostname(host);
		session.setDefaultPort(port);
		session.setDefaultUsername(user);
		Connection connection = Connection.getInstance();
		connection.setSession(session);
		return connection;
	}

	private Session connect(String user, String pwd) throws APIException {
		if (ip == null) {
			ip = IntegrationPointFactory.getInstance().createLocalIntegrationPoint(majorVersion, minorVersion);
			ip.setAutoStartIntegrityClient(true);
		}
		Session session = ip.createNamedSession(null, null, user, pwd);
		session.setDefaultHostname(host);
		session.setDefaultPort(port);
		session.setDefaultUsername(user);
		session.setDefaultPassword(pwd);
		Command imConnect = new Command("im", "connect");
		CmdRunner cmdRunner = session.createCmdRunner();
		Response res = cmdRunner.execute(imConnect);
		log.debug("Result: " + res.getExitCode());
		cmdRunner.release();
		return session;
	}

	private Session createSession(String user, String pwd) {
		Session session = null;
		try {
			session = connect(user, pwd);
			count = 0;
		} catch (APIException e) {
			log.warn(e.getMessage());
			if (count++ < 3) {
				return createSession(user, pwd);
			}
		}
		return session;
	}

	public Connection createConnection(String user, String pwd) {
		Connection connection = Connection.getInstance();
		connection.setSession(createSession(user, pwd));
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

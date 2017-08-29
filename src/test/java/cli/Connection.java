package cli;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.mks.api.CmdRunner;
import com.mks.api.Command;
import com.mks.api.Session;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;

public class Connection {

	public final static Logger log = Logger.getLogger(Connection.class);

	private static Connection instance;

	private Connection(Session session) throws APIException {
		validateConnection(session);
	}

	public synchronized static Connection getInstance(Session session) throws APIException {
		if(instance == null) {
			instance = new Connection(session);
		}
		return instance;
	}

	private CmdRunner getCmdRunner(Session session) throws APIException {
		Iterator<?> it = session.getCmdRunners();
		int i = 0;
		while(it.hasNext()) {
			it.next();
			i++;
		}
		log.info("User: " + session.getDefaultUsername() + ", session cmdRunners: " + i);
		if(i > 10) {
			try {
				wait(1000);
			} catch (InterruptedException e) {
				log.warn("CmdRunner wait InterruptedException: " + e);
			}
		}
		return session.createCmdRunner();
	}

	void validateConnection(Session session) throws APIException {
		Command cmd = new Command("api", "ping");
		execute(session, cmd);
	}

	public Response execute(Session session, Command command) throws APIException{
		long start = System.currentTimeMillis();
		CmdRunner cmdRunner = getCmdRunner(session);
		Response response = null;
		try {
			String[] cmdArray = command.toStringArray();
			StringBuffer sb = new StringBuffer("Executing: ");
			for (int i = 0; i < cmdArray.length; i++) {
				String arg = cmdArray[i];
				if (arg.toLowerCase().startsWith("--password")) {
					arg = "--password=XXXX";
				}
				if (i != 0) {
					sb.append(" ");
				}
				sb.append(arg);
			}
			log.info(sb);

			response = cmdRunner.execute(command);
			long spend = System.currentTimeMillis() - start;
			log.info("Spent: " + spend);
		} catch (APIException e) {
			response = e.getResponse();
			if ((response == null) || (response.getWorkItemListSize() < 1)) {
				log.error("COMMANDS Exception: " + e);
				throw e;
			}
			if (response.getWorkItemListSize() == 1) {
				APIException wix = null;
				try {
					WorkItem wi = response.getWorkItems().next();
					wix = wi.getAPIException();
				} catch (APIException ex2) {
					wix = ex2;
				}
				if (wix != null) {
					log.error("COMMANDS Exception: " + wix);
					throw wix;
				}
			}
		} finally {
			if (cmdRunner != null) {
				try {
					cmdRunner.release();
				} catch (APIException e) {
					log.info(e.getMessage());
				}
			}
		}
		return response;
	}

}

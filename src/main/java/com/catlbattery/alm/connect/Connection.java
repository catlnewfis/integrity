package com.catlbattery.alm.connect;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mks.api.CmdRunner;
import com.mks.api.Command;
import com.mks.api.Session;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;

public class Connection {

	private static Log log = LogFactory.getLog(Connection.class);

	private static Connection instance;

	private Session session;

	private Connection() {
		super();
	}

	public synchronized static Connection getInstance() {
		if (instance == null) {
			instance = new Connection();
		}
		return instance;
	}

	private CmdRunner getCmdRunner() throws APIException {
		Iterator<?> it = session.getCmdRunners();
		int i = 0;
		while (it.hasNext()) {
			it.next();
			i++;
		}
		log.info("User: " + session.getDefaultUsername() + ", session cmdRunners: " + i);
		if (i < 10) {
			return session.createCmdRunner();
		}
		try {
			wait(1000);
		} catch (InterruptedException e) {
			log.warn("CmdRunner wait InterruptedException: " + e);
		}
		return getCmdRunner();
	}

	public Response execute(Command command) throws APIException {
		long start = System.currentTimeMillis();
		CmdRunner cmdRunner = getCmdRunner();
		String[] cmds = command.toStringArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cmds.length; i++) {
			sb.append(" ");
			sb.append(cmds[i]);
		}
		log.info("Excecuting command: " + sb);
		Response response;
		try {
			response = cmdRunner.execute(command);
			long spend = System.currentTimeMillis() - start;
			log.info("Spent: " + spend);
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

	public void setSession(Session session) {
		this.session = session;
	}

	public void terminate() {
		if (session != null) {
			try {
				session.release();
			} catch (IOException | APIException e) {
			}
		}
	}
}

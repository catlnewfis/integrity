package com.catlbattery.alm.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.catlbattery.alm.vo.UserSession;

public class LoginListener implements HttpSessionAttributeListener {

	private static Log logger = LogFactory.getLog(LoginListener.class);

	Map<String, HttpSession> map = new HashMap<String, HttpSession>();

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		String name = event.getName();
		 if (name.equals("UserSession")) {

	            UserSession userSession = (UserSession) event.getValue();

	            if (map.get(userSession.getUsername()) != null) {

	                HttpSession session = map.get(userSession.getUsername());

	                try {
						session.removeAttribute("UserSession");

						session.invalidate();
					} catch (Exception e) {
						logger.warn("LoginListener:" + e.getMessage());
					}
	            }
	            map.put(userSession.getUsername(), event.getSession());
	        }
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		 String name = event.getName();

	        if (name.equals("userSession")) {

	            UserSession userSession = (UserSession) event.getValue();

	            map.remove(userSession.getUsername());

	        }
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {

	}

}

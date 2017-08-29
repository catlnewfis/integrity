package com.catlbattery.alm.service;

public interface ILdapService {

	String getDnForUser(String cn);

	boolean authenticate(String userDn, String credentials);

}

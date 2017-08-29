package com.catlbattery.alm.connect;

public class MksInfo {

	private String host;

	private int port = 7001; // 默认端口 Nov 15, 2016

	private String user;

	private String password;

	private int majorVersion = 4; // 默认版本 Nov 15, 2016

	private int minorVersion = 16; // August 29, 2017

	private boolean secure;

	private int maxCmdRunners = 30; // 最大数量

	private int initSession = 5; // 初始数量

	private int maxSessionSize = 15; // 最大数量，不得超过100

	private int waitTimes = 60; // 等待命令执行时间

	private long lazyCheck = 60 * 60;

	private long periodCheck = 60 * 60 * 2; // 每2小时session恢复到初始INIT_SESSION数量

	public MksInfo() {

	}

	public MksInfo(String host, String user, String password) {
		super();
		this.host = host;
		this.user = user;
		this.password = password;
	}

	public MksInfo(String host, int port, String user, String password, int majorVersion, int minorVersion) {
		super();
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public void setMajorVersion(int majorVersion) {
		this.majorVersion = majorVersion;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public void setMinorVersion(int minorVersion) {
		this.minorVersion = minorVersion;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public int getMaxCmdRunners() {
		return maxCmdRunners;
	}

	public void setMaxCmdRunners(int maxCmdRunners) {
		this.maxCmdRunners = maxCmdRunners;
	}

	public int getInitSession() {
		return initSession;
	}

	public void setInitSession(int initSession) {
		this.initSession = initSession;
	}

	public int getMaxSessionSize() {
		return maxSessionSize;
	}

	public void setMaxSessionSize(int maxSessionSize) {
		this.maxSessionSize = maxSessionSize;
	}

	public int getWaitTimes() {
		return waitTimes;
	}

	public void setWaitTimes(int waitTimes) {
		this.waitTimes = waitTimes;
	}

	public long getLazyCheck() {
		return lazyCheck;
	}

	public void setLazyCheck(long lazyCheck) {
		this.lazyCheck = lazyCheck;
	}

	public long getPeriodCheck() {
		return periodCheck;
	}

	public void setPeriodCheck(long periodCheck) {
		this.periodCheck = periodCheck;
	}

}

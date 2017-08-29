package com.catlbattery.alm.service.impl;

import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import com.catlbattery.alm.service.ILdapService;

@Service("ldapService")
public class LdapServiceImpl implements ILdapService {

	@Autowired
	private LdapTemplate ldapTemplate;

	private String equalsFilter;

	private static Log log = LogFactory.getLog(LdapServiceImpl.class);

	@Override
	public String getDnForUser(String accountName) {
		EqualsFilter f = new EqualsFilter(equalsFilter, accountName);
		List<?> result = ldapTemplate.search("", f.toString(), new AbstractContextMapper<Object>() {

			@Override
			protected Object doMapFromContext(DirContextOperations paramDirContextOperations) {
				return paramDirContextOperations.getNameInNamespace();
			}
		});

		if (result.size() < 1) {
			throw new RuntimeException("User not found: " + accountName + ", size: " + result.size());
		}
		if(result.size() > 1) {
			for (int i = 0; i < result.size(); i++) {
				log.warn("user not unique: " + i + ", result:" + result.get(i));
			}
		}

		return (String) result.get(0);

	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean authenticate(String userDn, String credentials) {
		boolean authen = false;
		try {
			// 组装登陆ldap需要的信息数据
			Hashtable<Object, Object> ht = new Hashtable<Object, Object>();
			// 获取已有的登陆信息
			ht = (Hashtable<Object, Object>) ldapTemplate.getContextSource().getReadOnlyContext().getEnvironment();
			// 设置用户
			ht.put(Context.SECURITY_PRINCIPAL, userDn);
			// 设置用户登陆密码
			ht.put(Context.SECURITY_CREDENTIALS, credentials);
			// 设置用户验证方式为simple
			ht.put(Context.SECURITY_AUTHENTICATION, "simple");
			// 出现异常时表示用当前登陆人登陆失败
			DirContext ctx = new InitialDirContext(ht);
			authen = true;
			ctx.close();
		} catch (NamingException e) {
			log.info("NamingException: " + e + ", message: " + e.getMessage());
			authen = false;
		}
		return authen;
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	public void setEqualsFilter(String equalsFilter) {
		this.equalsFilter = equalsFilter;
	}

}

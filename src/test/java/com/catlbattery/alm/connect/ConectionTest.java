package com.catlbattery.alm.connect;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.catlbattery.alm.service.IIntegrityService;
import com.catlbattery.alm.service.impl.IntegrityServiceImpl;
import com.catlbattery.alm.util.IntegrityUtil;
import com.mks.api.response.APIException;

public class ConectionTest {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"spring-context.xml"});
		IIntegrityService iu = ac.getBean(IntegrityServiceImpl.class);
		try {
			Map<String, String> item = iu.findItemById("163");
			System.out.println(item);
		} catch (APIException e) {
			System.out.println(IntegrityUtil.getMsg(e));
		}
	}
}

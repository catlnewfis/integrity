package com.catlbattery.alm;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.catlbattery.alm.service.IGroupRoleService;
import com.catlbattery.alm.service.IGroupService;
import com.catlbattery.alm.service.IRoleService;

public class ContextTest {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext(new String[]{"spring-context.xml"});
		IRoleService rs = ac.getBean(IRoleService.class);
		System.out.println(rs.findById(2).getCreatedDate() + rs.findList().get(0).getDescription());
		ac.getBean(IGroupRoleService.class);
		IGroupService gs = ac.getBean(IGroupService.class);
		System.out.println(gs.findById(1).getCreatedDate() + gs.findList().get(0).getName());
	}

}

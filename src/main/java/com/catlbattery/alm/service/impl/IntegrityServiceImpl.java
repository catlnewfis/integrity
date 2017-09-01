package com.catlbattery.alm.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catlbattery.alm.service.IIntegrityService;
import com.catlbattery.alm.util.IntegrityUtil;
import com.mks.api.response.APIException;

@Service("integrityService")
public class IntegrityServiceImpl implements IIntegrityService {

	@Autowired
	IntegrityUtil integrityUtil;

	@Override
	public Map<String, String> findItemById(String id) throws APIException {
		return integrityUtil.findissueById(id);
	}

}

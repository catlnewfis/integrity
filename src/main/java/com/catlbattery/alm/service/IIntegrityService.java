package com.catlbattery.alm.service;

import java.util.Map;

import com.mks.api.response.APIException;

public interface IIntegrityService {

	Map<String, String> findItemById(String id) throws APIException;

}

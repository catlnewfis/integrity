package com.catlbattery.alm.caches;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存 Aug-27 2017
 * 
 * @author
 *
 */
public class Caches {

	public static final Set<String> NON_HISTORY = new HashSet<String>();

	public static ConcurrentMap<String, Object> objects = new ConcurrentHashMap<>();

	public static Map<String, String> environment = System.getenv();

	public static List<String> projectList;

	public static List<String> typeList;

	public static Hashtable<String, String> typeClass = new Hashtable<>();

	public static Hashtable<String, String> dateFields = new Hashtable<>();

	public static Set<String> attachmentFields = new HashSet<>();

	public static Set<String> relationshipFields = new HashSet<>();

	public static Set<String> textFields = new HashSet<>();

	public static Set<String> userFields = new HashSet<>();

	public static Set<String> pickFields = new HashSet<>();

	public static Set<String> richContentFields = new HashSet<>();

	static {
		NON_HISTORY.add("MKSIssueCreatedBy");
		NON_HISTORY.add("MKSIssueCreatedDate");
		NON_HISTORY.add("MKSIssueModifiedBy");
		NON_HISTORY.add("MKSIssueModifiedDate");
		NON_HISTORY.add("MKSIssueAddedRelationships");
		NON_HISTORY.add("MKSIssueRemovedRelationships");
	}

}

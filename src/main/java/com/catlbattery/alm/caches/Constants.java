package com.catlbattery.alm.caches;

/**
 * 常量
 * 
 * @author
 *
 */
public class Constants {

	public static final String NEWLINE = System.getProperty("line.separator");

	public static final String USER_HOME = "user.home";

	/** * */
	public static final String MKSSI_HOST = "MKSSI_HOST";

	/** * */
	public static final String MKSSI_USER = "MKSSI_USER";

	/** * */
	public static final String MKSSI_PORT = "MKSSI_PORT";

	public static final String MKSSI_QUERYDEFINITION = "MKSSI_QUERYDEFINITION";

	/**
	 * * MKSSI_SORTASCENDING=true
	 */
	public static final String MKSSI_SORTASCENDING = "MKSSI_SORTASCENDING";

	/**
	 * * the sort field MKSSI_SORTFIELD=ID
	 */
	public static final String MKSSI_SORTFIELD = "MKSSI_SORTFIELD";

	/**
	 * * the issues of selected in current view MKSSI_NISSUE=5
	 */
	public static final String MKSSI_NISSUE = "MKSSI_NISSUE";

	/**
	 * * the issues ID of selected in current view MKSSI_ISSUE3=4444,
	 * MKSSI_ISSUE4=4445, MKSSI_ISSUE1=4442
	 */
	public static final String MKSSI_ISSUE_X = "MKSSI_ISSUE%s";

	/** * issues */
	public static final String MKSSI_WINDOW = "MKSSI_WINDOW";

	public static final String MKSSI_QUERY = "MKSSI_QUERY";

	public static final String MKSSI_NFIELD = "MKSSI_NFIELD";

	/**
	 * * MKSSI_FIELD1=ID, MKSSI_FIELD2=Type, MKSSI_FIELD3=State
	 */
	public static final String MKSSI_FIELD_X = "MKSSI_FIELD%s";

	public static final String MKSSI_FIELD_X_WIDTH = "MKSSI_FIELD%s_WIDTH";

	public static final String USERDOMAIN = "USERDOMAIN";

	public static final String COMPUTERNAME = "COMPUTERNAME";

	public static final String PROCESSOR_LEVEL = "PROCESSOR_LEVEL";

	public static final String ITEMLIST = "com.mks.api.response.ItemList";

	public static final String ISSUES = "issues";

	public static final String VIEW_ISSUE = "viewissue";

	public static final String CREATE_CONTENT = "createcontent";

	public static final String VIEW_FIELD = "viewfield";

	public static final String VIEW_TYPE = "viewtype";

	public static final String QUERY_DEFINITION = "queryDefinition";

	public static final String API_EXCEPTION = "APIException";

	public static final String CONTAINS = "Contains";

	public static final String CONTAINED_BY = "Contained By";

	public static final String REFERENCES = "References";

	public static final String SHARED_CATEGORY = "Shared Category";

	public static final String DOUCUMENT_ID = "Document ID";

	public static final String PARENT_ID = "parentID";

	public static final String SEGMENT = "segment";

	public static final String MEANINGFUL = "meaningful";

	public static final String PICKS = "picks";

	public static final String LABEL = "label";

	public static final String PHASE = "phase";

	/** 父字段 **/
	public static final String PARENT_FIELD = "Contained By";

	/** 子字段 **/
	public static final String SUB_FIELD = "Contains";

	/** 关联字段 **/
	public static final String REFERENCE_FIELD = "References";

	/** 状态字段 **/
	public static final String STATE_FIELD = "State";

	/** 类型字段 **/
	public static final String TYPE_FIELD = "Type";

	/** 字段 **/
	public static final String FIELDS = "fields";

	/** test suite类型 **/
	public static final String TYPE_TEST_SUIT = "Test Suit";

	/** tc类型 **/
	public static final String TYPE_TEST_CASE = "Test Case";

	/** 操作类型 **/
	public static final String INSERT_OPERATION = "Insert";

	/** 富文本前缀 **/
	public static final String RICH_CONTENT_PREFIX = "<!-- MKS HTML -->";

	/** 富文本附件描述 **/
	public static final String RICH_CONTENT_ATTACHMENT_SUMMARY = "Attachment added automatically by rich content";

	/** 富文本mks路径 **/
	public static final String RICH_CONTENT_MKS_SRC_PREFIX = "mks:///item/field?fieldid=%s&attachmentname=";

}

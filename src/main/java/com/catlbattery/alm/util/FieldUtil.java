package com.catlbattery.alm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.catlbattery.alm.caches.Constants;
import com.catlbattery.alm.connect.Connection;
import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.Item;
import com.mks.api.response.ItemList;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;

@Repository
public class FieldUtil {

	private static Log log = LogFactory.getLog(FieldUtil.class);

	@Autowired
	private Connection conn;

	private static Map<String, List<String>> categories;

	private FieldUtil() {
	}

	public synchronized Map<String, List<String>> getCategories() throws APIException {
		if (categories == null || categories.isEmpty()) {
			categories = categories();
		}
		return categories;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllPickValues(String field) throws APIException {
		List<String> visiblePicks = new ArrayList<>();
		Command cmd = new Command("im", "fields");
		cmd.addOption(new Option("noAsAdmin"));
		cmd.addOption(new Option("fields", "picks"));
		cmd.addSelection(field);
		Response res = conn.execute(cmd);
		if (res != null) {
			WorkItem wi = res.getWorkItem(field);
			if (wi != null) {
				Field picks = wi.getField("picks");
				List<Item> itemList = picks.getList();
				if (itemList != null) {
					for (Item item : itemList) {
						String visiblePick = item.getId();
						if (!visiblePicks.contains(visiblePick)) {
							visiblePicks.add(visiblePick);
						}
					}
				}
			}
		}
		return visiblePicks;
	}

	@SuppressWarnings("unchecked")
	public List<String> getActivePickValues(String field) throws APIException {
		List<String> visiblePicks = new ArrayList<>();
		Command cmd = new Command("im", "viewfield");
		cmd.addSelection(field);
		Response res = conn.execute(cmd);
		if (res != null) {
			WorkItem wi = res.getWorkItem(field);
			if (wi != null) {
				Field picks = wi.getField("picks");
				List<Item> itemList = picks.getList();
				if (itemList != null) {
					for (Item item : itemList) {
						String pickName = item.getId();
						Field attribute = item.getField("active");
						if (attribute != null && attribute.getValueAsString().equalsIgnoreCase("true")
								&& !visiblePicks.contains(pickName)) {
							visiblePicks.add(pickName);
						}
					}
				}
			}
		}
		return visiblePicks;
	}

	private Map<String, List<String>> categories() throws APIException {
		Map<String, List<String>> map = new HashMap<>();
		if (conn == null) {
			log.warn("invoke categories() ----- connection is null.");
			throw new APIException("invoke categories() ----- connection is null.");
		}
		Command command = new Command(Command.IM, Constants.VIEW_FIELD);
		command.addSelection(Constants.SHARED_CATEGORY);
		Response res = conn.execute(command);
		WorkItem wi = res.getWorkItem(Constants.SHARED_CATEGORY);
		ItemList il = (ItemList) wi.getField(Constants.PICKS).getList();
		List<String> segments = new ArrayList<String>();
		List<String> meaningfuls = new ArrayList<String>();
		for (int i = 0; i < il.size(); i++) {
			Item item = (Item) il.get(i);
			String label = item.getField(Constants.LABEL).getValueAsString();
			String phase = item.getField(Constants.PHASE).getValueAsString();
			if (Constants.MEANINGFUL.equals(phase)) {
				meaningfuls.add(label);
			} else if (Constants.SEGMENT.equals(phase)) {
				segments.add(label);
			}
		}
		map.put(Constants.SEGMENT, segments);
		map.put(Constants.MEANINGFUL, meaningfuls);
		return map;
	}

}

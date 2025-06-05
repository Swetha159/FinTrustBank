package com.bank.fintrustbank.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TimeFormatter {

	public static void convertMillisToDateTime(List<Map<String, Object>> dataList, String key) {
	    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	    for (Map<String, Object> map : dataList) {
	        Object value = map.get(key);

	        if (value == null || value.toString().isEmpty()) {
	            map.put(key, "Not yet modified");
	        } else {
	            try {
	                long millis = Long.parseLong(value.toString());
	                String formatted = formatter.format(new Date(millis));
	                map.put(key, formatted);
	            } catch (NumberFormatException e) {
	                // Handle unexpected format
	                map.put(key, "Invalid date format");
	            }
	        }
	    }
	}

}

package com.bank.fintrustbank.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TimeFormatter {

    public static void convertMillisToDateTime(List<Map<String, Object>> dataList, String key) {
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
System.out.println("coming");
        for (Map<String, Object> map : dataList) {
        	
            Object value = map.get(key);

                long millis =  Long.parseLong((String) value);
                System.out.println(millis);
                String formatted = formatter.format(new Date(millis));
                System.out.println(formatted);
                map.put(key, formatted); 
            
        }
    }
}

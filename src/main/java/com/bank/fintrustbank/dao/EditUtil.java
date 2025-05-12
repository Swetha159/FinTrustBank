package com.bank.fintrustbank.dao;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;
import querybuilder.QueryBuilder;

public class EditUtil {

	public static Query  update(Object entity, String table,String  conditionColumn ,List<String> excludeColumns) throws TaskException {
	    try {
	        Class<?> clazz = entity.getClass();
	        Field[] fields = clazz.getDeclaredFields();
            boolean conditionSet  = false ; 
	        QueryBuilder qb = new QueryBuilder()
	        		.update(table);
	        for (Field field : fields) {
	            field.setAccessible(true);
	            Object value = field.get(entity);
	            String column = field.getName();
	            if (value != null  && !excludeColumns.contains(column)&& !column.equals(conditionColumn)) {
	                qb.set(column,value);
	                
	              
	            }
	            else if(column.equals(conditionColumn))
	            {
	            	qb.where(conditionColumn , "=" , value);
	            	conditionSet = true;
	            }

	        }
	       
	        		
	        	
	        if (!conditionSet) {
	            throw new TaskException("Condition column value is missing or null.");
	        }	
	       
	        Query updateQuery = qb.build();
	        if (updateQuery.getValues().isEmpty()) {
	            throw new TaskException("No non-null fields to update.");
	        }
	    	return updateQuery;
	      


	      
	    } catch (Exception e) {
	        throw new TaskException("Failed to update entity: " + e.getMessage(), e);
	    }
	
	}

}

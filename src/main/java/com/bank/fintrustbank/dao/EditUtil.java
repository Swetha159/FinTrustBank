package com.bank.fintrustbank.dao;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.zoho.training.exceptions.TaskException;

import querybuilder.Column;
import querybuilder.Query;
import querybuilder.QueryBuilder;

public class EditUtil {
	/*
	 * public static Query update(Object entity, Column table, Column
	 * conditionColumn, List<Column> excludeColumns) throws TaskException { try {
	 * Class<?> clazz = entity.getClass(); Field[] fields =
	 * clazz.getDeclaredFields(); boolean conditionSet = false;
	 * 
	 * QueryBuilder qb = new QueryBuilder().update(table);
	 * 
	 * // Hold sets and condition separately Map<String, Object> setFields = new
	 * java.util.LinkedHashMap<>(); Object conditionValue = null;
	 * 
	 * for (Field field : fields) { field.setAccessible(true); Object value =
	 * field.get(entity); String columnCamel = field.getName(); String column =
	 * toSnakeCase(columnCamel);
	 * 
	 * if (value != null && !excludeColumns.contains(column)) {
	 * System.out.println(column); if (column.equals(conditionColumn)) {
	 * conditionValue = value; conditionSet = true; } else { setFields.put(column,
	 * value); } } }
	 * 
	 * 
	 * if (!conditionSet) { throw new
	 * TaskException("Condition column value is missing or null."); }
	 * 
	 * // Apply set fields first for (Map.Entry<String, Object> entry :
	 * setFields.entrySet()) { qb.set(entry.getKey(), entry.getValue()); }
	 * 
	 * // Then apply where clause qb.where(conditionColumn, "=", conditionValue);
	 * 
	 * Query updateQuery = qb.build(); if (updateQuery.getValues().isEmpty()) {
	 * throw new TaskException("No non-null fields to update."); }
	 * 
	 * return updateQuery;
	 * 
	 * } catch (Exception e) { throw new TaskException("Failed to update entity: " +
	 * e.getMessage(), e); } }
	 */
	public static <T> Query update(Object entity, T table, T conditionColumn, List<T> excludeColumns) throws TaskException {
	    try {
	        Class<?> clazz = entity.getClass();
	        Field[] fields = clazz.getDeclaredFields();
	        boolean conditionSet = false;

	        QueryBuilder qb = new QueryBuilder().update((Column) table);
	        Object conditionValue = null;

	        // Determine the enum class (e.g., AccountField.class)
	        Class<?> columnEnumClass = table.getClass().isEnum() ? table.getClass() : table.getClass().getEnclosingClass();

	        for (Field field : fields) {
	            field.setAccessible(true);
	            Object value = field.get(entity);
	            if (value == null) continue;

	            String snakeCase = toSnakeCase(field.getName());

	            // Find the matching column in the enum class
	            Column matchedColumn = getColumnByName(columnEnumClass, snakeCase);
	            if (matchedColumn == null || excludeColumns.contains(matchedColumn)) {
	                continue;
	            }

	            if (matchedColumn.getColumnName().equals(((Column) conditionColumn).getColumnName())) {
	                conditionValue = value;
	                conditionSet = true;
	            } else {
	                qb.set(matchedColumn, value);
	            }
	        }

	        if (!conditionSet) {
	            throw new TaskException("Condition column value is missing or null.");
	        }

	        qb.where((Column) conditionColumn, "=", conditionValue);
	        Query updateQuery = qb.build();

	        if (updateQuery.getValues().isEmpty()) {
	            throw new TaskException("No non-null fields to update.");
	        }

	        return updateQuery;

	    } catch (Exception e) {
	        throw new TaskException("Failed to update entity: " + e.getMessage(), e);
	    }
	}
	private static Column getColumnByName(Class<?> enumClass, String columnName) {
	    if (!enumClass.isEnum()) return null;
	    for (Object constant : enumClass.getEnumConstants()) {
	        Column col = (Column) constant;
	        if (col.getColumnName().equalsIgnoreCase(columnName)) {
	            return col;
	        }
	    }
	    return null;
	}

	public static String toSnakeCase(String camelCase) {
	    if (camelCase == null || camelCase.isEmpty()) {
	        return camelCase;
	    }
	    StringBuilder result = new StringBuilder();
	    char firstChar = camelCase.charAt(0);
	    result.append(Character.toLowerCase(firstChar));
	    for (int i = 1; i < camelCase.length(); i++) {
	        char c = camelCase.charAt(i);
	        if (Character.isUpperCase(c)) {
	            result.append('_');
	            result.append(Character.toLowerCase(c));
	        } else {
	            result.append(c);
	        }
	    }
	    return result.toString();
	}


}

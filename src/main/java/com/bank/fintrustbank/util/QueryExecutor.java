package com.bank.fintrustbank.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import querybuilder.Query;

public class QueryExecutor {
    private final DataSource dataSource  = DataSourceHolder.getInstance().getDataSource();


    public int  execute(String query, List<Object> values) {
    	int rowAffected =0;
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {

            setParameters(statement, values);
            System.out.println(statement);
            rowAffected = statement.executeUpdate();
             
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return rowAffected ;
    }

    public void executeBatch(String query, List<List<Object>> batchValues) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {

            for (List<Object> values : batchValues) {
                setParameters(statement, values);
                statement.addBatch();
            }

            int[] rowsAffected = statement.executeBatch();

        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }
    private void setParameters(PreparedStatement statement, List<Object> values) throws SQLException {
        for (int i = 0; i < values.size(); i++) {
            Object value = values.get(i);
            if (value instanceof String) {
                statement.setString(i + 1, (String) value);
            } else if (value instanceof Integer) {
                statement.setInt(i + 1, (Integer) value);
            } else if (value instanceof Long) {
                statement.setLong(i + 1, (Long) value);
            } else if (value instanceof Double) {
                statement.setDouble(i + 1, (Double) value);
            } else if (value instanceof Float) {
                statement.setFloat(i + 1, (Float) value);
            } else if (value instanceof Boolean) {
                statement.setBoolean(i + 1, (Boolean) value);
            } else if (value instanceof Date) {
                statement.setDate(i + 1, (Date) value);
            } else if (value instanceof Timestamp) {
                statement.setTimestamp(i + 1, (Timestamp) value);
            } else {
                statement.setObject(i + 1, value);
            }
        }
    }

    public List<Map<String, Object>> executeQuery(String query, List<Object> values) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {

            setParameters(statement, values);
            try(ResultSet resultSet = statement.executeQuery())
            {
            	
    
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    Map<String, Object> rowMap = new HashMap<>();
                    
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object columnValue = resultSet.getObject(i);
                        rowMap.put(columnName, columnValue);
                    }
                    System.out.println(rowMap);
                    resultList.add(rowMap);
                }
            }

         catch (SQLException e) {
            e.printStackTrace();  
        }
       
        return resultList;
    }
   

}
    
    
   public  boolean transaction(List<Query> queryList) throws SQLException 
    {
	   try(Connection con = dataSource.getConnection())
	   {
		   con.setAutoCommit(false);
	   for(Query query : queryList)
	   {
		  try(PreparedStatement statement = con.prepareStatement(query.getQuery()))
		  {
			  setParameters(statement, query.getValues());
	            System.out.println(statement);
	            statement.executeUpdate();
	            
		  } 
		  catch(SQLException e )
		   {
			   con.rollback();
			    e.printStackTrace();
	            return false;
		   }
		   
	   }
	   con.commit();
	   return true;
	   }

    	
    }
}

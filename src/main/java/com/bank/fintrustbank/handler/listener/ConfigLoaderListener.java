package com.bank.fintrustbank.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.yaml.snakeyaml.Yaml;

import com.bank.fintrustbank.util.DataSourceHolder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConfigLoaderListener implements ServletContextListener {
	private static HikariDataSource dataSource;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();

		try (InputStream inputStream = context.getResourceAsStream("/WEB-INF/endpoints.yml");
				InputStream configInputStream = context.getResourceAsStream("/WEB-INF/config.properties")) {
			if (inputStream == null) {
				throw new RuntimeException("endpoints.yaml not found in /WEB-INF");
			}

			Yaml yaml = new Yaml();
			Map<String, Map<String, String>> endpointConfig = yaml.load(inputStream);

			if (endpointConfig == null || endpointConfig.isEmpty()) {
				throw new RuntimeException("endpoints.yaml is empty or invalid");
			}

			context.setAttribute("endpointConfig", endpointConfig);

		} catch (IOException | RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException("Critical error: Failed to initialize endpoint config", e);

		}
		try (InputStream configInputStream = context.getResourceAsStream("/WEB-INF/config.properties")) {
			if (configInputStream == null) {
				throw new RuntimeException("config.properties not found in /WEB-INF");
			}
			Properties properties = new Properties();
			properties.load(configInputStream);

			String dbUrl = properties.getProperty("DB_URL");
			String dbUser = properties.getProperty("DB_USER");
			String dbPassword = properties.getProperty("DB_PASSWORD");
			int poolSize = Integer.parseInt(properties.getProperty("POOL_SIZE", "10"));
			String dbDriver =  properties.getProperty("DB_DRIVER");
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(dbUrl);
			config.setUsername(dbUser);
			config.setPassword(dbPassword);
			config.setMaximumPoolSize(poolSize);
			config.setDriverClassName(dbDriver);
			dataSource = new HikariDataSource(config);
		      DataSourceHolder.getInstance().setDataSource(dataSource);
	//		context.setAttribute("dataSource", dataSource);

			System.out.println("Database connection pool created successfully.");

		} catch (IOException | RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException("Critical error: Failed to set up db connection pool ", e);

		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (dataSource != null) {
			dataSource.close();
			System.out.println("Database connection pool closed.");
		}
		/*
		 * Enumeration<Driver> drivers = DriverManager.getDrivers(); while
		 * (drivers.hasMoreElements()) { Driver driver = drivers.nextElement(); try {
		 * DriverManager.deregisterDriver(driver);
		 * System.out.println("Deregistered JDBC driver: " + driver); } catch
		 * (SQLException e) { System.err.println("Error deregistering driver " + driver
		 * + ": " + e); } }
		 * 
		 * try { AbandonedConnectionCleanupThread.checkedShutdown(); } catch (Exception
		 * e) { Thread.currentThread().interrupt();
		 * System.err.println("Cleanup thread shutdown interrupted: " + e.getMessage());
		 * 
		 * }
		 */
	}
}

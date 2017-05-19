package com.tripAdvisorIR.dbmanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SamtikCityCodes {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/TripAdvisor-FlightsData";
//	static final String DB_URL = "jdbc:mysql://localhost:3306/TripAdvisor-FlightsData?characterEncod‌​ing=UTF-8";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "amir";
	private static Connection conn = null;
	private static Statement stmt = null;

	

	public static void writeToDB(Map<String, String> cityCode) {
		String sql;
		
		Set<String> keys = new HashSet<>();
		keys = cityCode.keySet();
		for (String key : keys) {
			try {
				String value = cityCode.get(key);
				sql = "INSERT INTO `TripAdvisor-FlightsData`.`Samtik_CityCodes` ( code, description ) VALUES ('" + value + "','" + key + "');";
				System.out.println(sql);
				
				int executeUpdate = stmt.executeUpdate(sql);
				System.out.println(executeUpdate);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
		}
		
	}
	
	
	// init Connection method
private static void initConnections() {
		
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			// STEP 4: Execute a query
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// close connections method
	private static void closeConnections() {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException se2) {
			
		}
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

}

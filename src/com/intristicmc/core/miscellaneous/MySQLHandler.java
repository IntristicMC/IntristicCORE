package com.intristicmc.core.miscellaneous;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class MySQLHandler {
	 static final String DBNAME = "jdbc:mysql://185.46.120.194:3306/zirconcf_intristic";
	 static final String DBUSER = "zirconcf_intrist";
	 static final String DBPASS = "mFH]^QXziuCR(;OZ";
	 static Connection conn;
	 static Statement s;
	 
	 public static void connect() {
	  try {
		  Class.forName("com.mysql.jdbc.Driver");
		  conn = DriverManager.getConnection(DBNAME, DBUSER, DBPASS);
		  s = conn.createStatement();
	  } catch(Exception e) {
		  e.printStackTrace();
	  }
	 }
	 
	 public static void createNewStatement() {
		 try {
			s = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 }
	 
	 public static void closeConnection() {
		 try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 }
	 
	 public static void closeStatement() {
		 try {
			 s.close();
		 } catch(SQLException e) {
			 e.printStackTrace();
		 }
	 }

	public static Statement returnStatement() {
		return s;
	}
}
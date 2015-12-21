package uk.intristicmc.core.miscellaneous;

import java.sql.DriverManager;

import java.sql.Connection;
import java.sql.Statement;

public class MySQLHandler {
	 static final String DBNAME = "jdbc:mysql://185.46.120.194:3306/zirconcf_intristic";
	 static final String DBUSER = "zirconcf_mcserv";
	 static final String DBPASS = "MvQr6VGtchVuM8J9";
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
	 
	 public static Statement returnStatement() {
		 return s;
	 }
}
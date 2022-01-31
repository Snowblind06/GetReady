package com.getready.connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

	
	static String driver = "com.mysql.jdbc.Driver";
	static String url = "jdbc:mysql://localhost:3306/";
	static String password;
	static String user ;
	static Connection connexion;
	
	
	
	//CHARGEMENT DATABASE
	public static Connection loadDatabase() {
		
			
		// Chargement du driver
		try {
	       Class.forName("com.mysql.jdbc.Driver");
	     } catch (ClassNotFoundException e) {
	        }
			
	    //on cr��e la connexion avec les identifiants/mdp
		try {
			connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/getready", "getready", "Taratata1234!");
		}catch (SQLException e) {
			e.printStackTrace();
			}
		
		return connexion;
	}
	
	
}

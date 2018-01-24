package com.pwc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DatabseValidation {

	Connection connect(HashMap<String,String> map) throws SQLException{
		
	String userName=map.get("Name");
	String url=map.get("Url");
	String password=map.get("Password");
	
		
		Connection conn=DriverManager.getConnection(url,userName,password);
		
		return conn;
	}
	
	public boolean dbValidate(String username,String password){
	
		
		try {
			Property property=new Property();
			
			Class.forName(property.readProperty().get("ClassName"));
			Connection connect=connect(property.readProperty());
			Statement stmnt=connect.createStatement();
			
			ResultSet rs=stmnt.executeQuery("SELECT * FROM USER WHERE NAME='"+username+"' AND PASSWORD='"+password+"';");  
			
			
			while(rs.next()) {
				
				return true;
			}
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return false;
	}
	
	
	
}

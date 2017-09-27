package com.cooksys.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cooksys.entity.Location;

public class LocationDao {
	
	private Connection conn;
	private Statement stmt;
	
	public LocationDao(Connection conn)
	{
		this.conn = conn;
	}
	
	public Location get(Long id)
	{
		Location location = null;
		
		try 
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM \"Location\" WHERE location_id = "+id);
			rs.next();
			location = new Location();
			location.setId(id);
			location.setCity(rs.getString("city"));
			location.setState(rs.getString("state"));
			location.setCountry(rs.getString("country"));
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return location;
	}
	
	public Long save(Location loc)
	{
		//Long id = loc.getId();
		Long id = null;
		String city = loc.getCity();
		String state = loc.getState();
		String country = loc.getCountry();
		
		try
		{
			stmt = conn.createStatement();
			ResultSet tempRS = stmt.executeQuery("select location_id from \"Location\" where city = '"+city+"' and state = '"+state+"' and country = '"+country+"'");
			tempRS.next();
			id = tempRS.getLong("location_id");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		if(id == null)
		{
			String values = "'"+city+"','"+state+"','"+country+"'";
			
			//add to db		
			try 
			{	
				stmt.execute("INSERT INTO \"Location\" (city,state,country) VALUES ("+values+")");		
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			//if id doesn't exist in db throw exception
			//else update person @ id
			
			String set = "SET city = '"+city+"' , state = '"+state+"' ,country = '"+country+"'";
			
			try 
			{
				stmt.executeUpdate("UPDATE \"Location\" "+set+" WHERE location_id = "+ id);
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		return id;
	}

}

package com.cooksys.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cooksys.entity.Interest;

public class InterestDao {
	
	private Connection conn;
	private Statement stmt;
	
	public InterestDao(Connection conn)
	{
		this.conn = conn;
	}
	
	public Interest get(Long id)
	{
		Interest interest = null;
		
		try 
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM \"Interest\" WHERE interest_id = '"+id+"'");
			rs.next();
			interest = new Interest();
			interest.setId(id);
			interest.setTitle(rs.getString("title"));
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return interest;
	}
	
	public void save(Interest i,Long personID)
	{
		//Long id = i.getId();
		Long id = null;
		int interestCount = 0;
		String title = i.getTitle();
		
		try
		{
			stmt = conn.createStatement();
			ResultSet intIdRS = stmt.executeQuery("select interest_id from \"Interest\" where title = '"+title+"'");
			intIdRS.next();
			id = intIdRS.getLong("interest_id");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		if(id == null)
		{
			String values = "'"+title+"'";
			String valuesOther = personID+","+id;
			
			//add to db		
			try 
			{	
				stmt.execute("INSERT INTO \"Interest\" (title) VALUES ("+values+")");
		
				stmt.execute("INSERT INTO \"Person-Interest\" (person_id,interest_id) VALUES ("+valuesOther+")");
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
			boolean alreadyHasInterest = false;
			
			try
			{				
				stmt = conn.createStatement();
				ResultSet tempRS = stmt.executeQuery("select * from \"Person-Interest\" where person_id = "+personID);
				while(tempRS.next())
				{	
					if(tempRS.getLong("interest_id")==(id))
					{
						interestCount++;
					}					
				}
				
				if(interestCount < 1)
					alreadyHasInterest = true;
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			String set = "SET title = '"+title+"'";
			String setOther = "SET person_id = '"+personID+"'";
			
			try 
			{
				stmt.executeUpdate("UPDATE \"Interest\" "+set+" WHERE interest_id = "+ id);
				if(alreadyHasInterest)
					stmt.executeUpdate("UPDATE \"Person-Interest\" "+setOther+" WHERE interest_id = "+id);
				else stmt.execute("INSERT INTO \"Person-Interest\" (person_id,interest_id) VALUES ("+personID+","+id+")");
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
		}
	}

}

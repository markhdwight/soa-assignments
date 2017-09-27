package com.cooksys.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.cooksys.entity.Interest;
import com.cooksys.entity.Person;

public class PersonDao {
	
	private static final String JDBC_DRIVER = "org.postgresql.Driver";
	private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
	private static final String USER = "postgres";
	private static final String PASS = "bondstone";
	
	private Connection conn;
	private Statement stmt;
	//private ResultSet rs;
	
	private LocationDao locationDao;
	private InterestDao interestDao;
	
	public PersonDao()
	{
		
		conn = null;
    	stmt = null;
		try{
			
    		Class.forName(JDBC_DRIVER);
    		conn = DriverManager.getConnection(DB_URL,USER,PASS);
    		//stmt = conn.createStatement();   
    		
    		locationDao = new LocationDao(conn);
    		interestDao = new InterestDao(conn);
    	}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public Person get(long id)
	{
		Person person = null;
		Set<Interest> interests;
		
		try 
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM \"Person\" WHERE person_id = "+ id);
			rs.next();
			person = new Person();
			person.setId(id);
			person.setFirstName(rs.getString("firstname"));
			person.setLastName(rs.getString("lastname"));
			person.setLocation(locationDao.get(rs.getLong("location_id")));
			
			interests = new HashSet<Interest>();
			stmt = conn.createStatement();
			ResultSet rs2 = stmt.executeQuery("SELECT * FROM \"Person-Interest\" WHERE person_id = "+id);
			while(rs2.next())
			{
				interests.add(interestDao.get(rs2.getLong("interest_id")));	//
			}
			person.setInterests(interests);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return person;
	}
	
	public void save(Person p)
	{
		//Long id = p.getId();
		Long id = null;
		String first = p.getFirstName();
		String last = p.getLastName();
		
		Long locID = locationDao.save(p.getLocation());
		
		try
		{
			stmt = conn.createStatement();
			ResultSet tempRS = stmt.executeQuery("Select person_id from \"Person\" where firstname = '"+first+"' and lastname ='"+last+"' and location_id = "+locID);
			tempRS.next();
			id = tempRS.getLong("person_id");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		if(id == null)
		{
			String values = "'"+first+"','"+last+"',"+locID;
			
			//add to db		
			try 
			{	
				stmt.execute("INSERT INTO \"Person\" (firstname,lastname,location_id) VALUES ("+values+")");
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
			
			String set = "SET firstname = '"+first+"' , lastname = '"+last+"' ,location_id = "+locID;
			
			try 
			{
				stmt.executeUpdate("UPDATE \"Person\" "+set+" WHERE person_id = "+ id);
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		
		for(Interest i : p.getInterests())
		{
			interestDao.save(i,id);
		}
		
	}
	
	public Set<Person> getEveryone()
	{
		Set<Person> everyone = new HashSet<Person>();
		Set<Interest> interests;
		Person person;
		String command = "select * from \"Person\"";
		
		try 
		{
			stmt = conn.createStatement();
			ResultSet rsE = stmt.executeQuery(command);
			
			while(rsE.next())
			{
				person = new Person();
				person.setId(rsE.getLong("person_id"));
				person.setFirstName(rsE.getString("firstname"));
				person.setLastName(rsE.getString("lastname"));
				person.setLocation(locationDao.get(rsE.getLong("location_id")));
				
				interests = new HashSet<Interest>();
				stmt = conn.createStatement();
				ResultSet rsE2 = stmt.executeQuery("SELECT * FROM \"Person-Interest\" WHERE person_id = "+person.getId());
				while(rsE2.next())
				{
					interests.add(interestDao.get(rsE2.getLong("interest_id")));	//
				}
				person.setInterests(interests);
				
				everyone.add(person);
			}
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return everyone;
	}
	
	public Set<Person> findInterestGroups()
	{
		Set<Person> groups = new HashSet<Person>();
		String command = "select person_id from (select interest_id from (select distinct \"Person\".firstname, \"Person\".lastname, \"Location\".city, \"Location\".state, \"Interest\".interest_id, \"Interest\".title from \"Person\" inner join \"Person-Interest\" on \"Person\".person_id = \"Person-Interest\".person_id inner join \"Location\" on \"Person\".location_id = \"Location\".location_id inner join \"Interest\" on \"Person-Interest\".interest_id = \"Interest\".interest_id where \"Person\".location_id = 1) as join_query group by interest_id having count(interest_id) > 1) as interestRS inner join \"Person-Interest\" on interestRS.interest_id = \"Person-Interest\".interest_id";
		
		try 
		{
			stmt = conn.createStatement();
			ResultSet rs3 = stmt.executeQuery(command);
			
			while(rs3.next())
			{
				groups.add(get(rs3.getLong("person_id")));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return groups;
	}
	
	public void closeConnection()
	{
		try {
            if(stmt != null) {
                stmt.close();
            }
            if(conn != null) {
                conn.close();
            }
            System.out.println("Connection closed");
            
		} catch(SQLException se){
            se.printStackTrace();
         }
	}

}

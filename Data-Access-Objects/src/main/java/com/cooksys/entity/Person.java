package com.cooksys.entity;

import java.util.Set;

public class Person {
	
	private Long id;
	private String firstName;
	private String lastName;
	private Location location;
	private Set<Interest> interests;
	
	public Person(){
		
	}
	
	public Person(String firstName,String lastName,Location location,Set<Interest> interests)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.location = location;
		this.interests = interests;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Set<Interest> getInterests() {
		return interests;
	}
	public void setInterests(Set<Interest> interests) {
		this.interests = interests;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + id);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", location=" + location
				+ ", interests=" + interests + "]\n";
	}
	
}
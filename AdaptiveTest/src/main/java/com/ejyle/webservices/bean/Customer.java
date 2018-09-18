package com.ejyle.webservices.bean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {
	private int id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String newPassword;
	private String email;
	private String phone;
	private String password;

	@JsonCreator
	public Customer(@JsonProperty("id") int id, @JsonProperty("firstName") String firstName,
			@JsonProperty("middleName") String middleName, @JsonProperty("lastName") String lastName,
			@JsonProperty("email") String email, @JsonProperty("phone") String phone,
			@JsonProperty("newPassword") String newPassword,
			@JsonProperty("password") String password) {
		this.id = id;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
		this.phone =phone;
		this.password = password;
		this.newPassword = newPassword;
		
	}

	public Customer() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", newPassword=" + newPassword + ", email=" + email + ", phone=" + phone + ", password="
				+ password + "]";
	}

}
	
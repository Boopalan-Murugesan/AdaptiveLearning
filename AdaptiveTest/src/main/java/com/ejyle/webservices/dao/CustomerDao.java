package com.ejyle.webservices.dao;

import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Repository;

import com.ejyle.webservices.bean.Customer;
import com.ejyle.webservices.services.EmailService;

@Repository("customerDao")
public class CustomerDao
{
	private JdbcTemplate jdbcTemplate;
    private EmailService emailService;

	@Autowired
	public void setDataSource(DataSource dataSource, EmailService emailService) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.emailService = emailService;
	}
	
	/* Get all the Customers */
	
	public List<Customer> getCustomers() 
	{
		List<Customer> customers = null;
		try 
		{
			customers = jdbcTemplate.query("SELECT * FROM users", new BeanPropertyRowMapper<Customer>(Customer.class));
		} 
		catch (DataAccessException e) 
		{
			e.printStackTrace();
		}
		return customers;
	}

	
	/* Get a particular Customer using ID */
	
	public Customer getCustomer(int ID) 
	{
		Customer customer = null;
		try 
		{
			customer = jdbcTemplate.queryForObject("SELECT * FROM users WHERE ID = ?", new Object[] { ID },
					new BeanPropertyRowMapper<Customer>(Customer.class));
		} 
		catch (DataAccessException e) 
		{
			e.printStackTrace();
		}
		return customer;
	}

	/* Create method for Customer */
	public int createCustomer(Customer customer) 
	{
		int count = jdbcTemplate.update(
				"INSERT INTO users(ID,FirstName,MiddleName,LastName,Email,Phone,Password)VALUES(?,?,?,?,?,?,?)",
				new Object[] { customer.getId(), customer.getFirstName(), customer.getMiddleName(),customer.getLastName(), customer.getEmail(), customer.getPhone(),
						customer.getPassword() });
		return count;
	}

	
	/* Delete method for Customer */
	public int deleteCustomer(int ID) 
	{
		int count = jdbcTemplate.update("DELETE from users WHERE ID = ?", new Object[] { ID });
		return count;
	}

	
	/* Update method for Customer */
	public int updateCustomer(Customer customer) 
	{
		int count = jdbcTemplate.update(
				"UPDATE users set FirstName = ? , MiddleName = ?,LastName = ?, Email=? ,Phone=? where ID = ?",
				new Object[] { customer.getFirstName(), customer.getMiddleName(), customer.getLastName(),
					 customer.getEmail(),
						customer.getPhone(), 
						customer.getPassword(),customer.getId() });
		return count;
	}

	
	/* Login method */
	public String Login(final Customer customer) 
	{
				String SELECT_PWD = "select Password from login where Email='" + customer.getEmail() + "'";
				String pwd = jdbcTemplate.queryForObject(SELECT_PWD, String.class);
				if (pwd.equals(customer.getPassword()))
					return "success";
				else
					return "fail";
	}

		
	/* Registration for Customer */
	public int Registration(Customer customer) 
	{
		int count = jdbcTemplate.update(
				"INSERT INTO users(FirstName,MiddleName, LastName, Email,Phone,Password)VALUES(?,?,?,?,?,?); INSERT INTO login(Email,Password) VALUES(?,?);",
				new Object[] { customer.getFirstName(), customer.getMiddleName(),
						customer.getLastName(), 
						customer.getEmail(), customer.getPhone(),customer.getPassword(),customer.getEmail(), customer.getPassword()
						  });
		return count;
	}

	/* Change Password */
	@SuppressWarnings("unused")
	public String ChangePassword(final Customer customer) 
	{
		String SELECT_PWD = "select Password from login where Email='" + customer.getEmail() + "'";
		String pwd = jdbcTemplate.queryForObject(SELECT_PWD, String.class);
		if (pwd.equals(customer.getPassword())) 
		{
			int count = jdbcTemplate.update(
					"UPDATE login set Password = ? where Email = ?;UPDATE users set Password = ? where Email=?",
					new Object[] { customer.getNewPassword(), customer.getEmail(), customer.getNewPassword(),
							customer.getEmail() });			
			
			return "sucess";
		} 
		else 
		{
			return "failiure";
		}
	}
	
	
	/* Forget Password */
	@SuppressWarnings("unused")
	public String Forget(Customer customer) 
	{
		String CheckMail = "SELECT Email FROM users WHERE Email='" + customer.getEmail() + "'";
		String mail;
		try 
		{
			mail = jdbcTemplate.queryForObject(CheckMail, String.class);
			if (mail.equals(customer.getEmail())) 
			{
				String Name = "select FirstName from users where Email='" + customer.getEmail() + "'";
				String F_Name = jdbcTemplate.queryForObject(Name, String.class);
				int count1 = jdbcTemplate.update(
						"UPDATE login set Password = ? where Email= ?;UPDATE users set Password = ? where Email =?",
						new Object[] { randomStr, customer.getEmail(), randomStr,customer.getEmail()});
			 /* Sending Mail */
				SimpleMailMessage registrationEmail = new SimpleMailMessage();
				registrationEmail.setTo(customer.getEmail());
				registrationEmail.setSubject("Temporary Password");
				registrationEmail.setText("Hi " + F_Name + ","
						+ "\n \n \n It looks like you requested a new password. \n\n If that sounds right, you can use this temporary password to login your account: "
						+ randomStr);
				registrationEmail.setFrom("boopalangc@gmail.com");
				emailService.sendEmail(registrationEmail);
				return "success";
			} 
			else 
			{
				return "Mail not sent";
			}
		  } 
			catch (DataAccessException e) 
		{
			return "Not a registered mail id";
		}
	}

	
	/* Method to generate 7 digit temporary password */
	String aToZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	String randomStr = generateRandom(aToZ);// 36 letter.
	
	private String generateRandom(String aToZ) 
	{
		Random rand = new Random();
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < 7; i++) {
			int randIndex = rand.nextInt(aToZ.length());
			temp.append(aToZ.charAt(randIndex));
	}
		return temp.toString();
	}
}
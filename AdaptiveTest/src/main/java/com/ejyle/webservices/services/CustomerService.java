package com.ejyle.webservices.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ejyle.webservices.bean.Customer;
import com.ejyle.webservices.dao.CustomerDao;
import com.ejyle.webservices.status.ProjectStatus;

@RestController
@Service("customerService")
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "access-control-allow-credentials,access-control-allow-origin,access-control-expose-headers,authorization,content-type,")

public class CustomerService 
{
	@Autowired
	private CustomerDao customerDao;

	@RequestMapping(value = "/customer", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Customer>> customers() 
	{
		HttpHeaders headers = new HttpHeaders();
		List<Customer> customers = customerDao.getCustomers();
		if (customers == null) 
		{
			return new ResponseEntity<List<Customer>>(HttpStatus.NOT_FOUND);
		}
		headers.add("Number Of Records Found", String.valueOf(customers.size()));
		return new ResponseEntity<List<Customer>>(customers, headers, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/customer/{ID}", method = RequestMethod.GET)
	public ResponseEntity<Customer> getCustomer(@PathVariable("ID") int ID) 
	{
		Customer customer = customerDao.getCustomer(ID);
		if (customer == null) 
		{
			return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/customer", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) 
	{
		HttpHeaders headers = new HttpHeaders();
		if (customer == null) 
			{
			return new ResponseEntity<Customer>(HttpStatus.BAD_REQUEST);
			}
		customerDao.createCustomer(customer);
		headers.add("Customer Created  - ", String.valueOf(customer.getId()));
		return new ResponseEntity<Customer>(customer, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/customer/{ID}", method = RequestMethod.PUT)
	public ResponseEntity<Customer> updateCustomer(@PathVariable("ID") int ID, @RequestBody Customer customer) 
	{
		HttpHeaders headers = new HttpHeaders();
		Customer isExist = customerDao.getCustomer(ID);
		if (isExist == null) 
		{
			return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
		} else if (customer == null) 
		{
			return new ResponseEntity<Customer>(HttpStatus.BAD_REQUEST);
		}
		customerDao.updateCustomer(customer);
		headers.add("Customer Updated  - ", String.valueOf(ID));
		return new ResponseEntity<Customer>(customer, headers, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/customer/delete/{ID}", method = RequestMethod.DELETE)
	public ResponseEntity<Customer> deleteCustomer(@PathVariable("ID") int ID) 
	{
		HttpHeaders headers = new HttpHeaders();
		Customer customer = customerDao.getCustomer(ID);
		if (customer == null) 
		{
			return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
		}
		customerDao.deleteCustomer(ID);
		headers.add("Customer Deleted - ", String.valueOf(ID));
		return new ResponseEntity<Customer>(customer, headers, HttpStatus.NO_CONTENT);
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Customer> Login(@RequestBody Customer customer) 
	{
		ResponseEntity response = null;
		try 
		{
			String loginResp = customerDao.Login(customer);
			response = new ResponseEntity<ProjectStatus>(new ProjectStatus (loginResp),HttpStatus.OK);
		} 
		catch (RuntimeException err) 
		{
			response = new ResponseEntity("Invalid", HttpStatus.SERVICE_UNAVAILABLE);
		}
		return response;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Customer> Registration(@RequestBody Customer customer) 
	{
		HttpHeaders headers = new HttpHeaders();
		if (customer == null) 
		{
			return new ResponseEntity<Customer>(HttpStatus.BAD_REQUEST);
		}
		customerDao.Registration(customer);
		headers.add("Registration done  - ", String.valueOf(customer.getId()));
		return new ResponseEntity<Customer>(customer, headers, HttpStatus.CREATED);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ResponseEntity<Customer> ChangePassword(@RequestBody Customer customer)
	{
		ResponseEntity response = null;
		try 
		{
			String pwdResp = customerDao.ChangePassword(customer);
			response = new ResponseEntity<ProjectStatus>(new ProjectStatus (pwdResp),HttpStatus.OK);
		} 
		catch (RuntimeException err) 
		{
			response = new ResponseEntity("Invalid", HttpStatus.SERVICE_UNAVAILABLE);
		}
		return response;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/forget", method = RequestMethod.POST)
	public ResponseEntity<Customer> Forget(@RequestBody Customer customer) 
	{
		ResponseEntity response = null;
		try 
		{
			String forgetResp = customerDao.Forget(customer);
			response = new ResponseEntity<ProjectStatus>(new ProjectStatus (forgetResp),HttpStatus.OK);
		} catch (RuntimeException err) 
		{
			response = new ResponseEntity("Invalid", HttpStatus.BAD_REQUEST);
		}
		return response;
	}
}
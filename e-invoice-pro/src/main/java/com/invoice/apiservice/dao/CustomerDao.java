package com.invoice.apiservice.dao;

import java.util.List;

import com.invoice.bean.db.Customer;

/**
 * 小票等单据计算
 * @author Baij
 */
public interface CustomerDao {

	public Customer getCustomer(Customer p);
	
	public List<Customer> getCustomerByName(Customer p);
	
	public Customer getWebCustomerByName(Customer p);

	public int setCustomer(Customer p);
	
	public int updateCustomer(Customer p);
	
	public int insertCustomer(Customer p);
	
	public int updateBzCustomer(Customer p);
	
	public int insertBzCustomer(Customer p);
	
	public Customer getWebBzCustomerByName(Customer p);
	
	public List<Customer> getBzCustomerByName(Customer p);
	
}

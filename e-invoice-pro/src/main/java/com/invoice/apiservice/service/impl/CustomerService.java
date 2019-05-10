package com.invoice.apiservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoice.apiservice.dao.CustomerDao;
import com.invoice.bean.db.Customer;

@Service
public class CustomerService {
	@Autowired
	CustomerDao dao;
	
	/**
	 * 根据用户电话等获取用户常用信息
	 * @param user
	 * @return
	 */
	public Customer getCustomer(Customer c) {
		return dao.getCustomer(c);
	}
	public List<Customer> getWebCustomer(Customer c) {
		return dao.getCustomerByName(c);
	}
	public Customer getWebCustomerByName(Customer c) {
		return dao.getWebCustomerByName(c);
	}
	public List<Customer> getBzCustomer(Customer c) {
		return dao.getBzCustomerByName(c);
	}
	public Customer getWebBzCustomerByName(Customer c) {
		return dao.getWebBzCustomerByName(c);
	}
	
	public Customer setCustomer(Customer c) {
		dao.setCustomer(c);
		return c;
	}
	public Customer addorupdate(Customer c) {
		Customer customer = dao.getWebCustomerByName(c);
		if(customer != null){
			dao.updateCustomer(c);
		}else{
			dao.insertCustomer(c);
		}
		return c;
	}
	public Customer addorBzupdate(Customer c) {
		Customer customer = dao.getWebBzCustomerByName(c);
		if(customer != null){
			dao.updateBzCustomer(c);
		}else{
			dao.insertBzCustomer(c);
		}
		return c;
	}
}

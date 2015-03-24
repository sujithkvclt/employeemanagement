package com.qburst.employeemanagement.service;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
	
	public void saveOrUpdateEmployee(Employee employee);

	public Employee getEmployee(Integer id);

	public void deleteEmployee(Integer id);

	public List<Employee> listEmployee();
	
	public void saveImageAsFile(Employee employee,MultipartFile file);
	
	public void validateEmployee(Employee employee,BindingResult bindingResult);
	
	public  ResponseEntity<byte[]> generatePdfReportAsByte(Employee employee);
	
}

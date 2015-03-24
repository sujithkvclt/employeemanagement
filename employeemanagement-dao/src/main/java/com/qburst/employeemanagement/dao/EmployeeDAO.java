package com.qburst.employeemanagement.dao;


import java.util.List;

public interface EmployeeDAO {

	public void saveOrUpdateEmployee(EmployeeEntity employee);

	public EmployeeEntity getEmployee(Integer id);

	public void deleteEmployee(Integer id);

	public List<EmployeeEntity> listEmployee();

}

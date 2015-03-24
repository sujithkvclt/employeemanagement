package com.qburst.employeemanagement.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public void saveOrUpdateEmployee(EmployeeEntity employeeEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(employeeEntity);
	}

	public List<EmployeeEntity> listEmployee() {
		List<EmployeeEntity> employeeList = new ArrayList<EmployeeEntity>();
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from EmployeeEntity where isdeleted is false");
		employeeList = query.list();
		return employeeList;
	}

	public EmployeeEntity getEmployee(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		EmployeeEntity employeeEntity = (EmployeeEntity) session.get(EmployeeEntity.class, id);
		return employeeEntity;
	}

	public void deleteEmployee(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		EmployeeEntity employee = (EmployeeEntity)session.load(EmployeeEntity.class, id);
		if (null != employee) {
			employee.setIsdeleted(true);
			sessionFactory.getCurrentSession().update(employee);
		}
	}

}

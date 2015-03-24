package com.qburst.employeemanagement.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.qburst.employeemanagement.dao.EmployeeDAO;
import com.qburst.employeemanagement.dao.EmployeeEntity;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeDAO employeeDAO;

	@Autowired
	private Validator validator;

	@Autowired
	private EmployeeServiceHelper employeeServiceHelper;

	@Transactional
	public void saveOrUpdateEmployee(Employee employee) {
		EmployeeEntity employeeEntity = employeeServiceHelper.map(employee,
				EmployeeEntity.class);
		employeeDAO.saveOrUpdateEmployee(employeeEntity);
	}

	@Transactional
	public Employee getEmployee(Integer id) {
		EmployeeEntity employeeEntity = employeeDAO.getEmployee(id);
		Employee employee = employeeServiceHelper.map(employeeEntity,
				Employee.class);
		return employee;
	}

	@Transactional
	public void deleteEmployee(Integer id) {
		employeeDAO.deleteEmployee(id);
	}

	@Transactional
	public List<Employee> listEmployee() {
		List<EmployeeEntity> employeeEntityList = employeeDAO.listEmployee();
		List<Employee> employeeList = employeeServiceHelper.mapCollection(
				employeeEntityList, Employee.class);
		return employeeList;
	}

	public void validateEmployee(Employee employee, BindingResult bindingResult) {
		EmployeeEntity employeeEntity = (EmployeeEntity) employeeServiceHelper
				.map(employee, EmployeeEntity.class);
		validator.validate(employeeEntity, bindingResult);
	}
	
	public ResponseEntity<byte[]> generatePdfReportAsByte(Employee employee) {
		EmployeePdfView employeePdfView = new EmployeePdfView(employee);
		byte[] pdfBytes = employeePdfView.generatePdfBytes();
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    String filename = employee.getName();
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(pdfBytes, headers, HttpStatus.OK);
		return responseEntity;
	}
	
	public void saveImageAsFile(Employee employee, MultipartFile file) {
		try {
			if (!file.isEmpty()) {

				String rootPath = System.getProperty("catalina.home");
				String fileExtension = getFileExtension(file);
				if (validateFileExtension(fileExtension)) {
					File newFile = new File(rootPath + "/image_uploads/"
							+ employee.getName() + "." + fileExtension);
					if (!newFile.exists())
						newFile.mkdirs();
					file.transferTo(newFile);
					employee.setImage("/uploads/" + employee.getName() + "."
							+ fileExtension);
				} else {
					if (employee.getImage() == null) {
						employee.setImage("/uploads/default.jpg");
					}
				}
			} else {
				if (employee.getImage().isEmpty()) {
					employee.setImage("/uploads/default.jpg");
				}

			}
		} catch (IOException e) {
		}
	}
	
	private boolean validateFileExtension(String extension) {
		if (extension.equals("jpg") || extension.equals("png")
				|| extension.equals("bmp") || extension.equals("gif")) {
			return true;
		} else {
			return false;
		}
	}
	private String getFileExtension(MultipartFile file){
		String[] originalFileName = StringUtils.split(
				file.getOriginalFilename(), ".");
		String fileExtension = originalFileName[originalFileName.length - 1];
		return fileExtension;		
	}
}

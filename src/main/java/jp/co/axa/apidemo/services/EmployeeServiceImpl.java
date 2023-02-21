package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> retrieveEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    public Employee getEmployeeById(Long employeeId) {
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        return optEmp.orElse(null);
    }

    public void createEmployee(Employee employee){
        employeeRepository.save(employee);
    }

    public void deleteEmployee(Long employeeId){
        employeeRepository.deleteById(employeeId);
    }

    public Employee updateEmployee(Employee orgEmployee, Employee tempEmployee) {
        updateEmployeeData(orgEmployee, tempEmployee);
        employeeRepository.save(orgEmployee);
        return orgEmployee;
    }

    /*
        Update name, salary, department of found employee in DB by tempEmployee
     */
    private void updateEmployeeData(Employee orgEmployee, Employee tempEmployee) {
        orgEmployee.setName(tempEmployee.getName());
        orgEmployee.setSalary(tempEmployee.getSalary());
        orgEmployee.setDepartment(tempEmployee.getDepartment());
    }
}
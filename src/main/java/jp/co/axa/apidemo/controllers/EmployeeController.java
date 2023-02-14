package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /*
        Get all employees
     */
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return ResponseEntity.ok(employees);
    }

    /*
        Get Employee by employeeId if exist
     */
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        Employee foundEmployee = employeeService.getEmployee(employeeId);
        if (foundEmployee != null) {
            return ResponseEntity.ok(foundEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*
        Create employee
     */
    @PostMapping("/employees")
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee){
        employeeService.saveEmployee(employee);
        System.out.println("Employee Saved Successfully");
        return ResponseEntity.ok(employee);
    }

    /*
        Delete employee by employeeId if exist
     */
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        if (employeeService.getEmployee((employeeId)) != null) {
            employeeService.deleteEmployee(employeeId);
            System.out.printf("Employee with Id %s Deleted Successfully%n", employeeId.toString());
        }
        return ResponseEntity.noContent().build();
    }

    /*
        Update employee by employeeId if exist
     */
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){
        Employee foundEmployee = employeeService.getEmployee(employeeId);
        if(foundEmployee != null){
            employeeService.updateEmployee(foundEmployee, employee);
            return ResponseEntity.ok(foundEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}

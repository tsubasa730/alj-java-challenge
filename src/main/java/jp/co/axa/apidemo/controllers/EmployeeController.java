package jp.co.axa.apidemo.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    @ApiOperation(value = "Get all employees")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @GetMapping("/employees")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return ResponseEntity.ok(employees);
    }

    /*
        Get Employee by employeeId if exist
     */
    @ApiOperation(value = "Get employee by employeeId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(name="employeeId")Long employeeId) {
        Employee foundEmployee = employeeService.getEmployeeById(employeeId);
        if (foundEmployee != null) {
            return ResponseEntity.ok(foundEmployee);
        } else {
            return ResponseEntity.ok(foundEmployee);
        }
    }

    /*
        Create employee
     */
    @ApiOperation(value = "Create employee")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Error"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @PreAuthorize("hasAuthority('CREATE')")
    @PostMapping("/create-employee")
    public ResponseEntity<Employee> saveEmployee(@RequestBody @Valid Employee employee){
        employeeService.createEmployee(employee);
        System.out.println("Employee Saved Successfully");
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    /*
        Delete employee by employeeId if exist
     */
    @ApiOperation(value = "Delete employee by employeeId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden") })
    @PreAuthorize("hasAuthority('DELETE')")
    @DeleteMapping("/delete-employee/{employeeId}")
    public ResponseEntity<Object> deleteEmployeeById(@PathVariable(name="employeeId")Long employeeId){
        if (employeeService.getEmployeeById((employeeId)) != null) {
            employeeService.deleteEmployee(employeeId);
            System.out.printf("Employee with Id %s Deleted Successfully%n", employeeId.toString());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    /*
        Update tempEmployee by employeeId if exist
     */
    @ApiOperation(value = "Update tempEmployee by employeeId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found") })
    @PreAuthorize("hasAuthority('UPDATE')")
    @PutMapping("/update-employee/{employeeId}")
    public ResponseEntity<Employee> updateEmployeeById(@RequestBody Employee tempEmployee,
                                                       @PathVariable(name="employeeId")Long employeeId){
        Employee foundEmployee = employeeService.getEmployeeById(employeeId);
        if(foundEmployee != null){
            Employee updatedEmployee = employeeService.updateEmployee(foundEmployee, tempEmployee);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } else {
            return ResponseEntity.ok(foundEmployee);
        }

    }

}

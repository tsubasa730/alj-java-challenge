package jp.co.axa.apidemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.controllers.EmployeeController;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiDemoApplicationTests {

	private MockMvc mockMvc;

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private EmployeeService employeeService;

	@InjectMocks
	private EmployeeController employeeController;

	Employee EMPLOYEE_1 = new Employee(1L, "Amy", 15000, "IT");
	Employee EMPLOYEE_2 = new Employee(2L, "Billy", 20000, "Account");
	Employee EMPLOYEE_3 = new Employee(3L, "Candy", 30000, "Human Resource");

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
	}

	@Test
	public void getAllRecords() throws Exception {
		List<Employee> employees = new ArrayList<>(Arrays.asList(EMPLOYEE_1, EMPLOYEE_2, EMPLOYEE_3));

		when(employeeService.retrieveEmployees()).thenReturn(employees);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v1/employees")
				.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].name", is(EMPLOYEE_1.getName())))
				.andExpect(jsonPath("$[0].salary", is(EMPLOYEE_1.getSalary())))
				.andExpect(jsonPath("$[0].department", is(EMPLOYEE_1.getDepartment())))
				.andExpect(jsonPath("$[1].name", is(EMPLOYEE_2.getName())))
				.andExpect(jsonPath("$[1].salary", is(EMPLOYEE_2.getSalary())))
				.andExpect(jsonPath("$[1].department", is(EMPLOYEE_2.getDepartment())))
				.andExpect(jsonPath("$[2].name", is(EMPLOYEE_3.getName())))
				.andExpect(jsonPath("$[2].salary", is(EMPLOYEE_3.getSalary())))
				.andExpect(jsonPath("$[2].department", is(EMPLOYEE_3.getDepartment())));
	}

	@Test
	public void createEmployee() throws Exception {
		doNothing().when(employeeService).createEmployee(EMPLOYEE_1);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/v1/employees")
						.content(objectMapper.writeValueAsString(EMPLOYEE_1))
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.name", is(EMPLOYEE_1.getName())))
				.andExpect(jsonPath("$.salary", is(EMPLOYEE_1.getSalary())))
				.andExpect(jsonPath("$.department", is(EMPLOYEE_1.getDepartment())));
	}

	@Test
	public void deleteEmployeeSuccess() throws Exception {
		when(employeeService.getEmployeeById(EMPLOYEE_1.getId())).thenReturn(EMPLOYEE_1);
		doNothing().when(employeeService).deleteEmployee(EMPLOYEE_1.getId());

		mockMvc.perform(
						MockMvcRequestBuilders.delete("/api/v1/employees/" + EMPLOYEE_1.getId())
				)
				.andExpect(status().isOk());
	}

	@Test
	public void deleteEmployeeNoContent() throws Exception {
		when(employeeService.getEmployeeById(EMPLOYEE_1.getId())).thenReturn(null);
		doNothing().when(employeeService).deleteEmployee(EMPLOYEE_1.getId());

		mockMvc.perform(
						MockMvcRequestBuilders.delete("/api/v1/employees/" + EMPLOYEE_1.getId())
				)
				.andExpect(status().isNoContent());
	}

	@Test
	public void updateEmployee() throws Exception {
		Employee updateEmployee = new Employee(EMPLOYEE_1.getId(), EMPLOYEE_1.getName(), 99999, "Security");

		when(employeeService.getEmployeeById(EMPLOYEE_1.getId())).thenReturn(EMPLOYEE_1);
		when(employeeService.updateEmployee(eq(EMPLOYEE_1), any(Employee.class))).thenReturn(updateEmployee);

		mockMvc.perform(
						MockMvcRequestBuilders.put("/api/v1/employees/" + EMPLOYEE_1.getId())
								.content(objectMapper.writeValueAsString(updateEmployee))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.name", is(EMPLOYEE_1.getName())))
				.andExpect(jsonPath("$.salary", is(updateEmployee.getSalary())))
				.andExpect(jsonPath("$.department", is(updateEmployee.getDepartment())));
	}
}

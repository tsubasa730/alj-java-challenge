package jp.co.axa.apidemo.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="EMPLOYEE")
public class Employee {

    @ApiModelProperty(notes = "id", example = "1", required = true)
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "name", example = "Mary", required = true)
    @Getter
    @Setter
    @NotBlank(message = "name is mandatory")
    @Column(name="EMPLOYEE_NAME", nullable = false)
    private String name;

    @ApiModelProperty(notes = "salary", example = "10000")
    @Getter
    @Setter
    @Column(name="EMPLOYEE_SALARY")
    private Integer salary = 0;

    @ApiModelProperty(notes = "department", example = "Account")
    @Getter
    @Setter
    @Column(name="DEPARTMENT")
    private String department = "";

}

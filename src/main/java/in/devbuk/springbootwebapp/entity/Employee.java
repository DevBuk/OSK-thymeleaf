package in.devbuk.springbootwebapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * The class represents an instructor-employee.
 *
 */
@Entity
@Table(name = "tbl_employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private Integer age;
    @Column
    private String description;
    @Column
    private String categories;

    @OneToMany(mappedBy = "employee")
    private List<Day> daysOfEmployee;
}

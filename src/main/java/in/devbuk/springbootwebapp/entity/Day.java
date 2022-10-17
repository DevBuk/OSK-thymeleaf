package in.devbuk.springbootwebapp.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * The class represents workday for an instructor-employee.
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "day")
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private java.sql.Date drivingDay;

    @ManyToOne
    private Employee employee;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "day_id")
    private List<Hour> hour;
}



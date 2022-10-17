package in.devbuk.springbootwebapp.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class represents a customer-user.
 *
 */
@Entity
@Table
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column
    private String name;

    @Column
    private String surname;

    @Column(nullable = false)
    private String password;

    @Column
    private int active;

    @Column
    private String roles = "";

    public User(String username, String name, String surname, String password, String roles){
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.roles = roles;
        this.active = 1;
    }

    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}

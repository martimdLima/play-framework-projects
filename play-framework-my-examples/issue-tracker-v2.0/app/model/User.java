package model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQueries({
        @NamedQuery(name = "User.getAll", query = "SELECT u FROM User u")
})
public class User {
    @Id
    public Long id;
    public String name;
    protected String email;
    protected String password;
    @Transient
    public List<Issue> issueList;

    /*
    public User() {
    }

    public User(String name, String email, String password, List<Issue> issueList) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.issueList = issueList;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.issueList = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String name;
    protected String email;
    protected String password;
    @Transient
    public List<Issue> issueList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }*/
}

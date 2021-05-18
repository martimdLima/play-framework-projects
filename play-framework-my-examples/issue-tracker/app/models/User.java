package models.userPersistence;

import play.data.validation.Constraints.*;
import io.ebean.Model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class User extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Required(message="You must add a name")
    @MaxLength(value=10, message = "Your name should have less than 10 characters")
    public String name;

    @Required(message="You must add an email")
    @Email()
    protected String email;

    @Required(message="You must add a password between 6 and 20 characters long")
    @MinLength(value=6, message="You must add a password with at least 6 characters")
    @MaxLength(value=20, message="You must add a password with less than 20 characters")
    protected String password;
    @Transient
    public List<Integer> issueList;
    public Date created_at;
    public Date updated_at;

    public User() {
    }

    public User(Date created_at, Date updated_at) {
        super();
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public User(Long id, String name, String email, String password) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.issueList = new ArrayList<>();
        this.created_at = new Timestamp(new Date().getTime());;
    }

    public User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.issueList = new ArrayList<>();
        this.created_at = new Timestamp(new Date().getTime());;
    }

    @Override
    public String toString() {
        return id + ": " + name + " [" + email +"]" + created_at;
    }

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
    }

    public List<Integer> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<Integer> issueList) {
        this.issueList = issueList;
    }
}

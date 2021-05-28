package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * User entity managed by Ebean
 */
@Entity
public class User extends BaseModel {

    private static final long serialVersionUID = 1L;
    //@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @ManyToMany(fetch = FetchType.LAZY)
    private final List<Issue> issues = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private final List<Comment> comments = new ArrayList<>();
    @Constraints.Required
    public String name;
    @Constraints.Required
    public String email;
    @Constraints.Required
    public String password;

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
}


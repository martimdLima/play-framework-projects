package models;

import java.util.List;

public class UserForm {

    private Long id;
    private String name;
    private String email;
    private String password;
    public List<Integer> issueList;


    public UserForm() {
    }

    public Long getId() {
        return id;
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

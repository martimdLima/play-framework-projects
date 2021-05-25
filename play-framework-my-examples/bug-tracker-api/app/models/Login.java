package models;

import play.data.validation.Constraints;

public class Login extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String email;

    @Constraints.Required
    public String password;

    public Login() {
    }

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
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
package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;

/**
 * User entity managed by Ebean
 */
@Entity 
public class User extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String email;

    @Constraints.Required
    public String password;

}


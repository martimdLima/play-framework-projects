package models.bo;

import models.User;

import java.util.ArrayList;
import java.util.List;

public class UserBO {

    public long id;

    public String name;

    public String email;

    public String password;


    public static UserBO generate(User user) {
        UserBO result = new UserBO();

        result.id = user.id;
        result.name = user.name;
        result.email = user.email;
        result.password = user.password;
        ;

        return result;
    }

    public static List<UserBO> generate(List<User> users) {

        List<UserBO> result = new ArrayList<>();

        for (User c : users) {
            result.add(UserBO.generate(c));
        }

        return result;
    }
}

package services;

import com.google.inject.ImplementedBy;
import model.User;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(UserServiceImpl.class)
public interface UserService {
    CompletionStage<Optional<User>> create(User user);
    CompletionStage<Optional<User>> getById(String userId);
    CompletionStage<List<User>> getAll();
    CompletionStage<Optional<User>> update(User user);
    CompletionStage<Optional<User>> delete(String userId);

}

package repository;


import com.google.inject.ImplementedBy;
import model.User;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(UserRepositoryImpl.class)
public interface UserRepository {
    CompletionStage<Optional<User>> createUser(User user);
    CompletionStage<Optional<User>> updateUser(User user);
    CompletionStage<Optional<User>> getUserById(String userId);
    CompletionStage<Optional<User>> deleteUserById(String userId);
    CompletionStage<List<User>> getAll();

}

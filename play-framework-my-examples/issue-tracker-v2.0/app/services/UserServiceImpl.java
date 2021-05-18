package services;

import model.User;
import repository.UserRepository;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;


    @Inject
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }


    @Override
    public CompletionStage<Optional<User>> create(User user) {
        return userRepository.createUser(user);
    }

    @Override
    public CompletionStage<Optional<User>> getById(String userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public CompletionStage<List<User>> getAll() {
        return userRepository.getAll();
    }

    @Override
    public CompletionStage<Optional<User>> update(User user) {
        return userRepository.updateUser(user);
    }

    @Override
    public CompletionStage<Optional<User>> delete(String userId) {
        return userRepository.deleteUserById(userId);
    }

}

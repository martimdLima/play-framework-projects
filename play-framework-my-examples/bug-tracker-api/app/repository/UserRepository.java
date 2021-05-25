package repository;

import io.ebean.*;
import models.User;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class UserRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public UserRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    /**
     * Return a paged list of User
     *
     * @param page     Page to display
     * @param pageSize Number of users per page
     * @param sortBy   Computer property used for sorting
     * @param order    Sort order (either or asc or desc)
     * @param filter   Filter applied on the name column
     * @return CCompletionStage<PagedList < User>>
     */
    public CompletionStage<PagedList<User>> page(int page, int pageSize, String sortBy, String order, String filter) {
        return supplyAsync(() ->
                ebeanServer.find(User.class).where()
                        .ilike("name", "%" + filter + "%")
                        .orderBy(sortBy + " " + order)
                        .setFirstRow(page * pageSize)
                        .setMaxRows(pageSize)
                        .findPagedList(), executionContext);
    }

    /**
     * Return a list of User
     *
     * @ return CompletionStage<List<User>>
     */
    public CompletionStage<List<User>> list() {
        return supplyAsync(() ->
                ebeanServer.find(User.class).findList(), executionContext);
    }

    /**
     * Retrieve a specific User
     *
     * @param id The id of the user to retrieve
     * @return CompletionStage<Optional < User>>
     */
    public CompletionStage<Optional<User>> lookup(Long id) {
        return supplyAsync(() -> Optional.ofNullable(ebeanServer.find(User.class).setId(id).findOne()), executionContext);
    }

    /**
     * Retrieve a specific User by email
     *
     * @param email The email of the user to retrieve
     * @return CompletionStage<Optional < User>>
     */
    public CompletionStage<Optional<User>> lookupByEmail(String email) {
        return supplyAsync(() -> Optional.ofNullable(ebeanServer.find(User.class).setId(email).findOne()), executionContext);
    }

    /**
     * Retrieve a specific User by email
     *
     * @param email The id of the user to retrieve
     * @return CompletionStage<Optional < User>>
     */
    public CompletionStage<User> retrieveUserByEmail(String email) {
        return supplyAsync(() -> {
            List<User> users = ebeanServer.find(User.class).findList();

            User foundUser = new User();
            for (int i = 0; i < users.size(); i++) {

                if (users.get(i).email.equals(email)) {
                    foundUser = users.get(i);
                }
            }

            return foundUser;

        }, executionContext);
    }

    /**
     * Creates a new User
     *
     * @param user
     * @return
     */
    public CompletionStage<Long> insert(User user) {
        return supplyAsync(() -> {
            user.id = System.currentTimeMillis(); // not ideal, but it works
            ebeanServer.insert(user);
            return user.id;
        }, executionContext);
    }

    /**
     * Updates a specific User
     *
     * @param id
     * @param newUserData
     * @return CompletionStage<Optional < Long>>
     */
    public CompletionStage<Optional<Long>> update(Long id, User newUserData) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<Long> value = Optional.empty();
            try {
                User savedUser = ebeanServer.find(User.class).setId(id).findOne();
                if (savedUser != null) {
                    savedUser.name = newUserData.name;
                    savedUser.email = newUserData.email;
                    savedUser.password = newUserData.password;

                    savedUser.update();
                    txn.commit();
                    value = Optional.of(id);
                }
            } finally {
                txn.end();
            }
            return value;
        }, executionContext);
    }

    /**
     * Deletes a specific User
     *
     * @param id
     * @return CompletionStage<Optional < Long>>
     */
    public CompletionStage<Optional<Long>> delete(Long id) {
        return supplyAsync(() -> {
            try {
                final Optional<User> userOptional = Optional.ofNullable(ebeanServer.find(User.class).setId(id).findOne());
                userOptional.ifPresent(Model::delete);
                return userOptional.map(c -> c.id);
            } catch (Exception e) {
                return Optional.empty();
            }
        }, executionContext);
    }

    /**
     * Returns a specific User
     *
     * @param id
     * @return User
     */
    public User getUser(long id) {
        return ebeanServer.find(User.class).setId(id).findOne();
    }

    /**
     * Returns a List of User
     *
     * @return List<User>
     */
    public Map<String, String> getUsers() throws ExecutionException, InterruptedException {
        Map<String, String> users = supplyAsync(() -> ebeanServer.find(User.class).orderBy("name").findList(), executionContext).thenApply(list -> {
            HashMap<String, String> options = new LinkedHashMap<String, String>();
            for (User c : list) {
                options.put(c.id.toString(), c.name);
            }
            return options;
        }).get();

        return users;
    }
}

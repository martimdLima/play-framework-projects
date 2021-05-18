package repository;


import model.DatabaseExecutionContext;
import model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public class UserRepositoryImpl implements UserRepository {
    private JPAApi jpaApi;
    private DatabaseExecutionContext executionContext;

    @Inject
    public UserRepositoryImpl(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    @Override
    public CompletionStage<Optional<User>> createUser(User user) {
        return supplyAsync(() -> wrap(entityManager -> {
            user.setId(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
            entityManager.persist(user);
            return Optional.ofNullable(user);
        }), executionContext);
    }

    @Override
    public CompletionStage<Optional<User>> updateUser(User user) {
        return supplyAsync(() -> wrap(entityManager -> {
            entityManager.merge(user);
            return Optional.ofNullable(user);
        }), executionContext);
    }

    @Override
    public CompletionStage<Optional<User>> getUserById(String userId) {
        return supplyAsync(() -> wrap(entityManager ->
                Optional.ofNullable(entityManager.find(User.class, userId))), executionContext);
    }

    @Override
    public CompletionStage<Optional<User>> deleteUserById(String userId) {
        return supplyAsync(() -> wrap(entityManager -> {
            final User user = entityManager.find(User.class, userId);
            if(user != null) entityManager.remove(user);
            return Optional.ofNullable(user);
        }), executionContext);
    }

    @Override
    public CompletionStage<List<User>> getAll() {
        return supplyAsync(() -> wrap(entityManager -> {
            final Session session = entityManager.unwrap(Session.class);
            final TypedQuery<User> query = session.createNamedQuery("User.getAll", User.class);
            return query.getResultList();
        }), executionContext);
    }
}

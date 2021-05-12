package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */
public class JPAIssueRepository implements IssueRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAIssueRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Issue> add(Issue issue) {
        return supplyAsync(() -> wrap(em -> insert(em, issue)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Issue>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Issue insert(EntityManager em, Issue issue) {
        em.persist(issue);
        return issue;
    }

    private Stream<Issue> list(EntityManager em) {
        List<Issue> issues = em.createQuery("select i from Issue i", Issue.class).getResultList();
        System.out.println(issues);
        return issues.stream();
    }
}



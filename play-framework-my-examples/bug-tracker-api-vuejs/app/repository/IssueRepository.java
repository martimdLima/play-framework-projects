package repository;

import io.ebean.*;
import models.Issue;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class IssueRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public IssueRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    /**
     * Return a paged list of issue
     *
     * @param page     Page to display
     * @param pageSize Number of issues per page
     * @param sortBy   Computer property used for sorting
     * @param order    Sort order (either or asc or desc)
     * @param filter   Filter applied on the name column
     * @return CCompletionStage<PagedList < Issue>>
     */
    public CompletionStage<PagedList<Issue>> page(int page, int pageSize, String sortBy, String order, String filter) {
        return supplyAsync(() ->
                ebeanServer.find(Issue.class).where()
                        .ilike("name", "%" + filter + "%")
                        .orderBy(sortBy + " " + order)
                        .fetch("user")
                        .setFirstRow(page * pageSize)
                        .setMaxRows(pageSize)
                        .findPagedList(), executionContext);
    }

    /**
     * Return a list of Issue
     *
     * @ return CompletionStage<List<Issue>>
     */
    public CompletionStage<List<Issue>> list() {
        return supplyAsync(() ->
                ebeanServer.find(Issue.class).findList(), executionContext);
    }

    /**
     * Retrieve a specific Issue
     *
     * @param id The id of the issue to retrieve
     * @return CompletionStage<Optional < Issue>>
     */
    public CompletionStage<Optional<Issue>> lookup(Long id) {
        return supplyAsync(() -> Optional.ofNullable(ebeanServer.find(Issue.class).setId(id).findOne()), executionContext);
    }

    /**
     * Creates a new Issue
     *
     * @param issue
     * @return
     */
    public CompletionStage<Long> insert(Issue issue) {
        return supplyAsync(() -> {
            issue.id = System.currentTimeMillis(); // not ideal, but it works
            ebeanServer.insert(issue);
            return issue.id;
        }, executionContext);
    }

    /**
     * Updates a specific Issue
     *
     * @param id
     * @param newIssueData
     * @return CompletionStage<Optional < Long>>
     */
    public CompletionStage<Optional<Long>> update(Long id, Issue newIssueData) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<Long> value = Optional.empty();
            try {
                Issue savedIssue = ebeanServer.find(Issue.class).setId(id).findOne();
                if (savedIssue != null) {
                    //savedIssue.user = newIssueData.user;
                    savedIssue.discontinued = newIssueData.discontinued;
                    savedIssue.introduced = newIssueData.introduced;
                    savedIssue.name = newIssueData.name;
                    savedIssue.description = newIssueData.description;
                    savedIssue.summary = newIssueData.summary;
                    savedIssue.application = newIssueData.application;
                    savedIssue.category = newIssueData.category;
                    savedIssue.status = newIssueData.status;
                    savedIssue.user = newIssueData.user;

                    savedIssue.update();
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
     * Deletes a specific Issue
     *
     * @param id
     * @return CompletionStage<Optional < Long>>
     */
    public CompletionStage<Optional<Long>> delete(Long id) {
        return supplyAsync(() -> {
            try {
                final Optional<Issue> issueOptional = Optional.ofNullable(ebeanServer.find(Issue.class).setId(id).findOne());
                issueOptional.ifPresent(Model::delete);
                return issueOptional.map(c -> c.id);
            } catch (Exception e) {
                return Optional.empty();
            }
        }, executionContext);
    }

    /**
     * Returns a List of Issue
     *
     * @return List<Issue>
     */
    public List<Issue> getIssues() {
        return ebeanServer.find(Issue.class).findList();
    }

    /**
     * Returns a specific Issue
     *
     * @param id
     * @return Issue
     */
    public Issue getIssue(long id) {
        return ebeanServer.find(Issue.class).setId(id).findOne();
    }

}

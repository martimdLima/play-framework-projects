package repository;

import io.ebean.*;
import models.Comment;
import models.Issue;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CommentRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public CommentRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    /**
     * Return a paged list of Comment
     *
     * @param page     Page to display
     * @param pageSize Number of comments per page
     * @param sortBy   Computer property used for sorting
     * @param order    Sort order (either or asc or desc)
     * @param filter   Filter applied on the name column
     * @return CCompletionStage<PagedList < Comment>>
     */
    public CompletionStage<PagedList<Comment>> page(long id, int page, int pageSize, String sortBy, String order, String filter) {
        String issueToFilter = Optional.ofNullable(ebeanServer.find(Issue.class).setId(id).findOne()).get().name;
        return supplyAsync(() ->
                ebeanServer.find(Comment.class).where()
                        .eq("issue.name", issueToFilter)
                        .ilike("message", "%" + filter + "%")
                        .orderBy(sortBy + " " + order)
                        .setFirstRow(page * pageSize)
                        .setMaxRows(pageSize)
                        .findPagedList(), executionContext);
    }

    /**
     * Return a list of Comment
     *
     * @ return CompletionStage<List<Comment>>
     */
    public CompletionStage<List<Comment>> list() {
        return supplyAsync(() ->
                ebeanServer.find(Comment.class).findList(), executionContext);
    }

    /**
     * Creates a new Comment
     *
     * @param comment
     * @return
     */
    public CompletionStage<Long> insert(Comment comment) {
        return supplyAsync(() -> {
            comment.id = System.currentTimeMillis(); // not ideal, but it works
            ebeanServer.insert(comment);
            return comment.id;
        }, executionContext);
    }

    /**
     * Retrieve a specific Comment
     *
     * @param id The id of the comment to retrieve
     * @return CompletionStage<Optional < Comment>>
     */
    public CompletionStage<Optional<Comment>> lookup(Long id) {
        return supplyAsync(() -> Optional.ofNullable(ebeanServer.find(Comment.class).setId(id).findOne()), executionContext);
    }

    /**
     * Updates a specific Comment
     *
     * @param id
     * @param newCommentData
     * @return CompletionStage<Optional < Long>>
     */
    public CompletionStage<Optional<Long>> update(Long id, Comment newCommentData) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<Long> value = Optional.empty();
            try {
                Comment savedComment = ebeanServer.find(Comment.class).setId(id).findOne();

                if (savedComment != null) {
                    savedComment.message = newCommentData.message;
                    //savedComment.user = newCommentData.user;
                    savedComment.updated = newCommentData.updated;

                    savedComment.update();
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
     * Deletes a specific Comment
     *
     * @param id
     * @return CompletionStage<Optional < Long>>
     */
    public CompletionStage<Optional<Long>> delete(Long id) {
        return supplyAsync(() -> {
            try {
                final Optional<Comment> issueOptional = Optional.ofNullable(ebeanServer.find(Comment.class).setId(id).findOne());
                issueOptional.ifPresent(Model::delete);
                return issueOptional.map(c -> c.id);
            } catch (Exception e) {
                return Optional.empty();
            }
        }, executionContext);
    }
}

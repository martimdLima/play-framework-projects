package repository;

import io.ebean.*;
import models.Comment;
import models.Issue;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
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

    public CompletionStage<PagedList<Comment>> pageAll(int page, int pageSize, String sortBy, String order, String filter) {
        return supplyAsync(() ->
                ebeanServer.find(Comment.class).where()
                        .ilike("message", "%" + filter + "%")
                        .orderBy(sortBy + " " + order)
                        .setFirstRow(page * pageSize)
                        .setMaxRows(pageSize)
                        .findPagedList(), executionContext);
    }

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

    public CompletionStage<Long> insert(Comment comment) {
        return supplyAsync(() -> {
            comment.id = System.currentTimeMillis(); // not ideal, but it works
            ebeanServer.insert(comment);
            return comment.id;
        }, executionContext);
    }

    public CompletionStage<Optional<Comment>> lookup(Long id) {
        return supplyAsync(() -> Optional.ofNullable(ebeanServer.find(Comment.class).setId(id).findOne()), executionContext);
    }

    public CompletionStage<Optional<Long>> update(Long id, Comment newCommentData) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            Optional<Long> value = Optional.empty();
            try {
                Comment savedComment = ebeanServer.find(Comment.class).setId(id).findOne();

                if (savedComment != null) {
                    savedComment.message = newCommentData.message;
                    savedComment.user = newCommentData.user;
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

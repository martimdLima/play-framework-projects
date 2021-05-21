package controllers;

import models.Comment;
import models.Issue;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import repository.CommentRepository;
import repository.IssueRepository;
import repository.UserRepository;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static play.mvc.Results.ok;

public class CommentController {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;

    /**
     * This result directly redirect to Comment list.
     */
    private final Result GO_HOME = Results.redirect(
            routes.IssueController.list(0, "name", "asc", "")
    );

    @Inject
    public CommentController(IssueRepository issueRepository, UserRepository userRepository, CommentRepository commentRepository, FormFactory formFactory, HttpExecutionContext httpExecutionContext, MessagesApi messagesApi) {
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.formFactory = formFactory;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }


    /**
     * @param request
     * @param id
     * @param page
     * @param sortBy
     * @param order
     * @param filter
     * @return
     */
    public CompletionStage<Result> list(Http.Request request, long id, int page, String sortBy, String order, String filter) {
        // Run a db operation in another thread (using DatabaseExecutionContext)
        return commentRepository.page(id, page, 10, sortBy, order, filter).thenApplyAsync(comments -> {
            // This is the HTTP rendering thread context
            return ok(views.html.comments.list.render(comments, id, sortBy, order, filter, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }


    /**
     * @param request
     * @param id
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public CompletionStage<Result> create(Http.Request request, long id) throws ExecutionException, InterruptedException {
        Form<Comment> commentForm = formFactory.form(Comment.class);

        final Map<String, String> users = userRepository.getUsers();
        final Map<String, String> issues = new HashMap<>();
        issues.put(issueRepository.lookup(id).toCompletableFuture().join().get().id.toString(), issueRepository.lookup(id).toCompletableFuture().join().get().name);

        // Run issues db operation and then render the form
        return commentRepository.lookup(id).thenApplyAsync((nul) -> {
            // This is the HTTP rendering thread context
            return ok(views.html.comments.createForm.render(id, commentForm, users, issues, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }


    /**
     * Handle the 'new Comment form' submission
     *
     * @param request
     * @param id
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public CompletionStage<Result> save(Http.Request request, Long id) throws ExecutionException, InterruptedException {
        Form<Comment> commentForm = formFactory.form(Comment.class).bindFromRequest(request);

        final Map<String, String> users = userRepository.getUsers();
        final Map<String, String> issues = new HashMap<>();
        issues.put(issueRepository.lookup(id).toCompletableFuture().join().get().id.toString(), issueRepository.lookup(id).toCompletableFuture().join().get().name);

        if (commentForm.hasErrors()) {
            // Run issues db operation and then render the form
            return issueRepository.page(0, 10, "name", "asc", "").thenApplyAsync(nul -> {
                // This is the HTTP rendering thread context
                return ok(views.html.comments.createForm.render(id, commentForm, users, issues, request, messagesApi.preferred(request)));
            }, httpExecutionContext.current());
        }

        Comment comment = commentForm.get();

        // Run insert db operation, then redirect
        return commentRepository.insert(comment).thenApplyAsync(data -> {
            // This is the HTTP rendering thread context
            return GO_HOME
                    .flashing("success", "Comment " + comment.id + " has been created");
        }, httpExecutionContext.current());
    }


    /**
     * Handle the 'edit form' submission
     *
     * @param request
     * @param id      Id of the issue to edit
     * @return
     * @throws PersistenceException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public CompletionStage<Result> update(Http.Request request, Long id) throws PersistenceException, ExecutionException, InterruptedException {
        Form<Comment> commentForm = formFactory.form(Comment.class).bindFromRequest(request);

        final Map<String, String> users = userRepository.getUsers();
        final Map<String, String> issues = new HashMap<>();
        Issue issue = issueRepository.lookup(commentRepository.lookup(id).toCompletableFuture().join().get().issue.id).toCompletableFuture().join().get();
        issues.put(issue.id.toString(), issue.name);

        if (commentForm.hasErrors()) {
            // Run comments db operation and then render the failure case
            return commentRepository.lookup(id).thenApplyAsync(nul -> {
                // This is the HTTP rendering thread context
                return ok(views.html.comments.createForm.render(id, commentForm, users, issues, request, messagesApi.preferred(request)));
            }, httpExecutionContext.current());
        } else {
            Comment newCommentData = commentForm.get();
            // Run update operation and then flash and then redirect
            return commentRepository.update(id, newCommentData).thenApplyAsync(data -> {
                // This is the HTTP rendering thread context
                return GO_HOME
                        .flashing("success", "Comment " + id + " for issue " + issue.name + " has been updated");
            }, httpExecutionContext.current());
        }
    }


    /**
     * Display the 'edit form' of a existing comment.
     *
     * @param request
     * @param id      Id of the comment to edit
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public CompletionStage<Result> edit(Http.Request request, Long id) throws ExecutionException, InterruptedException {

        final Map<String, String> users = userRepository.getUsers();
        final Map<String, String> issues = new HashMap<>();

        Issue issue = issueRepository.lookup(commentRepository.lookup(id).toCompletableFuture().join().get().issue.id).toCompletableFuture().join().get();

        issues.put(issue.id.toString(), issue.name);


        // Run the lookup also in another thread, then combine the results:
        return commentRepository.lookup(id).thenApplyAsync((commentOptional) -> {
            // This is the HTTP rendering thread context
            Comment comment = commentOptional.get();
            Form<Comment> commentForm = formFactory.form(Comment.class).fill(comment);
            return ok(views.html.comments.editForm.render(id, commentForm, users, issues, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }


    /**
     * Handle user deletion
     *
     * @param id
     * @return
     */
    public CompletionStage<Result> delete(Long id) {
        // Run delete db operation, then redirect
        return commentRepository.delete(id).thenApplyAsync(v -> {
            // This is the HTTP rendering thread context
            return GO_HOME
                    .flashing("success", "Comment has been deleted");
        }, httpExecutionContext.current());
    }
}

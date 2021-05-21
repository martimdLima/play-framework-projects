package controllers;

import models.Comment;
import models.Comment;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

public class CommentController {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;

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
     * This result directly redirect to Comment list.
     */
    private Result GO_HOME = Results.redirect(
            routes.IssueController.list(0, "name", "asc", "")
    );

    public CompletionStage<Result> listUserComments(Http.Request request, long id, int page, String sortBy, String order, String filter) {
        // Run a db operation in another thread (using DatabaseExecutionContext)
        return commentRepository.page(id, page, 10, sortBy, order, filter).thenApplyAsync(list -> {
            // This is the HTTP rendering thread context
            return ok(views.html.comments.list.render(list, sortBy, order, filter, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> create(Http.Request request, long id) {
        Form<Comment> commentForm = formFactory.form(Comment.class);

        final Map<String, String> users = getUsers();
        final Map<String, String> issues = new HashMap<>();
        issues.put(issueRepository.lookup(id).toCompletableFuture().join().get().id.toString(), issueRepository.lookup(id).toCompletableFuture().join().get().name);

        // Run issues db operation and then render the form
        return userRepository.options().thenApplyAsync((nul) -> {
            // This is the HTTP rendering thread context
            return ok(views.html.comments.createForm.render(id, commentForm, users, issues, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    /**
     * Handle the 'new Comment form' submission
     */
    public CompletionStage<Result> save(Http.Request request, Long id) {
        Form<Comment> commentForm = formFactory.form(Comment.class).bindFromRequest(request);

        final Map<String, String> users = getUsers();
        final Map<String, String> issues = new HashMap<>();
        issues.put(issueRepository.lookup(id).toCompletableFuture().join().get().id.toString(), issueRepository.lookup(id).toCompletableFuture().join().get().name);

        if (commentForm.hasErrors()) {
            // Run issues db operation and then render the form
            return issueRepository.page(0, 10,"name", "asc", "").thenApplyAsync(nul -> {
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


    private Map<String, String> getIssues() {
        Map<String, String> issueList = new HashMap<>();
        try {
            issueList = issueRepository.getIssues();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return issueList;
    }

    private Map<String, String> getUsers() {
        Map<String, String> userList = new HashMap<>();
        try {
            userList = userRepository.getUsers();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userList;
    }
}

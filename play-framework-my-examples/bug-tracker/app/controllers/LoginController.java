package controllers;

import models.Issue;
import models.Login;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import repository.IssueRepository;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class LoginController extends Controller {

    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;

    @Inject
    public LoginController(FormFactory formFactory,
                           UserRepository userRepository,
                           IssueRepository issueRepository,
                           HttpExecutionContext httpExecutionContext,
                           MessagesApi messagesApi) {
        this.formFactory = formFactory;
        this.userRepository = userRepository;
        this.issueRepository = issueRepository;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }

    private Result GO_HOME = Results.redirect(
            routes.IssueController.list(0, "name", "asc", "")
    );

    public CompletionStage<Result> auth(Http.Request request) {
        Form<Login> authForm = formFactory.form(Login.class).bindFromRequest(request);

        // Run issues db operation and then render the form
        return issueRepository.page(0, 10,"name", "asc", "").thenApplyAsync((nul) -> {
            // This is the HTTP rendering thread context
            return ok(views.html.auth.login.render(authForm, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }


    public CompletionStage<Result> login(Http.Request request) throws ExecutionException, InterruptedException {
        //System.out.println("LOGIN");
        Form userForm = formFactory.form(Login.class).bindFromRequest(request);
        Login login = (Login) userForm.get();
        User user = this.userRepository.retrieveUserByEmail(login.getEmail()).toCompletableFuture().get();

        //System.out.println(login.email);

        if(userForm.hasErrors() || (user == null || !user.password.equals(login.password))) {
            return issueRepository.page(0, 10, "name", "asc", "").thenApplyAsync(list -> {
                // This is the HTTP rendering thread context
                //return ok(views.html.issue.list.render(list, "name", "asc", "", request, messagesApi.preferred(request))).flashing("failed", "User " + user.email + "has failed to authenticate");
                return Results.redirect(
                        routes.LoginController.login()
                ).flashing("failed", "User " + user.email + "has failed to authenticate");
            }, httpExecutionContext.current());
        }


        return this.issueRepository.page(0, 10, "name", "asc", "").thenApplyAsync(list -> {
            // This is the HTTP rendering thread context
            return ok(views.html.issue.list.render(list, "name", "asc", "", request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

}


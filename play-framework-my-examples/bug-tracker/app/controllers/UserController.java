package controllers;

import models.User;
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
import javax.persistence.PersistenceException;
import java.util.Map;
import java.util.concurrent.CompletionStage;

public class UserController extends Controller {

    //private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;

    @Inject
    public UserController(FormFactory formFactory,
                          UserRepository userRepository,
                          HttpExecutionContext httpExecutionContext,
                          MessagesApi messagesApi) {
        //this.issueRepository = issueRepository;
        this.formFactory = formFactory;
        this.userRepository = userRepository;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }

    /**
     * This result directly redirect to application home.
     */
    private Result GO_HOME = Results.redirect(
            routes.HomeController.index()
    );

    public CompletionStage<Result> listUsers(Http.Request request, int page, String sortBy, String order, String filter) {
        // Run a db operation in another thread (using DatabaseExecutionContext)
        return userRepository.page(page, 10, sortBy, order, filter).thenApplyAsync(listUsers -> {
            // This is the HTTP rendering thread context
            return ok(views.html.listUsers.render(listUsers, sortBy, order, filter, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> userSignup(Http.Request request) {
        Form<User> userForm = formFactory.form(User.class);
        // Run issues db operation and then render the form
        return userRepository.options().thenApplyAsync((Map<String, String> users) -> {
            // This is the HTTP rendering thread context
            return ok(views.html.signupForm.render(userForm, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> saveUser(Http.Request request) {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest(request);

        if (userForm.hasErrors()) {
            // Run issues db operation and then render the form
            return userRepository.options().thenApplyAsync((nul) -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.signupForm.render(userForm, request, messagesApi.preferred(request)));
            }, httpExecutionContext.current());
        }


        User user = userForm.get();
        // Run insert db operation, then redirect
        return userRepository.insert(user).thenApplyAsync(data -> {
            // This is the HTTP rendering thread context
            return GO_HOME
                    .flashing("success", "User " + user.name + " has been created");
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> editUser(Http.Request request,Long id) {

        // Run a db operation in another thread (using DatabaseExecutionContext)
        CompletionStage<Map<String, String>> usersFuture = userRepository.options();

        // Run the lookup also in another thread, then combine the results:
        return userRepository.lookup(id).thenApplyAsync((userOptional) -> {
            // This is the HTTP rendering thread context
            User c = userOptional.get();
            Form<User> userForm = formFactory.form(User.class).fill(c);
            return ok(views.html.editUserForm.render(id, userForm, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> updateUser(Http.Request request, Long id) throws PersistenceException {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest(request);
        if (userForm.hasErrors()) {
            // Run issues db operation and then render the failure case
            return userRepository.options().thenApplyAsync((nul) -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.signupForm.render(userForm, request, messagesApi.preferred(request)));
            }, httpExecutionContext.current());
        } else {
            User newUserData = userForm.get();
            // Run update operation and then flash and then redirect
            return userRepository.update(id, newUserData).thenApplyAsync(data -> {
                // This is the HTTP rendering thread context
                return GO_HOME
                        .flashing("success", "User " + newUserData.name + " has been updated");
            }, httpExecutionContext.current());
        }
    }

    public CompletionStage<Result> deleteUser(Long id) {
        // Run delete db operation, then redirect
        return userRepository.delete(id).thenApplyAsync(v -> {
            // This is the HTTP rendering thread context
            return GO_HOME
                    .flashing("success", "Issue has been deleted");
        }, httpExecutionContext.current());
    }
}

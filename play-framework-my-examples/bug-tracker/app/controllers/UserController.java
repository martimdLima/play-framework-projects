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
import repository.UserRepository;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Manage a database of users
 */
public class UserController extends Controller {

    private final UserRepository userRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;

    @Inject
    public UserController(FormFactory formFactory,
                          UserRepository userRepository,
                          HttpExecutionContext httpExecutionContext,
                          MessagesApi messagesApi) {
        this.formFactory = formFactory;
        this.userRepository = userRepository;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }

    /**
     * This result directly redirect to the user list
     */
    private Result GO_HOME = Results.redirect(
            routes.UserController.listUsers(0, "name", "asc", "")
    );

    /**
     * Display the paginated list of users.
     *
     * @param page   Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order  Sort order (either asc or desc)
     * @param filter Filter applied on computer names
     */
    public CompletionStage<Result> listUsers(Http.Request request, int page, String sortBy, String order, String filter) {
        // Run a db operation in another thread (using DatabaseExecutionContext)
        return userRepository.page(page, 10, sortBy, order, filter).thenApplyAsync(listUsers -> {
            // This is the HTTP rendering thread context
            return ok(views.html.user.listUsers.render(listUsers, sortBy, order, filter, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    /**
     * Display the 'new user form'.
     */
    public CompletionStage<Result> userSignup(Http.Request request) {
        Form<User> userForm = formFactory.form(User.class);
        // Run issues db operation and then render the form
        return userRepository.options().thenApplyAsync((Map<String, String> users) -> {
            // This is the HTTP rendering thread context
            return ok(views.html.user.signupForm.render(userForm, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    /**
     * Handle the 'new user form' submission
     */
    public CompletionStage<Result> saveUser(Http.Request request) {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest(request);

        if (userForm.hasErrors()) {
            // Run issues db operation and then render the form
            return userRepository.options().thenApplyAsync((nul) -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.user.signupForm.render(userForm, request, messagesApi.preferred(request)));
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

    /**
     * Display the 'edit form' of a existing user.
     *
     * @param id Id of the user to edit
     */
    public CompletionStage<Result> editUser(Http.Request request,Long id) {

        // Run a db operation in another thread (using DatabaseExecutionContext)
        CompletionStage<Map<String, String>> usersFuture = userRepository.options();

        // Run the lookup also in another thread, then combine the results:
        return userRepository.lookup(id).thenApplyAsync((userOptional) -> {
            // This is the HTTP rendering thread context
            User c = userOptional.get();
            Form<User> userForm = formFactory.form(User.class).fill(c);
            return ok(views.html.user.editUserForm.render(id, userForm, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    /**
     * Handle the 'edit form' submission
     *
     * @param id Id of the user to edit
     */
    public CompletionStage<Result> updateUser(Http.Request request, Long id) throws PersistenceException {
        Form<User> userForm = formFactory.form(User.class).bindFromRequest(request);
        if (userForm.hasErrors()) {
            // Run issues db operation and then render the failure case
            return userRepository.options().thenApplyAsync((nul) -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.user.signupForm.render(userForm, request, messagesApi.preferred(request)));
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

    /**
     * Handle user deletion
     */
    public CompletionStage<Result> deleteUser(Long id) {
        // Run delete db operation, then redirect
        return userRepository.delete(id).thenApplyAsync(v -> {
            // This is the HTTP rendering thread context
            return GO_HOME
                    .flashing("success", "Issue has been deleted");
        }, httpExecutionContext.current());
    }
}

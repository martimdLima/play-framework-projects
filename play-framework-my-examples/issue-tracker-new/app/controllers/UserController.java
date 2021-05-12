package controllers;

import models.User;
import models.UserRepository;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

/**
 * The controller keeps all database operations behind the repository, and uses
 * {@link play.libs.concurrent.HttpExecutionContext} to provide access to the
 * {@link play.mvc.Http.Context} methods like {@code request()} and {@code flash()}.
 */
public class UserController extends Controller {

    private final FormFactory formFactory;
    private final UserRepository userRepository;
    private final HttpExecutionContext ec;

    @Inject
    public UserController(FormFactory formFactory, UserRepository userRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.userRepository = userRepository;
        this.ec = ec;
    }

    public Result index(final Http.Request request) {
        return ok(views.html.index.render(request));
    }

    public CompletionStage<Result> addUser(final Http.Request request) {
        User user = formFactory.form(User.class).bindFromRequest(request).get();
        return userRepository
                .add(user)
                .thenApplyAsync(p -> redirect(routes.UserController.index()), ec.current());
    }

    public CompletionStage<Result> getUsers() {
        return userRepository
                .list()
                .thenApplyAsync(personStream -> ok(toJson(personStream.collect(Collectors.toList()))), ec.current());
    }

}

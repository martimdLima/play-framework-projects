package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import model.User;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.UserService;
import utils.Util;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class UserController extends Controller {

    private UserService userService;
    private HttpExecutionContext executionContext;

    @Inject
    public UserController(UserService userService, HttpExecutionContext executionContext) {
        this.userService = userService;
        this.executionContext = executionContext;
    }

    public CompletionStage<Result> createUser(Http.Request request) {
        final JsonNode jsonNode = request.body().asJson();
        final User user = Util.mapToObject(jsonNode, User.class);
        return userService.create(user).thenApplyAsync(userOptional -> userOptional.map(s -> {
            return ok(Util.createResponse(s, true));
        }).orElse(notFound(Util.createResponse("User not found", false))));
    }

    public CompletionStage<Result> updateUser(Http.Request request) {
        final JsonNode jsonNode = request.body().asJson();
        final User user = Util.mapToObject(jsonNode, User.class);
        return userService.update(user).thenApplyAsync(userOptional -> userOptional.map(s -> {
            return ok(Util.createResponse(s, true));
        }).orElse(notFound(Util.createResponse("User not found", false))));
    }

    public CompletionStage<Result> getUserById(Http.Request request, String userId) {
        return userService.getById(userId).thenApplyAsync(userOptional -> userOptional.map(user -> {
            return ok(Util.createResponse(user, true));
        }).orElse(notFound(Util.createResponse("User not found", false))));
    }

    public CompletionStage<Result> deleteUserById(Http.Request request, String userId) {
        return userService.delete(userId).thenApplyAsync(userOptional -> userOptional.map(user -> {
            return ok(Util.createResponse(user, true));
        }).orElse(notFound(Util.createResponse("User not found", false))));
    }

    public CompletionStage<Result> getUsers(Http.Request request) {
        return userService.getAll().thenApplyAsync(users -> {
            return ok(Util.createResponse(users, true));
        });
    }

}

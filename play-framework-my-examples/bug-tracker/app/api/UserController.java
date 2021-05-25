package api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.User;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.UserRepository;
import utils.Util;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Manage a database of users
 */
public class UserController extends Controller {

    private final UserRepository userRepository;
    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;


    @Inject
    public UserController(UserRepository userRepository,
                          HttpExecutionContext httpExecutionContext,
                          MessagesApi messagesApi) {
        this.userRepository = userRepository;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }

    public CompletionStage<Result> getAll(Http.Request request) {
        // Run a db operation in another thread (using DatabaseExecutionContext)
        return userRepository.list().thenApplyAsync(users -> {
            List<User> result = users;
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonData = mapper.convertValue(result, JsonNode.class);
            return ok(Util.createResponse(jsonData, true));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> create(Http.Request request) {
        JsonNode json = request.body().asJson();
        final User user = Json.fromJson(json, User.class);
        return userRepository.insert(user).thenApplyAsync(savedResource -> {
            return created(Json.toJson(savedResource));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> get(Http.Request request, long id) {

        return userRepository.lookup(id).thenApplyAsync(optionalResource -> {

            User user = optionalResource.get();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonData = mapper.convertValue(user, JsonNode.class);
            return ok(Util.createResponse(jsonData, true));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> update(Http.Request request, long id) {
        JsonNode json = request.body().asJson();
        User resource = Json.fromJson(json, User.class);
        return userRepository.update(id, resource).thenApplyAsync(updatedUsr -> {

            if (json == null) {
                return badRequest(Util.createResponse("Expecting Json data", false));
            }

            JsonNode jsonObject = Json.toJson(updatedUsr);

            return ok(Util.createResponse(jsonObject, true));
        }, httpExecutionContext.current());
    }


    public CompletionStage<Result> delete(Long id) {

        return userRepository.delete(id).thenApplyAsync(savedResource -> {
            return ok(Json.toJson(savedResource));
        }, httpExecutionContext.current());
    }
}
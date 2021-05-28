package api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Comment;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import repository.CommentRepository;
import repository.IssueRepository;
import repository.UserRepository;
import utils.Util;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.*;

public class CommentController {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;

    /**
     * @param issueRepository
     * @param userRepository
     * @param commentRepository
     * @param formFactory
     * @param httpExecutionContext
     * @param messagesApi
     */
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
     * Gets all the Comments
     *
     * @return CompletionStage<Result>
     */
    public CompletionStage<Result> getAll() {

        return commentRepository.list().thenApplyAsync(payload -> {

            ObjectMapper mapper = new ObjectMapper();

            List<Comment> comments = payload;

            JsonNode jsonData = mapper.convertValue(comments, JsonNode.class);

            return ok(Util.createResponse(Json.toJson(jsonData), true));
        }, httpExecutionContext.current());
    }

    /**
     * Gets a specific Comment
     *
     * @param request
     * @param id
     * @return CompletionStage<Result>
     */
    public CompletionStage<Result> get(Http.Request request, long id) {

        return commentRepository.lookup(id).thenApplyAsync(payload -> {

            Comment comment = payload.get();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonData = mapper.convertValue(comment, JsonNode.class);
            return ok(Util.createResponse(jsonData, true));
        }, httpExecutionContext.current());
    }

    /**
     * Creates a new Comment
     *
     * @param request
     * @return CompletionStage<Result>
     */
    public CompletionStage<Result> create(Http.Request request) {
        JsonNode json = request.body().asJson();
        final Comment comment = Json.fromJson(json, Comment.class);
        return commentRepository.insert(comment).thenApplyAsync(savedResource -> {
            return created(Json.toJson(savedResource));
        }, httpExecutionContext.current());
    }

    /**
     * Updates an existing comment
     *
     * @param request
     * @param id
     * @return CompletionStage<Result>
     */
    public CompletionStage<Result> update(Http.Request request, long id) {
        JsonNode json = request.body().asJson();
        Comment resource = Json.fromJson(json, Comment.class);
        return commentRepository.update(id, resource).thenApplyAsync(updatedUsr -> {

            if (json == null) {
                return badRequest(Util.createResponse("Expecting Json data", false));
            }

            JsonNode jsonObject = Json.toJson(updatedUsr);

            return ok(Util.createResponse(jsonObject, true));
        }, httpExecutionContext.current());
    }

    /**
     * Deltes a Comment
     *
     * @param id
     * @return CompletionStage<Result>
     */
    public CompletionStage<Result> delete(Long id) {

        return commentRepository.delete(id).thenApplyAsync(savedResource -> {
            return ok(Json.toJson(savedResource));
        }, httpExecutionContext.current());
    }
}

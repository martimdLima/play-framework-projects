package api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Comment;
import models.Issue;
import models.User;
import models.bo.CommentBO;
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
import java.util.Date;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.*;

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

    /*
    public CompletionStage<Result> getAll(Long id) {

        // Run a db operation in another thread (using DatabaseExecutionContext)
        return commentRepository.list().thenApplyAsync(comments -> {

            //return ok(Util.createResponse(Json.toJson(CommentBO.generate(comments)), true));
            return ok(Util.createResponse(Json.toJson(comments), true));
        }, httpExecutionContext.current());
    }
     */

    public CompletionStage<Result> getAll() {
        return commentRepository.list().thenApplyAsync(payload -> {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonData = mapper.convertValue(payload, JsonNode.class);

            return ok(Util.createResponse(Json.toJson(jsonData), true));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> get(Http.Request request, long id) {

        return commentRepository.lookup(id).thenApplyAsync(cmmt -> {

            Comment comment = cmmt.get();

            //ObjectMapper mapper = new ObjectMapper();
            //JsonNode jsonData = mapper.convertValue(comment, JsonNode.class);
            //return ok(Util.createResponse(jsonData, true));
            return ok(Util.createResponse(Json.toJson(CommentBO.generate(comment)), true));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> create(Http.Request request) {
        JsonNode json = request.body().asJson();

        /*

        User author = new User();
        author.id = json.get("user").get("id").asLong();
        author.email = json.get("user").get("email").asText();
        author.password = json.get("user").get("text").asText();

        Issue assignedIssue = new Issue();
        assignedIssue.id = json.get("issue").get("id").asLong();
         */

        Comment newComment = new Comment();
        newComment.id = json.get("id").asLong();
        newComment.message = json.get("message").asText();
        newComment.introduced = new Date(json.get("introduced").asLong());
        newComment.updated = new Date(json.get("updated").asLong());
        newComment.user = Json.fromJson(json.get("user"), User.class);
        newComment.issue = Json.fromJson(json.get("issue"), Issue.class);


        //final Comment comment = Json.fromJson(json, Comment.class);
        return commentRepository.insert(newComment).thenApplyAsync(savedResource -> {
            return created(Json.toJson(savedResource));
        }, httpExecutionContext.current());
    }


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


    public CompletionStage<Result> delete(Long id) {

        return commentRepository.delete(id).thenApplyAsync(savedResource -> {
            return ok(Json.toJson(savedResource));
        }, httpExecutionContext.current());
    }
}

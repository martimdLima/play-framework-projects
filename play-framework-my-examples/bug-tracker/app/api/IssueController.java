package api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Issue;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.CommentRepository;
import repository.IssueRepository;
import repository.UserRepository;
import utils.Util;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class IssueController extends Controller {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;

    @Inject
    public IssueController(FormFactory formFactory,
                           IssueRepository issueRepository,
                           UserRepository userRepository,
                           CommentRepository commentRepository,
                           HttpExecutionContext httpExecutionContext,
                           MessagesApi messagesApi) {
        this.issueRepository = issueRepository;
        this.formFactory = formFactory;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }

    public CompletionStage<Result> getAll() {

        return issueRepository.list().thenApplyAsync(payload -> {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonData = mapper.convertValue(payload, JsonNode.class);

            return ok(Util.createResponse(Json.toJson(jsonData), true));
        }, httpExecutionContext.current());
    }


    public CompletionStage<Result> get(Http.Request request, long id) {

        return issueRepository.lookup(id).thenApplyAsync(payload -> {

            ObjectMapper mapper = new ObjectMapper();

            Issue issue = payload.get();

            JsonNode jsonData = mapper.convertValue(issue, JsonNode.class);
            return ok(Util.createResponse(jsonData, true));
        }, httpExecutionContext.current());
    }


    public CompletionStage<Result> create(Http.Request request) {
        JsonNode json = request.body().asJson();
        final Issue issue = Json.fromJson(json, Issue.class);
        return issueRepository.insert(issue).thenApplyAsync(savedResource -> {
            return created(Json.toJson(savedResource));
        }, httpExecutionContext.current());
    }


    public CompletionStage<Result> update(Http.Request request, long id) {
        JsonNode json = request.body().asJson();
        Issue resource = Json.fromJson(json, Issue.class);
        return issueRepository.update(id, resource).thenApplyAsync(updatedUsr -> {

            if (json == null) {
                return badRequest(Util.createResponse("Expecting Json data", false));
            }

            JsonNode jsonObject = Json.toJson(updatedUsr);

            return ok(Util.createResponse(jsonObject, true));
        }, httpExecutionContext.current());
    }


    public CompletionStage<Result> delete(Long id) {

        return issueRepository.delete(id).thenApplyAsync(savedResource -> {
            return ok(Json.toJson(savedResource));
        }, httpExecutionContext.current());
    }
}

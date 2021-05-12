package controllers;

import models.Issue;
import models.IssueRepository;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

public class IssueController extends Controller {

    private final FormFactory formFactory;
    private final IssueRepository issueRepository;
    private final HttpExecutionContext ec;

    @Inject
    public IssueController(FormFactory formFactory, IssueRepository issueRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.issueRepository = issueRepository;
        this.ec = ec;
    }

    public Result index(final Http.Request request) {
        return ok(views.html.index.render(request));
    }

    public CompletionStage<Result> addIssue(final Http.Request request) {
        Issue issue = formFactory.form(Issue.class).bindFromRequest(request).get();
        return issueRepository
                .add(issue)
                .thenApplyAsync(i -> redirect(routes.IssueController.index()), ec.current());
    }

    public CompletionStage<Result> getIssues() {
        return issueRepository
                .list()
                .thenApplyAsync(issueStream -> ok(toJson(issueStream.collect(Collectors.toList()))), ec.current());
    }

}
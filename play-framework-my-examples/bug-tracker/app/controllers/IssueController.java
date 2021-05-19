package controllers;

import models.Issue;
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

public class IssueController extends Controller {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;

    @Inject
    public IssueController(FormFactory formFactory,
                          IssueRepository issueRepository,
                          UserRepository userRepository,
                          HttpExecutionContext httpExecutionContext,
                          MessagesApi messagesApi) {
        this.issueRepository = issueRepository;
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

    public CompletionStage<Result> list(Http.Request request, int page, String sortBy, String order, String filter) {
        // Run a db operation in another thread (using DatabaseExecutionContext)
        return issueRepository.page(page, 10, sortBy, order, filter).thenApplyAsync(list -> {
            // This is the HTTP rendering thread context
            return ok(views.html.list.render(list, sortBy, order, filter, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> create(Http.Request request) {
        Form<Issue> issueForm = formFactory.form(Issue.class);
        // Run issues db operation and then render the form
        return userRepository.options().thenApplyAsync((Map<String, String> issues) -> {
            // This is the HTTP rendering thread context
            return ok(views.html.createForm.render(issueForm, issues, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    /**
     * Handle the 'new issue form' submission
     */
    public CompletionStage<Result> save(Http.Request request) {
        Form<Issue> issueForm = formFactory.form(Issue.class).bindFromRequest(request);
        if (issueForm.hasErrors()) {
            // Run issues db operation and then render the form
            return userRepository.options().thenApplyAsync(issues -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.createForm.render(issueForm, issues, request, messagesApi.preferred(request)));
            }, httpExecutionContext.current());
        }

        Issue issue = issueForm.get();
        // Run insert db operation, then redirect
        return issueRepository.insert(issue).thenApplyAsync(data -> {
            // This is the HTTP rendering thread context
            return GO_HOME
                    .flashing("success", "Issue " + issue.name + " has been created");
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> update(Http.Request request, Long id) throws PersistenceException {
        Form<Issue> issueForm = formFactory.form(Issue.class).bindFromRequest(request);
        if (issueForm.hasErrors()) {
            // Run issues db operation and then render the failure case
            return userRepository.options().thenApplyAsync(issues -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.editForm.render(id, issueForm, issues, request, messagesApi.preferred(request)));
            }, httpExecutionContext.current());
        } else {
            Issue newIssueData = issueForm.get();
            // Run update operation and then flash and then redirect
            return issueRepository.update(id, newIssueData).thenApplyAsync(data -> {
                // This is the HTTP rendering thread context
                return GO_HOME
                        .flashing("success", "Issue " + newIssueData.name + " has been updated");
            }, httpExecutionContext.current());
        }
    }

    public CompletionStage<Result> edit(Http.Request request,Long id) {

        // Run a db operation in another thread (using DatabaseExecutionContext)
        CompletionStage<Map<String, String>> issuesFuture = userRepository.options();

        // Run the lookup also in another thread, then combine the results:
        return issueRepository.lookup(id).thenCombineAsync(issuesFuture, (issueOptional, issues) -> {
            // This is the HTTP rendering thread context
            Issue c = issueOptional.get();
            Form<Issue> issueForm = formFactory.form(Issue.class).fill(c);
            return ok(views.html.editForm.render(id, issueForm, issues, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> delete(Long id) {
        // Run delete db operation, then redirect
        return issueRepository.delete(id).thenApplyAsync(v -> {
            // This is the HTTP rendering thread context
            return GO_HOME
                    .flashing("success", "Issue has been deleted");
        }, httpExecutionContext.current());
    }
}

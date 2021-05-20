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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Manage a database of issues
 */
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
     * This result directly redirect to issue list.
     */
    private Result GO_HOME = Results.redirect(
            routes.IssueController.list(0, "name", "asc", "")
    );

    /**
     * Display the paginated list of issues.
     *
     * @param page   Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order  Sort order (either asc or desc)
     * @param filter Filter applied on computer names
     */
    public CompletionStage<Result> list(Http.Request request, int page, String sortBy, String order, String filter) {
        // Run a db operation in another thread (using DatabaseExecutionContext)
        return issueRepository.page(page, 10, sortBy, order, filter).thenApplyAsync(list -> {
            // This is the HTTP rendering thread context
            return ok(views.html.issue.list.render(list, sortBy, order, filter, request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    /**
     * Display the 'new issue form'.
     */
    public CompletionStage<Result> create(Http.Request request) {
        Form<Issue> issueForm = formFactory.form(Issue.class);

        List<String> statusOptions = getStatusesOptions();
        List<String> categoryOptions = getCategoryOptions();
        List<String> applicationOptions = getApplicationOptions();

        // Run issues db operation and then render the form
        return userRepository.options().thenApplyAsync((Map<String, String> users) -> {
            // This is the HTTP rendering thread context
            return ok(views.html.issue.createForm.render(issueForm, users, applicationOptions, categoryOptions, statusOptions,  request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    /**
     * Handle the 'new issue form' submission
     */
    public CompletionStage<Result> save(Http.Request request) {
        Form<Issue> issueForm = formFactory.form(Issue.class).bindFromRequest(request);

        List<String> statusOptions = getStatusesOptions();
        List<String> categoryOptions = getCategoryOptions();
        List<String> applicationOptions = getApplicationOptions();

        if (issueForm.hasErrors()) {
            // Run issues db operation and then render the form
            return userRepository.options().thenApplyAsync(users -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.issue.createForm.render(issueForm, users, applicationOptions, categoryOptions, statusOptions,  request, messagesApi.preferred(request)));
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

    /**
     * Handle the 'edit form' submission
     *
     * @param id Id of the issue to edit
     */
    public CompletionStage<Result> update(Http.Request request, Long id) throws PersistenceException {
        Form<Issue> issueForm = formFactory.form(Issue.class).bindFromRequest(request);

        List<String> statusOptions = getStatusesOptions();
        List<String> categoryOptions = getCategoryOptions();
        List<String> applicationOptions = getApplicationOptions();

        if (issueForm.hasErrors()) {
            // Run issues db operation and then render the failure case
            return userRepository.options().thenApplyAsync(users -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.issue.editForm.render(id, issueForm, users, applicationOptions, categoryOptions, statusOptions,  request, messagesApi.preferred(request)));
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

    /**
     * Display the 'edit form' of a existing issue.
     *
     * @param id Id of the issue to edit
     */
    public CompletionStage<Result> edit(Http.Request request,Long id) {

        // Run a db operation in another thread (using DatabaseExecutionContext)
        CompletionStage<Map<String, String>> issuesFuture = userRepository.options();

        List<String> statusOptions = getStatusesOptions();
        List<String> categoryOptions = getCategoryOptions();
        List<String> applicationOptions = getApplicationOptions();

        // Run the lookup also in another thread, then combine the results:
        return issueRepository.lookup(id).thenCombineAsync(issuesFuture, (issueOptional, users) -> {
            // This is the HTTP rendering thread context
            Issue c = issueOptional.get();
            Form<Issue> issueForm = formFactory.form(Issue.class).fill(c);
            return ok(views.html.issue.editForm.render(id, issueForm, users, applicationOptions, categoryOptions, statusOptions,  request, messagesApi.preferred(request)));
        }, httpExecutionContext.current());
    }

    /**
     * Handle issue deletion
     */
    public CompletionStage<Result> delete(Long id) {
        // Run delete db operation, then redirect
        return issueRepository.delete(id).thenApplyAsync(v -> {
            // This is the HTTP rendering thread context
            return GO_HOME
                    .flashing("success", "Issue has been deleted");
        }, httpExecutionContext.current());
    }

    private List<String> getStatusesOptions() {
        List<String> statusOptions = new ArrayList<>();
        statusOptions.add("Resolved");
        statusOptions.add("UnResolved");
        return statusOptions;
    }

    private List<String> getCategoryOptions() {
        List<String> statusOptions = new ArrayList<>();
        statusOptions.add("Finance");
        statusOptions.add("Automotive");
        statusOptions.add("Automation");
        statusOptions.add("Robotics");
        return statusOptions;
    }

    private List<String> getApplicationOptions() {
        List<String> statusOptions = new ArrayList<>();
        statusOptions.add("DemoApp1");
        statusOptions.add("DemoApp2");
        statusOptions.add("DemoApp3");
        statusOptions.add("DemoApp4");
        statusOptions.add("DemoApp5");
        return statusOptions;
    }
}

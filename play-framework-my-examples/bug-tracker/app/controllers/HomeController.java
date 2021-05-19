package controllers;

import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import repository.UserRepository;
import repository.IssueRepository;

import javax.inject.Inject;

/**
 * Manage a database of Issues
 */
public class HomeController extends Controller {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;
    private final MessagesApi messagesApi;

    @Inject
    public HomeController(FormFactory formFactory,
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
        routes.IssueController.list(0, "name", "asc", "")
    );

    /**
     * Handle default path requests, redirect to Issues list
     */
    public Result index() {
        return GO_HOME;
    }
}
            

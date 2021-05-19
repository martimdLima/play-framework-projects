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
import javax.inject.Singleton;

/**
 * Manage a database of Issues
 */
public class HomeController extends Controller {

    @Inject
    public HomeController(FormFactory formFactory,
                          IssueRepository issueRepository,
                          UserRepository userRepository,
                          HttpExecutionContext httpExecutionContext,
                          MessagesApi messagesApi) {
    }

    /**
     * Handle default path requests, redirect to Issues list
     */
    public Result index() {
        return Results.redirect(
                routes.IssueController.list(0, "name", "asc", "")
        );
    }
}
            

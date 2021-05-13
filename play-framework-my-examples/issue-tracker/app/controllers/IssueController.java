package controllers;

import models.ContactInfo;
import models.Issue;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.api.i18n.MessagesApi;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import java.util.*;

import views.html.createUser;
import views.html.issueList;
import views.html.issueDescription;
import views.html.createIssue;

import javax.inject.Inject;

import static play.libs.Scala.asScala;


public class IssueController  extends Controller{

    //private DataController dataController = new DataController();


    final private DataController dataController;

    final private FormFactory formFactory;

    final private Form<Issue> form;

    private MessagesApi messagesApi;

    private final Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Inject
    public IssueController(DataController dataController, FormFactory formFactory, MessagesApi messagesApi) {
        this.dataController = dataController;
        this.formFactory = formFactory;
        this.form = formFactory.form(Issue.class);
        this.messagesApi = messagesApi;
    }

    public Result listIssues(Http.Request request){
        List<Issue> issues = dataController.getIssueList();
        ContactInfo contactInfo = dataController.getContactInfo();

        return ok(issueList.render(asScala(issues), contactInfo, request));
    }

    public Result detailIssue(Long id, Http.Request request){
        List<Issue> issues = dataController.getIssueList();
        ContactInfo contactInfo = dataController.getContactInfo();

        Issue issue = issues.get(id.intValue());
        if(issue==null){
            return notFound();
        }
        return ok(issueDescription.render(issue, contactInfo, request));
    }

    public Result createView(Http.Request request) {
        ContactInfo contactInfo = dataController.getContactInfo();
        return ok(createIssue.render(formFactory.form(Issue.class), contactInfo, request,  messagesApi.preferred(request)));
    }


    public Result create(Http.Request request) {
        final Form<Issue> uForm = form.bindFromRequest(request);
        ContactInfo contactInfo = dataController.getContactInfo();

        if (uForm.hasErrors()) {
            return ok(createIssue.render(uForm, contactInfo, request, messagesApi.preferred(request)));
        }

        Issue issue = uForm.get();

        int id = (int)(Math.random()*(10000-0+1)+0);
        issue.setId(new Long(id));
        issue.setSubmitted(new Date());
        issue.setLastUpdated(new Date());

        this.dataController.addIssue(issue);
        return redirect(routes.IssueController.listIssues());
    }
}

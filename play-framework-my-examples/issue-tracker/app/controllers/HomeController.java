package controllers;

import java.util.List;

import models.ContactInfo;
import models.Issue;
import play.libs.Json;
import play.mvc.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import static play.libs.Scala.asScala;

@Singleton
public class HomeController extends Controller {

    private final List<Issue> issueList;
    private final String monthlyRevenues;
    private final ContactInfo contactInfo;

    @Inject
    public HomeController() {
        DataController dataController = new DataController();
        this.issueList = dataController.getIssueList();
        this.contactInfo = dataController.getContactInfo();
        this.monthlyRevenues = Json.stringify(Json.toJson(dataController.getLast6MonthsRevenue()));

    }

    public Result index(Http.Request request) {
        boolean hide = true;
        return ok(views.html.index.render(asScala(issueList), monthlyRevenues, contactInfo, request));
    }

}

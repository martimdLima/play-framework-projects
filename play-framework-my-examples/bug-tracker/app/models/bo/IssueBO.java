package models.bo;

import models.Issue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IssueBO {

    public Long id;

    public String name;

    public Date introduced;

    public Date updated;

    public Date discontinued;

    public String application;

    public String category;

    public String status;

    public String summary;

    public String description;

    public List<CommentBO> comments;

    public UserBO user;


    public static IssueBO generate(Issue issue) {
        IssueBO result = new IssueBO();

        result.id = issue.id;
        result.name = issue.name;
        result.introduced = issue.introduced;
        result.updated = issue.updated;
        result.discontinued = issue.discontinued;
        result.application = issue.application;
        result.category = issue.category;
        result.status = issue.status;
        result.summary = issue.summary;
        result.description = issue.description;
        result.comments = CommentBO.generate(issue.comments);
        result.user = UserBO.generate(issue.user);

        return result;
    }

    public static List<IssueBO> generate(List<Issue> issues) {

        List<IssueBO> result = new ArrayList<>();

        for (Issue c : issues) {
            result.add(IssueBO.generate(c));
        }

        return result;
    }
}

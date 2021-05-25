package models;

import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Comment extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String message;

    @Formats.DateTime(pattern = "yyyy-MM-dd")
    public Date introduced;

    @Formats.DateTime(pattern = "yyyy-MM-dd")
    public Date updated;

    @ManyToOne(fetch = FetchType.LAZY)
    public Issue issue;

    @ManyToOne(fetch = FetchType.LAZY)
    public User user;


    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

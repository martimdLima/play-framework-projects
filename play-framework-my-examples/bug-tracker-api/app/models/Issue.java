package models;

import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Issue entity managed by Ebean
 */
@Entity
public class Issue extends BaseModel {

    private static final long serialVersionUID = 1L;
    @OneToMany(mappedBy = "issue", fetch = FetchType.EAGER)
    private final List<Comment> comments = new ArrayList<>();
    @Constraints.Required
    public String name;
    @Formats.DateTime(pattern = "yyyy-MM-dd")
    public Date introduced;
    @Formats.DateTime(pattern = "yyyy-MM-dd")
    public Date updated;
    @Formats.DateTime(pattern = "yyyy-MM-dd")
    public Date discontinued;
    @Constraints.Required
    public String application;
    @Constraints.Required
    public String category;
    @Constraints.Required
    public String status;
    @Constraints.Required
    public String summary;
    @Constraints.Required
    public String description;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    User user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getIntroduced() {
        return introduced;
    }

    public void setIntroduced(Date introduced) {
        this.introduced = introduced;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(Date discontinued) {
        this.discontinued = discontinued;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}


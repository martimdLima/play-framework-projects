package model;

import java.util.Date;

public class Issue {
    public Long id;

    public String title;
    public String application;
    public String category;
    public String viewStatus;

    public String summary;
    public String description;

    public Date submitted;
    public Date lastUpdated;

    public Issue() {
    }

    public Issue(Long id, String title, String application, String category, String viewStatus, String summary, String description, Date submitted, Date lastUpdated) {
        this.id = id;
        this.title = title;
        this.application = application;
        this.category = category;
        this.viewStatus = viewStatus;
        this.summary = summary;
        this.description = description;
        this.submitted = submitted;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(String viewStatus) {
        this.viewStatus = viewStatus;
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

    public Date getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Date submitted) {
        this.submitted = submitted;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}


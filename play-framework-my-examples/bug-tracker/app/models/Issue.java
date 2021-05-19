package models;

import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Issue entity managed by Ebean
 */
@Entity 
public class Issue extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String name;
    
    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date introduced;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date updated;
    
    @Formats.DateTime(pattern="yyyy-MM-dd")
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
    
    @ManyToOne
    public User user;
    
}


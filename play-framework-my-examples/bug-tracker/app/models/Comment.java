package models;

import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Comment extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String message;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date introduced;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date updated;

    @ManyToOne
    public User user;

    @ManyToOne
    public Issue issue;
}

package models.bo;

import models.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentBO {

    public long id;

    public String message;

    public Date introduced;

    public Date updated;

    //public User user;
    //public Issue issue;
    public Long user_id;
    public Long issue_id;

    public static CommentBO generate(Comment comment) {
        CommentBO result = new CommentBO();

        result.id = comment.id;
        result.message = comment.message;
        result.introduced = comment.introduced;
        result.updated = comment.updated;
        //result.user = comment.user;
        //result.issue = comment.issue;
        //result.user_id = comment.user.id;
        //result.issue_id = comment.issue.id;

        return result;
    }

    public static List<CommentBO> generate(List<Comment> comments) {

        List<CommentBO> result = new ArrayList<>();

        for (Comment c : comments) {
            result.add(CommentBO.generate(c));
        }

        return result;
    }
}

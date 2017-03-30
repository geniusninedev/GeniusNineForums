package com.nineinfosys.android.geniusnineforums;

/**
 * Created by Dev on 30-03-2017.
 */

public class Comment {
    private String UserId;
    private String Comment;


    public Comment() {
    }

    public Comment(String userId, String comment) {
        UserId = userId;
        Comment = comment;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}

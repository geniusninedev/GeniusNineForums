package com.nineinfosys.android.geniusnineforums;

/**
 * Created by Dev on 26-03-2017.
 */

public class Forum {
    private String UserId;
    private String Title;
    private String Content;

    public Forum() {
    }

    public Forum(String userId, String title, String content) {
        UserId = userId;
        Title = title;
        Content = content;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}

package com.nineinfosys.android.geniusnineforums;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Dev on 26-03-2017.
 */

public class ForumViewHolder extends RecyclerView.ViewHolder {
    View view;
    public ForumViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }
    public void setTitle(String title){
        TextView textViewForumTitle = (TextView)view.findViewById(R.id.textViewForumTitle);
        textViewForumTitle.setText(title);
    }
    public void setContent(String content){
        TextView textViewForumContent = (TextView)view.findViewById(R.id.textViewForumContent);
        textViewForumContent.setText(content);
    }
    public void setUserID(String userID){
        String userId = userID;
    }


}

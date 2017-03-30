package com.nineinfosys.android.geniusnineforums;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Dev on 30-03-2017.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {
    View view;

    public CommentViewHolder(View itemView) {
        super(itemView);
        view = itemView;

    }
    public void setComment(String comment){
        TextView textViewComment = (TextView)view.findViewById(R.id.textViewComment);
        textViewComment.setText(comment);
    }

    public void setUserID(String userID){
        String userId = userID;
    }
}

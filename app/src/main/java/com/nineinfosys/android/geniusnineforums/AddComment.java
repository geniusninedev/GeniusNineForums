package com.nineinfosys.android.geniusnineforums;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddComment extends AppCompatActivity {

    private DatabaseReference forumDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private EditText editTextComment;
    private ImageButton buttonPostComment;
    private RecyclerView commentRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        forumDatabaseReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.app_id)).child("Forum");
        firebaseAuth = FirebaseAuth.getInstance();
        editTextComment = (EditText)findViewById(R.id.editTextComment);
        buttonPostComment = (ImageButton)findViewById(R.id.buttonPostComment);
        buttonPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });
        commentRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewComments);
        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    private void postComment(){
        Intent intent = getIntent();
        String forumPostID = intent.getStringExtra("forumPostID");
        String user_id = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReferencePostComment = forumDatabaseReference.child(forumPostID).child("Comment").push();
        String comment = editTextComment.getText().toString();


        if(!TextUtils.isEmpty(comment)){
            databaseReferencePostComment.child("UserId").setValue(user_id);
            databaseReferencePostComment.child("Comment").setValue(comment);

            editTextComment.setText("");

            Toast.makeText(AddComment.this, "Submitted", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(AddComment.this, "add comment", Toast.LENGTH_LONG).show();
        }

    }

    private void populateRecyclerView(){
        Intent intent = getIntent();
        String forumPostID = intent.getStringExtra("forumPostID");
        DatabaseReference databaseReferencePostComment = forumDatabaseReference.child(forumPostID).child("Comment");
        FirebaseRecyclerAdapter<Comment, CommentViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>
                (Comment.class, R.layout.row_comment, CommentViewHolder.class, databaseReferencePostComment) {
            @Override
            protected void populateViewHolder(CommentViewHolder viewHolder, Comment model, int position) {

                viewHolder.setComment(model.getComment());
                viewHolder.setUserID(model.getUserId());



            }
        };
        commentRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MainActivity:", "Starting auth listener");

        populateRecyclerView();



    }
}

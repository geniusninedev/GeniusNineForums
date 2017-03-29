package com.nineinfosys.android.geniusnineforums;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPost extends AppCompatActivity {

    private EditText editTextForumTitle;
    private EditText editTextForumContent;
    private Button buttonForumSubmit;
    private DatabaseReference forumDatabaseReference;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        forumDatabaseReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.app_id)).child("Forum");
        firebaseAuth = FirebaseAuth.getInstance();
        editTextForumTitle = (EditText)findViewById(R.id.editTextForumTitle);
        editTextForumContent =(EditText)findViewById(R.id.editTextForumContent);
        buttonForumSubmit = (Button)findViewById(R.id.buttonForumSubmit);
        buttonForumSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
    }
    private void addPost(){
        DatabaseReference newPost = forumDatabaseReference.push();
        String title = editTextForumTitle.getText().toString();
        String content = editTextForumContent.getText().toString();
        String user_id = firebaseAuth.getCurrentUser().getUid();
        if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(content)){
            newPost.child("UserId").setValue(user_id);
            newPost.child("Title").setValue(title);
            newPost.child("Content").setValue(content);
            editTextForumTitle.setText("");
            editTextForumContent.setText("");
            Toast.makeText(AddPost.this, "Submitted", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(AddPost.this, "Complete all fields", Toast.LENGTH_LONG).show();
        }
    }
}

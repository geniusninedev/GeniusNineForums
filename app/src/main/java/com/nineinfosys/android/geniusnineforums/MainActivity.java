package com.nineinfosys.android.geniusnineforums;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //Firebase variables... for authentication and contact uploading to firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    private DatabaseReference databaseReferenceForums;

    private RecyclerView forumRecyclerView;
    private long children;
    private boolean likeStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseReferenceForums = FirebaseDatabase.getInstance().getReference().child(getString(R.string.app_id)).child("Forum");
        children = 0;
        likeStatus = false;
        authenticate();
        firebaseAuth.addAuthStateListener(firebaseAuthListner);

        forumRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewForum);
        forumRecyclerView.setHasFixedSize(true);
        forumRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        populateRecyclerView();

    }
    private void authenticate(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Log.e("MainActivity:", "User was null so directed to Login activity");
                    Intent loginIntent = new Intent(MainActivity.this, Login.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);

                }
                else {

                }

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MainActivity:", "Starting auth listener");




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
        }
        if (id == R.id.action_add_post) {
            Intent i = new Intent(MainActivity.this, AddPost.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateRecyclerView(){

        FirebaseRecyclerAdapter<Forum, ForumViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Forum, ForumViewHolder>
                (Forum.class, R.layout.forum_row, ForumViewHolder.class, databaseReferenceForums) {
            @Override
            protected void populateViewHolder(ForumViewHolder viewHolder, Forum model, int position) {
                final String post_key = getRef(position).getKey();

                Log.e("-------", post_key);
                viewHolder.setTitle(model.getTitle());
                viewHolder.setContent(model.getContent());
                viewHolder.setUserID(model.getUserId());
                viewHolder.imageButtonForumComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, AddComment.class);
                        intent.putExtra("forumPostID", post_key );
                        startActivity(intent);
                    }
                });
                if(showLikeStatus(post_key)){
                    viewHolder.setLikeStatus(getTotalChildren(post_key)+ " likes...and "+"You also have Liked post");
                    Log.e("-------", post_key +"  set for ");
                }else{
                    viewHolder.setLikeStatus(getTotalChildren(post_key)+" likes...and "+"You have not Liked post");
                    Log.e("-------", post_key +"  set for ");
                }
                viewHolder.forumLikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String user_id = firebaseAuth.getCurrentUser().getUid();
                        if(showLikeStatus(post_key)){
                            databaseReferenceForums.child(post_key).child("Likes").child(user_id).removeValue();

                        }else{
                            databaseReferenceForums.child(post_key).child("Likes").child(user_id).setValue("Liked");
                        }
                    }
                });



            }
        };
        forumRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    private boolean showLikeStatus(String post_key){

        DatabaseReference databaseReferenceForumsLikes = databaseReferenceForums.child(post_key).child("Likes");
        final String user_id = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceForumsLikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(user_id)){
                    likeStatus = false;
                }
                else{
                    likeStatus = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return likeStatus;

    }

    private long getTotalChildren(String post_key){
        DatabaseReference databaseReferenceForumsLikes = databaseReferenceForums.child(post_key).child("Likes");
        final String user_id = firebaseAuth.getCurrentUser().getUid();
        databaseReferenceForumsLikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               children =  dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return children;
    }
}

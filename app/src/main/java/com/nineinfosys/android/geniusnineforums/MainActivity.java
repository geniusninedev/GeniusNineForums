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

import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //Firebase variables... for authentication and contact uploading to firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListner;
    private DatabaseReference databaseReferenceForums;
    private RecyclerView forumRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenticate();
        databaseReferenceForums = FirebaseDatabase.getInstance().getReference().child(getString(R.string.app_id)).child("Forum");
        forumRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewForum);
        forumRecyclerView.setHasFixedSize(true);
        forumRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        firebaseAuth.addAuthStateListener(firebaseAuthListner);
        populateRecyclerView();



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
                viewHolder.setTitle(model.getTitle());
                viewHolder.setContent(model.getContent());
                viewHolder.setUserID(model.getUserId());

            }
        };
        forumRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}

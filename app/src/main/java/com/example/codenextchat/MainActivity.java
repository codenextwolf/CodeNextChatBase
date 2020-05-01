package com.example.codenextchat;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    EditText editText;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    ChatMessageAdapter messagesAdapter;
    String username = "Anonymous";
    String profilePic = null;

    //TODO: Define a DatabaseReference reference to the messages object here
    DatabaseReference databaseMessages;
    ChildEventListener messagesEventListener;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    public static final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = findViewById(R.id.edit_text);

        //TODO: Place a FirebaseDatabase reference to the database here
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        //TODO: Instantiate the DatabaseReference to the messages object here
        databaseMessages = database.getReference().child("messages");


        ListView listView = findViewById(R.id.list_view);
        messagesAdapter = new ChatMessageAdapter(this, messages);
        listView.setAdapter(messagesAdapter);


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    onSignedIn(user.getDisplayName(), user.getPhotoUrl());
                } else {
                    onSignedOut();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Signed in.", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == RESULT_CANCELED) {
            Toast.makeText(MainActivity.this, "Sign in canceled.", Toast.LENGTH_LONG)
                    .show();
            finish();
        }
    }



    public void sendMessage(View view) {
        //get the string from the edittext field
        //push the message as a child to the messages object in the db
        String message = editText.getText().toString();
        editText.setText("");

        //TODO: push the message as a child to the messages object in the db
        ChatMessage chatMessage = new ChatMessage(username, message, profilePic);
        databaseMessages.push().setValue(chatMessage);

    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        detachReadListener();
        messagesAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sign_out_menu) {
            AuthUI.getInstance().signOut(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void attachReadListener() {
        //TODO: Add a listener for when a child message is added to the messages object in the db here
        //when a child message has been added to the db:
        //get the message String from the Snapshot and add it to the ArrayList
        //notify the adapter to update the listview with a new message
        if(messagesEventListener == null) {
            messagesEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    messages.add(chatMessage);
                    messagesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            databaseMessages.addChildEventListener(messagesEventListener);
        }

    }

    public void detachReadListener() {
        if(messagesEventListener != null) {
            databaseMessages.removeEventListener(messagesEventListener);
            messagesEventListener = null;
        }
    }

    private void onSignedIn(String signedInUsername, Uri signedInProfilePic) {
        username = signedInUsername;
        if (signedInProfilePic != null) {
            profilePic = signedInProfilePic.toString();
        }
        attachReadListener();
    }

    private void onSignedOut() {
        username = "Anonymous";
        profilePic = null;
        messagesAdapter.clear();
        detachReadListener();
    }


}

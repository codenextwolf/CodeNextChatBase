package com.example.codenextchat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    ArrayList<String> messages = new ArrayList<>();
    ArrayAdapter<String> messagesAdapter;

    //TODO: Define a DatabaseReference reference to the messages object here
    DatabaseReference databaseMessages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edit_text);

        //TODO: Place a FirebaseDatabase reference to the database here
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        //TODO: Instantiate the DatabaseReference to the messages object here
        databaseMessages = database.getReference().child("messages");


        ListView listView = findViewById(R.id.list_view);
        messagesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, messages);
        listView.setAdapter(messagesAdapter);

        //TODO: Add a listener for when a child message is added to the messages object in the db here
        //when a child message has been added to the db:
        //get the message String from the Snapshot and add it to the ArrayList
        //notify the adapter to update the listview with a new message
        databaseMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messages.add(dataSnapshot.getValue(String.class));
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
        });




    }

    public void sendMessage(View view) {
        //get the string from the edittext field
        //push the message as a child to the messages object in the db
        String message = editText.getText().toString();
        editText.setText("");

        //TODO: push the message as a child to the messages object in the db
        databaseMessages.push().setValue(message);

    }
}

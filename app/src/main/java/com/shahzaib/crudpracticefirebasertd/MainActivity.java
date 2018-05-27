package com.shahzaib.crudpracticefirebasertd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = "123456";

    ListView groupMembersList;
    ListAdapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Member> members = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        groupMembersList = findViewById(R.id.groupMembersList);
        adapter = new ListAdapter(this);


    }


    @Override
    protected void onResume() {
        super.onResume();

        members.clear(); // clear the previous and reload new
        // initializing fireBase RTD objects and data change listener
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("GroupMember");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.i(LOG_TAG, "Key: " + dataSnapshot.getKey());

                String key = dataSnapshot.getKey(); // key is memberID
                String name = dataSnapshot.child("Name").getValue(String.class);
                Long age = dataSnapshot.child("Age").getValue(Long.class);
                Double height = dataSnapshot.child("Height").getValue(Double.class);
                Boolean isMale = dataSnapshot.child("isMale").getValue(Boolean.class);
//                Log.i(LOG_TAG,"\n\nName: "+name+"\nHeight: "+height+"\nAge: "+age
//                        +"\nisMale: "+isMale);

                if (name != null && age != null && height != null && isMale != null) {
                    members.add(new Member(key,name, age, height, isMale));
                    adapter.setData(members);
                    groupMembersList.setAdapter(adapter);
                }
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
                Log.i(LOG_TAG, "Error while loading data: " + databaseError.toException().toString());

            }

        });

    }

    public void addMember(View view) {
        startActivity(new Intent(this, AddUpdateMember.class));
    }


}

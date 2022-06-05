package com.example.managase_eldroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class viewStudent extends AppCompatActivity {
    RecyclerView recyclerView;
    List<viewStudentSingleModel> modelList = new ArrayList<viewStudentSingleModel>();
    viewStudentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        recyclerView = (RecyclerView) findViewById(R.id.rc_student);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseDatabase.getInstance().getReference().child("student").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String name = snapshot.child("firstName").getValue().toString() + " "+ snapshot.child("middleName").getValue().toString()+ " "+snapshot.child("lastName").getValue().toString();
                String address = snapshot.child("address").getValue().toString();
                String ID = snapshot.child("studentID").getValue().toString();
                String url = snapshot.child("profileURL").getValue().toString();
                viewStudentSingleModel model = new viewStudentSingleModel(name,address,ID,url,snapshot.getKey());
                modelList.add(model);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new viewStudentAdapter(modelList, new viewStudentAdapter.itemOnClick() {
            @Override
            public void itemDelete(int pos) {

            }

            @Override
            public void itemEdit(viewStudentSingleModel model) {

            }
        });

    }
}
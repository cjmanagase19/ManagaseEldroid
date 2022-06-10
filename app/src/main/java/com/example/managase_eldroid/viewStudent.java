package com.example.managase_eldroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class viewStudent extends AppCompatActivity {
    RecyclerView recyclerView;
    List<viewStudentSingleModel> modelList = new ArrayList<viewStudentSingleModel>();
    viewStudentAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        recyclerView = (RecyclerView) findViewById(R.id.rc_student);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button logoutBtn = (Button) findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent login = new Intent(viewStudent.this,loginActivity.class);
                startActivity(login);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("student").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String name = snapshot.child("firstName").getValue().toString() + " "+ snapshot.child("middleName").getValue().toString()+ " "+snapshot.child("lastName").getValue().toString();
                String address = snapshot.child("address").getValue().toString();
                String ID = snapshot.child("studentID").getValue().toString();
                String url = snapshot.child("profileUrl").getValue().toString();
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
            public void itemDelete(int pos, viewStudentSingleModel model) {
                FirebaseDatabase.getInstance().getReference().child("student").orderByKey().equalTo(model.getUID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            dataSnapshot.getRef().removeValue();
                            adapter.notifyItemRemoved(pos);
                            modelList.remove(pos);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void itemEdit(viewStudentSingleModel model) {
                    Intent intent = new Intent(viewStudent.this,createAccountStudent.class);
                    intent.putExtra("UID",model.getUID());
                    startActivity(intent);
            }
        });

    }
}
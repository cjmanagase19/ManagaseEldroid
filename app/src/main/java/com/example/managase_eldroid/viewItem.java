package com.example.managase_eldroid;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class viewItem extends AppCompatActivity {
    RecyclerView recyclerView;
    final int PICK_IMAGE = 100;

    List<viewItemModel> modelList = new ArrayList<viewItemModel>();
    viewStudentAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ImageView imageView;
    Dialog addDialog;
    EditText priceEt,TitleEt,DateEt,AuthorEt,searchET;
    Uri uri1;
    String UID,editDate,URL;
    Boolean isEdited=false,isEditPic = false;
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    imageView.setImageURI(uri);
                    uri1 = uri;
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        recyclerView = (RecyclerView) findViewById(R.id.rc_student);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button logoutBtn = (Button) findViewById(R.id.logoutBtn);
        Button addBtn = (Button) findViewById(R.id.btn_add_item);
        addDialog = new Dialog(viewItem.this);
        addDialog.setContentView(R.layout.activity_add_item);
        addDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addDialog.getWindow().getAttributes().windowAnimations = R.style.Animation_AppCompat_Dialog; //Setting the animations to dialog
        priceEt = (EditText) addDialog.findViewById(R.id.et_price_item);
        TitleEt = (EditText) addDialog.findViewById(R.id.et_title_item);
        DateEt = (EditText) addDialog.findViewById(R.id.et_date_item);
        AuthorEt = (EditText) addDialog.findViewById(R.id.et_author_item);
        imageView = (ImageView) addDialog.findViewById(R.id.img_itemImg_item);
        Button close = (Button) addDialog.findViewById(R.id.btn_cancel_item);
        Button submitBtn = (Button) addDialog.findViewById(R.id.btn_submit_item);
        searchET =(EditText) findViewById(R.id.et_search);
        Button searchBtn = (Button) findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!searchET.getText().toString().isEmpty())
                {
                    FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("items").orderByKey().addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Log.d("ID1",snapshot.getKey());
                            String id = snapshot.getKey();
                            FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("items").child(id).orderByValue().startAt(searchET.getText().toString()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot dataSnapshot:snapshot.getChildren())
                                    {
                                        String author = dataSnapshot.child("author").getValue().toString();
                                        String date = dataSnapshot.child("date").getValue().toString();
                                        String price = dataSnapshot.child("price").getValue().toString();
                                        String title = dataSnapshot.child("title").getValue().toString();
                                        String url = dataSnapshot.child("url").getValue().toString();
                                        viewItemModel model = new viewItemModel("Price: "+price,"Title: "+title,url,"Date: "+date,"Author: "+ author,snapshot.getKey());
                                        modelList.add(model);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
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
                }
                else
                {
                    Toast.makeText(viewItem.this, "Please enter search entry", Toast.LENGTH_SHORT).show();
                }
            }
        });
        verifyPermissions();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog.dismiss();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                mGetContent.launch("image/*");
                if(isEdited==true)
                {
                    isEditPic = true;
                }
            }
        });
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("items").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String author = snapshot.child("author").getValue().toString();
                String date = snapshot.child("date").getValue().toString();
                String price = snapshot.child("price").getValue().toString();
                String title = snapshot.child("title").getValue().toString();
                String url = snapshot.child("url").getValue().toString();
                viewItemModel model = new viewItemModel("Price: "+price,"Title: "+title,url,"Date: "+date,"Author: "+ author,snapshot.getKey());
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
            public void itemDelete(int pos, viewItemModel model) {
                FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("items").orderByKey().equalTo(model.getUID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            dataSnapshot.getRef().removeValue();
                            modelList.remove(pos);
                            adapter.notifyItemRemoved(pos);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void itemEdit(viewItemModel model) {
                isEdited=true;
                addDialog.show();
                UID = model.getUID();
                editDate = model.getDate();
                URL = model.getURL();
                priceEt.setText(model.getPrice());
                TitleEt.setText(model.getTitle());
                AuthorEt.setText(model.getAuthor());
                DateEt.setText(model.getDate());
                Picasso.get().load(model.getURL()).into(imageView);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(priceEt.getText().toString().isEmpty()||TitleEt.getText().toString().isEmpty()||AuthorEt.getText().toString().isEmpty())
                {
                    Toast.makeText(viewItem.this, "Please fill up all fields", Toast.LENGTH_SHORT).show();

                }
                else if(imageView.getDrawable()==null)
                {
                    Toast.makeText(viewItem.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
                else if(isEditPic==true)
                {
                    upload();
                }
                else if(isEdited)
                {
                    viewItemModel model = new viewItemModel(priceEt.getText().toString(),TitleEt.getText().toString(),URL,editDate,AuthorEt.getText().toString(),"");
                    FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("items").child(UID).setValue(model);
                }
                else
                {
                    upload();
                }
                isEdited=false;
                isEditPic=false;
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog.show();
                DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                String date = dateFormat.format(new Date());
                DateEt.setText(date);
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent login = new Intent(viewItem.this,loginActivity.class);
                startActivity(login);
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void upload()
    {
        StorageReference storageReference;
        storageReference = FirebaseStorage.getInstance().getReference("studentProfiles").child(System.currentTimeMillis() + "." +getFileExtension(uri1));
        storageReference.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uploadImage uploadImage = new uploadImage("itemImage", uri.toString());


                        if(isEditPic)
                        {
                            viewItemModel model = new viewItemModel(priceEt.getText().toString(),TitleEt.getText().toString(),uploadImage.getImageUrl(),editDate,AuthorEt.getText().toString(),"");
                            FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("items").child(UID).setValue(model);
                        }
                        else
                        {
                            viewItemModel model = new viewItemModel(priceEt.getText().toString(),TitleEt.getText().toString(),uploadImage.getImageUrl(),DateEt.getText().toString(),AuthorEt.getText().toString(),"");
                            FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("items").push().setValue(model);
                        }

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

            }
        });

    }
    private void verifyPermissions() {
        Log.d("11", "verifyPermissions: asking user for permissions");
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    PICK_IMAGE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
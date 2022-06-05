package com.example.managase_eldroid;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Random;

public class createAccountStudent extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final int PICK_IMAGE = 100;
    EditText etFirstName,etLastName,etMiddleName,etAddress,etAge,etCourse,etYear;
    Button submit;
    Uri uri1;
    Bitmap bitmap;
    ImageView imageView;
    Dialog option;
    StorageReference storageReference;
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    imageView.setImageURI(uri);
                    uri1 = uri;
                }
            });
    ActivityResultLauncher<Intent> mGetContent1 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK && result.getData()!=null)
            {
                Bundle bundle = result.getData().getExtras();
                bitmap = (Bitmap) bundle.get("data");
                imageView.setImageBitmap(bitmap);

            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_student);
        etFirstName = (EditText) findViewById(R.id.et_firstName_student);
        etMiddleName = (EditText) findViewById(R.id.et_middleName_student);
        etLastName = (EditText) findViewById(R.id.et_lastname_student);
        etAddress = (EditText) findViewById(R.id.et_address_student);
        etCourse = (EditText) findViewById(R.id.et_course_student);
        etYear = (EditText) findViewById(R.id.et_year_student2);
        etAge = (EditText) findViewById(R.id.et_age_student2);
        submit = (Button) findViewById(R.id.btn_submit_student);
        imageView = (ImageView) findViewById(R.id.img_profile_student);

        verifyPermissions();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                mGetContent.launch("image/*");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title"+ Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }
    public void upload()
    {
        storageReference = FirebaseStorage.getInstance().getReference("studentProfiles").child(System.currentTimeMillis() + "." +getFileExtension(uri1));
        storageReference.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Random rnd = new Random();
                        int random = rnd.nextInt(100000);
                        uploadImage uploadImage = new uploadImage("studentProfile", uri.toString());
                        studentModel model = new studentModel(etFirstName.getText().toString(),etLastName.getText().toString(),etMiddleName.getText().toString(),etAge.getText().toString(),etAddress.getText().toString(),etCourse.getText().toString(),etYear.getText().toString(),uploadImage.getImageUrl(),random+"");
                        FirebaseDatabase.getInstance().getReference().child("student").child(user.getUid()).setValue(model);

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
                Intent intent = new Intent(createAccountStudent.this,viewStudent.class);
                startActivity(intent);
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
package com.example.managase_eldroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signUpStudent1 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EditText etUserName, etPassword;
        Button signUpBtn;
        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference();
        setContentView(R.layout.activity_sign_up_student1);
        etPassword = (EditText) findViewById(R.id.et_password_student);
        etUserName = (EditText) findViewById(R.id.et_username_student);
        signUpBtn = (Button) findViewById(R.id.btn_signup_student);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);
        gsc.signOut();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);


        if (mAuth != null) {
            FirebaseAuth.getInstance().signOut();
        }

        if (acct != null) {
            Bundle bundle = new Bundle(getIntent().getExtras());
            if (bundle != null) {
                etUserName.setText(bundle.getString("email"));
                etUserName.setEnabled(false);
            }
        }

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etPassword.getText().toString().isEmpty()||etUserName.getText().toString().isEmpty())
                {
                    Toast.makeText(signUpStudent1.this, "Please fill the user and password entry", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    String pass = etPassword.getText().toString();
                    String user = etUserName.getText().toString();
                    Log.d("nisud",pass);
                    createUser(user,pass);
                }

            }
        });
    }
    public void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(signUpStudent1.this,createAccountStudent.class);
                            startActivity(intent);
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(signUpStudent1.this, "The email is already use", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                Toast.makeText(signUpStudent1.this, "Unexpected error", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }
                });
    }
}
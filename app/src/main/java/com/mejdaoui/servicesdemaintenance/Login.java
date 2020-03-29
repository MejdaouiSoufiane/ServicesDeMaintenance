package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private Button newaccount;
    private Button login;
    private Button test;

    TextInputEditText username;
    TextInputEditText password;

    private FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        login = findViewById(R.id.connect);
        newaccount = findViewById(R.id.newaccount);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        test = findViewById(R.id.test);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = username.getText().toString().trim();
                String passwd = password.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    username.setError("Invalide Email");
                    username.setFocusable(true);
                }
                else
                    loginUser(email, passwd);
            }
        });

        pd = new ProgressDialog(this);
        pd.setMessage("Logging In...");

        newaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create = new Intent(Login.this, Register.class);
                startActivity(create);
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(Login.this, AccFonctionnaire.class);
                startActivity(t);
            }
        });
    }

    public void loginUser(String email, String password){
        pd.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(Login.this, ListeCommande.class));
                            finish();
                        } else {
                            pd.dismiss();

                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

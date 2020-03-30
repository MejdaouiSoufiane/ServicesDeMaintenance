package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button newaccount, connection;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        newaccount = (Button) this.findViewById(R.id.newaccount);
        email = (EditText) this.findViewById(R.id.email);
        password = (EditText) this.findViewById(R.id.password);
        connection = (Button) this.findViewById(R.id.connection);


        connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = email.getText().toString();
                String sPassword = password.getText().toString();

                if(!TextUtils.isEmpty(sEmail) && !TextUtils.isEmpty(sPassword)){
                    loginUser(sEmail,sPassword);
                }
                else{
                    Toast.makeText(Login.this,"Failed Login: Empty Inputs are not allowed",Toast.LENGTH_SHORT).show();
                }
            }
        });
        newaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create = new Intent(Login.this, Register.class);
                startActivity(create);
            }
        });
    }

    private void loginUser(final String semail,final String spassword){
        mAuth.signInWithEmailAndPassword(semail,spassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String RegisteredUserID = mAuth.getUid();

                    DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("fonctionnaire").child(RegisteredUserID).child("type");

                    System.out.println(mAuth.getCurrentUser().getProviderData());
                    // database.addValueEventListener(new ValueEventListener() {

                    Intent intent = new Intent(Login.this, ListeDemande.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                else {
                    FirebaseAuthException e =(FirebaseAuthException)task.getException();
                    Toast.makeText(Login.this,"Failed Login: "+e.getMessage()+email+" "+password,Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}

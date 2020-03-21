package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterClient extends AppCompatActivity {

    private Button creer;
    private EditText nom;
    private EditText prenom;
    private EditText ville;
    private EditText tel;
    private EditText email;
    private EditText password;
    private EditText confirm;

    DatabaseReference database;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        database = FirebaseDatabase.getInstance().getReference("clients");

        mAuth = FirebaseAuth.getInstance();

        creer = (Button) this.findViewById(R.id.creer);
        nom = (EditText) this.findViewById(R.id.nom);
        prenom = (EditText) this.findViewById(R.id.prenom);
        ville = (EditText) this.findViewById(R.id.ville);
        tel = (EditText) this.findViewById(R.id.tel);
        email = (EditText) this.findViewById(R.id.email);
        password = (EditText) this.findViewById(R.id.pswd);
        confirm = (EditText) this.findViewById(R.id.confirm);

        creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addUser();

            }
        });
    }

    private void addUser() {
        String tnom = nom.getText().toString();
        String tprenom = prenom.getText().toString();
        String temail = email.getText().toString().trim();
        String tpassword = password.getText().toString().trim();
        String tville = ville.getText().toString();
        String ttel = tel.getText().toString();
        String tconfirm = confirm.getText().toString().trim();

        if (temail.isEmpty()) {
            email.setError("Email obligatoire");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(temail).matches()) {
            email.setError("Entrez un email valide");
            email.requestFocus();
            return;
        }

        if (tpassword.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if (password.length() < 6) {
            password.setError("Minimum lenght of password should be 6");
            password.requestFocus();
            return;
        }
        if (!tconfirm.equals(tpassword)) {

            confirm.setError("Mot de passe incorrect");
            confirm.requestFocus();
            return;
        }

        String id = database.push().getKey();

        Client client = new Client(id, tnom, tprenom, temail, tville, ttel);

        database.child(id).setValue(client);

        mAuth.createUserWithEmailAndPassword(temail, tpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    finish();
                    Toast.makeText(getApplicationContext(), "Compte bien créé", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterClient.this, Login.class));
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "Vous êtes déjà inscrit", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }



}

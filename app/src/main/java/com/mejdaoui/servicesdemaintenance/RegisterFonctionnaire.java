package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegisterFonctionnaire extends AppCompatActivity implements View.OnClickListener {

    private Button creer;
    private EditText nom;
    private EditText prenom;
    private EditText ville;
    private EditText tel;
    private EditText email;
    private EditText password;
    private EditText confirm;
    private CheckBox tapisserie;
    private CheckBox plomberie;
    private CheckBox platerie;
    private CheckBox maçonnerie;
    private CheckBox peinture;
    private CheckBox electricite;
    private EditText otherSecteur;
    private List<String> secteur = new ArrayList<>();


    DatabaseReference database;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_fonctionnaire);

        database = FirebaseDatabase.getInstance().getReference("fonctionnaires");

        mAuth = FirebaseAuth.getInstance();

        creer = (Button) this.findViewById(R.id.creer);
        nom = (EditText) this.findViewById(R.id.nom);
        prenom = (EditText) this.findViewById(R.id.prenom);
        ville = (EditText) this.findViewById(R.id.ville);
        tel = (EditText) this.findViewById(R.id.tel);
        email = (EditText) this.findViewById(R.id.email);
        password = (EditText) this.findViewById(R.id.pswd);
        confirm = (EditText) this.findViewById(R.id.confirm);
        platerie = (CheckBox) this.findViewById(R.id.platerie);
        plomberie = (CheckBox) this.findViewById(R.id.plomberie);
        peinture = (CheckBox) this.findViewById(R.id.peinture);
        maçonnerie = (CheckBox) this.findViewById(R.id.maçonnerie);
        electricite = (CheckBox) this.findViewById(R.id.electricite);
        tapisserie = (CheckBox) this.findViewById(R.id.tapisserie);
        otherSecteur = (EditText) this.findViewById(R.id.otherSecteur);


        creer.setOnClickListener(this);

        platerie.setOnClickListener(this);
        plomberie.setOnClickListener(this);
        peinture.setOnClickListener(this);
        tapisserie.setOnClickListener(this);
        maçonnerie.setOnClickListener(this);
        electricite.setOnClickListener(this);

    }


    private void addUser() {
        String tnom = nom.getText().toString();
        String tprenom = prenom.getText().toString();
        String temail = email.getText().toString().trim();
        String tpassword = password.getText().toString().trim();
        String tville = ville.getText().toString();
        String ttel = tel.getText().toString();
        String tconfirm = confirm.getText().toString().trim();
        String tsecteur = otherSecteur.getText().toString();

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
            password.setError("Mot de passe obligatoire");
            password.requestFocus();
            return;
        }

        if (!tconfirm.equals(tpassword)) {

             confirm.setError("Mot de passe incorrect");
             confirm.requestFocus();
             return;
        }

        if (!tsecteur.isEmpty()) {
            secteur.add(tsecteur);
        }

        String id = database.push().getKey();

        Fonctionnaire fonctionnaire = new Fonctionnaire(id, tnom, tprenom, temail, tville, ttel, secteur);

        database.child(id).setValue(fonctionnaire);

        mAuth.createUserWithEmailAndPassword(temail, tpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    finish();
                    Toast.makeText(getApplicationContext(), "Compte bien créé", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterFonctionnaire.this, Login.class));
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


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.creer:
                addUser();
                break;

            case R.id.platerie:
                if (((CheckBox)v).isChecked())
                    secteur.add("platerie");
                break;

            case R.id.plomberie:
                if (((CheckBox)v).isChecked())
                 secteur.add("plomberie");
                break;

            case R.id.peinture:
                if (((CheckBox)v).isChecked())
                secteur.add("peinture");
                break;

            case R.id.tapisserie:
                if (((CheckBox)v).isChecked())
                secteur.add("tapisserie");
                break;

            case R.id.maçonnerie:
                if (((CheckBox)v).isChecked())
                secteur.add("maçonnerie");
                break;

            case R.id.electricite:
                if (((CheckBox)v).isChecked())
                secteur.add("éléctricité");
                break;
        }

    }
}
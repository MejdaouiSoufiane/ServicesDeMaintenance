package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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

public class RegisterFonctionnaire extends AppCompatActivity {

    private Button creer;
    private EditText nom, prenom, ville, tel, email, password, confirm, otherSecteur;

    private CheckBox tapisserie, plomberie, platerie, maçonnerie, peinture, electricité ;

    private List<String> secteur = new ArrayList<>();

    private String tnom, tprenom, tville, ttel, temail, tpassword, fnom, fprenom, fville, ftel, fsecteur ;

    DatabaseReference database;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_fonctionnaire_2);

        database = FirebaseDatabase.getInstance().getReference("fonctionnaires");

        mAuth = FirebaseAuth.getInstance();

        UserInfoFragment fragment = new UserInfoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment,fragment, fragment.getTag()).commit();


    }


    public void addUser() {

        //receiveLoginData();


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

    private void receiveLoginData() {
        Intent intent = getIntent();
        temail = intent.getStringExtra("email");
        tpassword = intent.getStringExtra("password");

    }

    public void receiveUserData(){
        Intent intent = getIntent();
        tnom = intent.getStringExtra("nom");
        tprenom = intent.getStringExtra("prenom");
        tville = intent.getStringExtra("ville");
        ttel = intent.getStringExtra("tel");

    }

    private void receiveSecteurData() {
        Intent intent = getIntent();
        secteur = intent.getStringArrayListExtra("secteur");
    }


    @Override
    protected void onResume() {
        super.onResume();
        final String sender=this.getIntent().getStringExtra("fragment");
        final String type=this.getIntent().getStringExtra("type");

        if(sender != null && sender.equals("UserInfo")){
            receiveUserData();
            Toast.makeText(getApplicationContext(), fnom, Toast.LENGTH_SHORT).show();

            if(type.equals("fonctionnaire")){
                secteurFragment fragment = new secteurFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment,fragment, fragment.getTag()).commit();

            }
           else{
                loginInfoFragment fragment = new loginInfoFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment,fragment, fragment.getTag()).commit();

            }
        }

        else if(sender != null && sender.equals("secteurFragment")){
            receiveSecteurData();
            loginInfoFragment fragment = new loginInfoFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment,fragment, fragment.getTag()).commit();
        }

        else if(sender != null && sender.equals("loginFragment")){
            receiveLoginData();
            addUser();
        }
    }
}
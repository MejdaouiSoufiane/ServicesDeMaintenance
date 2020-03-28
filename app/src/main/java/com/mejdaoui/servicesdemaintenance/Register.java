package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {

    static private List<String> secteur = new ArrayList<>();

   static private String tnom, tprenom, tville, ttel, temail, tpassword, type ;

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

        String id = database.push().getKey();

        if(type.equals("fonctionnaire")){

        Fonctionnaire fonctionnaire = new Fonctionnaire(id, tnom, tprenom, temail, tville, ttel, secteur);
        database.child(id).setValue(fonctionnaire);}

        else if (type.equals("client")){
            Client client = new Client(id, tnom, tprenom, temail, tville, ttel);
            database.child(id).setValue(client);
        }



        mAuth.createUserWithEmailAndPassword(temail, tpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    finish();
                    Toast.makeText(getApplicationContext(), "Compte bien créé", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this, Login.class));
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
        tnom =intent.getStringExtra("nom");
        tprenom = intent.getStringExtra("prenom");
        tville = intent.getStringExtra("ville");
        ttel = intent.getStringExtra("tel");
        type = intent.getStringExtra("type");
    }

    private void receiveSecteurData() {
        Intent intent = getIntent();
        secteur = this.getIntent().getStringArrayListExtra("secteur");
    }

    @Override
    protected void onResume() {
        super.onResume();
        final String sender=this.getIntent().getStringExtra("fragment");
        final String type=this.getIntent().getStringExtra("type");

        if(sender != null && sender.equals("UserInfo")){
            receiveUserData();

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


            Toast.makeText(getApplicationContext(), tnom, Toast.LENGTH_SHORT).show();
            receiveLoginData();
            addUser();
            //Toast.makeText(getApplicationContext(), temail, Toast.LENGTH_SHORT).show();
        }
    }
}
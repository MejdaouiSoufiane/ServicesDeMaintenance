package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FctProfile extends AppCompatActivity {

    TextView userName;
    TextView userVille, userRating, travauxNbr;
    TextView userMail, userPhone, userAdresse, userService;

    ImageView userImg;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_fnct);

        userName = findViewById(R.id.userName);
        userVille = findViewById(R.id.userVille);
        userRating = findViewById(R.id.userRating);
        travauxNbr = findViewById(R.id.travauxNbr);
        userMail = findViewById(R.id.userMail);
        userPhone = findViewById(R.id.userPhone);
        userAdresse = findViewById(R.id.userAdresse);
        userService = findViewById(R.id.userService);

        userImg = findViewById(R.id.userImg);

    }
        @Override
        protected void onStart() {
            super.onStart();

            String id = this.getIntent().getStringExtra("id");

            databaseReference = FirebaseDatabase.getInstance().getReference();

            DatabaseReference ref = databaseReference.child("fonctionnaires").child(id);

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String nom = dataSnapshot.child("nom").getValue(String.class);
                    String prenom = dataSnapshot.child("prenom").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String tel = dataSnapshot.child("telephone").getValue(String.class);
                    String ville = dataSnapshot.child("ville").getValue(String.class);
                    String adresse = dataSnapshot.child("adresse").getValue(String.class);
                    String img = dataSnapshot.child("image").getValue(String.class);

                    int nbre = (int)dataSnapshot.child("secteur").getChildrenCount();
                    List<String> secteur = new ArrayList<>();

                    for(int i=0; i<nbre; i++)
                        secteur.add(dataSnapshot.child("secteur").child(Integer.toString(i)).getValue(String.class));

                    String fullName = prenom + " " + nom;

                    userName.setText(fullName);
                    userVille.setText(ville);
                    userMail.setText(email);
                    userPhone.setText(tel);
                    userAdresse.setText(adresse);
                    userService.setText(secteur.toString());

                    Picasso.get().load(img).resize(50, 50).into(userImg);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            ref.addListenerForSingleValueEvent(eventListener);
        }
}

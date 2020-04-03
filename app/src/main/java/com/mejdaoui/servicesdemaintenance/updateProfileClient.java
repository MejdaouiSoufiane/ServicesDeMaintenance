package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class updateProfileClient extends AppCompatActivity {

    private EditText nomProfile, prenomProfile, mailProfile, phoneProfile, adresseProfile, villeProfile, telProfile;

    private Button update;
    FirebaseUser user;
    String uid ;
    DatabaseReference databaseReference;
    UserProfileChangeRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        nomProfile = (EditText) this.findViewById(R.id.nomProfile);
        prenomProfile = (EditText) this.findViewById(R.id.prenomProfile);
        mailProfile = (EditText) this.findViewById(R.id.mailProfile);
        telProfile = (EditText) this.findViewById(R.id.telProfile);
        villeProfile = (EditText) this.findViewById(R.id.villeprofile);
        adresseProfile = (EditText) this.findViewById(R.id.adresseprofile);
        update = (Button) this.findViewById(R.id.update);



        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
/*
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nom = dataSnapshot.child(uid).child("nom").getValue(String.class);
                String prenom = dataSnapshot.child(uid).child("prénom").getValue(String.class);
                String email = dataSnapshot.child(uid).child("email").getValue(String.class);
                String tel = dataSnapshot.child(uid).child("téléphone").getValue(String.class);
                String ville = dataSnapshot.child(uid).child("ville").getValue(String.class);
                String adresse = dataSnapshot.child(uid).child("adresse").getValue(String.class);


                nomProfile.setText(nom);
                phoneProfile.setText(prenom);
                adresseProfile.setText(adresse);
                mailProfile.setText(email);
                villeProfile.setText(ville);
                telProfile.setText(tel);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser(v);
            }
        });*/
    }

    public void updateUser(View v){
        String nom = nomProfile.getText().toString();
        String prenom = prenomProfile.getText().toString();
        String mail = mailProfile.getText().toString();
        String adresse = adresseProfile.getText().toString();
        String ville = villeProfile.getText().toString();
        String tel = telProfile.getText().toString();

        databaseReference=FirebaseDatabase.getInstance().getReference("clients").child(uid);

        Client client = new Client(uid, nom, prenom, mail, adresse,ville, tel );

        databaseReference.setValue(client);

        Toast.makeText(getApplicationContext(), "Profile modifié", Toast.LENGTH_SHORT).show();

    }

}

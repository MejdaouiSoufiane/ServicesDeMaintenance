package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientProfile extends AppCompatActivity {

    private ImageView profileImg ;
    private TextView nbrDmd ;
    private TextView profileMail;
    private TextView profilePhone;
    private TextView profileAdresse;
    private TextView profileVille;
    private TextView profileName;

    DatabaseReference databaseReference;
    FirebaseUser user ;
    private String uid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        profileImg = this.findViewById(R.id.profileImg);
        nbrDmd = this.findViewById(R.id.nbrDmd);
        profileName = this.findViewById(R.id.profileName);
        profileMail = this.findViewById(R.id.profileMail);
        profilePhone = this.findViewById(R.id.profilePhone);
        profileAdresse = this.findViewById(R.id.profileAdresse);
        profileVille = this.findViewById(R.id.profileVille);


    }

    @Override
    protected void onStart() {
        super.onStart();

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nom = dataSnapshot.child(uid).child("nom").getValue(String.class);
                String prenom = dataSnapshot.child(uid).child("prenom").getValue(String.class);
                String email = dataSnapshot.child(uid).child("email").getValue(String.class);
                String tel = dataSnapshot.child(uid).child("tel").getValue(String.class);
                String ville = dataSnapshot.child(uid).child("ville").getValue(String.class);
                String adresse = dataSnapshot.child(uid).child("adresse").getValue(String.class);

                String fullName = prenom+" "+ nom;

                profileName.setText(fullName);
                profileAdresse.setText(adresse);
                profileMail.setText(email);
                profileVille.setText(ville);
                profilePhone.setText(tel);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case R.id.item1:
                Intent intent = new Intent(ClientProfile.this, updateProfileClient.class);
                startActivity(intent);
                break;
            case R.id.item2:
                //Intent intent = new Intent(MyList.this, AjouterEtab.class);
                //startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
}

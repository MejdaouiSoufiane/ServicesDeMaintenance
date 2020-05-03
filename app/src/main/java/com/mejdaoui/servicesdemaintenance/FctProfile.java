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


        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayImage(v);

            }
        });
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
                    DataSnapshot secteur = dataSnapshot.child("secteur");

                    String fullName = prenom+" "+ nom;

                    userName.setText(fullName);
                    userVille.setText(ville);
                    userMail.setText(email);
                    userPhone.setText(tel);
                    userAdresse.setText(adresse);
                    userService.setText(secteur.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            ref.addListenerForSingleValueEvent(eventListener);

            if(user.getPhotoUrl()!= null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(profileImg);
            }

        }

        public void displayImage(View v) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.alert_dialog, null);
            ImageView iv = (ImageView) dialogLayout.findViewById(R.id.imageView);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user.getPhotoUrl()!= null){
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(iv);
            }
            builder.setPositiveButton("OK", null);
            builder.setView(dialogLayout);
            builder.create().show();

        }


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
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent intent1 = new Intent(ClientProfile.this, Login.class);
                    startActivity(intent1);
                    break;
            }
            return super.onOptionsItemSelected(item);
        }

        public void HandleImage(View view){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!= null){
                startActivityForResult(intent, TAKE_IMAGE_CODE);
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == TAKE_IMAGE_CODE){
                switch (resultCode){
                    case RESULT_OK:
                        final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        profileImg.setImageBitmap(bitmap);
                        //handleUpload(bitmap);
                }
            }
}

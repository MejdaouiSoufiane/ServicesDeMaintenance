package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
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

public class ClientProfile extends AppCompatActivity {

    public static final String TAG = "ClientProfile" ;
    private ImageView profileImg ;
    private TextView nbrDmd ;
    private TextView profileMail, profileName, profilePhone, profileAdresse, profileVille;
    //private Button up;

    private Toolbar tool;

    int TAKE_IMAGE_CODE = 10001 ;

    DatabaseReference databaseReference;
    FirebaseUser user ;
    private String uid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        profileImg = this.findViewById(R.id.img);
        nbrDmd = this.findViewById(R.id.nbrDmd);
        profileName = this.findViewById(R.id.profileName);
        profileMail = this.findViewById(R.id.profileMail);
        profilePhone = this.findViewById(R.id.profilePhone);
        profileAdresse = this.findViewById(R.id.profileAdresse);
        profileVille = this.findViewById(R.id.profileVille);

        tool = (Toolbar) this.findViewById(R.id.tool);


        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               displayImage(v);

            }
        });




        this.setTitle("Profile");
        setSupportActionBar(tool);

    }

  @Override
    protected void onStart() {
        super.onStart();

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference ref = databaseReference.child("clients").child(uid);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nom = dataSnapshot.child("nom").getValue(String.class);
                String prenom = dataSnapshot.child("prenom").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String tel = dataSnapshot.child("telephone").getValue(String.class);
                String ville = dataSnapshot.child("ville").getValue(String.class);
                String adresse = dataSnapshot.child("adresse").getValue(String.class);

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
    /* private void handleUpload(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uid+".jpeg");

        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure:", e.getCause() );

                    }
                });
    }*/

    /*private void getDownloadUrl(StorageReference reference){
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: "+ uri);
                       // setUserProfileUrl(uri);

                    }
                });
    }

    /*private void setUserProfileUrl(Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ClientProfile.this, "Photo de profile enregistrée", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ClientProfile.this, "photo de profile échouée", Toast.LENGTH_SHORT).show();
                    }
                });
                }
    */
}

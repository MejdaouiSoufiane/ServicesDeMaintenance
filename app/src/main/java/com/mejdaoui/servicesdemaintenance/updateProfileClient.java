package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class updateProfileClient extends AppCompatActivity {

    public static final String TAG = "updateProfileClient" ;
    private EditText nomProfile, prenomProfile, mailProfile, phoneProfile, adresseProfile, villeProfile, telProfile;
    private ImageView imageView;

    int TAKE_IMAGE_CODE = 10001 ;
    private Button update;
    FirebaseUser user;
    String uid ;
    DatabaseReference databaseReference;
    UserProfileChangeRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        imageView = (ImageView) this.findViewById(R.id.img);
        nomProfile = (EditText) this.findViewById(R.id.nomProfile);
        prenomProfile = (EditText) this.findViewById(R.id.prenomProfile);
        mailProfile = (EditText) this.findViewById(R.id.mailProfile);
        telProfile = (EditText) this.findViewById(R.id.telProfile);
        villeProfile = (EditText) this.findViewById(R.id.villeprofile);
        adresseProfile = (EditText) this.findViewById(R.id.adresseprofile);
        update = (Button) this.findViewById(R.id.update);


       imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
                //HandleImage(v);
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("clients").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nom = dataSnapshot.child("nom").getValue(String.class);
                String prenom = dataSnapshot.child("prenom").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String tel = dataSnapshot.child("telephone").getValue(String.class);
                String ville = dataSnapshot.child("ville").getValue(String.class);
                String adresse = dataSnapshot.child("adresse").getValue(String.class);

                nomProfile.setText(nom);
                prenomProfile.setText(prenom);
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
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getPhotoUrl()!= null){
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(imageView);

        }
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

        user.updateEmail(mail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Profile modifié", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        startActivity(new Intent(updateProfileClient.this, ClientProfile.class));

    }

    private void selectImage(View v) {
        final CharSequence[] options = { "Prendre une photo", "Choisir de la gallerie","Annuler" };

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Prendre une photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choisir de la gallerie")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Annuler")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap selectedImage;

        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                        handleUpload(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri uri =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (uri != null) {
                            Cursor cursor = getContentResolver().query(uri,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                selectedImage = BitmapFactory.decodeFile(picturePath);
                                imageView.setImageBitmap(selectedImage);
                                cursor.close();
                                handleUpload(selectedImage);

                            }
                        }

                    }
                    break;
            }
        }



    }


    private void handleUpload(Bitmap bitmap){
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
    }

    private void getDownloadUrl(StorageReference reference){
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: "+ uri);
                        setUserProfileUrl(uri);

                    }
                });
    }

    private void setUserProfileUrl(Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("clients");
        ref.child(uid).child("image").setValue(uri.toString());

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(updateProfileClient.this, "Photo de profile enregistrée", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(updateProfileClient.this, "photo de profile échouée", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}



   /* public void HandleImage(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!= null){
            startActivityForResult(intent, TAKE_IMAGE_CODE);
        }
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_IMAGE_CODE){
            switch (resultCode){
                case RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    profileImg.setImageBitmap(bitmap);
                    handleUpload(bitmap);

            }
        }
    }
*/
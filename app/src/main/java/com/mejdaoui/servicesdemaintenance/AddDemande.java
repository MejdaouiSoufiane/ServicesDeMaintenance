package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AddDemande extends AppCompatActivity {
    static final int CAMERA_REQUEST_CODE = 1;
    static final int MAP_REQUEST_CODE = 2;

    private EditText titre,desc;
    private Spinner spinner_service, spinner_genre,spinner_age;
    private TextView date_dispo,heure_dispo;
    ImageView img;

    Uri url;
    private Uri filePath;
    private String stitre,sdesc,sservice,sdate,sgenre,uid_user,ville;
    private double lat_location;
    private double long_location;
    private String sheure;
    private String sage;


    String s;
    Demande demande;

    private Button take_pic;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    DatabaseReference dbDemande;
    private StorageReference mStorage;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_demande);

        dbDemande = FirebaseDatabase.getInstance().getReference("Demandes");

        titre= (EditText)  this.findViewById(R.id.titre);
        desc= (EditText)  this.findViewById(R.id.description);

        heure_dispo= (TextView)  this.findViewById(R.id.heure);
        date_dispo = (TextView) this.findViewById(R.id.disponibilite);
        img=(ImageView)this.findViewById(R.id.new_image);

        //aut
       user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // UID specific to the provider
                uid_user = profile.getUid();
            }
        }
        //spinner service
        spinner_service();
        //spinner genre
        spinner_genre();
        //spinner_age
        spinner_age();
        //date
        date();
        //heure
        heure();
        //take picture
        take_picture();
    }



    private void heure() {
        heure_dispo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minutes = cal.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(
                        AddDemande.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                heure_dispo.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, false);

                //  dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("ff", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = day + "/" +month + "/" +  year;
                date_dispo.setText(date);
            }
        };
    }

    private void  date() {
        date_dispo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddDemande.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("ff", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = day + "/" +month + "/" +  year;
                date_dispo.setText(date);
            }
        };

    }

    private void spinner_age() {
        spinner_age = (Spinner)findViewById(R.id.spinner_age);
        ArrayAdapter<String> myAdapterA = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.age));
        myAdapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_age.setAdapter(myAdapterA);
        spinner_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                spinner_age.setSelection(i);
                String selectedItemText = (String) parent.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spinner_genre() {
        spinner_genre = (Spinner) findViewById(R.id.spinner_genre);
        ArrayAdapter<String> myAdapterG = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.genre));
        myAdapterG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_genre.setAdapter(myAdapterG);
        spinner_genre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                spinner_genre.setSelection(i);
                String selectedItemText = (String) parent.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void spinner_service() {
        spinner_service = (Spinner) findViewById(R.id.spinner_service);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.secteur));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_service.setAdapter(myAdapter);
        spinner_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                spinner_service.setSelection(i);
                String selectedItemText = (String) parent.getItemAtPosition(i);

                // if(selectedItemText.equals("Normal"))

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void btn_map(View view){
        Intent intent = new Intent(AddDemande.this, MapsActivity.class);
        startActivityForResult(intent, MAP_REQUEST_CODE);
    }



    private void take_picture() {
        take_pic = (Button)this.findViewById(R.id.button_img);
        take_pic.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {
                                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                            startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);

                                        }
                                    }

        );

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == RESULT_OK && requestCode==CAMERA_REQUEST_CODE && data != null && data.getData() != null){
                    Bitmap photo = (Bitmap) data.getExtras().get("data") ;
                    img.setImageBitmap(photo);
                    filePath = data.getData();
                    Toast.makeText
                            (getApplicationContext(), "here on activity" , Toast.LENGTH_SHORT)
                            .show();

    }
/// for map data
        if (requestCode == MAP_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                lat_location = data.getDoubleExtra("lat_location", 0);
                long_location = data.getDoubleExtra("long_location", 0);
                ville = data.getStringExtra("ville_location");
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText
                        (getApplicationContext(), "Vous devez selectionner votre position" , Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    public void storage_image(){
            mStorage = FirebaseStorage.getInstance().getReference().child("Photo_Demandes").child(filePath.getLastPathSegment());

            mStorage.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddDemande.this, "uploading.....;", Toast.LENGTH_SHORT).show();
                    //Uri url = taskSnapshot.getDownloadUrl();
                     Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                      while(!uri.isComplete());
                      url = uri.getResult();
                       dbDemande.child("adr_picture").setValue(url.toString());
                      //  s=url.toString();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddDemande.this, "Upload failed", Toast.LENGTH_SHORT).show();


                }
            });



    }

    public void btn_ajouter(View view){
        stitre = titre.getText().toString();
        sdesc = desc.getText().toString();
        sservice = spinner_service.getSelectedItem().toString();

        sdate = date_dispo.getText().toString();
        sheure = heure_dispo.getText().toString();
        sgenre = spinner_genre.getSelectedItem().toString();
        sage = spinner_age.getSelectedItem().toString();




        if (!TextUtils.isEmpty(stitre) && !TextUtils.isEmpty(sdesc) && !TextUtils.isEmpty(sservice)   && !TextUtils.isEmpty(sgenre) &&  !TextUtils.isEmpty(sage) && !TextUtils.isEmpty(sdate) && lat_location != 0 && long_location != 0 ) {
            String iddmd = dbDemande.push().getKey();
            List<String> listFct = new ArrayList<>();
           // sadrpict = mStorage.getDownloadUrl().toString();
            demande = new Demande(iddmd,uid_user, stitre, sdesc, sservice, sdate, sheure, lat_location, long_location,ville, sage, sgenre,"","En Attente",listFct);

            dbDemande.child(iddmd).setValue(demande);
            dbDemande = dbDemande.child(iddmd);
            storage_image();

            Toast.makeText(this,"Demande ajoutée",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AddDemande.this,ListDemande.class);
            startActivity(intent);
            finish();

        }
        else{
            Toast.makeText(this,"Veuillez remplir les champs",Toast.LENGTH_LONG).show();
        }

    }



}

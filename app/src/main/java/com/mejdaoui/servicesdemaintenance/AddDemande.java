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
import java.util.Calendar;
import java.util.UUID;

public class AddDemande extends AppCompatActivity {
    static final int CAMERA_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int PERMISSION_CODE = 1000 ;
    private static final int IMAGE_CAPTURE_CODE = 1001  ;

    private EditText titre,desc;
    private Spinner spinner_service, spinner_genre,spinner_age;
    private TextView date_dispo,heure_dispo;
    ImageView img;
    Bitmap bitmap;

    Uri uriimg,url;
    private Uri filePath;
    private String stitre,sdesc,sservice,sdate,sgenre,sadrpict;
    private int lat_location;
    private int long_location;
    private String sheure;
    private String sage;


    String s;
    Demande demande;

    private Button take_pic;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    DatabaseReference dbDemande;
    private StorageReference mStorage;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

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

        //auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
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

    private void date() {
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
                Toast.makeText
                        (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                        .show();
                // if(selectedItemText.equals("Normal"))

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void btn_map(View view){
        Intent intent = new Intent(AddDemande.this, MapsActivity.class);
        startActivity(intent);
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

    private void openCamera() {

        /*ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        uriimg = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uriimg);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);*/

    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE : {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    openCamera();
                }else  {
                    Toast.makeText(this,"Permission denied ...",Toast.LENGTH_SHORT).show();
                }
            }

        }
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == RESULT_OK && requestCode==CAMERA_REQUEST_CODE && data != null && data.getData() != null){
                    Bitmap photo = (Bitmap) data.getExtras().get("data") ;
                    img.setImageBitmap(photo);
                    filePath = data.getData();

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

                    Toast.makeText(AddDemande.this, "Uploaded "+url.toString(), Toast.LENGTH_SHORT).show();


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




        if (!TextUtils.isEmpty(stitre) && !TextUtils.isEmpty(sdesc) && !TextUtils.isEmpty(sservice)   && !TextUtils.isEmpty(sgenre) &&  !TextUtils.isEmpty(sage) && !TextUtils.isEmpty(sdate) ) {
            String iddmd = dbDemande.push().getKey();

           // sadrpict = mStorage.getDownloadUrl().toString();
            demande = new Demande(iddmd,firebaseUser.getProviderId(), stitre, sdesc, sservice, sdate, sheure, 0, 0, sage, sgenre,"","En Attente");
            dbDemande.child(iddmd).setValue(demande);
            dbDemande = dbDemande.child(iddmd);
            storage_image();

            Toast.makeText(this,"Demande ajoutée",Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(this,"Veuillez remplir les champs",Toast.LENGTH_LONG).show();
        }

    }



}

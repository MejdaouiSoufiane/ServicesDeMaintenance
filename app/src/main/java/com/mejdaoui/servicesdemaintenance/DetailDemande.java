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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class DetailDemande extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
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
    private String stitre,sdesc,sservice,sdate,sgenre,sadrpict;
    private int lat_location;
    private int long_location;
    private String sheure;
    private String sage;

    Demande newDemande,oldDemande;

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
        setContentView(R.layout.activity_detail_demande);

        titre= (EditText)  this.findViewById(R.id.titre);
        desc= (EditText)  this.findViewById(R.id.description);

        heure_dispo= (TextView)  this.findViewById(R.id.heure);
        date_dispo = (TextView) this.findViewById(R.id.disponibilite);
        img=(ImageView)this.findViewById(R.id.new_image);

        //auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        setDate();
        setHeure();
        takePicture();
        setValues();
    }

    private void takePicture() {
        //take picture
        take_pic = (Button)this.findViewById(R.id.button_img);
        take_pic.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {
                                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED||checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED){
                                                    //permission not enabled
                                                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                                    requestPermissions(permission,PERMISSION_CODE);
                                                }else{
                                                    openCamera();
                                                }
                                            }else{
                                                openCamera();
                                            }
                                        }
                                    }

        );

    }

    private void setHeure() {
        //heure
        heure_dispo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minutes = cal.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(
                        DetailDemande.this,
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

    private void setDate() {
        //date
        date_dispo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DetailDemande.this,
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

    private void setValues() {
        dbDemande = FirebaseDatabase.getInstance().getReference("Demandes").child(getIntent().getStringExtra("id_demande"));
        dbDemande.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                oldDemande = dataSnapshot.getValue(Demande.class);
                titre.setText(oldDemande.getTitre());
                desc.setText(oldDemande.getDescription());
                heure_dispo.setText(oldDemande.getHeure());
                date_dispo.setText(oldDemande.getDate_dispo());
                String s = oldDemande.getAdr_picture();
              //  Picasso.get().load(s).resize(50, 50).into(img);
                //spinner_age.add;


                //spinner service
                String compareValueService = oldDemande.getService();
                spinner_service = (Spinner) findViewById(R.id.spinner_service);
                ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(DetailDemande.this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.secteur));
                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_service.setAdapter(myAdapter);
                if (compareValueService != null){
                    int spinnerPosition = myAdapter.getPosition(compareValueService);
                    spinner_service.setSelection(spinnerPosition);
                }

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

                //spinner genre
                String compareValueGenre = oldDemande.getGenre_fon();
                spinner_genre = (Spinner) findViewById(R.id.spinner_genre);
                ArrayAdapter<String> myAdapterG = new ArrayAdapter<String>(DetailDemande.this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.genre));
                myAdapterG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_genre.setAdapter(myAdapterG);
                if (compareValueService != null){
                    int spinnerPosition = myAdapterG.getPosition(compareValueGenre);
                    spinner_genre.setSelection(spinnerPosition);
                }
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

                //spinner_age
                String compareValueAge = oldDemande.getAge_fonc();
                spinner_age = (Spinner)findViewById(R.id.spinner_age);
                ArrayAdapter<String> myAdapterA = new ArrayAdapter<String>(DetailDemande.this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.age));
                myAdapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_age.setAdapter(myAdapterA);
                if (compareValueAge != null){
                    int spinnerPosition = myAdapterA.getPosition(compareValueAge);
                    spinner_age.setSelection(spinnerPosition);
                }
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        uriimg = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uriimg);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);

    }

    @Override
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
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ){
            img.setImageURI(uriimg);
            mStorage = FirebaseStorage.getInstance().getReference().child("Photo_Demandes").child(uriimg.getLastPathSegment());

            mStorage.putFile(uriimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(DetailDemande.this, "uploading.....;", Toast.LENGTH_SHORT).show();
                    //Uri url = taskSnapshot.getDownloadUrl();
                          /* Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uri.isComplete());
                            Uri url = uri.getResult();
                            demande.setAdr_picture(url.toString());


                            Log.i("FBApp1 URL ", url.toString());*/
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });



        }
    }

    public void btn_modifier(View view){
        stitre = titre.getText().toString();
        sdesc = desc.getText().toString();
        sservice = spinner_service.getSelectedItem().toString();
        sdate = date_dispo.getText().toString();
        sheure = heure_dispo.getText().toString();
        sgenre = spinner_genre.getSelectedItem().toString();
        sage = spinner_age.getSelectedItem().toString();
      //  sadrpict = mStorage.getPath();


        if (!TextUtils.isEmpty(stitre) && !TextUtils.isEmpty(sdesc) && !TextUtils.isEmpty(sservice)   && !TextUtils.isEmpty(sgenre) &&  !TextUtils.isEmpty(sage) && !TextUtils.isEmpty(sdate) ) {
          //  String iddmd = dbDemande.getKey();
           // newDemande = new Demande(oldDemande.getIdDemande(),oldDemande.getIdClient(), stitre, sdesc, sservice, sdate, sheure, 0, 0, sage, sgenre,"En Attente");
            dbDemande.setValue(newDemande);
            Toast.makeText(this,"Demande modifiée",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(DetailDemande.this, ListDemande.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this,"Veuillez remplir les champs",Toast.LENGTH_LONG).show();
        }

    }
}

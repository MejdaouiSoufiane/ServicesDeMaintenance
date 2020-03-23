package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddDemande extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private EditText titre,desc,heure,age;
    private Spinner spinner_service, spinner_genre;
    private CalendarView date;
    ImageView img;
    Bitmap bitmap;
    Uri uriimg;
    private String stitre,sdesc,sservice,sdate,sgenre;
    private int lat_location,long_location,sheure,sage;

    FirebaseFirestore database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_demande);

        database = FirebaseFirestore.getInstance();

        titre= (EditText)  this.findViewById(R.id.titre);
        desc= (EditText)  this.findViewById(R.id.description);
        heure= (EditText)  this.findViewById(R.id.heure);
        age= (EditText)  this.findViewById(R.id.age);

        date = (CalendarView) this.findViewById(R.id.disponibilite);
        img=(ImageView)this.findViewById(R.id.new_image);
        //spinner service
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
        //spinner genre
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

    public void btn_map(View view){
        Intent intent = new Intent(AddDemande.this, MapsActivity.class);
        startActivity(intent);
    }

    public  void btn_img(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void btn_ajouter(View view){
        stitre = titre.getText().toString();
        sdesc = desc.getText().toString();
        sservice = spinner_service.getSelectedItem().toString();
        date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                sdate = year+"/"+month+"/"+dayOfMonth;
            }
        });
        sheure = Integer.parseInt(heure.getText().toString());
        sgenre = spinner_genre.getSelectedItem().toString();
        sage = Integer.parseInt(age.getText().toString());


        Demande demande = new Demande("0",stitre,sdesc,sservice,sdate,sheure,lat_location,long_location,sage,sgenre);
        database.collection("demandes").add(demande).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("0", "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("1", "Error adding document", e);
                    }
    });
    }

    final int PICTURE_CODE = 100;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICTURE_CODE:
                if (resultCode == RESULT_OK ){
                    try{
                        uriimg = data.getData();

                        if(uriimg != null){
                            InputStream stream = getContentResolver().openInputStream(uriimg);
                            bitmap = BitmapFactory.decodeStream(stream);
                            img.setImageBitmap(bitmap);
                            //   img.setImageURI(uriimg);


                        }
                    }catch (FileNotFoundException e){ e.printStackTrace();}
                }
                break;
        }
    }

}

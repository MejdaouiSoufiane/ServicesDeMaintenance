package com.mejdaoui.servicesdemaintenance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListFonct extends AppCompatActivity {

    private List<Fonctionnaire> fcts = new ArrayList<>();
    DatabaseReference databaseReference ;
    private String idDmd ;
    private List<String> idFct = new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fonct);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = this.getIntent();

        idDmd = intent.getStringExtra("idDmd");

        databaseReference = databaseReference.child("Demandes").child(idDmd);

        idFct = databaseReference.child("idFonctionnaire");


        RecyclerView rv = (RecyclerView) findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setAdapter(new FctAdapter(this, fcts));
    }


}

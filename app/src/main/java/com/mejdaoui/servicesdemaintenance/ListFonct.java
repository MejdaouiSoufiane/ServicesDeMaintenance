package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListFonct extends AppCompatActivity {

    private List<Fonctionnaire> fcts = new ArrayList<>();
    private String idDmd ;
    private List<String> idList = new ArrayList<>();

    DatabaseReference databaseReference ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fonct);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent intent = this.getIntent();

        idDmd = intent.getStringExtra("idDmd");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference ref = databaseReference.child("Demandes").child(idDmd).child("idFonctionnaire");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int nbreFct = (int)dataSnapshot.getChildrenCount();
                int i = 0;
                if(nbreFct != 0){
                    while (i<=nbreFct){
                        String id = dataSnapshot.child(Integer.toString(i)).getValue(String.class);
                        idList.add(id);
                        i++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ref.addValueEventListener(eventListener);

       /* for(String id : idList){

            final DatabaseReference dataRef = databaseReference.child("fonctionnaires").child(id);

            dataRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Fonctionnaire fct = dataSnapshot.getValue(Fonctionnaire.class);
                    fcts.add(fct);

                    String nom = dataSnapshot.child("nom").getValue(String.class);
                    String prenom = dataSnapshot.child("prenom").getValue(String.class);
                    String tel = dataSnapshot.child("telephone").getValue(String.class);



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }*/


        RecyclerView rv = (RecyclerView) findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setAdapter(new FctAdapter(this, fcts));
    }



}

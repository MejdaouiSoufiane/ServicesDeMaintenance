package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListFonct extends AppCompatActivity {


    private String idDmd ;
    private List<String> idList = new ArrayList<>();

    private DatabaseReference databaseReference ;

    private RecyclerView rv ;
    private List<Fonctionnaire> fcts = new ArrayList<>();
    private FirebaseRecyclerOptions<Fonctionnaire> options;
    private FirebaseRecyclerAdapter<Fonctionnaire,FctAdapter> adapter;


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fonct);

        rv = findViewById(R.id.list);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        Intent intent = this.getIntent();

        idDmd = intent.getStringExtra("idDmd");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference dbref = databaseReference.child("Demandes").child(idDmd).child("idFonctionnaire");
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

        dbref.addValueEventListener(eventListener);

        DatabaseReference ref = databaseReference.child("fonctionnaires");
        ref.keepSynced(true);


        options = new FirebaseRecyclerOptions.Builder<Fonctionnaire>().setQuery(ref,Fonctionnaire.class).build();

        adapter = new FirebaseRecyclerAdapter<Fonctionnaire, FctAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FctAdapter holder, int position, @NonNull Fonctionnaire fonctionnaire) {
                String id = fonctionnaire.getIdFonct();

                if(idList.indexOf(id)!=-1){
                    String name = fonctionnaire.getNom()+" "+fonctionnaire.getPrenom();
                    holder.fullName.setText(name);
                    holder.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(holder.accept.getContext());
                            builder.setTitle("Confirmer").setMessage("Etes vous sûr de choisir ce fonctionnaire ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = ((Activity)holder.accept.getContext()).getIntent() ;
                                            String idDmd = intent.getStringExtra("idDmd");

                                            DatabaseReference dbref = databaseReference.child("Demandes").child(idDmd);
                                            dbref.child("etat").setValue("en cours");
                                            dbref.child("employe").setValue(id);

                                        }
                                    })
                                    .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                            builder.create();
                        }

                    });

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            }

            @NonNull
            @Override
            public FctAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FctAdapter(LayoutInflater.from(ListFonct.this).inflate(R.layout.item,parent,false));
            }
        };

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));



    }
}

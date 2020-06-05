package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mejdaoui.servicesdemaintenance.Activity.ClientHome;
import com.mejdaoui.servicesdemaintenance.Activity.DetailDemandeClt;
import com.mejdaoui.servicesdemaintenance.Adapter.FctAdapter;
import com.mejdaoui.servicesdemaintenance.Model.Demande;
import com.mejdaoui.servicesdemaintenance.Model.Fonctionnaire;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class ListFonct extends AppCompatActivity {

    private Toolbar toolb ;
    private String idDmd ;
    private List<String> idList = new ArrayList<>();

    private DatabaseReference databaseReference ;

    private RecyclerView rv ;
    private List<Fonctionnaire> fcts = new ArrayList<>();
    private FirebaseRecyclerOptions<Fonctionnaire> options;
    private FirebaseRecyclerAdapter<Fonctionnaire, FctAdapter> adapter;


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

        toolb = findViewById(R.id.toolb);
        this.setTitle("Liste des postulants");
        setSupportActionBar(toolb);

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
                final String id = fonctionnaire.getIdFonct();

                if(idList.indexOf(id)!=-1){
                    String name = fonctionnaire.getNom()+" "+fonctionnaire.getPrenom();
                    holder.fullName.setText(name);
                    holder.profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ListFonct.this, FctProfile.class);
                            intent.putExtra("id",id);
                            startActivity(intent);
                        }
                    });

                    /*String s = fonctionnaire.getImage();
                    if(s.equals("")==false)
                        Glide.with(getBaseContext())
                                .load(s)
                                .into(holder.img);*/
                       // Picasso.get().load(s).resize(50, 50).into(holder.img);



                    holder.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           /* AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                            builder.setTitle("Confirmer").setMessage("Etes vous sûr de vouloir choisir ce fonctionnaire ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {*/
                                            Intent intent = getIntent() ;
                                            String idDmd = intent.getStringExtra("idDmd");

                                            final DatabaseReference dbref = databaseReference.child("Demandes").child(idDmd);

                                            dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String etat = dataSnapshot.child("etat").getValue(String.class);
                                                    if(etat.equals("En Cours")){
                                                        Toast.makeText(ListFonct.this, "Vous avez déjà choisi un fonctionnaie pour ce travail. Vous ne pouvez pas choisir un autre.", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        dbref.child("etat").setValue("En Cours");
                                                        dbref.child("employe").setValue(id);
                                                        Toast.makeText(ListFonct.this, "Le fonctionnaire choisi sera notifié.", Toast.LENGTH_SHORT).show();

                                                        startActivity(new Intent(ListFonct.this, ClientHome.class));
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });
                                    /*.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                            builder.create();
                        }

                    });*/
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

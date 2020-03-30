package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mejdaoui.servicesdemaintenance.ViewHolder.DemandeViewHolder;

public class AccFonctionnaire extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<Demande> options;
    FirebaseRecyclerAdapter<Demande, DemandeViewHolder> adapter;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_fonct);
        /******* navigation drawer tricks ***/
            Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open_navigation_drawer,R.string.close_navigation_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        /******* End navigation drawer tricks***/

        recyclerView = findViewById(R.id.acc_fonct_recy);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Demandes");
        //System.out.println("---ataRefrenece : " + FirebaseDatabase.getInstance().getReference());
        //System.out.println("---dataRefreneceChild : " + FirebaseDatabase.getInstance().getReference().child("Demandes"));
        //System.out.println("---dataRefreneceChild ***  : " + FirebaseDatabase.getInstance().getReference("Demandes"));

        //recyclerView.setHasFixedSize(true);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(AccFonctionnaire.this,"****data exists*****",Toast.LENGTH_SHORT).show();
                System.out.println("///// child "+dataSnapshot.getChildren().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AccFonctionnaire.this,"No data exists",Toast.LENGTH_SHORT).show();
            }
        });

        options = new FirebaseRecyclerOptions.Builder<Demande>()
                .setQuery(databaseReference,Demande.class).build();

        adapter = new FirebaseRecyclerAdapter<Demande, DemandeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DemandeViewHolder demandeViewHolder, int i, @NonNull Demande demande) {
                System.out.println("************** i = "+i);
                Toast.makeText(AccFonctionnaire.this, "OnBindView", Toast.LENGTH_SHORT).show();
                demandeViewHolder.srv.setText(demande.getService());
                    demandeViewHolder.timeville.setText(demande.getHeure());
                    demandeViewHolder.clt.setText(demande.getIdClient());
                    demandeViewHolder.desc.setText(demande.getDescription());
                    demandeViewHolder.imageView.setImageResource(R.drawable.user);
            }

            @NonNull
            @Override
            public DemandeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Toast.makeText(AccFonctionnaire.this, "OnCreateViewHolder **** ", Toast.LENGTH_SHORT).show();
                View view = LayoutInflater.from(AccFonctionnaire.this).inflate(R.layout.acc_fonct_items,parent,false);
                return new DemandeViewHolder(view);
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
        super.onBackPressed();
    }

    @Override
    public void onStart(){
        super.onStart();
        if(!adapter.equals(null)) adapter.startListening();
    }


    @Override
    public void onStop(){
        super.onStop();
        if(!adapter.equals(null)) adapter.stopListening();
    }


    @Override
    public void onResume(){
        super.onResume();
        if(!adapter.equals(null)) adapter.stopListening();
    }
}

package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mejdaoui.servicesdemaintenance.ViewHolder.DemandeDetailHolder;
import com.squareup.picasso.Picasso;

public class DemandeDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Demande> options;
    private FirebaseRecyclerAdapter<Demande, DemandeDetailHolder> adapter;
    private DatabaseReference databaseReference;


    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }


    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demande_details);


        recyclerView = findViewById(R.id.recyclerHorizImages);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Demandes");
        databaseReference.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<Demande>().setQuery(databaseReference,Demande.class).build();

        adapter = new FirebaseRecyclerAdapter<Demande, DemandeDetailHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DemandeDetailHolder holder, int i, @NonNull Demande demande) {

                String url = demande.getAdr_picture();
                Picasso.get().load(url).into(holder.imageView);

            }

            @NonNull
            @Override
            public DemandeDetailHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new DemandeDetailHolder(LayoutInflater.from(DemandeDetails.this).inflate(R.layout.dd_images_items, viewGroup,false));
            }
        };


        recyclerView.setAdapter(adapter);
    }


}

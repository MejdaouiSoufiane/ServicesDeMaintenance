package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mejdaoui.servicesdemaintenance.ViewHolder.DemandeViewHolder;

public class AccFonctionnaire extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<Demande> options;
    FirebaseRecyclerAdapter<Demande, DemandeViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_fonct);

        recyclerView = findViewById(R.id.acc_fonct_recy);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Demandes");
        recyclerView.setHasFixedSize(true);

        options = new FirebaseRecyclerOptions.Builder<Demande>()
                .setQuery(databaseReference,Demande.class).build();

        adapter = new FirebaseRecyclerAdapter<Demande, DemandeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DemandeViewHolder demandeViewHolder, int i, @NonNull Demande demande) {
                    demandeViewHolder.srv.setText(demande.getService());
                    demandeViewHolder.timeville.setText(demande.getHeure());
                    demandeViewHolder.clt.setText(demande.getIdClient());
                    demandeViewHolder.desc.setText(demande.getDescription());
            }

            @NonNull
            @Override
            public DemandeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acc_fonct_items,parent,false);
                return new DemandeViewHolder(view);
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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

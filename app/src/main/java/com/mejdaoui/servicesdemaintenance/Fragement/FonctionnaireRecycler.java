package com.mejdaoui.servicesdemaintenance.Fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mejdaoui.servicesdemaintenance.Demande;
import com.mejdaoui.servicesdemaintenance.FctHome;
import com.mejdaoui.servicesdemaintenance.FirebaseViewHolder;
import com.mejdaoui.servicesdemaintenance.R;

import java.util.ArrayList;
import java.util.List;

public class FonctionnaireRecycler extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Demande> options;
    private FirebaseRecyclerAdapter<Demande, FirebaseViewHolder> adapter;
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fonct_home_fragment, container, false);

        recyclerView = view.findViewById(R.id.acc_fonct_recy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Demandes");
        databaseReference.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<Demande>().setQuery(databaseReference,Demande.class).build();

        adapter = new FirebaseRecyclerAdapter<Demande, FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder holder, int i, @NonNull Demande demande) {

                holder.clt.setText(demande.getIdClient());
                holder.srv.setText(demande.getService());
                holder.timeville.setText(demande.getHeure());
                holder.desc.setText(demande.getDescription());

               /* holder.itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Toast.makeText(FctHome.this, "Termooooo", Toast.LENGTH_SHORT).show();
                    }
                });*/
            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.acc_fonct_items, viewGroup,false));
            }
        };

        recyclerView.setAdapter(adapter);
        return view;
    }


}

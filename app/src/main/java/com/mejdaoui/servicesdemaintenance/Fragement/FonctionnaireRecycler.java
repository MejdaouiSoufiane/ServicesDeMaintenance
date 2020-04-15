package com.mejdaoui.servicesdemaintenance.Fragement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mejdaoui.servicesdemaintenance.Client;
import com.mejdaoui.servicesdemaintenance.Demande;
import com.mejdaoui.servicesdemaintenance.DemandeDetails;
import com.mejdaoui.servicesdemaintenance.FctHome;
import com.mejdaoui.servicesdemaintenance.FirebaseViewHolder;
import com.mejdaoui.servicesdemaintenance.R;


public class FonctionnaireRecycler extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Demande> options;
    private FirebaseRecyclerAdapter<Demande, FirebaseViewHolder> adapter;
    private DatabaseReference databaseReference;
    private CardView cardView;


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
        final View view = inflater.inflate(R.layout.fonct_home_fragment, container, false);

        recyclerView = view.findViewById(R.id.acc_fonct_recy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Demandes");
        databaseReference.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<Demande>().setQuery(databaseReference,Demande.class).build();

        adapter = new FirebaseRecyclerAdapter<Demande, FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FirebaseViewHolder holder, int i, @NonNull final Demande demande) {

                DatabaseReference clt = FirebaseDatabase.getInstance().getReference("clients").child(demande.getIdClient());
                clt.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Client client = dataSnapshot.getValue(Client.class);
                        //System.out.println("Client name : "+client.getNom());
                        holder.clt.setText(client.getNom());
                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getContext(), DemandeDetails.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("nomServ",demande.getService());
                                bundle.putString("desc",demande.getDescription());
                                bundle.putString("date",demande.getHeure());
                                i.putExtras(bundle);
                                startActivity(i);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //holder.clt.setText(demande.getIdClient());
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

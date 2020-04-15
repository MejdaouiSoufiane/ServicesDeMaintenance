package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mejdaoui.servicesdemaintenance.ViewHolder.DemandeDetailHolder;
import com.squareup.picasso.Picasso;

public class DetailDemandeClt extends AppCompatActivity {

    private  TextView service, ville, date, desc ;

    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Demande> options;
    private FirebaseRecyclerAdapter<Demande, DemandeDetailHolder> adapter;
    private DatabaseReference databaseReference, dbref;
    private TextView post ;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_demande_clt);

        post = (TextView) this.findViewById(R.id.post);

        service = (TextView) this.findViewById(R.id.service);
        desc = (TextView) this.findViewById(R.id.desc);
        ville = (TextView) this.findViewById(R.id.ville);
        date = (TextView) this.findViewById(R.id.date);

        final Intent inte = this.getIntent();
        id = inte.getStringExtra("id_demande");

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailDemandeClt.this , ListFonct.class);
                intent.putExtra("idDmd",id);
                startActivity(intent);
            }
        });


        dbref = FirebaseDatabase.getInstance().getReference("Demandes").child(id);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String tservice = dataSnapshot.child("service").getValue(String.class);
                String tdesc = dataSnapshot.child("description").getValue(String.class);
                String tdate = dataSnapshot.child("date_dispo").getValue(String.class);
                //String tville = dataSnapshot.child("ville").getValue(String.class);

                service.setText(tservice);
                desc.setText(tdesc);
                date.setText(tdate);
                //ville.setText(tville);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Demandes");

        recyclerView = findViewById(R.id.recyclerHorizImages);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<Demande>().setQuery(databaseReference,Demande.class).build();

        adapter = new FirebaseRecyclerAdapter<Demande, DemandeDetailHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DemandeDetailHolder holder, int i, @NonNull Demande demande) {

                String url = demande.getDescription();
                Picasso.get().load(url).into(holder.imageView);

            }

            @NonNull
            @Override
            public DemandeDetailHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new DemandeDetailHolder(LayoutInflater.from(DetailDemandeClt.this)
                        .inflate(R.layout.dd_images_items, viewGroup,false));
            }
        };
        recyclerView.setAdapter(adapter);
    }

}

package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mejdaoui.servicesdemaintenance.ViewHolder.DemandeDetailHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DemandeDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Demande> options;
    private FirebaseRecyclerAdapter<Demande, DemandeDetailHolder> adapter;
    private DatabaseReference databaseReference;
    private TextView nomServ;
    private TextView desc;
    private TextView date;
    private TextView ville;
    public ImageView postuler;
    public ImageView appeler;
    public ImageView position;
    public TextView postuler_;
    public TextView appeler_;
    public TextView position_;


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
        nomServ = findViewById(R.id.dd_servName);
        desc = findViewById(R.id.dd_desc);
        date = findViewById(R.id.dd_date);
        ville = findViewById(R.id.dd_vile);
        postuler = findViewById(R.id.apply_icon);
        appeler = findViewById(R.id.call_icon);
        position =findViewById(R.id.position_icon);
        postuler_ = findViewById(R.id.apply_icon_);
        appeler_ = findViewById(R.id.call_icon_);
        position_ = findViewById(R.id.position_icon_);

        final Bundle b = getIntent().getExtras();
        nomServ.setText(b.getString("nomServ"));
        desc.setText(b.getString("desc"));
        date.setText(b.getString("date"));

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
                //Picasso.get().load(url).into(holder.imageView);
                //Toast.makeText(DemandeDetails.this, "Url : "+url, Toast.LENGTH_SHORT).show();

            }

            @NonNull
            @Override
            public DemandeDetailHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new DemandeDetailHolder(LayoutInflater.from(DemandeDetails.this)
                        .inflate(R.layout.dd_images_items, viewGroup,false));
            }
        };

        recyclerView.setAdapter(adapter);

        postuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = b.getStringArrayList("idf");
                System.out.println("+++ List "+list.size());
                list.add(b.getString("currentFonct"));
                System.out.println("+++ List "+list.size());
                DatabaseReference d = FirebaseDatabase.getInstance().getReference("Demandes").child(b.getString("dmd_id"));
                System.out.println("++ Path : "+d);
                d.child("idFonctionnaire").setValue(list);
                for(int i=0;i<list.size();i++)
                    System.out.println("++++ LISTE : "+list.get(i));
                Toast.makeText(DemandeDetails.this, "postuler", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

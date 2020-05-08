package com.mejdaoui.servicesdemaintenance.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mejdaoui.servicesdemaintenance.Helpers.ImageTrans_CircleTransform;
import com.mejdaoui.servicesdemaintenance.Model.Demande;
import com.mejdaoui.servicesdemaintenance.PositionFonct;
import com.mejdaoui.servicesdemaintenance.R;
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
    public ImageView clientimage;
    public TextView clientname;
    private static final int REQUEST_CALL = 1;
    Bundle b;


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
        b = getIntent().getExtras();

        recyclerView = findViewById(R.id.recyclerHorizImages);
        nomServ = findViewById(R.id.dd_servName);
        desc = findViewById(R.id.dd_desc);
        date = findViewById(R.id.dd_date);
        ville = findViewById(R.id.dd_ville);
        postuler = findViewById(R.id.apply_icon);
        appeler = findViewById(R.id.call_icon);
        position =findViewById(R.id.position_icon);
        postuler_ = findViewById(R.id.apply_icon_);
        appeler_ = findViewById(R.id.call_icon_);
        position_ = findViewById(R.id.position_icon_);
        clientimage = findViewById(R.id.clientimage);
        clientname = findViewById(R.id.textclientname);

        nomServ.setText(b.getString("nomServ"));
        desc.setText(b.getString("desc"));
        date.setText(b.getString("date"));
        ville.setText(b.getString("ville"));
        clientname.setText(b.getString("clientname"));

        Picasso.get().load(b.getString("imageurl"))
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.man)
                .resize(80, 80)
                .transform(new ImageTrans_CircleTransform())
                .into(clientimage);

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
                //System.out.println("+++ List "+list.size());
                System.out.println("+++ List "+list.size());
                DatabaseReference d = FirebaseDatabase.getInstance().getReference("Demandes").child(b.getString("dmd_id"));
                //System.out.println("++ Path : "+d);
                for(int i=0;i<list.size();i++){
                    System.out.println("++++ LISTE : "+list.get(i));
                    String str = list.get(i);
                    if(str.equals(b.getString("currentFonct"))){
                        Toast.makeText(DemandeDetails.this, "Vous avez déja postuler.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                list.add(b.getString("currentFonct"));
                d.child("idFonctionnaire").setValue(list);
                Toast.makeText(DemandeDetails.this, "postuler", Toast.LENGTH_SHORT).show();
            }
        });

        postuler_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = b.getStringArrayList("idf");
                System.out.println("+++ List "+list.size());
                DatabaseReference d = FirebaseDatabase.getInstance().getReference("Demandes").child(b.getString("dmd_id"));
                for(int i=0;i<list.size();i++){
                    System.out.println("++++ LISTE : "+list.get(i));
                    String str = list.get(i);
                    if(str.equals(b.getString("currentFonct"))){
                        Toast.makeText(DemandeDetails.this, "Vous avez déja postuler.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                list.add(b.getString("currentFonct"));
                d.child("idFonctionnaire").setValue(list);
                Toast.makeText(DemandeDetails.this, "Votre demande est envoyée.", Toast.LENGTH_SHORT).show();
            }
        });


        appeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCallPhone();
            }
        });
        appeler_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCallPhone();
            }
        });

        position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent location = new Intent(DemandeDetails.this, PositionFonct.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("lat",b.getDouble("lat"));
                bundle.putDouble("lang",b.getDouble("lang"));
                location.putExtras(bundle);
                startActivity(location);
            }
        });
    }

    private void makeCallPhone() {
            String phone = b.getString("cltPhone").trim();
            if(phone.length() > 0){
                if(ContextCompat.checkSelfPermission(DemandeDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(
                            DemandeDetails.this,new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL);

                }else {
                    String tel = "tel:" + phone;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(tel)));
                }

            }else {
                Toast.makeText(this, "Téléphone du client n'existe pas.", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                makeCallPhone();
            else
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
        }
    }
}

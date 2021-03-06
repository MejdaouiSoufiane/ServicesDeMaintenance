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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mejdaoui.servicesdemaintenance.Adapter.CommentAdapter;
import com.mejdaoui.servicesdemaintenance.Helpers.ImageTrans_CircleTransform;
import com.mejdaoui.servicesdemaintenance.Model.Comment;
import com.mejdaoui.servicesdemaintenance.Model.Demande;
import com.mejdaoui.servicesdemaintenance.PositionFonct;
import com.mejdaoui.servicesdemaintenance.R;
import com.mejdaoui.servicesdemaintenance.ViewHolder.DemandeDetailHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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


    //for commments
    private String id;
    private FirebaseUser user;
    private RecyclerView cRecyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    EditText addComment;
    ImageView image_profile;
    TextView envoyer;
    String postid;
    String publisherid;


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
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);


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
                        Toast.makeText(DemandeDetails.this, "Vous avez déja postulé pour ce travail!.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                list.add(b.getString("currentFonct"));
                d.child("idFonctionnaire").setValue(list);
                Toast.makeText(DemandeDetails.this, "Votre damande est bien postulé!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DemandeDetails.this, "Vous avez déja postulé.", Toast.LENGTH_SHORT).show();
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

        commenter();
    }

    private void commenter() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        id = b.getString("idDemande");

        cRecyclerView = findViewById(R.id.recyclerHorizComments);
        cRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        cRecyclerView.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<>();

        commentAdapter = new CommentAdapter(this,commentList);
        cRecyclerView.setAdapter(commentAdapter);

        addComment = findViewById(R.id.add_comment);
        image_profile = findViewById(R.id.image_profile);
        envoyer = findViewById(R.id.envoyer);



        postid = id;
        if (user != null) {
            publisherid = user.getUid();
        }

        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addComment.getText().toString().equals("")){
                    Toast.makeText(DemandeDetails.this,"\n" +
                            "vous ne pouvez pas envoyer un commentaire vide",Toast.LENGTH_SHORT).show();
                }else {
                    addComment();
                }
            }
        });

        readComments();
        //Toast.makeText(DemandeDetails.this,"here",Toast.LENGTH_SHORT).show();

    }
    private void addComment(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(id);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("comment",addComment.getText().toString());
        hashMap.put("publisher",publisherid);

        reference.push().setValue(hashMap);
        addComment.setText("");
    }

    private void readComments(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);

                }

                commentAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

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

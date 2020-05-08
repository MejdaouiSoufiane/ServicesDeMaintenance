package com.mejdaoui.servicesdemaintenance.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageButton;

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
import com.mejdaoui.servicesdemaintenance.Adapter.DemandeAdapter;
import com.mejdaoui.servicesdemaintenance.ListFonct;
import com.mejdaoui.servicesdemaintenance.Model.Comment;
import com.mejdaoui.servicesdemaintenance.Model.Demande;
import com.mejdaoui.servicesdemaintenance.R;
import com.mejdaoui.servicesdemaintenance.ViewHolder.DemandeDetailHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DetailDemandeClt extends AppCompatActivity {

    private  TextView service, ville, date, desc, titre;

    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Demande> options;
    private FirebaseRecyclerAdapter<Demande, DemandeDetailHolder> adapter;
    private DatabaseReference databaseReference, dbref;
    private TextView post ;
    private String id;
    private FirebaseUser user;

    //for commments
    private RecyclerView cRecyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    EditText addComment;
    ImageView image_profile;
    TextView envoyer;
    String postid;
    String publisherid;

   // RecyclerView recyclerView;
    ImageButton imageButton;
    ArrayList<Demande> demandeList;
    DatabaseReference reference;
    DemandeAdapter adapterd;
   // FirebaseUser user;
    String uid_user, type;

    private ImageView postulant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_demande_clt);

        user = FirebaseAuth.getInstance().getCurrentUser();

        post = (TextView) this.findViewById(R.id.post);
        postulant = (ImageView) this.findViewById(R.id.apply_icon);
        service = (TextView) this.findViewById(R.id.service);
        desc = (TextView) this.findViewById(R.id.desc);
        ville = (TextView) this.findViewById(R.id.ville);
        date = (TextView) this.findViewById(R.id.date);
        titre = (TextView) this.findViewById(R.id.dmd_titre);

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

        postulant.setOnClickListener(new View.OnClickListener() {
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
                String tville = dataSnapshot.child("ville").getValue(String.class);
                String stitre = dataSnapshot.child("titre").getValue(String.class);

                service.setText(tservice);
                desc.setText(tdesc);
                date.setText(dataSnapshot.child("date_demande").getValue(Date.class).toString());
                ville.setText(tville);
                titre.setText(stitre);
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

        commenter();
    }

    private void commenter() {
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
                    Toast.makeText(DetailDemandeClt.this,"\n" +
                            "vous ne pouvez pas envoyer un commentaire vide",Toast.LENGTH_SHORT).show();
                }else {
                    addComment();
                }
            }
        });

        readComments();
        Toast.makeText(DetailDemandeClt.this,"here",Toast.LENGTH_SHORT).show();

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
}

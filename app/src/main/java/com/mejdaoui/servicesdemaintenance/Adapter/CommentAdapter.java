package com.mejdaoui.servicesdemaintenance.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mejdaoui.servicesdemaintenance.Activity.DetailDemandeClt;
import com.mejdaoui.servicesdemaintenance.Model.Client;
import com.mejdaoui.servicesdemaintenance.Model.Comment;
import com.mejdaoui.servicesdemaintenance.Model.Fonctionnaire;
import com.mejdaoui.servicesdemaintenance.R;

import java.util.List;

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private Context mContext;
    private List<Comment> mComment;
   // private String postid;

    private FirebaseUser firebaseUser;

    public CommentAdapter(Context mContext, List<Comment> mComment) {
        this.mContext = mContext;
        this.mComment = mComment;
    }



    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Comment comment = mComment.get(position);
        holder.comment.setText(comment.getComment());
        getUserInfo(/*holder.image_profile,*/holder.username,comment.getPublisher());
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailDemandeClt.class);
                intent.putExtra("publisherid",comment.getPublisher());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username,comment;
      //  public ImageView image_profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           // image_profile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
        }
    }

    private void getUserInfo(/*final ImageView imageView,*/ final TextView username, String publisherid){
        if(FirebaseDatabase.getInstance().getReference().child("clients").child(publisherid)!= null){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("clients").child(publisherid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Client client = dataSnapshot.getValue(Client.class);
              //  Glide.with(mContext).load(client.getImage()).into(imageView);
                if (client != null){
                username.setText(client.getNom()+" "+client.getPrenom());
                System.out.println(client);}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }
        if(FirebaseDatabase.getInstance().getReference().child("fonctionnaires").child(publisherid)!= null){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("fonctionnaires").child(publisherid);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Fonctionnaire fonctionnaire = dataSnapshot.getValue(Fonctionnaire.class);
                    //  Glide.with(mContext).load(client.getImage()).into(imageView);
                    if (fonctionnaire != null) {
                        username.setText(fonctionnaire.getNom() + " " + fonctionnaire.getPrenom());
                        System.out.println(fonctionnaire.getNom());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}

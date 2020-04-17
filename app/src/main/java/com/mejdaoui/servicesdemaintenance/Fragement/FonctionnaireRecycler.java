package com.mejdaoui.servicesdemaintenance.Fragement;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mejdaoui.servicesdemaintenance.Client;
import com.mejdaoui.servicesdemaintenance.Demande;
import com.mejdaoui.servicesdemaintenance.DemandeDetails;
import com.mejdaoui.servicesdemaintenance.FctHome;
import com.mejdaoui.servicesdemaintenance.FirebaseViewHolder;
import com.mejdaoui.servicesdemaintenance.R;

import java.text.DateFormatSymbols;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;


public class FonctionnaireRecycler extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Demande> options;
    private FirebaseRecyclerAdapter<Demande, FirebaseViewHolder> adapter;
    private DatabaseReference databaseReference;
    private CardView cardView;
    public String d;


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
        Query sorted = databaseReference.orderByChild("counter");
        databaseReference.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<Demande>().setQuery(sorted,Demande.class).build();

        adapter = new FirebaseRecyclerAdapter<Demande, FirebaseViewHolder>(options) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onBindViewHolder(@NonNull final FirebaseViewHolder holder, int i, @NonNull final Demande demande) {

                String  clt = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference user = FirebaseDatabase.getInstance().getReference("clients").child(clt);
                DatabaseReference client = FirebaseDatabase.getInstance().getReference("clients").child(demande.getIdClient());

                client.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            Client client = dataSnapshot.getValue(Client.class);
                            holder.clt.setText(client.getNom());
                            holder.cardView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getContext(), DemandeDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("nomServ",demande.getService());
                                    bundle.putString("desc",demande.getDescription());
                                    bundle.putString("date",holder.timeville.getText().toString());
                                    i.putExtras(bundle);
                                    startActivity(i);
                                }
                            });
                        }
                        else
                            Toast.makeText(getContext(), "Il n'ya aucune demande.", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(demande.getDate_demande());
                int h = calendar.get(Calendar.HOUR_OF_DAY);
                int m = calendar.get(Calendar.MINUTE);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);

                holder.srv.setText(demande.getService());
                holder.timeville.setText(demande.getHeure());
                holder.desc.setText(demande.getDescription());
                holder.timeville.setText(""+day+" "+getMonthForInt(month)+" "+h+":"+m);

                /** comparaison des date pour le trie**/
                    Date date = addHoursToJavaUtilDate(demande.getDate_demande(),2);
                    if(date.compareTo(new Date()) < 0)
                        holder.newDemande.setVisibility(View.GONE);
                /** fin **/

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

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    public Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }


}

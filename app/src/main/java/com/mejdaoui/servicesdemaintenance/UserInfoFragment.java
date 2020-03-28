package com.mejdaoui.servicesdemaintenance;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class UserInfoFragment extends Fragment {

    private Button next;
    private EditText nom, prenom, ville, tel;

    protected String tnom, tprenom, tville, ttel;

    private CheckBox client;
    private CheckBox fonctionnaire;

    protected String ttype ;


    public UserInfoFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        next = (Button) view.findViewById(R.id.next);
        nom = (EditText) view.findViewById(R.id.nom);
        prenom = (EditText) view.findViewById(R.id.prenom);
        ville = (EditText) view.findViewById(R.id.ville);
        tel = (EditText) view.findViewById(R.id.tel);
        client = (CheckBox) view.findViewById(R.id.client);
        fonctionnaire = (CheckBox) view.findViewById(R.id.fonctionnaire);

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userType(v);
            }
        });
        fonctionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userType(v);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData(v);
            }
        });

        return view;
    }

    private void sendData(View v) {

        tnom = nom.getText().toString();
        tprenom = prenom.getText().toString();
        tville = ville.getText().toString();
        ttel = tel.getText().toString();

        Intent intent = new Intent(getActivity().getBaseContext(), Register.class);
        intent.putExtra("nom",tnom);
        intent.putExtra("prenom",tprenom);
        intent.putExtra("ville",tville);
        intent.putExtra("tel",ttel);
        intent.putExtra("type", ttype);

        intent.putExtra("fragment", "UserInfo");

        getActivity().startActivity(intent);
    }

    private void userType(View v) {
        switch (v.getId()){
            case R.id.client:
                if (((CheckBox)v).isChecked())
                    ttype = "client";
                break;

            case R.id.fonctionnaire:
                if (((CheckBox)v).isChecked())
                    ttype = "fonctionnaire";
                break;
        }
    }
}

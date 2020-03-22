package com.mejdaoui.servicesdemaintenance;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class loginInfo extends Fragment {

    private Button creer;
    private EditText email, password, confirm;

    protected String temail, tpassword, tconfirm;

    private CheckBox tapisserie, plomberie, platerie, maçonnerie, peinture, electricite;

    private EditText otherSecteur;

    private List<String> secteur = new ArrayList<>();

    public loginInfo() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login_info, container, false);

        creer = (Button) view.findViewById(R.id.creer);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        confirm = (EditText) view.findViewById(R.id.confirm);

        temail = email.getText().toString();
        tpassword = password.getText().toString();
        tconfirm = confirm.getText().toString();

        verifyData();

        otherSecteur = (EditText) view.findViewById(R.id.otherSecteur);

        platerie = (CheckBox) view.findViewById(R.id.platerie);
        plomberie = (CheckBox) view.findViewById(R.id.plomberie);
        peinture = (CheckBox) view.findViewById(R.id.peinture);
        maçonnerie = (CheckBox) view.findViewById(R.id.maçonnerie);
        electricite = (CheckBox) view.findViewById(R.id.electricite);
        tapisserie = (CheckBox) view.findViewById(R.id.tapisserie);


        final RegisterFonctionnaire activity = (RegisterFonctionnaire) getActivity();

        creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addUser();
            }
        });

        return view;
    }

    private void verifyData() {
        if (temail.isEmpty()) {
            email.setError("Email obligatoire");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(temail).matches()) {
            email.setError("Entrez un email valide");
            email.requestFocus();
            return;
        }

        if (tpassword.isEmpty()) {
            password.setError("Mot de passe obligatoire");
            password.requestFocus();
            return;
        }

        if (!tconfirm.equals(tpassword)) {

            confirm.setError("Mot de passe incorrect");
            confirm.requestFocus();
            return;
        }

        Intent intent = new Intent(getActivity().getBaseContext(), RegisterFonctionnaire.class);
        intent.putExtra("email", temail);
        intent.putExtra("password", tpassword);
    }

}
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


public class loginInfoFragment extends Fragment {

    private Button creer;
    private EditText email, password, confirm;

    protected String temail, tpassword, tconfirm;

    private CheckBox tapisserie, plomberie, platerie, maçonnerie, peinture, electricite;

    private EditText otherSecteur;

    private List<String> secteur = new ArrayList<>();

    public loginInfoFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login_info, container, false);

        creer = (Button) view.findViewById(R.id.creer);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        confirm = (EditText) view.findViewById(R.id.confirm);

        final RegisterFonctionnaire activity = (RegisterFonctionnaire) getActivity();

        creer.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyData(v);
            }
        });

        return view;
    }

    private void verifyData(View v) {

        temail = email.getText().toString();
        tpassword = password.getText().toString();
        tconfirm = confirm.getText().toString();

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
        intent.putExtra("fragment","loginFragment");
        getActivity().startActivity(intent);
    }

}
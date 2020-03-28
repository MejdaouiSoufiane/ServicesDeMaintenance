package com.mejdaoui.servicesdemaintenance;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class loginInfoFragment extends Fragment {

    private Button creer;
    private EditText email, password, confirm;

    protected String temail, tpassword, tconfirm;
    private String tnom, tprenom, tville, ttel;

    public loginInfoFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login_info, container, false);

        /*tnom =getArguments().getString("nom");
        tprenom =getArguments().getString("prenom");
        tville = getArguments().getString("ville");
        ttel = getArguments().getString("tel");*/

        creer = (Button) view.findViewById(R.id.creer);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        confirm = (EditText) view.findViewById(R.id.confirm);

        creer.setOnClickListener(new View.OnClickListener() {
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

        Intent intent = new Intent(getActivity().getBaseContext(), Register.class);
        intent.putExtra("email", temail);
        intent.putExtra("password", tpassword);
        intent.putExtra("fragment","loginFragment");
        getActivity().startActivity(intent);
    }

}
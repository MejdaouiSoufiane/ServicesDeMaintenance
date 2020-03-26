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

import java.util.ArrayList;
import java.util.List;


public class secteurFragment extends Fragment  implements View.OnClickListener {

    private Button next;

    private CheckBox tapisserie, plomberie, platerie, maçonnerie, peinture, electricite;

    private EditText otherSecteur;

    private String tnom, tprenom, tville, ttel;

    private String tsecteur;
    private List<String> secteur = new ArrayList<>();

    public secteurFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_secteur, container, false);
/*
        tnom =getArguments().getString("nom");
        tprenom =getArguments().getString("prenom");
        tville = getArguments().getString("ville");
        ttel = getArguments().getString("tel");*/


        next = (Button) view.findViewById(R.id.next);

        otherSecteur = (EditText) view.findViewById(R.id.otherSecteur);

        platerie = (CheckBox) view.findViewById(R.id.platerie);
        plomberie = (CheckBox) view.findViewById(R.id.plomberie);
        peinture = (CheckBox) view.findViewById(R.id.peinture);
        maçonnerie = (CheckBox) view.findViewById(R.id.maçonnerie);
        electricite = (CheckBox) view.findViewById(R.id.electricite);
        tapisserie = (CheckBox) view.findViewById(R.id.tapisserie);

        platerie.setOnClickListener(this);
        plomberie.setOnClickListener(this);
        peinture.setOnClickListener(this);
        tapisserie.setOnClickListener(this);
        maçonnerie.setOnClickListener(this);
        electricite.setOnClickListener(this);

        tsecteur = otherSecteur.getText().toString();

        if (!tsecteur.isEmpty()) {
            secteur.add(tsecteur);
        }


        next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity().getBaseContext(), Register.class);
                        intent.putExtra("fragment","secteurFragment");
                        intent.putStringArrayListExtra("secteur", (ArrayList<String>) secteur);

                        getActivity().startActivity(intent);
                    }
                });



        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.platerie:
                if (((CheckBox)v).isChecked())
                    secteur.add("platerie");
                break;

            case R.id.plomberie:
                if (((CheckBox)v).isChecked())
                    secteur.add("plomberie");
                break;

            case R.id.peinture:
                if (((CheckBox)v).isChecked())
                    secteur.add("peinture");
                break;

            case R.id.tapisserie:
                if (((CheckBox)v).isChecked())
                    secteur.add("tapisserie");
                break;

            case R.id.maçonnerie:
                if (((CheckBox)v).isChecked())
                    secteur.add("maçonnerie");
                break;

            case R.id.electricite:
                if (((CheckBox)v).isChecked())
                    secteur.add("éléctricité");
                break;
        }

    }
}

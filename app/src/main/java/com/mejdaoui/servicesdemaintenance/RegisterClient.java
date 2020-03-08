package com.mejdaoui.servicesdemaintenance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterClient extends AppCompatActivity {

    private Button creer;
    private EditText nom;
    private EditText prenom;
    private EditText adresse;
    private EditText tel;
    private EditText email;
    private EditText pswd;
    private EditText confirm;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        database = FirebaseDatabase.getInstance().getReference("clients");

        creer = (Button) this.findViewById(R.id.creer);
        nom = (EditText) this.findViewById(R.id.nom);
        prenom = (EditText) this.findViewById(R.id.prenom);
        adresse = (EditText) this.findViewById(R.id.adresse);
        tel = (EditText) this.findViewById(R.id.tel);
        email = (EditText) this.findViewById(R.id.email);
        pswd = (EditText) this.findViewById(R.id.pswd);
        confirm = (EditText) this.findViewById(R.id.confirm);

        creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addClient();

               Intent main = new Intent(RegisterClient.this, MainActivity.class);
                startActivity(main);
            }
        });
    }

    private void addClient() {
        String tnom = nom.getText().toString();
        String tprenom = prenom.getText().toString();
        String temail = email.getText().toString();
        String tpswd = pswd.getText().toString();
        String tadresse = adresse.getText().toString();
        String ttel = tel.getText().toString();
        String tconfirm = confirm.getText().toString();

        if (!TextUtils.isEmpty(tnom)) {

            if (!tconfirm.equals(tpswd)) {

                //  confirm.setError("Mot de passe incorrect");
            }
                String id = database.push().getKey();

                Client client = new Client(id, tnom, tprenom, temail, tpswd, tadresse, ttel);

                database.child(id).setValue(client);

                Toast.makeText(this, "Votre compte est créé", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Entrez votre nom", Toast.LENGTH_LONG).show();
        }
    }

}


package com.mejdaoui.servicesdemaintenance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_fnct extends AppCompatActivity {

    TextView userName;
    TextView userVille, userRating, travauxNbr;
    TextView userMail, userPhone, userAdresse, userService;

    ImageView userImg;

    //private FirebaseDatabase database;
    //private DatabaseReference userRef;
    //private static final String USERS = "users";

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_fnct);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        userName = findViewById(R.id.userName);
        userVille = findViewById(R.id.userVille);
        userRating = findViewById(R.id.userRating);
        travauxNbr = findViewById(R.id.travauxNbr);
        userMail = findViewById(R.id.userMail);
        userPhone = findViewById(R.id.userPhone);
        userAdresse = findViewById(R.id.userAdresse);
        userService = findViewById(R.id.userService);

        userImg = findViewById(R.id.userImg);

        userName.setText(user.getEmail());
        //System.out.println(user.getEmail());

        //database = FirebaseDatabase.getInstance();
        //userRef = database.getReference(USERS);


    }
}

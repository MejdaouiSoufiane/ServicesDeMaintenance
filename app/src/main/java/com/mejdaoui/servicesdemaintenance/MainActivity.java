package com.mejdaoui.servicesdemaintenance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView sinClient;
    TextView sinWorker;
    LinearLayout circle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circle = (LinearLayout)findViewById(R.id.circle);
        sinClient = (TextView)findViewById(R.id.signupClient);
        sinWorker = (TextView)findViewById(R.id.signupWorker);
        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(MainActivity.this,LoginClient.class);
                startActivity(it);

            }
        });
        sinClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(
                        MainActivity.this,RegisterClient.class);
                startActivity(it);
            }
        });

        sinWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(
                        MainActivity.this,RegisterFonctionnaire.class);
                startActivity(it);
            }
        });

    }
}

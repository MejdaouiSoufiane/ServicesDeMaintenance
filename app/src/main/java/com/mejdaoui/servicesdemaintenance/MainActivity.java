package com.mejdaoui.servicesdemaintenance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView sinClient;
    TextView sinWorker;
    LinearLayout circle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circle = (LinearLayout)findViewById(R.id.circle);
        sinClient = (TextView)findViewById(R.id.sin2);
        sinWorker = (TextView)findViewById(R.id.sin3);
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
                        MainActivity.this,LoginClient.class);
                startActivity(it);
            }
        });

        sinWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(
                        MainActivity.this,LoginWorker.class);
                startActivity(it);
            }
        });

    }
}

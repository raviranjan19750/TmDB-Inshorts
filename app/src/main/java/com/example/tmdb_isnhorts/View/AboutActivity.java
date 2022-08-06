package com.example.tmdb_isnhorts.View;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tmdb_isnhorts.R;

public class AboutActivity extends AppCompatActivity {

    ImageView githubImageView;
    TextView githubUserNameTextView;
    String url = "https://github.com/raviranjan19750/AwesomeProject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        githubImageView = findViewById(R.id.githubImageView);
        githubUserNameTextView = findViewById(R.id.githubUserNameTextView);

        getSupportActionBar().setTitle("TmDb-Inshorts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        githubImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse(url);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);

                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {

                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url)));
                }

            }
        });

        githubUserNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse(url);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);

                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {

                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url)));
                }

            }
        });
    }


}
package com.example.tmdb_isnhorts.View;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tmdb_isnhorts.R;

public class AboutActivity extends AppCompatActivity {

    ImageView githubImageView;
    TextView githubUserNameTextView, clickTextView;
    LinearLayout githubLinkLinearLayout;
    String url = "https://github.com/raviranjan19750/TmDB-Inshorts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        githubImageView = findViewById(R.id.githubImageView);
        githubUserNameTextView = findViewById(R.id.githubUserNameTextView);
        clickTextView = findViewById(R.id.clickTextView);
        githubLinkLinearLayout = findViewById(R.id.githubLinkLinearLayout);

        getSupportActionBar().setTitle("TmDb-Inshorts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        clickTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGithubLink();

            }
        });
       githubLinkLinearLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               openGithubLink();

           }
       });
    }

    private void openGithubLink() {

        Uri uri = Uri.parse(url);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(i);
        } catch (ActivityNotFoundException e) {

            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url)));
        }

    }


}
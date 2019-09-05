package com.example.android.materialme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView mNewsHeading = findViewById(R.id.title_detail);
        TextView mNewsDetail = findViewById(R.id.newsTitle_detail);
        ImageView mSportsImage = findViewById(R.id.sportsImage_detail);
        Bundle extras = getIntent().getExtras();
            if (extras != null){
                mNewsHeading.setText(getIntent().getStringExtra("heading"));
                mNewsDetail.setText(getIntent().getStringExtra("info"));
                Glide.with(this).load(getIntent().getIntExtra("image_resource", 0)).into(mSportsImage);
            }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String NEWS_HEADING_KEY = "heading";
        outState.putString(NEWS_HEADING_KEY, getIntent().getStringExtra("heading"));
        String NEWS_INFO_KEY = "info";
        outState.putString(NEWS_INFO_KEY, getIntent().getStringExtra("info"));
        super.onSaveInstanceState(outState);
    }
}

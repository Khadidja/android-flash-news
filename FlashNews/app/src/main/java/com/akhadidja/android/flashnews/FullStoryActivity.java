package com.akhadidja.android.flashnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class FullStoryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_story);
        Intent intent = getIntent();
        if (intent != null){
            int position = intent.getIntExtra(Intent.EXTRA_TEXT, -1);
            Toast.makeText(this, "Story at pos "+position, Toast.LENGTH_SHORT).show();
        }
    }
}

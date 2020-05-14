package com.jenyasubbotina.easycard;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class resultactivity extends AppCompatActivity {
    String coun = "";
    TextView points;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_resultactivity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView points = findViewById(R.id.points);
        points.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        coun = getIntent().getStringExtra("results");
        int right1 = Integer.parseInt(coun.substring(0, coun.indexOf('/')));
        int vsego = Integer.parseInt(coun.substring(coun.indexOf('/') + 1));
        Button fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(resultactivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        float procent = (right1*100)/vsego;
        points.setText(right1 + "/" + vsego + '\n' + '\n' + Math.round(procent) + "%");
    }
    public void onBackPressed()
    {
      String a1 = "";
    }
}

package com.jenyasubbotina.easycard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AddActivity extends AppCompatActivity
{
    EditText word1, translate1;
    String words;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        final EditText word1 = findViewById(R.id.word1);
        final EditText translate1 = findViewById(R.id.translate1);
        setSupportActionBar(toolbar);
        FloatingActionButton saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                EditText word1 = findViewById(R.id.word1);
                EditText translate1 = findViewById(R.id.translate1);
                if ((!word1.getText().toString().equals("")) && (!translate1.getText().toString().equals("")))
                {
                    if (isClean(word1.getText().toString(), translate1.getText().toString()))
                    {
                        String res1 = word1.getText().toString() + "$" + translate1.getText().toString() + "&";
                        Intent backToMain = new Intent(AddActivity.this, MainActivity.class);
                        backToMain.putExtra("vozvrat", res1);
                        setResult(RESULT_OK, backToMain);
                        finish();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "You can't use symbols $ and &", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "One of inputs is empty", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isClean(String s1, String s2)
    {
        boolean ans = true;
        for (int i = 0; i < s1.length(); i++)
        {
            if ((s1.charAt(i) == '&') || (s1.charAt(i) == '$'))
                ans = false;
        }
        for (int i = 0; i < s2.length(); i++)
        {
            if ((s2.charAt(i) == '&') || (s2.charAt(i) == '$'))
                ans = false;
        }
        return ans;
    }
}

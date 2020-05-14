package com.jenyasubbotina.easycard;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StartquizActivity extends AppCompatActivity {
    File fwords = new File("storage/emulated/0/words.txt");
    ArrayList<Integer> wordsIntRandomizer;
    ArrayList<String> words;
    TextView quesNumber;
    int number = 0;
    TextView slovo;
    int vrash = 0;
    int right1 = 0;
    String key1 = "";
    String cur = "";
    Map<String, String> forquiz = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.startquiz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent yourRes = new Intent(StartquizActivity.this, resultactivity.class);
                String send1 = right1 + "/" + words.size();
                yourRes.putExtra("results", send1);
                startActivity(yourRes);
            }
        });
        final TextView quesNumber = findViewById(R.id.quesNumber);
        final TextView slovo = findViewById(R.id.slovo);
        final TextView textview = findViewById(R.id.textView);
        final ImageView image1 = findViewById(R.id.image1);
        textview.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        Button yes1 = findViewById(R.id.yes1);
        yes1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (number + 1 < forquiz.size())
                {
                    right1 += 1;
                    number += 1;
                    cur = (new ArrayList<String>(forquiz.keySet()).get(number));
                    slovo.setText(cur);
                    textview.setText("Tap to see translation");
                    int cur = number+1;
                    quesNumber.setText(cur + " of " + forquiz.size());
                }
                else
                {
                    right1 += 1;
                    showRes();
                }
                vrash = 0;
            }
        });

        Button no1 = findViewById(R.id.no1);
        no1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (number + 1 < forquiz.size())
                {
                    number += 1;
                    cur = (new ArrayList<String>(forquiz.keySet()).get(number));
                    slovo.setText(cur);
                    textview.setText("Tap to see translation");
                    int cur = number+1;
                    quesNumber.setText(cur + " of " + forquiz.size());
                }
                else
                {
                    showRes();
                }
                vrash = 0;
            }
        });

        image1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Animation rotateR = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animrotate);
                rotateR.setAnimationListener(new Animation.AnimationListener()
                {
                    public void onAnimationStart(Animation anim)
                    {
                        slovo.setText("");
                    };
                    public void onAnimationRepeat(Animation anim)
                    {
                    };
                    public void onAnimationEnd(Animation anim)
                    {
                        vrash += 1;
                        if (vrash % 2 == 0)
                        {
                            cur = (new ArrayList<String>(forquiz.keySet()).get(number));
                            slovo.setText(cur);
                            textview.setText("Tap to see translation");
                        }
                        else
                        {
                            slovo.setText(forquiz.get(cur));
                            textview.setText("Tap to see word");
                        }
                    };
                });
                image1.startAnimation(rotateR);
            }
        });

        words = parseString(readFromFile(fwords));
        wordsIntRandomizer = randWords(words);
        forquiz = convertForShuffle(words);
        cur = (new ArrayList<String>(forquiz.keySet())).get(number);
        slovo.setText(cur);
        quesNumber.setText((number+1) + " of " + forquiz.size());
    }

    private void showRes()
    {
        Intent yourRes = new Intent(StartquizActivity.this, resultactivity.class);
        String send1 = right1 + "/" + words.size();
        yourRes.putExtra("results", send1);
        startActivity(yourRes);
    }

    private ArrayList<Integer> randWords(ArrayList<String> skolko)
    {
        ArrayList<Integer> res1 = new ArrayList<>();
        for (int i = 0; i < skolko.size(); i++) {
            res1.add(i);
        }
        Collections.shuffle(res1);
        return res1;
    }

    private Map<String, String> convertForShuffle(ArrayList<String> a1)
    {
        Map<String, String> res1 = new HashMap<String, String>();
        for (int i = 0; i < a1.size(); i++)
        {
            String cur = a1.get(i);
            String key1 = cur.substring(0, cur.indexOf('$'));
            String value1 = cur.substring(cur.indexOf('$')+1, cur.length());
            res1.put(key1, value1);
        }
        return res1;
    }

    private ArrayList<String> parseString(String s1)
    {
        ArrayList<String> res1 = new ArrayList<String>();
        String cur = "";
        for (int i = 0; i < s1.length(); i++)
        {
            if ((s1.charAt(i)!= '&') && (i != s1.length() - 1))
            {
                cur += s1.charAt(i);
            }
            else
            {
                res1.add(cur);
                cur = "";
            }
        }
        return res1;
    }

    private String readFromFile(File f1)
    {
        String res1 = "";
        String sub = "";
        try
        {
            BufferedReader read1 = new BufferedReader(new FileReader(fwords));
            while ((sub = read1.readLine()) != null)
                res1 += sub;
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
        return res1;
    }
}

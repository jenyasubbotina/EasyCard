package com.jenyasubbotina.easycard;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {
    private static final String MY_SETTINGS = "my_settings";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    final int DURATION1 = 1000;
    ListView cards1;
    File fwords = new File("storage/emulated/0/words.txt");
    String pathFwords = "storage/emulated/0/words.txt";
    ArrayList<String> carta;
    ArrayAdapter<String> adapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        FloatingActionButton fab = findViewById(R.id.addBtn);
        verifyStoragePermissions(this);
        ListView cards1 = findViewById(R.id.cards1);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent addWordIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(addWordIntent, 1);
            }
        });
        try
        {
            if (!fwords.exists())
            {
                verifyStoragePermissions(this);
                fwords.createNewFile();
            }
        }
        catch (Exception e)
        {}
        SharedPreferences sp = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        boolean hasVisited = sp.getBoolean("hasVisited", false);
        if (!hasVisited)
        {
            Intent infoIntent = new Intent(MainActivity.this, infoactivity.class);
            startActivity(infoIntent);
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.commit();
        }
        setWordsOnListView(carta);
        cards1.canScrollHorizontally(1);

        carta = deleteDollar(parseString(readFromFile(fwords)));
        cards1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ArrayList<String> carta = deleteDollar(parseString(readFromFile(fwords)));
                Intent deleteactivity = new Intent(MainActivity.this, editordeleteactivity.class);
                deleteactivity.putExtra("which", carta.get(position).toString());
                startActivityForResult(deleteactivity, 1);
            }
        });

    }

    public static void verifyStoragePermissions(Activity activity)
    {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        int permission1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission1 != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private String readFromFile(File f1)
    {
        String res1 = "";
        String sub = "";
        verifyStoragePermissions(this);
        try
        {
            BufferedReader read1 = new BufferedReader(new FileReader(fwords));
            while ((sub = read1.readLine()) != null)
                res1 += sub;
        }
        catch (Exception e)
        {}
        return res1;
    }

    private ArrayList<String> deleteDollar(ArrayList<String> a1)
    {
        ArrayList<String> res1 = new ArrayList<String>();
        for (int i = 0; i < a1.size(); i++)
        {
            String cur1 = a1.get(i);
            cur1 = cur1.substring(0, cur1.indexOf('$')) + "➟" + cur1.substring(cur1.indexOf('$') + 1, cur1.length());
            res1.add(cur1);
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

    private void writeInFile(File f1, String what) {
        verifyStoragePermissions(this);
        try
        {
            if(!fwords.exists())
            {
                fwords.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(fwords, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(what);
            bufferedWriter.newLine();
            bufferedWriter.close();
        }
        catch(Exception e)
        {}
        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data != null)
        {
            String result1 = "";
            result1 = data.getStringExtra("vozvrat");
            if (resultCode == -1)
            {
                writeInFile(fwords, result1);
                setWordsOnListView(carta);
            }
            else if (resultCode == 1)
            {
                String r = data.getStringExtra("editordelit");
                setWordsOnListView(carta);
            }
        }
    }

    private void setWordsOnListView(ArrayList<String> a1)
    {
        ListView cards1 = findViewById(R.id.cards1);
        ArrayList<String> carta = deleteDollar(parseString(readFromFile(fwords)));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, carta);
        cards1.setAdapter(adapter1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        ArrayList<String> carta = deleteDollar(parseString(readFromFile(fwords)));
        try
        {
            if (id == R.id.action_startQuiz)
            {
                if (carta.size() > 1)
                {
                    Intent quizIntent = new Intent(MainActivity.this, StartquizActivity.class);
                    startActivityForResult(quizIntent, 1);
                }
                else
                    Toast.makeText(getApplicationContext(), "You need to have at least 2 words to start quiz", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {};
        if (id == R.id.action_delete_all)
        {
            if (fwords.delete())
            {
                Toast.makeText(getApplicationContext(),"All words were deleted", Toast.LENGTH_SHORT).show();
            }
            try
            {
                fwords.createNewFile();
            }
            catch (Exception e) {}
            setWordsOnListView(carta);
        }
        if (id == R.id.action_see_info)
        {
            Intent infoIntent = new Intent(MainActivity.this, infoactivity.class);
            startActivity(infoIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}

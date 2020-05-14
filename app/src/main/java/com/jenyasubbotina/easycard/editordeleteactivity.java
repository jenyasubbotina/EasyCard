package com.jenyasubbotina.easycard;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class editordeleteactivity extends AppCompatActivity {

    File fwords = new File("storage/emulated/0/words.txt");
    String pathFwords = "storage/emulated/0/words.txt";
    ArrayList<String> carta;
    String res1;
    TextView word1, translate1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editordeleteactivity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button delete1 = findViewById(R.id.delete);
        Button edit1 = findViewById(R.id.edit);

        final TextView word1 = findViewById(R.id.word1);
        final TextView translate1 = findViewById(R.id.translate1);

        Bundle arguments = getIntent().getExtras();
        res1 = arguments.get("which").toString();
        res1 = backOneDollar(res1);
        carta = parseString(readFromFile(fwords));
        word1.setText(res1.substring(0, res1.indexOf('$')));
        translate1.setText(res1.substring(res1.indexOf('$') + 1));

        delete1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                for (int i = 0; i < carta.size(); i++)
                {
                    if (carta.get(i).equals(res1))
                    {
                        carta.remove(i);
                    }
                }
                writeAllInFile(fwords, carta);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                Intent backToMain = new Intent(editordeleteactivity.this, MainActivity.class);
                backToMain.putExtra("editordelit", res1);
                setResult(RESULT_FIRST_USER, backToMain);
                finish();
            }
        });

        edit1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if ((!word1.getText().toString().equals("")) && (!translate1.getText().toString().equals("")) && isClean(translate1.getText().toString(), word1.getText().toString()))
                {
                    for (int i = 0; i < carta.size(); i++)
                    {
                        if (carta.get(i).equals(res1))
                            carta.set(i, word1.getText().toString()+"$"+translate1.getText().toString());
                    }
                    writeAllInFile(fwords, carta);
                    Toast.makeText(getApplicationContext(), "Edited", Toast.LENGTH_SHORT).show();
                    Intent backToMain = new Intent(editordeleteactivity.this, MainActivity.class);
                    backToMain.putExtra("editordelit", res1);
                    setResult(RESULT_FIRST_USER, backToMain);
                    finish();
                }
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
        catch (Exception e) {}
        return res1;
    }

    private String backOneDollar(String s1)
    {
        String res1 = "";
        res1 = s1.substring(0, s1.indexOf('➟')) + "$" + s1.substring(s1.indexOf('➟')+1);
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

    private void writeAllInFile(File f1, ArrayList<String> a1)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(fwords);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int i = 0; i < a1.size(); i++)
            {
                bufferedWriter.write(a1.get(i) + "&");
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch(Exception e) { Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show(); }
    }

}


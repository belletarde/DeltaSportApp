package com.enterprise.barsemlona.barsemlona_20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class TermosActivity extends AppCompatActivity {
    Button btnTermo;
    CheckBox checkTermo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termos);

        btnTermo = (Button) findViewById(R.id.btn_termos);
        checkTermo = (CheckBox) findViewById(R.id.term_accept);

        btnTermo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkTermo.isChecked()){


                    SharedPreferences prefs = getSharedPreferences("TermosServicos", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("termo", "aceito");
                    editor.apply();


                    Intent i = new Intent(TermosActivity.this,LoginActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(TermosActivity.this, "Voce deve aceitar o termo antes de continuar.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
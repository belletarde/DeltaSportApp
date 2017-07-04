package com.enterprise.barsemlona.barsemlona_20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loading);
        NetworkCall myCall = new NetworkCall();
        myCall.execute("http://deltaws.azurewebsites.net/g1/rest/categoria");

    }
    private class NetworkCall extends AsyncTask<String, Void, String> {
        String retErro;
        @Override
        protected String doInBackground(String... params) {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                final InputStream urlCon = urlConnection.getInputStream();
                BufferedReader  bufferedReader = new BufferedReader(new InputStreamReader(urlCon, "UTF-8"));
                StringBuilder resultado = new StringBuilder();
                String linha = bufferedReader.readLine();
                while (linha != null) {
                    resultado.append(linha);
                    linha = bufferedReader.readLine();
                }
                return resultado.toString();
            } catch (Exception e) {
                retErro = "Verifique sua conexão e tente novamente";
            }
            return retErro;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                    JSONArray jArray = new JSONArray(result);
                    CategoriaSingleton categoria = CategoriaSingleton.getInstance();
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_obj = jArray.getJSONObject(i);
                        String nome = json_obj.getString("nomeCategoria");
                        String id = json_obj.getString("idCategoria");
                        String desc = json_obj.getString("descCategoria");
                        categoria.setNome(nome);
                        categoria.setId(id);
                        categoria.setDescricao(desc);
                    }
                SharedPreferences prefs = getSharedPreferences("TermosServicos", MODE_PRIVATE);
                if(prefs.getString("termo","").equals("")) {
                    Intent j = new Intent(LoadingActivity.this, TermosActivity.class);
                    startActivity(j);
                }else {
                    Intent j = new Intent(LoadingActivity.this, LoginActivity.class);
                    startActivity(j);
                }
            } catch (Exception e) {
                Alerta al = new Alerta();
                al.getAlertBtn("Conexão",result,LoadingActivity.this);
            }
        }
    }
}


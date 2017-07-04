package com.enterprise.barsemlona.barsemlona_20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CadastroActivity extends AppCompatActivity {
    Button enviar;
    EditText email,senha,nome,sobrenome,cpf,telefoneRes,telefoneCom,cel;Spinner diaNascCadastro,mesNascCadastro,anoNascCadastro;
    ProgressDialog progressDialog ;
    String recebeNewsLetter,nomeCompleto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        progressDialog = new ProgressDialog(CadastroActivity.this);
        enviar = (Button) findViewById(R.id.enviarCadastro);
        email = (EditText) findViewById(R.id.emailCadastro);
        senha = (EditText) findViewById(R.id.senhaCadastro);
        nome = (EditText) findViewById(R.id.nomeCadastro);
        sobrenome = (EditText) findViewById(R.id.sobrenomeCadastro);
        cpf = (EditText) findViewById(R.id.cpfCadastro);
        telefoneRes = (EditText) findViewById(R.id.telResCadastro);
        telefoneCom = (EditText) findViewById(R.id.telComercialCadastro);
        cel = (EditText) findViewById(R.id.celCadastro);
        anoNascCadastro = (Spinner) findViewById(R.id.anoNascCadastro);
        mesNascCadastro = (Spinner) findViewById(R.id.mesNascCadastro);
        diaNascCadastro = (Spinner) findViewById(R.id.diaNascCadastro);
        recebeNewsLetter = "0";
        nomeCompleto = nome.getText().toString() +" "+ sobrenome.getText().toString();

        String Ano[] = { "1990", "1991", "1992", "1993",
                "1994", "1994" };
        String Mes[] = { "01", "02", "03", "04",
                "05", "06", "07", "08", "09", "10", "11", "12" };
        String Dia[] = { "1", "2", "3", "4",
                "5", "6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",
        "21","22","23","24","25","26","27","28","29","30"};

        ArrayAdapter<String> anoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Ano);
        anoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        anoNascCadastro.setAdapter(anoAdapter);

        ArrayAdapter<String> diaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Dia);
        diaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diaNascCadastro.setAdapter(diaAdapter);

        ArrayAdapter<String> mesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Mes);
        mesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mesNascCadastro.setAdapter(mesAdapter);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                if (nome.getText().toString().length() > 0 &&
                        email.getText().toString().length() > 0 &&
                        senha.getText().toString().length() > 0 &&
                        sobrenome.getText().toString().length() > 0) {
                    try {
                        json.put("nomeCompletoCliente", nome.getText().toString() + " " + sobrenome.getText().toString());
                        json.put("emailCliente", email.getText().toString());
                        json.put("senhaCliente", senha.getText().toString());
                        json.put("cpfCliente", cpf.getText().toString());
                        json.put("celularCliente", cel.getText().toString());
                        json.put("telComercialCliente", telefoneCom.getText().toString());
                        json.put("telResidencialCliente", telefoneRes.getText().toString());
                        json.put("dtNascCliente", diaNascCadastro.getSelectedItem().toString() + "-" + mesNascCadastro.getSelectedItem().toString() + "-" + anoNascCadastro.getSelectedItem().toString());
                        json.put("recebeNewsLetter", recebeNewsLetter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    NetworkCall myCall = new NetworkCall();
                    myCall.execute("http://deltaws.azurewebsites.net/g1/rest/cadastro-cliente", json.toString());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }else{
                    Alerta al = new Alerta();
                    al.getAlert("Campos obrigatórios","Os campos: \n - nome,\n - sobrenome, \n - data" +
                            " de nascimento, \n - email \n - senha, \nsão obrigatórios.",CadastroActivity.this);
                }
            }
        });
    }

    private class NetworkCall extends AsyncTask<String, Void, String> {
        String retErro;
        @Override
        protected String doInBackground(String... params) {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Content-Length", "" + params[1].getBytes().length);
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(params[1]);
                writer.flush();
                writer.close();
                os.close();
                urlConnection.getResponseCode();
                InputStream in = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                return bufferedReader.readLine();
            } catch (Exception e) {
                if(isOnline()) {
                    retErro = "Erro no servidor.";
                }else {
                    retErro = "Erro na conexão, verifique sua internet e tente novamente.";
                }
            }
            return retErro;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (result.equals("DEU CERTO")) {
                    progressDialog.hide();
                    Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(CadastroActivity.this, LoginActivity.class);
                    startActivity(i);
                }else {progressDialog.hide();
                    Toast.makeText(
                        CadastroActivity.this, result, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                progressDialog.hide();
                Toast.makeText(CadastroActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

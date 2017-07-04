package com.enterprise.barsemlona.barsemlona_20;



import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class LoginActivity extends AppCompatActivity {
    Button btnCadastro,btnLogin;
    EditText email;
    ProgressDialog progressDialog ;
    EditText senha;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            btnLogin = (Button) findViewById(R.id.btn_login);
            btnCadastro = (Button) findViewById(R.id.btn_slogin);
            email = (EditText) findViewById(R.id.id_login);
            senha = (EditText) findViewById(R.id.id_pass);
            progressDialog = new ProgressDialog(LoginActivity.this);
            btnCadastro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LoginActivity.this,CadastroActivity.class);
                    startActivity(i);
                }
            });
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("emailCliente", email.getText().toString());
                        json.put("senhaCliente", senha.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    NetworkCall myCall = new NetworkCall();
                    myCall.execute("http://deltaws.azurewebsites.net/g1/rest/usuarios", json.toString());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            });
        }
        private class NetworkCall extends AsyncTask<String, Void, String> {
            String retErro = "Login e/ou senha errados.";
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
                        retErro = "Login e ou senha errados.";
                    }else {
                        retErro = "Erro na conexão.";
                    }
                }
                return retErro;
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                try {
                    JSONObject json = new JSONObject(result);
                    UserSingleton user = UserSingleton.getInstance();
                    user.setId(json.getString("idCliente"));
                    Intent i = new Intent(LoginActivity.this,MenuActivity.class);
                    startActivity(i);
                    progress();
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                    progress();
                }
            }
        }
        private void progress(){
            progressDialog.hide();
        }
        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
    }

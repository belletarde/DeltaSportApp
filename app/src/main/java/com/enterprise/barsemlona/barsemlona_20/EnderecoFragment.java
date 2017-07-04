package com.enterprise.barsemlona.barsemlona_20;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class EnderecoFragment extends Fragment {
    Button endereco;
    EditText nomeEndereco,logradouroEndereco,numeroEndereco,cepEndereco,complementoEndereco,
            cidadeEndereco,paisEndereco,ufEndereco;


    public EnderecoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View enderecoFrag = inflater.inflate(R.layout.fragment_endereco, container, false);
        ((MenuActivity) getActivity()).hideFloatingActionButton();
        endereco = (Button) enderecoFrag.findViewById(R.id.buttonEndereco);
        nomeEndereco = (EditText) enderecoFrag.findViewById(R.id.nomeEndereco);
        logradouroEndereco = (EditText) enderecoFrag.findViewById(R.id.logradouroEndereco);
        numeroEndereco = (EditText) enderecoFrag.findViewById(R.id.numeroEndereco);
        cepEndereco = (EditText) enderecoFrag.findViewById(R.id.cepEndereco);
        complementoEndereco = (EditText) enderecoFrag.findViewById(R.id.complementoEndereco);
        cidadeEndereco = (EditText) enderecoFrag.findViewById(R.id.cidadeEndereco);
        paisEndereco = (EditText) enderecoFrag.findViewById(R.id.paisEndereco);
        ufEndereco = (EditText) enderecoFrag.findViewById(R.id.ufEndereco);

        UserSingleton user = UserSingleton.getInstance();
        final String idU = user.getId();

        endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    if ( ( logradouroEndereco.getText().toString().trim().equals("") &&
                            ( numeroEndereco.getText().toString().trim().equals("")) &&
                            ( cepEndereco.getText().toString().trim().equals("")     &&
                            ( cidadeEndereco.getText().toString().trim().equals("")  &&
                            ( paisEndereco.getText().toString().trim().equals(""))   &&
                            ( ufEndereco.getText().toString().trim().equals("")
                    ))))){
                        Toast.makeText(getActivity(), "Os campos de Endereços são obrigatórios", Toast.LENGTH_SHORT).show();
                    }else{
                        json.put("idCliente", idU);
                        json.put("nomeEndereco", nomeEndereco.getText().toString());
                        json.put("logradouroEndereco", logradouroEndereco.getText().toString());
                        json.put("numeroEndereco", numeroEndereco.getText().toString());
                        json.put("cepEndereco", cepEndereco.getText().toString());
                        json.put("complementoEndereco", complementoEndereco.getText().toString());
                        json.put("cidadeEndereco", cidadeEndereco.getText().toString());
                        json.put("paisEndereco", paisEndereco.getText().toString());
                        json.put("ufEndereco", ufEndereco.getText().toString());

                        NetworkCall myCall = new NetworkCall();
                        myCall.execute("http://deltaws.azurewebsites.net/g1/rest/cadastro-endereco", json.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return enderecoFrag;
    }
    private class NetworkCall extends AsyncTask<String, Void, String> {
       String retErro;
        @Override
        protected String doInBackground(String... params) {
            try {
                // Cria o objeto de conexão
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

                // Executa a requisição pegando os dados
                InputStream in = urlConnection.getInputStream();

                // Cria um leitor para ler a resposta

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder resultado = new StringBuilder();

                // Retorna a string final contendo a resposta retornada
                return bufferedReader.readLine();

            } catch (Exception e) {
                if(isOnline()) {
                    retErro = "Erro no servidor.";
                }else {
                    retErro = "Erro na conexão.";
                }
            }
            // Caso tenha dado algum erro, retorna null
            return retErro;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                UserSingleton user = UserSingleton.getInstance();
                user.setEnderecoId((Integer.parseInt(result)));
                Toast.makeText(getActivity(), "Endereco cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                Fragment fragmentDetalhe = new FinalizarCompraFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragmentDetalhe);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

            }
        }

    }  public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(getActivity().CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

}

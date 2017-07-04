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
public class ConfirmacaoFragment extends Fragment {


    public ConfirmacaoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View confirma = inflater.inflate(R.layout.fragment_confirmacao, container, false);

        Button finalizarCompra = (Button) confirma.findViewById(R.id.finalizarCompra);
        finalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    UserSingleton user = UserSingleton.getInstance();
                    String idU = user.getId();
                    json.put("idCliente", idU);

                    String idP = user.getPagamentoId();
                    json.put("idTipoPagto", idP);

                    int idE = user.getEnderecoId();
                    json.put("idEndereco", idE);

                    json.put("idStatus", "2");
                    json.put("idAplicacao", "2");




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NetworkCall myCall = new NetworkCall();
                myCall.execute("http://deltaws.azurewebsites.net/g1/rest/finalizar-compra", json.toString());

            }
        });
        return confirma;
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
                    if (result.equals("Pedido realizado com sucesso!")) {
                        Toast.makeText(getActivity(), "Pedido realizado com sucesso!", Toast.LENGTH_SHORT).show();

                        CarrinhoSingleton user = CarrinhoSingleton.getInstance();
                        user.setDestroiTudo();

                        Fragment fragmentDetalhe = new CarrinhoFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragmentDetalhe);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }else{
                        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                    }

            } catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

            }
        }
    }public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(getActivity().CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

}

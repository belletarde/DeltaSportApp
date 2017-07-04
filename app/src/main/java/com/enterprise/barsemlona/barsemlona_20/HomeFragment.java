package com.enterprise.barsemlona.barsemlona_20;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;

public class HomeFragment extends Fragment {
    private ViewGroup mensagens;

    ImageView img;ProgressDialog progressDialog ;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeFragment = inflater.inflate(R.layout.fragment_home, container, false);
        ((MenuActivity) getActivity()).showFloatingActionButton();
        mensagens = (ViewGroup) homeFragment.findViewById(R.id.container);
        progressDialog = new ProgressDialog(getActivity());

        final Bundle bundle = this.getArguments();
        final String idC = bundle.getString("idCategoria");

        assert idC != null;
        if (idC.equals("noId")) {


            NetworkCall myCall = new NetworkCall();
            myCall.execute("http://deltaws.azurewebsites.net/g1/rest/produto");
        }else{
            NetworkCall myCall = new NetworkCall();
            myCall.execute("http://deltaws.azurewebsites.net/g1/rest/ProdutosCategoria/"+idC);
        }

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        return  homeFragment;
    }
    @SuppressLint("SetTextI18n")
    private void addItem(final String nomeProduto, final String desc , final String precoProduto, final String categoria, final String desconto, final String id) {
        CardView cardView = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.card_file_product, mensagens, false);
        TextView nomeP = (TextView) cardView.findViewById(R.id.titulo);
        TextView preco = (TextView) cardView.findViewById(R.id.precoProdList);
        TextView categoria_ = (TextView) cardView.findViewById(R.id.categoria);
        //TextView precoDescontado = (TextView) cardView.findViewById(R.id.descontoProdList);
        img = (ImageView) cardView.findViewById(R.id.img_product_detail);
        //String val = (new BigDecimal(precoProduto).subtract( new BigDecimal(desconto))).toString();
        //precoDescontado.setText(val);
        TextView precoPor = (TextView) cardView.findViewById(R.id.precoPor);


        nomeP.setText(nomeProduto);
        precoPor.setText("De:\nR$"+ (NumberFormat.getCurrencyInstance().format(new BigDecimal(precoProduto))));
        preco.setText("Por:\nR$"+ (NumberFormat.getCurrencyInstance().format((new BigDecimal(precoProduto).subtract(new BigDecimal(desconto))))));
        CategoriaSingleton categoriaSingleton = CategoriaSingleton.getInstance();
        for (int i = 0; i < categoriaSingleton.getId().size(); i++) {
            if(categoria.equals(categoriaSingleton.getId().get(i))) {
                categoria_.setText("Categoria: " + categoriaSingleton.getNome().get(i));
                break;
            }
        }
        mensagens.addView(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentDetalhe = new DetalheFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragmentDetalhe,"DetalheProduto");
                Bundle bundle = new Bundle();
                bundle.putString("idProduto", id);
                bundle.putString("nomeProduto", nomeProduto);
                bundle.putString("precoProduto",precoProduto );
                bundle.putString("descProduto", desc);
                bundle.putString("categoriaProduto", categoria);
                bundle.putString("descontoProduto",desconto);
                fragmentDetalhe.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }) ;
    }

   /* public class ImgCall extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                // Cria o objeto de conexão
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(params[0]).openConnection();

                // Executa a requisição pegando os dados
                InputStream in = urlConnection.getInputStream();

                // Cria um leitor para ler a resposta
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

                StringBuilder resultado = new StringBuilder();
                String linha = bufferedReader.readLine();

                // Lê linha a linha a resposta e armazena no StringBuilder
                while (linha != null) {
                    resultado.append(linha);
                    linha = bufferedReader.readLine();
                }

                // Transforma o StringBuilder em String, que contém a resposta final


                String respostaCompleta = resultado.toString();

                // Retorna a string final contendo a resposta retornada
                return respostaCompleta;

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Caso tenha dado algum erro, retorna null
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject json = new JSONObject(result);

                byte[] decodedString = Base64.decode(json.getString("imagem"), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                img.setImageBitmap(decodedByte);






            } catch (Exception e) {
                e.printStackTrace();
                img.setImageResource(R.mipmap.ic_launcher);

            }
        }
    }

*/
    private class NetworkCall extends AsyncTask<String, Void, String> {
        String retErro;
        @Override
        protected String doInBackground(String... params) {
            try {

                HttpURLConnection urlConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                // Executa a requisição pegando os dados
                InputStream in = urlConnection.getInputStream();
                // Cria um leitor para ler a resposta
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder resultado = new StringBuilder();
                String linha = bufferedReader.readLine();
                // Lê linha a linha a resposta e armazena no StringBuilder
                while (linha != null) {
                    resultado.append(linha);
                    linha = bufferedReader.readLine();
                }
                // Transforma o StringBuilder em String, que contém a resposta final
                // Retorna a string final contendo a resposta retornada
                return resultado.toString();
            } catch (Exception e) {

                if(isOnline()) {
                    retErro = "Erro no servidor";
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
                JSONArray jArray = new JSONArray (result);

                for (int i = 0; i < jArray.length();i++) {
                    JSONObject json_obj = jArray.getJSONObject(i);

                    //String qtdMinEstoque = json_obj.getString("qtdMinEstoque");

                   if (json_obj.getString("ativoProduto").equals("true")){

                        addItem(json_obj.getString("nomeProduto"),json_obj.getString("descProduto"),json_obj.getString("precProduto"),json_obj.getString("idCategoria"),json_obj.getString("descontoPromocao"),json_obj.getString("idProduto"));

                    }


                }

                progressDialog.hide();
            } catch (Exception e) {
                progressDialog.hide();

                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
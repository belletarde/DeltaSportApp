package com.enterprise.barsemlona.barsemlona_20;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;


public class DetalheFragment extends Fragment {

    TextView nome,preco,descricao,cat,desconto,id,qtd_detalhe;
    Button carrinho;
    ImageView img;

    public DetalheFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Detalhe = inflater.inflate(R.layout.fragment_detalhe, container, false);

        ((MenuActivity) getActivity()).hideFloatingActionButton();

        nome = (TextView) Detalhe.findViewById(R.id.nm_detalhe);
        preco = (TextView) Detalhe.findViewById(R.id.price_detalhe);
        descricao = (TextView) Detalhe.findViewById(R.id.txt_detalhe);
        desconto = (TextView) Detalhe.findViewById(R.id.price_por_detalhe);
        cat = (TextView) Detalhe.findViewById(R.id.category_detalhe);
        img = (ImageView) Detalhe.findViewById(R.id.img_detalhe);
        qtd_detalhe = (TextView) Detalhe.findViewById(R.id.qtd_detalhe);

        final Bundle bundle = this.getArguments();
        CategoriaSingleton categoriaSingleton = CategoriaSingleton.getInstance();

        NetworkCall myCall = new NetworkCall();
        String idImagem = bundle.getString("idProduto");
        myCall.execute("http://deltaws.azurewebsites.net/g1/rest/imagem/"+idImagem+"/145/145");


        final String idC = bundle.getString("categoriaProduto");

        for (int i = 0; i < categoriaSingleton.getId().size(); i++) {
            assert idC != null;
            if(idC.equals(categoriaSingleton.getId().get(i))) {
                cat.setText("Categoria: " + categoriaSingleton.getNome().get(i));
                break;
            }
        }
        nome.setText(bundle.getString("nomeProduto"));
        final String precoCarrinho = bundle.getString("precoProduto");
        final String descontoCarrinho = bundle.getString("descontoProduto");
        final String idCarrinho = bundle.getString("idProduto");
        preco.setText("De:\nR$"+ (NumberFormat.getCurrencyInstance().format(new BigDecimal(bundle.getString("precoProduto")))));

        descricao.setText(bundle.getString("descProduto"));
        BigDecimal val;
        val = new BigDecimal (bundle.getString("precoProduto")).subtract(new BigDecimal (bundle.getString("descontoProduto")));

        desconto.setText("Por:\nR$"+ (NumberFormat.getCurrencyInstance().format(new BigDecimal(val.toString()))));


        carrinho = (Button) Detalhe.findViewById(R.id.btn_comprar);
        carrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarrinhoSingleton carrinho = CarrinhoSingleton.getInstance();
                String qtdAdd = qtd_detalhe.getText().toString();
                boolean adicionar = true;
                if(carrinho.getId().size() == 0){
                    carrinho.setNome(nome.getText().toString());
                    carrinho.setPreco(precoCarrinho);
                    carrinho.setDescricao(descricao.getText().toString());
                    carrinho.setCat(idC);
                    carrinho.setDesconto(descontoCarrinho);
                    carrinho.setId(idCarrinho);
                    carrinho.setQtd(qtdAdd);
                }else{
                for(int i = 0;i < carrinho.getId().size();i++) {
                    assert idCarrinho != null;
                    if (idCarrinho.equals(carrinho.getId().get(i))) {
                        qtdAdd = (new BigDecimal(carrinho.getQtd().get(i)).add(new BigDecimal(qtd_detalhe.getText().toString()))).toString();
                        carrinho.setQtd(qtdAdd,i);
                        adicionar = false;

                    }
                }
                    if (adicionar) {
                        carrinho.setNome(nome.getText().toString());
                        carrinho.setPreco(precoCarrinho);
                        carrinho.setDescricao(descricao.getText().toString());
                        carrinho.setCat(idC);
                        carrinho.setDesconto(descontoCarrinho);
                        carrinho.setId(idCarrinho);
                        carrinho.setQtd(qtdAdd);
                    }




                }



                Fragment fragmentDetalhe = new CarrinhoFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragmentDetalhe);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });


        Button btnMaisProd = (Button) Detalhe.findViewById(R.id.btnMaisProd);
        Button btnMenosProd = (Button) Detalhe.findViewById(R.id.btnMenosProd);

        btnMaisProd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int j = Integer.parseInt(qtd_detalhe.getText().toString());
                j++;
                qtd_detalhe.setText(""+j);
            }
        });


        btnMenosProd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int j =Integer.parseInt(qtd_detalhe.getText().toString());
                if(j>1)
                {
                    j--;
                }
                qtd_detalhe.setText(""+j);

            }
        });
        return Detalhe;
    }

    private class NetworkCall extends AsyncTask<String, Void, String> {
        String retErro;
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


                // Retorna a string final contendo a resposta retornada
                return resultado.toString();

            } catch (Exception e) {
                if(isOnline()) {
                    retErro = "Produto sem foto.";
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

                JSONObject json = new JSONObject(result);

                byte[] decodedString = Base64.decode(json.getString("imagem"), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                img.setImageBitmap(decodedByte);
            } catch (Exception e) {
                e.printStackTrace();
                img.setImageResource(R.drawable.ic_menu_gallery);


                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(getActivity().CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

}

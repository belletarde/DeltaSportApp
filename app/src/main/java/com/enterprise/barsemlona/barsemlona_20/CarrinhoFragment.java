package com.enterprise.barsemlona.barsemlona_20;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;


public class CarrinhoFragment extends Fragment {

    private ViewGroup carrinhoListagem;

    public CarrinhoFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View Carrinho = inflater.inflate(R.layout.fragment_carrinho, container, false);
        ((MenuActivity) getActivity()).showFloatingActionButton();
        TextView text = (TextView) Carrinho.findViewById(R.id.totalComprado);
        carrinhoListagem = (ViewGroup) Carrinho.findViewById(R.id.carrinhoListagem);

        final CarrinhoSingleton listagemCarrinho = CarrinhoSingleton.getInstance();
        Button voltarComprar = (Button) Carrinho.findViewById(R.id.voltarComprar);
        voltarComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
            }
        });
        Button finalizarCompra;
        finalizarCompra = (Button) Carrinho.findViewById(R.id.finalizarCompra);
        finalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> produtos = listagemCarrinho.getId();

                if (produtos.size() > 0) {
                    Fragment fragmentDetalhe = new EnderecoFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragmentDetalhe);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else {
                    Toast.makeText(getActivity(), "Adicione produtos no carrinho", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String totalCarrinho =listagemCarrinho.getTotal();
        text.setText("Total de R$: " + (NumberFormat.getCurrencyInstance().format(new BigDecimal(totalCarrinho))));



        listagemCarrinho.getPreco();
        listagemCarrinho.getDesconto();


        for (int j = 0; j <listagemCarrinho.getNome().size(); j++) {
            String nome = listagemCarrinho.getNome().get(j);
            String preco = listagemCarrinho.getPreco().get(j);
            String descontado = listagemCarrinho.getDesconto().get(j);
            String precoCerto = (new BigDecimal(preco).subtract(new BigDecimal(descontado))).toString();
            String qtd = listagemCarrinho.getQtd().get(j);
            String id = listagemCarrinho.getId().get(j);
            String descricao = listagemCarrinho.getDescricao().get(j);
            String desconto = listagemCarrinho.getDesconto().get(j);
            String idCategoria = listagemCarrinho.getCat().get(j);
            addItem(nome,precoCerto,qtd,id,j,descricao,desconto,idCategoria);
        }

        return Carrinho;
    }

    private void addItem(final String nomeProduto,final String precoProduto, final String qtdProduto, final  String id,final int i,final String descricao,final String desconto,final String idCategoria) {
        CardView carrinhoCardView = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.card_carrinho, carrinhoListagem, false);
        TextView nomeProd = (TextView) carrinhoCardView.findViewById(R.id.nm_product_checkout);
        TextView price_product_checkout = (TextView) carrinhoCardView.findViewById(R.id.price_product_checkout);
        final TextView qtd_product_checkout = (TextView) carrinhoCardView.findViewById(R.id.qtd_product_checkout);
        Button mais,menos,excluir;
        menos = (Button) carrinhoCardView.findViewById(R.id.remove_product_checkout);
        mais = (Button) carrinhoCardView.findViewById(R.id.add_product_checkout);
        excluir = (Button) carrinhoCardView.findViewById(R.id.excluirProduto);
        final CarrinhoSingleton c = CarrinhoSingleton.getInstance();

        Fragment fragmentDetalhe = new CarrinhoFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragmentDetalhe);
        fragmentTransaction.addToBackStack(null);


        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(qtd_product_checkout.getText().toString());
                if (count > 1) {
                    count--;
                    c.setQtd(String.valueOf(count),i);
                }else { c.setDestroi(i);}

                fragmentTransaction.commit();


            }
        });

        mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(qtd_product_checkout.getText().toString());
                count++;

                c.setQtd(String.valueOf(count),i);
                fragmentTransaction.commit();



            }
        });

        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c.setDestroi(i);
                fragmentTransaction.commit();

            }
        });

        nomeProd.setText(nomeProduto);
        qtd_product_checkout.setText(qtdProduto);
        price_product_checkout.setText((NumberFormat.getCurrencyInstance().format(new BigDecimal(precoProduto))));


        carrinhoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentDetalhe = new DetalheFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragmentDetalhe);
                Bundle bundle = new Bundle();
                bundle.putString("idProduto", id);
                bundle.putString("nomeProduto", nomeProduto);
                bundle.putString("precoProduto",precoProduto );
                bundle.putString("descProduto", descricao);
                bundle.putString("categoriaProduto", idCategoria);
                bundle.putString("descontoProduto",desconto);
                fragmentDetalhe.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }) ;

        carrinhoListagem.addView(carrinhoCardView);

    }


}






package com.enterprise.barsemlona.barsemlona_20;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriaFragment extends Fragment {
    ViewGroup categoriaListagem;

    public CategoriaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View categoria = inflater.inflate(R.layout.fragment_categoria, container, false);
        categoriaListagem = (ViewGroup) categoria.findViewById(R.id.categoriaListagem);

        CategoriaSingleton ctS = CategoriaSingleton.getInstance();
        for (int j = 0; j <ctS.getId().size(); j++) {

            addItem(ctS.getId().get(j),ctS.getNome().get(j));
        }
        return  categoria;
    }
    private void addItem(final String Id,final String nome) {
        CardView categoriaCardView = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.card_cartegoria, categoriaListagem, false);
        TextView nomeCat = (TextView) categoriaCardView.findViewById(R.id.nomeCategoria);
        nomeCat.setText(nome);

        categoriaCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentDetalhe = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragmentDetalhe);
                Bundle bundle = new Bundle();
                bundle.putString("idCategoria", Id);
                fragmentDetalhe.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }) ;

        categoriaListagem.addView(categoriaCardView);

    }
}

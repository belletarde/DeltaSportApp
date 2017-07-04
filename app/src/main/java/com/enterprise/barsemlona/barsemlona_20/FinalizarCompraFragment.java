package com.enterprise.barsemlona.barsemlona_20;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;



public class FinalizarCompraFragment extends Fragment {
        RadioButton id_credito,id_pagseguro,id_boleto,id_paypal;

    public FinalizarCompraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View finaliza = inflater.inflate(R.layout.fragment_finalizar_compra, container, false);
        Button btn_finalizar_compra = (Button) finaliza.findViewById(R.id.btn_finalizar_compra);
        id_boleto = (RadioButton) finaliza.findViewById(R.id.id_boleto);
        id_credito = (RadioButton) finaliza.findViewById(R.id.id_credito);
        id_pagseguro = (RadioButton) finaliza.findViewById(R.id.id_pagseguro);
        id_paypal = (RadioButton) finaliza.findViewById(R.id.id_paypal);
        btn_finalizar_compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSingleton user = UserSingleton.getInstance();
                Fragment fragmentDetalhe = new ConfirmacaoFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragmentDetalhe);
                fragmentTransaction.addToBackStack(null);

                //if (if Total == 0) {
                    if (id_boleto.isChecked()) {
                        user.setPagamentoId("2");
                        fragmentTransaction.commit();

                    } else if (id_credito.isChecked()) {
                        user.setPagamentoId("1");
                        fragmentTransaction.commit();

                    } else if (id_pagseguro.isChecked()) {
                        user.setPagamentoId("3");
                        fragmentTransaction.commit();
                    }else if (id_paypal.isChecked()) {
                        user.setPagamentoId("4");
                        fragmentTransaction.commit();
                    } else {
                        Toast.makeText(getActivity(), "Selecione uma das opções de pagamento", Toast.LENGTH_SHORT).show();
                    }
                //}else{Toast.makeText(getActivity(), "Adicione produtos ao carrinho", Toast.LENGTH_SHORT).show();}


            }
        });


    return finaliza;

    }
}

package com.enterprise.barsemlona.barsemlona_20;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;



class Alerta {

    void getAlert(String titulo, String mensagem, Activity act){
        AlertDialog alertDialog = new AlertDialog.Builder(act).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(mensagem);
        alertDialog.show();
    }

    void getAlertBtn(String titulo, String mensagem, final Activity act){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(act);
        builder1.setTitle(titulo);
        builder1.setMessage(mensagem);
        builder1.setCancelable(true);
        builder1.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        act.finish();
                    }
                });

        AlertDialog alert1 = builder1.create();
        alert1.show();
    }


}

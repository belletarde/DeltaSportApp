package com.enterprise.barsemlona.barsemlona_20;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
public class QrCodeActivity extends AppCompatActivity {

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(QrCodeActivity.this);   // Programmatically initialize the scanner view
        setContentView(scannerView);

        if ( (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        scannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                // show the scanner result into dialog box.

                Intent return_intent = new Intent();
                return_intent.putExtra("qrCode", result.getText());
                setResult(RESULT_OK, return_intent);
                finish();
            }

        });
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

}

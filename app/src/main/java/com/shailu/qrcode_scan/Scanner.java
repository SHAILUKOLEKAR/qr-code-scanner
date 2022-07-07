package com.shailu.qrcode_scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

public class Scanner extends AppCompatActivity
{
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;
    TextView resultData,resultData1t;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        codeScannerView=(CodeScannerView)findViewById(R.id.codeScannerView);
        codeScanner= new CodeScanner(this,codeScannerView);
        //codeScanner.setFormats(CodeScanner.TWO_DIMENSIONAL_FORMATS);

/*
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK ;// or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS ;// list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE; // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE; // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true; // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false; // Whether to enable flash or not
 */

        resultData=findViewById(R.id.textView_mo_no);
        resultData1t=findViewById(R.id.textView_tbn);

        codeScanner.setDecodeCallback(new DecodeCallback()
        {
            @Override
            public void onDecoded(@NonNull final Result result)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                      //  resultData.setText(result.getText());
                        try
                        {
                            //converting the data to json
                            JSONObject obj = new JSONObject(result.getText());
                            //setting values to textviews
                            if (obj.has("Mo")&&obj.has("Tno"))
                            {
                                resultData.setText(obj.getString("Mo"));
                                resultData1t.setText(obj.getString("Tno"));
                                Intent intent=new Intent(Scanner.this,MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            //if control comes here
                            //that means the encoded format not matches
                            //in this case you can display whatever data is available on the qrcode
                            //to a toast
                            Toast.makeText(Scanner.this, result.getText(), Toast.LENGTH_SHORT).show();
                           // Toast.makeText(this, result.getText(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
       getPermission();
    }

    private void getPermission()
    {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response)
            {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response)
            {
                Toast.makeText(Scanner.this, "Permission Require...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    token.continuePermissionRequest();
            }
        }).check();
    }
}

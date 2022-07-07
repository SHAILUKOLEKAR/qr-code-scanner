package com.shailu.qrcode_scan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity
{
    private Button generate_btn, scan_btn;
    private EditText input1,input2t;
    private ImageView qr_view;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        generate_btn=(Button)findViewById(R.id.generate_qr_btn);
        scan_btn=(Button)findViewById(R.id.scan_qr_btn);
        input1=(EditText)findViewById(R.id.input_txt);
        input2t=(EditText)findViewById(R.id.input_txt2);
        qr_view=(ImageView)findViewById(R.id.qr_view);
       // final Activity activity=this;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(getApplicationContext(),Scanner.class));
                /*
                IntentIntegrator intentIntegrator=new IntentIntegrator(activity);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setPrompt("scan");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setBarcodeImageEnabled(false);
                intentIntegrator.initiateScan();
                */
            }
        });

        generate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mo=input1.getText().toString();
                final String tn=input2t.getText().toString();
                if (mo.isEmpty())
                {
                    input1.setError("Enter Text First");
                    input1.requestFocus();
                }
                if (tn.isEmpty())
                {
                    input2t.setError("Enter Text First");
                    input2t.requestFocus();
                }
                int n=Integer.valueOf(tn);
                for (int i=1;i<=n;i++)
                {
                String Text ="{'Mo':'"+mo+"','Tno':'"+i+"'}";
                MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
                BitMatrix bitMatrix= null;
                try {
                    bitMatrix = multiFormatWriter.encode(Text, BarcodeFormat.QR_CODE,500,500);
                    BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                    Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                    //qr_view.setImageBitmap(bitmap);

                    //  BitmapDrawable image=(BitmapDrawable) qr_view.getDrawable();
                    File path= Environment.getExternalStorageDirectory();
                    File dir= new File(path.getAbsolutePath()+"/demo/");
                    dir.mkdir();
                    File file=new File(dir,mo+"@"+i+".jpg");
                    OutputStream out=null;
                    try
                    {
                        out=new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                        out.flush();
                        out.close();

                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                } catch (WriterException e)
                {
                    e.printStackTrace();
                }
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if (result.getContents()==null)
            {
                Toast.makeText(this, "you cancelled...", Toast.LENGTH_SHORT).show();
            }
            else
            {

                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

}

/*

public class QRCodeGenerator {
    private static final String QR_CODE_IMAGE_PATH = "./MyQRCode.png";

    private static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void main(String[] args) {
        try {
            generateQRCodeImage("This is my first QR Code", 350, 350, QR_CODE_IMAGE_PATH);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }
    }
}
 */


/*
public class QRCodeReader {

    private static String decodeQRCode(File qrCodeimage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            File file = new File("MyQRCode.png");
            String decodedText = decodeQRCode(file);
            if(decodedText == null) {
                System.out.println("No QR Code found in the image");
            } else {
                System.out.println("Decoded text = " + decodedText);
            }
        } catch (IOException e) {
            System.out.println("Could not decode QR Code, IOException :: " + e.getMessage());
        }
    }
}


String str = "1234567890@12";
		String[] arrOfStr = str.split("@", 2);


			String str = "1234567890@12";
		String[] arrOfStr = str.split("@", 2);

		    String a=arrOfStr[0];
		    String b=arrOfStr[1];
			System.out.println(a);
			System.out.println(b);


 */
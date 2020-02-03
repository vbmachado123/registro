package victor.machado.com.br.registro;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImagemActivity extends AppCompatActivity {

    private static final int CAMERA = 1;
    private ImageView imagemDocumento;
    private Button assinarDoc;
    private Bitmap bitmap;

    private String caminhoDaImagem;
    private Uri uri;

    OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem);

        Toast.makeText(this, "Fotografe o RG/RNE ou CNH \n  do cliente para gerar o PDF", Toast.LENGTH_LONG).show();

        imagemDocumento = (ImageView) findViewById(R.id.imgDocumento);
        assinarDoc = (Button) findViewById(R.id.botaoAssinar);

        assinarDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    abrirCamera();
            }
        });
    }

    public void abrirCamera() {
        Toast.makeText(this, "Método abrir câmera chamado!", Toast.LENGTH_SHORT).show();
    }

}

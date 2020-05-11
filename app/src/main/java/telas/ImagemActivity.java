package telas;

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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import victor.machado.com.br.registro.R;

public class ImagemActivity extends AppCompatActivity {

    private static final int CAMERA = 1;
    private ImageView imagemDocumento;
    private Bitmap imagemDoc;
    private String currentDocPath = null;
    private File file;
    private Uri imageUri;
    private Button assinarDoc;
    private static int verificaDoc = 0;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Imagem Documento");
        setSupportActionBar(toolbar);

        imagemDocumento = (ImageView) findViewById(R.id.imgDocumento);
        assinarDoc = (Button) findViewById(R.id.botaoAssinar);

        tirarFoto(currentDocPath, CAMERA);
    }

    public void tirarFoto(String path, int valor ){
        Toast.makeText(this, "Fotografe o documento para prosseguir..!", Toast.LENGTH_SHORT).show();
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(camera.resolveActivity(getPackageManager()) != null){
            file = null;

            try{
                file = getImageFile(path);
            } catch (IOException e){
                e.printStackTrace();
            }

            if(file != null) {
                imageUri = FileProvider.getUriForFile(
                        this, "victor.machado.com.br.registro", file);
                camera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(camera, valor);
            }
        }
    }

    private File getImageFile(String path) throws IOException{
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String nomePasta = "/Registro/Imagens";
        String arquivo = path + System.currentTimeMillis() + ".jpg";

        File mydir = new File(root, nomePasta);
        File fileFoto = new File(mydir, arquivo);

        if(mydir.exists()) mydir.mkdirs();

        return fileFoto;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            currentDocPath = String.valueOf(file);
            imagemDoc = BitmapFactory.decodeFile(currentDocPath);
            imagemDocumento.setImageBitmap(imagemDoc);
            verificaDoc = 1;
        }
    }
}
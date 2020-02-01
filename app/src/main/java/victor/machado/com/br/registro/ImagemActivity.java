package victor.machado.com.br.registro;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImagemActivity extends AppCompatActivity {

    private ImageView imagemDocumento;
    private Uri uriImagem = null;
    private String caminhoDaImagem;
    private static final int CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem);

        File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imagem = new File(diretorio.getPath() + "/" + System.currentTimeMillis() + ".jpg");
        uriImagem  = Uri.fromFile(imagem);


        imagemDocumento = (ImageView) findViewById(R.id.imgDocumento);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == CAMERA && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imagem = (Bitmap) extras.get("data");

           String enderecoFinal = extras.getString("endereco");
           String nomeFinal = extras.getString("nome");

            Toast.makeText(this, enderecoFinal, Toast.LENGTH_SHORT).show();

            imagemDocumento.setImageBitmap(imagem);

                Intent novaIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriImagem);
                sendBroadcast(novaIntent);

                caminhoDaImagem = uriImagem.getPath();

                Toast.makeText(this, caminhoDaImagem, Toast.LENGTH_LONG).show();

        } else{

            Toast.makeText(this, "FALHA! A captura da imagem falhou!", Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}

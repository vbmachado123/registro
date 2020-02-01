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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImagemActivity extends AppCompatActivity {

    private ImageView imagemDocumento;
    private Uri uriImagem = null;
    private String caminhoDaImagem;
    private static final int CAMERA = 1;
    private Button assinarDoc;

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

        assinarDoc = (Button) findViewById(R.id.botaoAssinar);
        assinarDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImagemActivity.this, AssinaturaActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == CAMERA && resultCode == RESULT_OK) {

            final Intent intent = getIntent();
            Bundle extra= intent.getExtras(); //Recupera as informações

            Bundle extras = data.getExtras(); //Recupera a imagem
            Bitmap imagem = (Bitmap) extras.get("data");

            caminhoDaImagem = uriImagem.getPath();

            String endereco = extra.getString("endereco");
            String nome = extra.getString("nome");
            String Rg = extra.getString("rg");
            String Cpf = extra.getString("cpf");
            String Celular = extra.getString("celular");
            String Responsabilidade = extra.getString("responsabilidade");

            imagemDocumento.setImageBitmap(imagem);

                Intent novaIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uriImagem);
                sendBroadcast(novaIntent);

            //Setando as informações para a última ACTIVITY


               //Toast.makeText(this, caminhoDaImagem, Toast.LENGTH_LONG).show();

        } else{

            Toast.makeText(this, "FALHA! A captura da imagem falhou!", Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}

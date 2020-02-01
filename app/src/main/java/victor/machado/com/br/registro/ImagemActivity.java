package victor.machado.com.br.registro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ImagemActivity extends AppCompatActivity {

    private ImageView imagemDocumento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem);

        imagemDocumento = (ImageView) findViewById(R.id.imgDocumento);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imagem = (Bitmap) extras.get("data");

            imagemDocumento.setImageBitmap(imagem);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

package telas;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import model.Assinatura;
import victor.machado.com.br.registro.R;

public class AssinaturaActivity extends AppCompatActivity {

    private Assinatura assinatura;
    private Button salvar;
    private Toolbar toolbar;
    private String caminho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assinatura);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Assinatura Cliente");
        setSupportActionBar(toolbar);

        final RelativeLayout parent = (RelativeLayout) findViewById(R.id.rlAssinatura);
        assinatura = new Assinatura(this);
        parent.addView(assinatura);

        salvar = (Button) findViewById(R.id.salvar);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.setDrawingCacheEnabled(true);
                Bitmap b = parent.getDrawingCache();

                FileOutputStream out = null;
                File mydir = new File(Environment.getExternalStorageDirectory() + "/CheckGuincho");
                if(mydir.exists()) mydir.mkdir();
                caminho = mydir + "/Imagens/" + "Assinatura" + ".jpg";

                try{
                    out = new FileOutputStream(caminho);
                    b.compress(Bitmap.CompressFormat.JPEG, 30, out);
                    out.flush();
                    out.close();
                    Toast.makeText(AssinaturaActivity.this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}

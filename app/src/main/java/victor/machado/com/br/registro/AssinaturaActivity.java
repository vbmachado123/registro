package victor.machado.com.br.registro;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

import model.Assinatura;

public class AssinaturaActivity extends AppCompatActivity {

    private Button gerarDoc;
    private Assinatura assinatura;
    private Button salvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assinatura);

        final RelativeLayout parent = (RelativeLayout) findViewById(R.id.signImageParent);
        assinatura = new Assinatura(this);
        parent.addView(assinatura);

        salvar = (Button) findViewById(R.id.salvar);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                parent.setDrawingCacheEnabled(true);
                Bitmap b = parent.getDrawingCache();

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + "Assinatura.png");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                b.compress(Bitmap.CompressFormat.PNG, 95, fos);
            }
        });

    }

}

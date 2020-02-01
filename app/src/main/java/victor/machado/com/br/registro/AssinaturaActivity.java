package victor.machado.com.br.registro;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.UUID;

public class AssinaturaActivity extends AppCompatActivity {

    private PaintView paintView;
    private Button gerarDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assinatura);

        paintView = new PaintView(this);
        setContentView(paintView);

    }
}

package telas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import victor.machado.com.br.registro.R;

public class SplashScreen extends AppCompatActivity {

    private GifImageView gifRegistro;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Criando e verificando pasta do dispositivo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
           checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
           != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);

        }

        gifRegistro = (GifImageView) findViewById(R.id.gifRegistro);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility( progressBar.VISIBLE );

        //Setando a resolução do gif
        try{
            InputStream inputStream = getAssets().open("registro.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifRegistro.setBytes(bytes);
            gifRegistro.startAnimation();
        } catch (IOException e){
            //Faz alguma coisa

        }

        //Aguardando um tempo para redirecionar
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                SplashScreen.this.finish();
            }
        }, 3000);
    }

    //Código necessário para a permissão à memoria interna
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
       switch (requestCode){
           case 1000:
               if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   Toast.makeText(this, "Permissão Concedida!", Toast.LENGTH_SHORT).show();
               } else{
                   Toast.makeText(this, "Permissão Negada...!", Toast.LENGTH_SHORT).show();
                    finish();
               }
       }
    }
}

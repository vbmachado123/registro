package victor.machado.com.br.registro;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOExceptionWithCause;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class SplashScreen extends AppCompatActivity {

    private GifImageView gifRegistro;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

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


}

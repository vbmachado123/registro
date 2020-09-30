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
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import helper.FormularioDAO;
import helper.ImagemDAO;
import helper.TermoConfigDAO;
import helper.TermoDAO;
import model.Formulario;
import model.Imagem;
import model.Termo;
import model.TermoConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import util.Preferencias;
import victor.machado.com.br.registro.R;

public class ImagemActivity extends AppCompatActivity {

    private static final int CAMERA = 1, DOC2 = 2, DOC3 = 3, DOC4 = 4;

    private ImageView imagemDocumento, imagemDocumento1, imagemDocumento2, imagemDocumento3;
    private Bitmap imagemDoc, imagemDoc1, imagemDoc2, imagemDoc3;
    private String currentDocPath = null, currentDocPath1 = null, currentDocPath2 = null, currentDocPath3 = null;
    private String doc0 = "doc0", doc1 = "doc1", doc2 = "doc2", doc3 = "doc3";
    private File file, file1, file2, file3;

    private Uri imageUri;
    private Button assinarDoc;
    private static int verificaDoc = 0;
    private Toolbar toolbar;
    private FormularioDAO dao;
    private Formulario f = new Formulario();
    private Termo termo;
    private TermoDAO termoDAO;
    private TermoConfig config;
    private TermoConfigDAO configDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Imagem Documento");
        setSupportActionBar(toolbar);
        dao = new FormularioDAO(this);

        config = new TermoConfig();
        configDAO = new TermoConfigDAO(this);
        config = configDAO.getById(termo.getId());

        f = dao.recupera();
        valicaCampo();

    }

    private void valicaCampo() {

        imagemDocumento = (ImageView) findViewById(R.id.imgDocumento);
        imagemDocumento1 = (ImageView) findViewById(R.id.imgDocumento1);
        imagemDocumento2 = (ImageView) findViewById(R.id.imgDocumento2);
        imagemDocumento3 = (ImageView) findViewById(R.id.imgDocumento3);

        assinarDoc = (Button) findViewById(R.id.botaoAssinar);

        assinarDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaDoc == config.getQuantidadeFotos()) {
                    Intent it = new Intent(ImagemActivity.this, AssinaturaActivity.class);
                    startActivity(it);
                } else {
                    int restante = config.getQuantidadeFotos() - verificaDoc;
                    Toast.makeText(ImagemActivity.this, "Faltam " + restante + " fotos para prosseguir!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Preferencias preferencias = new Preferencias(this);

        termoDAO = new TermoDAO(this);
        termo = termoDAO.getById(preferencias.getTermo());

        tirarFoto(doc0, currentDocPath, CAMERA);

        verificaQtdFotos();
    }

    private void verificaQtdFotos() {


        /* Tornar a quantidade de fotos dinamicas tambem */
        /* REFATORAR ESSA PARTE DEPOIS, TA UMA BOSTA MANO */

        if (config.getQuantidadeFotos() == 2) {
            imagemDocumento1.setVisibility(View.VISIBLE);
        } else if (config.getQuantidadeFotos() == 3) {
            imagemDocumento1.setVisibility(View.VISIBLE);
            imagemDocumento2.setVisibility(View.VISIBLE);
        } else if (config.getQuantidadeFotos() == 4) {
            imagemDocumento1.setVisibility(View.VISIBLE);
            imagemDocumento2.setVisibility(View.VISIBLE);
            imagemDocumento3.setVisibility(View.VISIBLE);
        }

        imagemDocumento1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarFoto(doc1, currentDocPath1, DOC2);
            }
        });

        imagemDocumento2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarFoto(doc2, currentDocPath2, DOC3);
            }
        });

        imagemDocumento3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarFoto(doc3, currentDocPath3, DOC4);
            }
        });
    }

    public void tirarFoto(String doc, String path, int valor) {
        Toast.makeText(this, "Fotografe o " + doc + " para prosseguir..!", Toast.LENGTH_SHORT).show();
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camera.resolveActivity(getPackageManager()) != null) {

            file = null;

            try {
                file = getImageFile(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (file != null) {
                imageUri = FileProvider.getUriForFile(
                        this, "victor.machado.com.br.registro", file);
                camera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(camera, valor);
            }
        }
    }

    private File getImageFile(String path) throws IOException {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String nomePasta = "/Registro/Imagens";
        String arquivo = path + System.currentTimeMillis() + ".jpg";

        File mydir = new File(root, nomePasta);
        File fileFoto = new File(mydir, arquivo);

        if (mydir.exists()) mydir.mkdirs();

     /*   f.setCaminhoImagem(String.valueOf(fileFoto));
        dao.atualizar(f);
        Log.i("Imagem", f.getCaminhoImagem());*/
        return fileFoto;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Formulario formulario = new Formulario();
        FormularioDAO formularioDAO = new FormularioDAO(this);
        formulario = formularioDAO.recupera();

        Imagem imagem = new Imagem();
        ImagemDAO imagemDAO = new ImagemDAO(this);

        imagem.setFormulario(1);
        imagem.setIdFormulario(formulario.getId());

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    currentDocPath = String.valueOf(file);
                    imagemDoc = BitmapFactory.decodeFile(currentDocPath);
                    imagemDocumento.setImageBitmap(imagemDoc);

                    imagem.setCaminho(currentDocPath);
                    imagemDAO.inserir(imagem);
                    verificaDoc = 1;
                    break;

                case 2:
                    currentDocPath1 = String.valueOf(file);
                    imagemDoc1 = BitmapFactory.decodeFile(currentDocPath1);
                    imagemDocumento1.setImageBitmap(imagemDoc1);

                    imagem.setCaminho(currentDocPath1);
                    imagemDAO.inserir(imagem);
                    verificaDoc = 2;
                    break;

                case 3:
                    currentDocPath2 = String.valueOf(file);
                    imagemDoc2 = BitmapFactory.decodeFile(currentDocPath2);
                    imagemDocumento2.setImageBitmap(imagemDoc2);

                    imagem.setCaminho(currentDocPath2);
                    imagemDAO.inserir(imagem);
                    verificaDoc = 3;
                    break;

                case 4:
                    currentDocPath3 = String.valueOf(file);
                    imagemDoc3 = BitmapFactory.decodeFile(currentDocPath3);
                    imagemDocumento3.setImageBitmap(imagemDoc3);

                    imagem.setCaminho(currentDocPath3);
                    imagemDAO.inserir(imagem);
                    verificaDoc = 2;
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_limpar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_limpar:
                tirarFoto(doc0, currentDocPath, CAMERA);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
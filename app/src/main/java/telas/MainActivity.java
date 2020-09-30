package telas;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import helper.DocumentoDAO;
import helper.TermoAdapter;
import helper.TermoConfigDAO;
import helper.TermoDAO;
import model.Documento;
import model.Termo;
import model.TermoConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import util.ConfiguracaoFirebase;
import model.Formulario;
import helper.FormularioDAO;
import util.Permissao;
import util.Preferencias;
import victor.machado.com.br.registro.R;

public class MainActivity extends AppCompatActivity {

    private Context context = this;

    //Informações gerais
    private EditText endereco, nome, rg, cpf, cnpj, cnh, inscricaoEstadual, inscricaoMunicipal, celular;
    private Formulario formulario = null;
    private FirebaseAuth usuarioAutenticacao;

    private Termo termo;
    private TermoDAO termoDAO;

    private TextView tituloTermoSelecionado, textoOpcoes;

    private String opcaoEscolhida = "";

    private Button botaoEnviar;

    private Toolbar toolbar;
    private int backButtonCount = 0;

    /* LOCALIZAÇÃO */
    private Location location;
    private LocationManager locationManager;
    private Address enderecoLocalizacao;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private int countClick = 0;
    private String[] rua;
    private TermoConfig config;
    private TermoConfigDAO configDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Permissao permissao = new Permissao();
        permissao.Permissoes(this);

        Preferencias preferencias = new Preferencias(this);

        termoDAO = new TermoDAO(this);
        termo = termoDAO.getById(preferencias.getTermo());

        config = new TermoConfig();
        configDao = new TermoConfigDAO(this);
        config = configDao.getByIdTermo(termo.getId());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        validaCampo();
    }

    private void validaCampo() {
        //Campos de texto
        endereco = (EditText) findViewById(R.id.endClienteId);
        nome = (EditText) findViewById(R.id.nomeClienteId);
        celular = (EditText) findViewById(R.id.celularClienteId);

        endereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (countClick == 1) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager = (LocationManager)
                            getSystemService(Context.LOCATION_SERVICE);
                    location =
                            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                    } else {
                        //endereco.setText("Localização indisponível...!");
                        Toast.makeText(context, "Localização indisponível...!", Toast.LENGTH_SHORT).show();
                        countClick = 0;
                    }

                    try {

                        enderecoLocalizacao = buscaEndereco(latitude, longitude);

                        String end = enderecoLocalizacao.getAddressLine(0);// rua numero e bairro;
                        rua = end.split(",");

                        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle)
                                .setTitle("Confirme o Endereço")
                                .setMessage(rua[0])
                                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        countClick = 0;
                                    }
                                })
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        endereco.setText(rua[0]);
                                        countClick = 0;
                                    }
                                }).create();
                        dialog.show();


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        Toast.makeText(context, "Localização indisponível...!", Toast.LENGTH_SHORT).show();
                        countClick = 0;
                    }
                } else {
                    Toast.makeText(context, "Clique novamente para abrir endereço rápido", Toast.LENGTH_SHORT).show();
                    countClick++;
                }
            }
        });

        /* Documentos */
        rg = (EditText) findViewById(R.id.rgClienteId);
        cpf = (EditText) findViewById(R.id.cpfClienteId);
        cnpj = (EditText) findViewById(R.id.cnpjClienteId);
        cnh = (EditText) findViewById(R.id.cnhClienteId);
        inscricaoEstadual = (EditText) findViewById(R.id.ieId);
        inscricaoMunicipal = (EditText) findViewById(R.id.imId);

        tituloTermoSelecionado = (TextView) findViewById(R.id.tituloTermoId);
        textoOpcoes = (TextView) findViewById(R.id.opcaoEscolhidaId);

        tituloTermoSelecionado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Termo> termos = termoDAO.obterTodos();

                final TermoAdapter adapter = new TermoAdapter(MainActivity.this, (ArrayList<Termo>) termos);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
                builder
                        .setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                termo = (Termo) adapter.getItem(which);

                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                preferencias.salvarTermoSelecionado(termo.getId());
                                acessaActivity(MainActivity.class);
                            }
                        })
                        .setTitle("Selecione o formulário: ")
                        .show();
            }
        });

        config.getOpcoes();

        String[] documentosForm = config.getDocumentosNecessarios();
        for (int i = 0; i < documentosForm.length; i++) {
            switch (documentosForm[i]) {
                case "RG":
                    rg.setVisibility(View.VISIBLE);
                    mascara("NN.NNN.NNN", rg);
                    break;
                case "CPF":
                    cpf.setVisibility(View.VISIBLE);
                    mascara("NNN.NNN.NNN-NN", cpf);
                    break;
                case "CNPJ":
                    cnpj.setVisibility(View.VISIBLE);
                    mascara("NN.NNN.NNN/NNNN-NN", cnpj);
                    break;
                case "IE":
                    inscricaoEstadual.setVisibility(View.VISIBLE);
                    mascara("NNN.NNN.NNN.NNN", inscricaoEstadual);
                    break;
                case "IM":
                    inscricaoMunicipal.setVisibility(View.VISIBLE);
                    mascara("N.NNN.NNN-N", inscricaoMunicipal);
                    break;
                case "CCM":
                    inscricaoMunicipal.setVisibility(View.VISIBLE);
                    mascara("N.NNN.NNN-N", inscricaoMunicipal);
                    break;
                case "CNH":
                    cnh.setVisibility(View.VISIBLE);
                    mascara("NNNNNNNNNNN", cnh);
                    break;
            }
        }

        mascara("(NN) NNNNN-NNNN", celular);

        tituloTermoSelecionado.setText(termo.getTitulo());

        final TermoConfig finalConfig = config;
        textoOpcoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
                switch (finalConfig.getUnicaEscolha()) {
                    case 1:
                        builder
                                .setSingleChoiceItems(finalConfig.getOpcoes(), 0, null)
                                .setTitle("O cliente é: ")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        int selecionado = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                        opcaoEscolhida = finalConfig.getOpcoes()[selecionado];
                                        textoOpcoes.setText("O cliente é: " + opcaoEscolhida);
                                    }
                                }).show();
                        break;
                    case 0:
                        final List<String> listaOpcoes = Arrays.asList(finalConfig.getOpcoes());
                        final boolean[] checkedOptions = new boolean[]{
                                false,
                        };

                        builder
                                .setMultiChoiceItems(finalConfig.getOpcoes(), checkedOptions, new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        checkedOptions[which] = isChecked;

                                        String currentItem = listaOpcoes.get(which);
                                    }
                                })

                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        textoOpcoes.setText("O cliente é: ");
                                        // Do something when click positive button
                                        //tv.setText("Your preferred colors..... \n");
                                        for (int i = 0; i < checkedOptions.length; i++) {
                                            boolean checked = checkedOptions[i];
                                            if (checked) {
                                                textoOpcoes.setText(textoOpcoes.getText() + listaOpcoes.get(i) + ", ");
                                            }
                                        }
                                    }
                                }).show();
                        break;
                }
            }
        });


        //Valida sessão
        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        botaoEnviar = (Button) findViewById(R.id.gerarDocId);

        File mydir = new File(Environment.getExternalStorageDirectory() + "/Registro");
        String caminhoLogo = mydir + "/Imagens/" + "logo" + ".png";

        final File file = new File(caminhoLogo);

        //Recuperando o formulário para atualizar
        Intent it = getIntent();
        if (it.hasExtra("formulario")) {

            //Recuperando informação
            formulario = (Formulario) it.getSerializableExtra("formulario");

            //Setando para o usuário
            endereco.setText(formulario.getEndereco());
            nome.setText(formulario.getNome());

            cpf.setText(formulario.getCpf());
            celular.setText(formulario.getCelular());

            /* Fazer a pesquisa na tabela de documento pelo idFormulario */
            DocumentoDAO documentoDAO = new DocumentoDAO(this);
            List<Documento> documentoList = documentoDAO.obterTodosIdForm(formulario.getId());
            for (Documento doc : documentoList) {
                switch (doc.getTipo()) {
                    case "RG":
                        rg.setVisibility(View.VISIBLE);
                        rg.setText(doc.getDocumento());
                        break;
                    case "CPF":
                        cpf.setVisibility(View.VISIBLE);
                        cpf.setText(doc.getDocumento());
                        break;
                    case "CNPJ":
                        cnpj.setVisibility(View.VISIBLE);
                        cnpj.setText(doc.getDocumento());
                        break;
                    case "IE":
                        inscricaoEstadual.setVisibility(View.VISIBLE);
                        inscricaoEstadual.setText(doc.getDocumento());
                        break;
                    case "IM":
                        inscricaoMunicipal.setVisibility(View.VISIBLE);
                        inscricaoMunicipal.setText(doc.getDocumento());
                        break;
                    case "CCM":
                        inscricaoMunicipal.setVisibility(View.VISIBLE);
                        inscricaoMunicipal.setText(doc.getDocumento());
                        break;
                    case "CNH":
                        cnh.setVisibility(View.VISIBLE);
                        cnh.setText(doc.getDocumento());
                        break;
                }
            }
        }

        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean prosseguir = validaCampoVazio();
                if (prosseguir) {
                    if (!(file.exists())) criarPasta();
                    if (formulario != null) {
                        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle)
                                .setTitle("Atenção")
                                .setMessage("Deseja gerar um novo PDF?")
                                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        atualizaBanco();
                                    }
                                })
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        geraDoc();
                                    }
                                }).create();
                        dialog.show();
                    } else {
                        geraDoc();
                    }
                } else {
                    Toast.makeText(context, "Preencha todas as informações!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private Address buscaEndereco(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        Address address = null;
        List<Address> addresses;

        geocoder = new Geocoder(getApplicationContext());
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        if (addresses.size() > 0) {
            address = addresses.get(0);
        }
        return address;
    }

    private void mascara(String mascara, EditText campo) {
        SimpleMaskFormatter smf = new SimpleMaskFormatter(mascara);
        MaskTextWatcher mtw = new MaskTextWatcher(campo, smf);
        campo.addTextChangedListener(mtw);
    }


    @Override
    public void onBackPressed() {

        if (backButtonCount >= 1) {
            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle)
                    .setTitle("Atenção")
                    .setMessage("Deseja realmente sair? as informações serão perdidas!")
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).create();
            dialog.show();
        } else {
            Toast.makeText(this, "Pressione novamente para sair do atendimento.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }


    private boolean validaCampoVazio() {
        boolean valida = true;
       /* if (endereco.getText().toString().isEmpty())
            valida = false;
        if (nome.getText().toString().isEmpty())
            valida = false;
        if (rg.getText().toString().isEmpty())
            valida = false;
        if (cpf.getText().toString().isEmpty())
            valida = false;
*/
        return valida;
    }

    private void criarPasta() {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        Drawable mDrawable = new BitmapDrawable(getResources(), bitmap);
        BitmapDrawable drawable = ((BitmapDrawable) mDrawable);
        Bitmap bitmap1 = drawable.getBitmap();
        OutputStream out = null;
        File mydir = new File(Environment.getExternalStorageDirectory() + "/Registro");
        File imagem = new File(mydir, "Imagens");
        String caminhoLogo = mydir + "/Imagens/" + "logo" + ".png";
        File logo = new File(imagem, "logo.png");

        if (!(logo.exists())) {
            mydir.mkdir();
            imagem.mkdirs();
            try {
                logo.createNewFile();
                out = new FileOutputStream(logo);
                bitmap1.compress(Bitmap.CompressFormat.PNG, 50, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void geraDoc() {
        if (opcaoEscolhida.isEmpty())
            opcaoEscolhida = "Morador Responsável";
        Bundle bundle = new Bundle();
        bundle.putString("endereco", endereco.getText().toString());
        bundle.putString("nome", nome.getText().toString());

        String[] documentosForm = config.getDocumentosNecessarios();
        for (int i = 0; i < documentosForm.length; i++) {
            switch (documentosForm[i]) {
                case "RG":
                    bundle.putString("rg", rg.getText().toString());
                    break;
                case "CPF":
                    bundle.putString("cpf", cpf.getText().toString());
                    break;
                case "CNPJ":
                    bundle.putString("cnpj", cnpj.getText().toString());
                    break;
                case "IE":
                    bundle.putString("ie", inscricaoEstadual.getText().toString());
                    break;
                case "IM":
                    bundle.putString("im", inscricaoMunicipal.getText().toString());
                    break;
                case "CCM":
                    bundle.putString("im", inscricaoMunicipal.getText().toString());
                    break;
                case "CNH":
                    bundle.putString("cnh", cnh.getText().toString());
                    break;
            }
        }

        bundle.putString("celular", celular.getText().toString());
        bundle.putString("responsabilidade", opcaoEscolhida);

        Intent intent = new Intent(MainActivity.this, ConfirmaActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void atualizaBanco() {

        formulario.setEndereco(endereco.getText().toString());
        formulario.setNome(nome.getText().toString());
        formulario.setRg(rg.getText().toString());
        formulario.setCpf(cpf.getText().toString());
        formulario.setCelular(celular.getText().toString());
        formulario.setResponsabilidade(opcaoEscolhida.toString());

        FormularioDAO dao = new FormularioDAO(MainActivity.this);
        dao.atualizar(formulario);

        Toast.makeText(MainActivity.this, "Formulário atualizado com sucesso!", Toast.LENGTH_SHORT).show();

        limpar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.itemSair:
                deslogarUsuario();
                return true;
            case R.id.itemConfiguracoes:
                acessaActivity(ConfiguracoesActivity.class);
                return true;
            case R.id.item_lista:
                acessaActivity(ListarFormsActivity.class);
                return true;
            case R.id.item_limpa:
                limpar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void acessaActivity(Class c) {
        Intent i = new Intent(MainActivity.this, c);
        startActivity(i);
        finish();
    }

    private void deslogarUsuario() {
        usuarioAutenticacao.signOut();
        acessaActivity(LoginActivity.class);
        finish();
    }

    public void limpar() {
        acessaActivity(MainActivity.class);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

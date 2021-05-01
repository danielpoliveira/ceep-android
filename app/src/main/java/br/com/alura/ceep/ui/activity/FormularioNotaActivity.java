package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Cor;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaCoresAdapter;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class FormularioNotaActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR_INSERE = "Insere nota";
    public static final String TITULO_APPBAR_ALTERA = "Altera nota";
    public static final String NOTA_STATE_KEY = "NOTA_STATE";
    public static final String POSICAO_STATE_KEY = "POSICAO_STATE";
    private int posicaoRecibida = POSICAO_INVALIDA;
    private TextView titulo;
    private TextView descricao;
    private ConstraintLayout layout;
    private Nota nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        setTitle(TITULO_APPBAR_INSERE);
        List<Cor> cores = pegaTodasCores();
        configuraRecyclerView(cores);
        inicializaCampos();
        configuraNotaSalva(savedInstanceState);
        preencheCampos();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        configuraNota();
        outState.putInt(POSICAO_STATE_KEY, posicaoRecibida);
        outState.putSerializable(NOTA_STATE_KEY, nota);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ehMenuSalvaNota(item)) {
            configuraNota();
            retornaNota(nota);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void configuraNotaSalva(Bundle savedInstanceState) {
        if (existeEstadoSalvo(savedInstanceState)) {
            getNotaSalva(savedInstanceState.getSerializable(NOTA_STATE_KEY));
            getPosicaoSalva(savedInstanceState.getInt(POSICAO_STATE_KEY, POSICAO_INVALIDA));
        } else {
            Intent dadosRecebidos = getIntent();
            if (existeNota(dadosRecebidos)) {
                configuraAlteraNota(dadosRecebidos);
            } else {
                nota = new Nota();
            }
        }
    }

    private void configuraAlteraNota(Intent dadosRecebidos) {
        setTitle(TITULO_APPBAR_ALTERA);
        getNotaSalva(dadosRecebidos.getSerializableExtra(CHAVE_NOTA));
        getPosicaoSalva(dadosRecebidos.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA));
    }

    private void configuraRecyclerView(List<Cor> cores) {
        RecyclerView recyclerView = findViewById(R.id.formulario_nota_cores_recyclerview);
        configuraAdapter(cores, recyclerView);
    }

    private void configuraAdapter(List<Cor> cores, RecyclerView recyclerView) {
        ListaCoresAdapter adapter = new ListaCoresAdapter(this, cores);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this::alteraCorDoContainer);
    }

    private void alteraCorDoContainer(Cor cor) {
        layout.setBackgroundColor(paraCor(cor));
    }

    private int paraCor(Cor cor) {
        return Color.parseColor(cor.getCorString());
    }

    private List<Cor> pegaTodasCores() {
        return Cor.getAll();
    }

    private void preencheCampos() {
        titulo.setText(nota.getTitulo());
        descricao.setText(nota.getDescricao());

        if (nota.getCor() != null) {
            alteraCorDoContainer(paraCor(nota.getCor().getCorString()));
        }
    }

    private void inicializaCampos() {
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
        layout = findViewById(R.id.formulario_nota_constraint_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void configuraNota() {
        nota.setTitulo(titulo.getText().toString());
        nota.setDescricao(descricao.getText().toString());
        nota.setCor(getCorFundoLayout());
    }

    private void retornaNota(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecibida);
        setResult(Activity.RESULT_OK, resultadoInsercao);
    }

    private Cor getCorFundoLayout() {
        int intCor = getDrawableColor();
        String stringCor = String.format("#%06X", 0xFFFFFF & intCor);
        return paraCor(stringCor);
    }

    private int getDrawableColor() {
        int cor = Color.WHITE;
        Drawable background = layout.getBackground();
        if (background instanceof ColorDrawable)
            cor = ((ColorDrawable) background).getColor();
        return cor;
    }

    private boolean ehMenuSalvaNota(MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_ic_salva;
    }

    private boolean existeEstadoSalvo(Bundle savedInstanceState) {
        return savedInstanceState != null;
    }

    private boolean existeNota(Intent dadosRecebidos) {
        return dadosRecebidos.hasExtra(CHAVE_NOTA);
    }

    private void getNotaSalva(Serializable serializable) {
        nota = (Nota) serializable;
    }

    private void getPosicaoSalva(int value) {
        posicaoRecibida = value;
    }

    private Cor paraCor(String stringCor) {
        return Cor.toCor(stringCor);
    }
}

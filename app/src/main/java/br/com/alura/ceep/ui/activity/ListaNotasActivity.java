package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Layout;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.preferences.EstadoLayoutPreferences;
import br.com.alura.ceep.repository.NotaRepository;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.alura.ceep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

import static br.com.alura.ceep.model.Layout.GRID;
import static br.com.alura.ceep.model.Layout.LINEAR;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {
    private static final String MENSAGEM_ERRO_BUSCA_NOTAS = "Não foi possível carregar as notas no banco de dados";
    private static final String MENSAGEM_ERRO_REMOCAO = "Não foi possível remover o produto";
    private static final String MENSAGEM_ERRO_SALVA = "Não foi possível salvar o produto";
    private static final String MENSAGEM_ERRO_EDICAO = "Não foi possível editar o produto";

    public static final String TITULO_APPBAR = "Notas";

    private Layout layout;
    private EstadoLayoutPreferences preferences;
    private ListaNotasAdapter adapter;
    private NotaRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_APPBAR);
        configuraBotaoInsereNota();
        configuraRecyclerView();
        repository = new NotaRepository(this);

        buscaNotas();
    }

    private void mostraErro(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_notas, menu);
        configuraLayoutOptionsMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_lista_notas_ajuda) {
            vaiParaFeedbackActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void configuraLayoutOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_lista_notas_layout);
        carregaLayout();
        configuraBotaoAlterarLayout(item);
        configuraLayout(item);
    }

    private boolean verificaLayout() {
        return layout == LINEAR;
    }

    private void carregaLayout() {
        preferences = new EstadoLayoutPreferences(this);
        String layoutString = preferences.getEstadoLayout();
        layout = Layout.toLayout(layoutString);
    }

    private void configuraBotaoAlterarLayout(MenuItem item) {
        item.setOnMenuItemClickListener(this::alteraLayout);
    }

    private boolean alteraLayout(MenuItem item) {
        layout = verificaLayout() ? GRID : LINEAR;

        configuraLayout(item);
        preferences.saveEstadoLayout(layout);
        return true;
    }

    private void configuraLayout(MenuItem item) {
        if (verificaLayout()) {
            item.setIcon(R.drawable.ic_grid);
        } else {
            item.setIcon(R.drawable.ic_rows);
        }

        configuraLayoutManagerRecyclerview();
    }

    private void configuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(view -> vaiParaFormularioNotaActivityInsere());
    }

    private void configuraLayoutManagerRecyclerview() {
        RecyclerView recyclerview = findViewById(R.id.lista_notas_recyclerview);
        recyclerview.setLayoutManager(verificaLayout() ?
                new LinearLayoutManager(this) :
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent iniciaFormularioNota =
                new Intent(ListaNotasActivity.this,
                        FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota,
                CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private void vaiParaFeedbackActivity() {
        Intent iniciaFeedback =
                new Intent(ListaNotasActivity.this,
                        FeedbackActivity.class);
        startActivity(iniciaFeedback);
    }

    private void buscaNotas() {
        repository.buscaNotas(new NotaRepository.Resultado<List<Nota>>() {
            @Override
            public void quandoSucesso(List<Nota> resultado) {

                adapter.atualiza(resultado);
            }

            @Override
            public void quandoFalha(String erro) {
                mostraErro(MENSAGEM_ERRO_BUSCA_NOTAS);
            }
        });
    }

    private void adiciona(Nota nota) {
        repository.adiciona(nota, new NotaRepository.Resultado<Nota>() {
            @Override
            public void quandoSucesso(Nota resultado) {
                adapter.adiciona(resultado);
            }

            @Override
            public void quandoFalha(String erro) {
                mostraErro(MENSAGEM_ERRO_SALVA);
            }
        });
    }

    private void altera(Nota nota, int posicao) {
        repository.edita(nota, new NotaRepository.Resultado<Nota>() {
            @Override
            public void quandoSucesso(Nota resultado) {
                adapter.altera(posicao, resultado);
            }

            @Override
            public void quandoFalha(String erro) {
                mostraErro(MENSAGEM_ERRO_EDICAO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ehResultadoInsereNota(requestCode, data)) {

            if (resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adiciona(notaRecebida);
            }

        }

        if (ehResultadoAlteraNota(requestCode, data)) {
            if (resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                if (ehPosicaoValida(posicaoRecebida)) {
                    altera(notaRecebida, posicaoRecebida);
                }
            }
        }
    }

    private boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean ehResultadoAlteraNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode) &&
                temNota(data);
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private boolean ehResultadoInsereNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) &&
                temNota(data);
    }

    private boolean temNota(Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private boolean resultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void configuraRecyclerView() {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(listaNotas);
        configuraItemTouchHelper(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView recyclerview) {
        NotaItemTouchHelperCallback notaItemTouchHelperCallback = new NotaItemTouchHelperCallback(adapter);
        notaItemTouchHelperCallback.setOnItemSwippedListener((nota, posicao) -> repository.remove(nota,
                new NotaRepository.Resultado<Void>() {
                    @Override
                    public void quandoSucesso(Void resultado) {
                        adapter.remove(posicao);
                    }

                    @Override
                    public void quandoFalha(String erro) {
                        mostraErro(MENSAGEM_ERRO_REMOCAO);
                    }
                }
        ));

        ItemTouchHelper itemTouchHelper =
                new ItemTouchHelper(notaItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerview);
    }

    private void configuraAdapter(RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(this::vaiParaFormularioNotaActivityAltera);
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }

}

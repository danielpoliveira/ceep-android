package br.com.alura.ceep.ui.recyclerview.helper.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class NotaItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ListaNotasAdapter adapter;
    private OnItemSwippedListener onItemSwippedListener;

    public NotaItemTouchHelperCallback(ListaNotasAdapter adapter) {
        this.adapter = adapter;
    }

    public void setOnItemSwippedListener(OnItemSwippedListener onItemSwippedListener) {
        this.onItemSwippedListener = onItemSwippedListener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int marcacoesDeDeslize = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        int marcacoesDeArrastar = ItemTouchHelper.DOWN | ItemTouchHelper.UP
                | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(marcacoesDeArrastar, marcacoesDeDeslize);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int posicaoDaNotaDeslizada = viewHolder.getBindingAdapterPosition();
        removeNota(posicaoDaNotaDeslizada, onItemSwippedListener);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int posicaoInicial = viewHolder.getBindingAdapterPosition();
        int posicaoFinal = target.getBindingAdapterPosition();

        trocaNotas(posicaoInicial, posicaoFinal);

        return true;
    }

    private void trocaNotas(int inicio, int fim) {
        adapter.troca(inicio, fim);
    }

    private void removeNota(int posicao, OnItemSwippedListener listener) {
        Nota nota = adapter.getNota(posicao);
        if (nota != null) {
            listener.onItemSwipped(nota, posicao);
        }
    }

    public interface OnItemSwippedListener {
        void onItemSwipped(Nota nota, int posicao);
    }

}

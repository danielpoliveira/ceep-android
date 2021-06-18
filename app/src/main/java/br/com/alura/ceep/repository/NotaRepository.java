package br.com.alura.ceep.repository;

import android.content.Context;

import java.util.List;

import br.com.alura.ceep.asynctask.BaseAsyncTask;
import br.com.alura.ceep.database.AppDatabase;
import br.com.alura.ceep.database.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;

public class NotaRepository {
    private final NotaDAO dao;

    public NotaRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        dao = db.getNotaDAO();
    }

    public void buscaNotas(Resultado<List<Nota>> callback) {
        new BaseAsyncTask<>(dao::todos, callback::quandoSucesso).execute();
    }

    public void adiciona(Nota nota, Resultado<Nota> callback) {
        new BaseAsyncTask<>(() -> {
            long id = dao.insere(nota);
            return dao.buscaNota(id);
        }, callback::quandoSucesso).execute();
    }

    public void edita(Nota nota, Resultado<Nota> callback) {
        new BaseAsyncTask<>(() -> {
            dao.altera(nota);
            return nota;
        }, callback::quandoSucesso).execute();
    }

    public void remove(Nota nota, Resultado<Void> callback) {
        new BaseAsyncTask<>(() -> {
            dao.remove(nota);
            return null;
        }, callback::quandoSucesso).execute();
    }

    public interface Resultado<T> {
        void quandoSucesso(T resultado);

        void quandoFalha(String erro);
    }

}

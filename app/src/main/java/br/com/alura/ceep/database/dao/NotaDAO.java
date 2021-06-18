package br.com.alura.ceep.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.alura.ceep.model.Nota;

@Dao
public interface NotaDAO {

    @Query("SELECT * FROM nota")
    List<Nota> todos();

    @Query("SELECT * FROM Nota WHERE id = :id")
    Nota buscaNota(long id);

    @Insert
    void insereVarios(Nota... notas);

    @Insert
    long insere(Nota nota);

    @Update
    void altera(Nota nota);

    @Delete
    void remove(Nota nota);

}

package br.com.alura.ceep.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import br.com.alura.ceep.database.converter.CorConverter;
import br.com.alura.ceep.database.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;

@Database(entities = {Nota.class}, version = 2, exportSchema = false)
@TypeConverters(value = {CorConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final String CEEP_DB_NAME = "ceep.db";

    private static AppDatabase db;

    public static AppDatabase getInstance(Context context) {

        if (db == null) {
            db = Room.databaseBuilder(
                    context,
                    AppDatabase.class,
                    CEEP_DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return db;
    }

    public abstract NotaDAO getNotaDAO();

}

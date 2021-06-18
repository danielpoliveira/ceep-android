package br.com.alura.ceep.database.converter;

import androidx.room.TypeConverter;

import br.com.alura.ceep.model.Cor;

public class CorConverter {

    @TypeConverter
    public Cor paraCor(String valor) { return Cor.toCor(valor); }

    @TypeConverter
    public String paraString(Cor cor) {
        return cor != null ? cor.getCorString() : "";
    }
}

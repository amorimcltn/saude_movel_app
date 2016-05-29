package br.com.ifbavca.saudemovel.dados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Cleiton on 22/10/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "saudemovel";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS VISITA (COD_VISITA INTEGER, COD_PROF INTEGER, COD_PACIENTE INTEGER, NOME TEXT, IDADE TEXT, SEXO TEXT, RUA TEXT, NUMERO TEXT, " +
                "BAIRRO TEXT, CIDADE TEXT, ESTADO TEXT, CEP TEXT, LATITUDE TEXT, LONGITUDE TEXT, " +
                "PATOLOGIAS TEXT, DATA_HORA TEXT, DATA_VISITA TEXT, STATUS INTEGER, ANOTACOES TEXT, PRIORIDADE INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean salvaDados(String tabela, ContentValues valores){
        boolean retorno = false;
        try{
            long resultado = getWritableDatabase().insert(tabela, null, valores);
            if (resultado > 0){
                retorno = true;
            }
        }catch(Exception erro){

        }
        return retorno;
    }

    public Cursor getVisitas(){
        String[] columns = {"cod_visita", "cod_prof", "cod_paciente", "nome", "idade", "sexo", "rua", "numero", "bairro",
        "cidade", "estado", "cep", "latitude", "longitude", "patologias", "data_hora", "data_visita", "status", "anotacoes", "prioridade"};
        Cursor cursor = getWritableDatabase().query("visita", columns, null, null, null, null, null);
        return cursor;
    }

    public Cursor getPacientesByBairro(String pesquisa){
        String[] args = new String[1];
        args[0] = pesquisa;
        Cursor cursor = getWritableDatabase().rawQuery("select * from visita where bairro = ?", args);
        return cursor;
    }

    public Cursor getPacienteById(int id){
        String[] args = new String[1];
        args[0] = String.valueOf(id);
        Cursor cursor = getWritableDatabase().rawQuery("select * from visita where cod_paciente = ?", args);
        return cursor;
    }

    public int upDateInfo(String patologias, String observacoes, int idpaciente, int prioridade){
        ContentValues cv = new ContentValues();
        cv.put("patologias", patologias);
        cv.put("anotacoes", observacoes);
        cv.put("prioridade", prioridade);
        int r = getWritableDatabase().update("visita", cv, "cod_paciente " + "=" + idpaciente, null);
        return r;
    }

    public int finalizaVisita(int idpaciente, int status, int prioridade){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        cv.put("data_hora", dateFormat.format(date));
        cv.put("prioridade", prioridade);
        int r = getWritableDatabase().update("visita", cv, "cod_paciente "+"="+idpaciente, null);
        return r;
    }

    public Cursor getVisitasFinalizadas(){
        String[] args = new String[1];
        args[0] = "1";
        Cursor cursor = getWritableDatabase().rawQuery("select cod_paciente, patologias, anotacoes, data_hora, prioridade from visita where status = ?", args);
        return cursor;
    }

    public int delete(String tabela) {
        return getWritableDatabase().delete(tabela, null, null);
    }
}

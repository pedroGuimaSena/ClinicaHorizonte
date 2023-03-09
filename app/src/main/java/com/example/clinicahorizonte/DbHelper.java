package com.example.clinicahorizonte;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "apppacientesfinal.db";
    static final int DB_VERSION = 3;
    static final String TABLE_PACIENTES = "pacientes";
    static final String C_ID = "_id";
    static final String C_NOME = "edit_nome";
    static final String C_IDADE = "edit_idade";
    static final String C_TEMPERATURA = "edit_temperatura";
    static final String C_TOSSE = "dias_tosse";
    static final String C_DOR = "dias_dor";
    static final String C_SEMANAS = "semanas_viagem";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String sql = String.format("create table %s (%s integer primary key autoincrement, %s text, %s integer, %s real, %s integer, %s integer, %s integer);", TABLE_PACIENTES, C_ID, C_NOME, C_TEMPERATURA, C_IDADE, C_TOSSE, C_DOR, C_SEMANAS);
            Log.i("LOG_TESTE", sql);
            db.execSQL(sql);
        } catch (Exception e) {
            Log.i("LOG_TESTE", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            String sql = String.format("drop table if exists %s", TABLE_PACIENTES);
            db.execSQL(sql);
            onCreate(db);
        } catch (Exception e) {
            Log.i("LOG_TESTE", e.getMessage());
        }
    }
}

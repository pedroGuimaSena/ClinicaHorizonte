package com.example.clinicahorizonte;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    private Button btnNovoPaciente;
    private EditText edit_nome;
    private EditText edit_idade;
    private EditText edit_temperatura;
    private EditText dias_tosse;
    private EditText dias_dor;
    private EditText edit_semanasViagem;
    private DbHelper dbHelper;
    private ListView listViewPacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNovoPaciente = findViewById(R.id.btnCadastrar);
        btnNovoPaciente.setOnClickListener(this);
        dbHelper = new DbHelper(this);
        listViewPacientes = findViewById(R.id.listViewPacientes);
        lerPacientes();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCadastrar) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            View dialogNovoPaciente = getLayoutInflater().inflate(R.layout.alert_novo_paciente, null);
            edit_nome = dialogNovoPaciente.findViewById(R.id.edit_nome);
            edit_idade = dialogNovoPaciente.findViewById(R.id.edit_idade);
            edit_temperatura = dialogNovoPaciente.findViewById(R.id.edit_temperatura);
            dias_tosse = dialogNovoPaciente.findViewById(R.id.dias_tosse);
            dias_dor = dialogNovoPaciente.findViewById(R.id.dias_dor);
            edit_semanasViagem = dialogNovoPaciente.findViewById(R.id.edit_semanasViagem);


            builder.setView(dialogNovoPaciente);
            builder.setTitle(R.string.txtTitleAlertNovoPaciente);
            builder.setPositiveButton(R.string.txtAlertSalvar, this);
            builder.setNegativeButton(R.string.txtAlertCancelar, null);

            builder.show();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String paciente = edit_nome.getText().toString();
        int idade = Integer.parseInt(edit_idade.getText().toString());
        float temperatura = Float.parseFloat(edit_temperatura.getText().toString());
        int tosse = Integer.parseInt(dias_tosse.getText().toString());
        int dor = Integer.parseInt(dias_dor.getText().toString());
        int semanasViagem = Integer.parseInt(edit_semanasViagem.getText().toString());

        //MENSAGENS DE EXIBIÇÃO DO STATUS DO PACIENTE:
        if (semanasViagem <= 6 && tosse > 5 && temperatura > 37) {
            Context contexto = getApplicationContext();
            String texto = "O paciente deve ser internado para tratamento";
            int duracao = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(contexto, texto, duracao);
            toast.show();

        } else if (idade > 60 && semanasViagem <= 6 && temperatura > 37 && dor > 3 && tosse > 5) {
            if (idade < 10) {
                Context contexto2 = getApplicationContext();
                String texto2 = "O paciente deve ser enviado à quarentena";
                int duracao2 = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(contexto2, texto2, duracao2);
                toast.show();
            }
        } else {
            Context contexto3 = getApplicationContext();
            String texto3 = "O paciente deve ser liberado";
            int duracao3 = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(contexto3, texto3, duracao3);
            toast.show();
        }


        //INSERT NO BANCO DE DADOS:
        try {

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(dbHelper.C_NOME, paciente);
            values.put(dbHelper.C_IDADE, idade);
            values.put(dbHelper.C_TEMPERATURA, temperatura);
            values.put(dbHelper.C_TOSSE, tosse);
            values.put(dbHelper.C_DOR, dor);
            values.put(dbHelper.C_SEMANAS, semanasViagem);

            try {
                int id = (int) db.insertOrThrow(DbHelper.TABLE_PACIENTES, null, values);
                Log.i("LOG_TESTE", "" + id);
            } finally {
                db.close();
            }

        } catch (Exception e) {
            Log.i("LOG_TESTE", e.getMessage());


        }
        lerPacientes();
    }



    private void lerPacientes() {
        SQLiteDatabase db = null;

        try {
           db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query(DbHelper.TABLE_PACIENTES, null, null, null, null, null, DbHelper.C_NOME);

            String[] from = {DbHelper.C_NOME, DbHelper.C_IDADE};
            int[] to = {R.id.textViewExibirNome, R.id.textViewExibirIdade};

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.item_lista_pacientes, cursor, from, to, 0 );

            listViewPacientes.setAdapter(simpleCursorAdapter);

        }catch (Exception e) {
            Log.i("LOG_TESTE", e.getMessage());
        }finally {
            if (db!=null){
                db.close();
            }
        }
    }
}

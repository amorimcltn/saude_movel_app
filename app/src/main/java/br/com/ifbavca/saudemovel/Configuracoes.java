package br.com.ifbavca.saudemovel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.ifbavca.saudemovel.classes.Visita;
import br.com.ifbavca.saudemovel.dados.DatabaseHelper;
import br.com.ifbavca.saudemovel.servico.FinalizarTask;

public class Configuracoes extends ActionBarActivity implements ISincronizaAgendamentos {

    private FinalizarTask finalizarTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_statusbar));
            window.setNavigationBarColor(getResources().getColor(R.color.gray_bar));
            window.setTitle("");
        }

        finalizarTask = new FinalizarTask(this);
        finalizarTask.delegate = this;

        Button btFinaliza = (Button) findViewById(R.id.btSincronizar);
        btFinaliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Configuracoes.this);
                builder.setMessage("Finalizar itinerário?");
                builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                        Cursor cursor = databaseHelper.getVisitasFinalizadas();
                        List<Visita> visitasLocal = new ArrayList<Visita>();
                        while (cursor.moveToNext()) {
                            Visita visita = new Visita();
                            visita.setCodPaciente(cursor.getInt(cursor.getColumnIndex("COD_PACIENTE")));
                            visita.setPatologias(cursor.getString(cursor.getColumnIndex("PATOLOGIAS")));
                            visita.setDataHora(cursor.getString(cursor.getColumnIndex("DATA_HORA")));
                            visita.setAnotacoes(cursor.getString(cursor.getColumnIndex("ANOTACOES")));
                            visita.setPrioridade(cursor.getInt(cursor.getColumnIndex("PRIORIDADE")));
                            visitasLocal.add(visita);
                        }
                        cursor.close();
                        databaseHelper.close();

                        if (visitasLocal.size() > 0) {
                            finalizarTask.execute(visitasLocal);
                        } else {

                        }
                    }
                });

                builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void retornaVisitas(List<Visita> visitas) {

    }

    @Override
    public void finalizaFeedBack(String resposta) {
        if ("1".equalsIgnoreCase(resposta)){
            Toast.makeText(this, "Sincronizado com sucesso!", Toast.LENGTH_SHORT).show();
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            databaseHelper.delete("visita");
            databaseHelper.close();
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("sincronismo", true);
            editor.commit();
        }else{
            Toast.makeText(this, "Erro ao finalizar, tente novamente!", Toast.LENGTH_LONG).show();
        }
    }
}

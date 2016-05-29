package br.com.ifbavca.saudemovel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.ifbavca.saudemovel.classes.Visita;
import br.com.ifbavca.saudemovel.classes.VisitaBairro;
import br.com.ifbavca.saudemovel.classes.VisitaItemApapter;
import br.com.ifbavca.saudemovel.dados.DatabaseHelper;
import br.com.ifbavca.saudemovel.servico.BuscaVisitasTask;

public class PrincipalActivity extends AppCompatActivity implements ISincronizaAgendamentos {

    private List<Visita> visitasServer = new ArrayList<>();
    private List<VisitaBairro> visitasBairro = new ArrayList<>();
    private ListView listVisitas;
    private VisitaItemApapter adapter;
    private BuscaVisitasTask buscaVisitasTask;
    private InstanciaUnica instanciaUnica = InstanciaUnica.getInstance();
    private SharedPreferences settings;
    public static final String SINCRONISMO = "sincronismo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_statusbar));
            window.setNavigationBarColor(getResources().getColor(R.color.gray_bar));
            window.setTitle("");
        }

        listVisitas = (ListView) findViewById(R.id.listview_agendamentos);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean sincronismo = settings.getBoolean(SINCRONISMO, true);
        if (sincronismo) {
            Toast.makeText(this, "SERVIDOR", Toast.LENGTH_SHORT).show();
            buscaVisitasTask = new BuscaVisitasTask(this);
            buscaVisitasTask.delegate = this;
            buscaVisitasTask.execute(instanciaUnica.getToken());
        }else{
            Toast.makeText(this, "LOCAL", Toast.LENGTH_SHORT).show();
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            Cursor cursor = databaseHelper.getVisitas();
            while (cursor.moveToNext()){
                Visita visita = new Visita();
                visita.setCodVisita(cursor.getInt(cursor.getColumnIndex("COD_VISITA")));
                visita.setCodProf(cursor.getInt(cursor.getColumnIndex("COD_PROF")));
                visita.setCodPaciente(cursor.getInt(cursor.getColumnIndex("COD_PACIENTE")));
                visita.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
                visita.setNascimento(cursor.getString(cursor.getColumnIndex("IDADE")));
                visita.setSexo(cursor.getString(cursor.getColumnIndex("SEXO")));
                visita.setRua(cursor.getString(cursor.getColumnIndex("RUA")));
                visita.setNumero(cursor.getString(cursor.getColumnIndex("NUMERO")));
                visita.setBairro(cursor.getString(cursor.getColumnIndex("BAIRRO")));
                visita.setCidade(cursor.getString(cursor.getColumnIndex("CIDADE")));
                visita.setEstado(cursor.getString(cursor.getColumnIndex("ESTADO")));
                visita.setCep(cursor.getString(cursor.getColumnIndex("CEP")));
                visita.setLatitude(cursor.getDouble(cursor.getColumnIndex("LATITUDE")));
                visita.setLongitude(cursor.getDouble(cursor.getColumnIndex("LONGITUDE")));
                visita.setPatologias(cursor.getString(cursor.getColumnIndex("PATOLOGIAS")));
                visita.setDataHora(cursor.getString(cursor.getColumnIndex("DATA_HORA")));
                visita.setDataVisita(cursor.getString(cursor.getColumnIndex("DATA_VISITA")));
                visita.setStatus(cursor.getInt(cursor.getColumnIndex("STATUS")));
                visita.setAnotacoes(cursor.getString(cursor.getColumnIndex("ANOTACOES")));
                visita.setPrioridade(cursor.getInt(cursor.getColumnIndex("PRIORIDADE")));
                visitasServer.add(visita);
            }
            ordenaVisitas(visitasServer);
            adapter = new VisitaItemApapter(getApplicationContext(), visitasBairro);
            listVisitas.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        listVisitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (visitasServer.size() > 0){
                    instanciaUnica.setNomeBairro(visitasBairro.get(position).getNome());
                    Intent intent = new Intent(PrincipalActivity.this, PacientesBairroActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Configuracoes.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void retornaVisitas(List<Visita> visitas) {
        if (visitas.size() > 0){
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(SINCRONISMO, false);
            editor.commit();
            ordenaVisitas(visitas);
            adapter = new VisitaItemApapter(getApplicationContext(), visitasBairro);
            listVisitas.setAdapter(adapter);
            visitasServer = visitas;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void finalizaFeedBack(String resposta) {

    }

    public void ordenaVisitas(List<Visita> visitas) {
        String nomeTemp = "";
        for (int x = 0; x < visitas.size(); x++){
            int cont = 1;
            if (nomeTemp.equalsIgnoreCase(visitas.get(x).getBairro())){
                visitas.remove(x);
            }
            nomeTemp = visitas.get(x).getBairro();
            visitasBairro.add(new VisitaBairro(visitas.get(x).getBairro(), cont));
            for (int y = x+1; y < visitas.size(); y++){
                if (nomeTemp.equalsIgnoreCase(visitas.get(y).getBairro())){
                    cont++;
                    visitasBairro.get(x).setNumero(cont);
                }
            }
        }
        Comparador mComparador = new Comparador();
        Collections.sort(visitasBairro, mComparador);
    }

    public class Comparador implements java.util.Comparator {
        public int compare(Object o1, Object o2){
            VisitaBairro c1 = (VisitaBairro) o1;
            VisitaBairro c2 = (VisitaBairro) o2;

            return  c2.getNumero() - c1.getNumero();
        }
    }
}

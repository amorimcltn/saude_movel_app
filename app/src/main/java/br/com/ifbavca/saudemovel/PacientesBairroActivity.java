package br.com.ifbavca.saudemovel;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import br.com.ifbavca.saudemovel.classes.Paciente;
import br.com.ifbavca.saudemovel.classes.PacienteItemAdapter;
import br.com.ifbavca.saudemovel.dados.DatabaseHelper;

public class PacientesBairroActivity extends AppCompatActivity {

    private List<Paciente> listaPacientes = new ArrayList<>();
    private InstanciaUnica instanciaUnica = InstanciaUnica.getInstance();
    private ListView listViewPacientes;
    private PacienteItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes_bairro);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_statusbar));
            window.setNavigationBarColor(getResources().getColor(R.color.gray_bar));
            window.setTitle("");
        }

        TextView bairro = (TextView) findViewById(R.id.bairro_nome);
        bairro.setText(instanciaUnica.getNomeBairro());
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getPacientesByBairro(instanciaUnica.getNomeBairro());
        while (cursor.moveToNext()){
            Paciente paciente = new Paciente();
            paciente.setCod_paciente(cursor.getInt(cursor.getColumnIndex("COD_PACIENTE")));
            paciente.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
            paciente.setNascimento(Date.valueOf(cursor.getString(cursor.getColumnIndex("IDADE"))));
            paciente.setSexo(cursor.getString(cursor.getColumnIndex("SEXO")));
            paciente.setStatus(cursor.getInt(cursor.getColumnIndex("STATUS")));
            paciente.setPrioridade(cursor.getInt(cursor.getColumnIndex("PRIORIDADE")));
            listaPacientes.add(paciente);
        }
        listViewPacientes = (ListView) findViewById(R.id.listViewPacientes);
        adapter = new PacienteItemAdapter(this, listaPacientes);
        listViewPacientes.setAdapter(adapter);

        listViewPacientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                instanciaUnica.setIdpaciente(listaPacientes.get(position).getCod_paciente());
                PacientesBairroActivity.this.startActivity(new Intent(PacientesBairroActivity.this, PacienteDetalheActivity.class));
                finish();
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

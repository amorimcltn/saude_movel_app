package br.com.ifbavca.saudemovel.servico;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.ifbavca.saudemovel.ISincronizaAgendamentos;
import br.com.ifbavca.saudemovel.PrincipalActivity;
import br.com.ifbavca.saudemovel.classes.Visita;
import br.com.ifbavca.saudemovel.dados.DatabaseHelper;

/**
 * Created by Cleiton on 16/10/2015.
 */
public class BuscaVisitasTask extends AsyncTask<String, Void, List<Visita>> {

    private Activity context;
    public ISincronizaAgendamentos delegate = null;
    private ProgressDialog progressDialog;

    public BuscaVisitasTask(Activity context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sincronizando, aguarde...");
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected List<Visita> doInBackground(String... params) {
        List<Visita> visitas;
        FachadaSW fachadaSW = new FachadaSW();
        visitas = fachadaSW.getVisitas(params[0]);
        return visitas;
    }

    @Override
    protected void onPostExecute(List<Visita> visitas) {
        if (visitas.size() > 0){
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            ContentValues values = new ContentValues();
            databaseHelper.delete("VISITA");
            for (Visita v: visitas){
                values.put("COD_VISITA", v.getCodVisita());
                values.put("COD_PROF", v.getCodProf());
                values.put("COD_PACIENTE", v.getCodPaciente());
                values.put("NOME", v.getNome());
                values.put("IDADE", v.getNascimento());
                values.put("SEXO", v.getSexo());
                values.put("RUA", v.getRua());
                values.put("NUMERO", v.getNumero());
                values.put("BAIRRO", v.getBairro());
                values.put("CIDADE", v.getCidade());
                values.put("ESTADO", v.getEstado());
                values.put("CEP", v.getCep());
                values.put("LATITUDE", String.valueOf(v.getLatitude()));
                values.put("LONGITUDE", String.valueOf(v.getLongitude()));
                values.put("PATOLOGIAS", v.getPatologias());
                values.put("DATA_HORA", v.getDataHora());
                values.put("DATA_VISITA", v.getDataVisita());
                values.put("STATUS", v.getStatus());
                values.put("ANOTACOES", v.getAnotacoes());
                values.put("PRIORIDADE", v.getPrioridade());
                databaseHelper.salvaDados("VISITA", values);
                values.clear();
            }
            databaseHelper.close();
            delegate.retornaVisitas(visitas);
        }
        progressDialog.dismiss();
        super.onPostExecute(visitas);
    }
}

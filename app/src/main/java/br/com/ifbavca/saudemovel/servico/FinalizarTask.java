package br.com.ifbavca.saudemovel.servico;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.util.List;

import br.com.ifbavca.saudemovel.ISincronizaAgendamentos;
import br.com.ifbavca.saudemovel.classes.Visita;

/**
 * Created by Cleiton on 04/12/2015.
 */
public class FinalizarTask extends AsyncTask<List<Visita>, Void, String> {

    private Activity context;
    public ISincronizaAgendamentos delegate = null;
    private ProgressDialog progressDialog;

    public FinalizarTask(Activity context){
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
    protected String doInBackground(List<Visita>... params) {
        String res;
        FachadaSW fachadaSW = new FachadaSW();
        res = fachadaSW.finalizarVisitas(params[0]);
        return res;
    }

    @Override
    protected void onPostExecute(String s) {
        delegate.finalizaFeedBack(s);
        progressDialog.dismiss();
        super.onPostExecute(s);
    }
}

package br.com.ifbavca.saudemovel.servico;

/**
 * Created by Cleiton on 16/10/2015.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import br.com.ifbavca.saudemovel.InstanciaUnica;
import br.com.ifbavca.saudemovel.PrincipalActivity;

public class SincronizarDados extends AsyncTask<String, Void, String> {

    private Activity context;
    private InstanciaUnica instanciaUnica = InstanciaUnica.getInstance();
    private ProgressDialog progressDialog;
    private String usuario, senha;

    public SincronizarDados(Activity context){
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
    protected String doInBackground(String... login) {
        String token = "";
        usuario = login[0];
        senha = login[1];
        try {
            FachadaSW fachadaSW = new FachadaSW();
            token = fachadaSW.getToken(login[0], login[1]);
            Log.i("TOKEN", token);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SINCRONIA SERVIDOR", "Não foi possivel buscar dados no servidor!");
        }
        return token;
    }

    @Override
    protected void onPostExecute(String s) {
        if (!"".equalsIgnoreCase(s) && !"-1".equalsIgnoreCase(s)){
            progressDialog.dismiss();
            instanciaUnica.setToken(s);
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("TOKEN", s);
            editor.putString("USUARIO", usuario);
            editor.putString("SENHA", senha);
            editor.commit();
            Intent intent = new Intent(context, PrincipalActivity.class);
            context.startActivity(intent);
            context.finish();
        }else{
            progressDialog.dismiss();
            Toast.makeText(context, "CPF ou senha incorretos!", Toast.LENGTH_SHORT).show();
        }

        super.onPostExecute(s);
    }
}


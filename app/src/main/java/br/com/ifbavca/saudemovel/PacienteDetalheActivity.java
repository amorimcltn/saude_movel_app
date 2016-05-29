package br.com.ifbavca.saudemovel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.ifbavca.saudemovel.Utils.Utils;
import br.com.ifbavca.saudemovel.dados.DatabaseHelper;

public class PacienteDetalheActivity extends ActionBarActivity {

    private InstanciaUnica instanciaUnica = InstanciaUnica.getInstance();
    private Double latitude, longitude;
    private String patologias, observacoes;
    private int prior = 0;
    private SeekBar seekBar;
    private boolean flag;
    private Button btFinalizar = null;
    private TextView nomePac, idadePac, sexoPac, enderecoPac, patologiasPac, observacoesPac, prioridade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente_detalhe);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.gray_statusbar));
            window.setNavigationBarColor(getResources().getColor(R.color.gray_bar));
            window.setTitle("");
        }

        prioridade = (TextView) findViewById(R.id.prior);
        nomePac = (TextView) findViewById(R.id.nomePaciente);
        idadePac = (TextView) findViewById(R.id.idadePaciente);
        sexoPac = (TextView) findViewById(R.id.sexoPaciente);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        patologiasPac = (TextView) findViewById(R.id.patologiasPaciente);
        enderecoPac = (TextView) findViewById(R.id.enderecoPaciente);
        observacoesPac = (TextView) findViewById(R.id.observacoesPaciente);
        btFinalizar = (Button) findViewById(R.id.btfinalizar);
        Utils utils = new Utils();
        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.getPacienteById(instanciaUnica.getIdpaciente());
        while (cursor.moveToNext()){
            nomePac.setText(cursor.getString(cursor.getColumnIndex("NOME")));
            String data = cursor.getString(cursor.getColumnIndex("IDADE"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dataNascimento = null;
            try {
                dataNascimento = sdf.parse(data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            idadePac.setText(String.valueOf(utils.calculaIdade(dataNascimento)));
            if (cursor.getString(cursor.getColumnIndex("SEXO")).equalsIgnoreCase("M")){
                sexoPac.setText("Masculino");
            }else{
                sexoPac.setText("Feminino");
            }

            if (cursor.getInt(cursor.getColumnIndex("STATUS")) == 1){
                flag = false;
                btFinalizar.setText("REABRIR VISITA");
            } else {
                flag = true;
                btFinalizar.setText("FINALIZAR VISITA");
            }

            enderecoPac.setText(cursor.getString(cursor.getColumnIndex("RUA"))+
            ", "+ cursor.getString(cursor.getColumnIndex("NUMERO"))+
            " - "+ cursor.getString(cursor.getColumnIndex("BAIRRO"))+
            ", "+ cursor.getString(cursor.getColumnIndex("CIDADE"))+
            " - "+ cursor.getString(cursor.getColumnIndex("ESTADO"))+
            ", "+ cursor.getString(cursor.getColumnIndex("CEP")));
            patologiasPac.setText(cursor.getString(cursor.getColumnIndex("PATOLOGIAS")));
            patologias = cursor.getString(cursor.getColumnIndex("PATOLOGIAS"));
            if (cursor.getString(cursor.getColumnIndex("ANOTACOES")).equalsIgnoreCase("")){
                observacoesPac.setText("Ainda não há observações.");
            }else{
                observacoesPac.setText(cursor.getString(cursor.getColumnIndex("ANOTACOES")));
                observacoes = cursor.getString(cursor.getColumnIndex("ANOTACOES"));
            }
            latitude = cursor.getDouble(cursor.getColumnIndex("LATITUDE"));
            longitude = cursor.getDouble(cursor.getColumnIndex("LONGITUDE"));
            seekBar.setProgress(cursor.getInt(cursor.getColumnIndex("PRIORIDADE")));
            prior = cursor.getInt(cursor.getColumnIndex("PRIORIDADE"));
            prioridade.setText(String.valueOf(prior));
        }

        Button btRota = (Button) findViewById(R.id.btrota);
        btRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instanciaUnica.setLatitudeDest(latitude);
                instanciaUnica.setLongitudeDest(longitude);
                final LayoutInflater inflater = LayoutInflater.from(PacienteDetalheActivity.this);
                final View dialoglayout = inflater.inflate(R.layout.track_dialog, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(PacienteDetalheActivity.this);
                final AlertDialog alert = builder.create();
                ImageButton carButton = (ImageButton) dialoglayout.findViewById(R.id.imageCar);
                ImageButton manButton = (ImageButton) dialoglayout.findViewById(R.id.imageMan);
                carButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.cancel();
                        instanciaUnica.setMode("driving");
                        Intent intent = new Intent(PacienteDetalheActivity.this, MapsActivity.class);
                        if (intent != null){
                            PacienteDetalheActivity.this.startActivity(intent);
                        }
                    }
                });
                manButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.cancel();
                        instanciaUnica.setMode("walking");
                        Intent intent = new Intent(PacienteDetalheActivity.this, MapsActivity.class);
                        if (intent != null) {
                            PacienteDetalheActivity.this.startActivity(intent);
                        }
                    }
                });
                alert.setView(dialoglayout);
                alert.show();
            }
        });

        final Button btEdit = (Button) findViewById(R.id.btEdit);
        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = LayoutInflater.from(PacienteDetalheActivity.this);
                final View dialoglayout = inflater.inflate(R.layout.editinfodialog, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(PacienteDetalheActivity.this);
                final AlertDialog alert = builder.create();
                final EditText editPatologias = (EditText) dialoglayout.findViewById(R.id.editPatologias);
                editPatologias.setText(patologias);
                final EditText editObservacoes = (EditText) dialoglayout.findViewById(R.id.editObservacoes);
                editObservacoes.setText(observacoes);
                Button btSalvar = (Button) dialoglayout.findViewById(R.id.btSalvarInfo);
                btSalvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        patologias = editPatologias.getText().toString();
                        observacoes = editObservacoes.getText().toString();
                        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                        databaseHelper.upDateInfo(patologias, observacoes, instanciaUnica.getIdpaciente(), prior);
                        alert.cancel();
                        patologiasPac.setText(patologias);
                        observacoesPac.setText(observacoes);
                    }
                });
                alert.setView(dialoglayout);
                alert.show();
            }
        });

        btFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PacienteDetalheActivity.this);
                if (flag){
                    builder.setMessage("Finalizar visita?");
                }else{
                    builder.setMessage("Reabrir visita?");
                }

                builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (flag) {
                            databaseHelper.finalizaVisita(instanciaUnica.getIdpaciente(), 1, prior);
                            databaseHelper.close();
                            btFinalizar.setText("REABRIR VISITA");
                            flag = false;
                        } else {
                            databaseHelper.finalizaVisita(instanciaUnica.getIdpaciente(), 0, prior);
                            databaseHelper.close();
                            btFinalizar.setText("FINALIZAR VISITA");
                            flag = true;
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

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                prior = progress;
                prioridade.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
        startActivity(new Intent(this, PacientesBairroActivity.class));
        finish();
    }
}

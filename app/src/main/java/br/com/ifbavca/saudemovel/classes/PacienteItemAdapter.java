package br.com.ifbavca.saudemovel.classes;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.ifbavca.saudemovel.R;

/**
 * Created by Cleiton on 23/10/2015.
 */
public class PacienteItemAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Paciente> lista;
    private Activity context;

    public PacienteItemAdapter(Activity context, List<Paciente> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_paciente, null);
        Paciente paciente = lista.get(position);
        ImageView image = (ImageView) convertView.findViewById(R.id.imagePacienteItem);
        ImageView imageStatus = (ImageView) convertView.findViewById(R.id.statusItemPaciente);
        ImageView imageHome = (ImageView) convertView.findViewById(R.id.visitehome);
        TextView nome = (TextView) convertView.findViewById(R.id.nome_pac_item);
        TextView prioridade = (TextView) convertView.findViewById(R.id.prioridade);
        nome.setText(paciente.getNome());
        if ("M".equalsIgnoreCase(paciente.getSexo())) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.male));
        } else {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.female));
        }
        int prio = paciente.getPrioridade();
        if (prio >= 1 && prio <= 3){
            imageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.status_green));
            prioridade.setText("Prioridade baixa");
        }else{
            if (prio > 3 && prio <= 5){
                imageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.status_yellow));
                prioridade.setText("Prioridade média");
            }else{
                imageStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.status_red));
                prioridade.setText("Prioridade alta");
            }
        }

        if (paciente.getStatus() == 1) {
            imageHome.setVisibility(View.VISIBLE);
        }else{
            imageHome.setVisibility(View.INVISIBLE);
        }
            return convertView;
    }

    /**
     * Calcula a idade de acordo com a data passada.
     *
     * @param data
     * @return
     * @author Isaias Pfaffenseller
     */
    public static Integer getIdade(Date data) {
        Calendar dataNascimento = Calendar.getInstance();
        dataNascimento.setTime(data);
        Calendar dataAtual = Calendar.getInstance();

        Integer diferencaMes = dataAtual.get(Calendar.MONTH) - dataNascimento.get(Calendar.MONTH);
        Integer diferencaDia = dataAtual.get(Calendar.DAY_OF_MONTH) - dataNascimento.get(Calendar.DAY_OF_MONTH);
        Integer idade = (dataAtual.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR));

        if(diferencaMes < 0  || (diferencaMes == 0 && diferencaDia < 0)) {
            idade--;
        }

        return idade;
    }
}

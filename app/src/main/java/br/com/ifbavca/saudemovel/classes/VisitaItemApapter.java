package br.com.ifbavca.saudemovel.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.ifbavca.saudemovel.R;

/**
 * Created by Cleiton on 16/10/2015.
 */
public class VisitaItemApapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<VisitaBairro> listaVisitas;
    private Context context;

    public VisitaItemApapter(Context context, List<VisitaBairro> lista){
        listaVisitas = lista;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaVisitas.size();
    }

    @Override
    public Object getItem(int position) {
        return listaVisitas.get(position);
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
            convertView = inflater.inflate(R.layout.item_visita, null);

        VisitaBairro visita = listaVisitas.get(position);
        TextView bairro = (TextView) convertView.findViewById(R.id.nome_bairro);
        TextView numero = (TextView) convertView.findViewById(R.id.num_pacientes);
        bairro.setText(visita.getNome());
        numero.setText(String.valueOf(visita.getNumero()));
        return convertView;
    }
}

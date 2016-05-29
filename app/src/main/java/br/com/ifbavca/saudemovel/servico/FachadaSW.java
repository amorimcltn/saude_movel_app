package br.com.ifbavca.saudemovel.servico;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.ifbavca.saudemovel.classes.Visita;

public class FachadaSW {

    private static final String ENDERECO_SERVICO = "http://saudemovel.xp3.biz/saude-movel-sw/servico/servico.php";

    public String getToken(String cpf, String senha) throws Exception {
        String token = "";
        String[] json = new WebService().get(ENDERECO_SERVICO + "/autenticacao/" + cpf + "/" + senha);
        token = json[1];
        return token;
    }

    public List<Visita> getVisitas(String token){
        String[] json = new WebService().get(ENDERECO_SERVICO + "/visitas/" + token);
        ArrayList<Visita> visitas = new ArrayList<>();
        if (json[0].equals("200") && json[1].length() > 4) {
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(json[1]).getAsJsonArray();
            Iterator iterator = jsonArray.iterator();

            while (iterator.hasNext()) {
                JsonElement json2 = (JsonElement) iterator.next();
                Gson gson = new Gson();
                Visita visita = gson.fromJson(json2, Visita.class);
                visitas.add(visita);
            }

            return visitas;
        } else {
            return visitas;
        }
    }

    public String finalizarVisitas(List<Visita> visitas){
        Gson gson = new Gson();
        String visitasJSON = gson.toJson(visitas);
        try {
            visitasJSON = URLEncoder.encode(visitasJSON, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] json = new WebService().get(ENDERECO_SERVICO + "/finalizar/" + visitasJSON);
        String res = json[1];
        Log.i("FIM", res);
        return res;
    }

}

package br.com.ifbavca.saudemovel;

import java.util.List;

import br.com.ifbavca.saudemovel.classes.Visita;

/**
 * Created by Cleiton on 16/10/2015.
 */
public interface ISincronizaAgendamentos {

    void retornaVisitas(List<Visita> visitas);
    void finalizaFeedBack(String resposta);

}

package br.com.ifbavca.saudemovel.classes;

import java.sql.Date;

/**
 * Created by Cleiton on 16/10/2015.
 */
public class Visita {

    private Integer cod_visita;
    private Integer cod_prof;
    private Integer cod_paciente;
    private String nome;
    private String nascimento;
    private String sexo;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private Double longitude;
    private Double latitude;
    private String patologias;
    private String data_hora;
    private String data_visita;
    private int status;
    private String anotacoes;
    private int prioridade;

    public Integer getCodVisita() {
        return cod_visita;
    }

    public void setCodVisita(Integer cod_visita) {
        this.cod_visita = cod_visita;
    }

    public Integer getCodProf() {
        return cod_prof;
    }

    public void setCodProf(Integer cod_prof) {
        this.cod_prof = cod_prof;
    }

    public Integer getCodPaciente() {
        return cod_paciente;
    }

    public void setCodPaciente(Integer cod_paciente) {
        this.cod_paciente = cod_paciente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getPatologias() {
        return patologias;
    }

    public void setPatologias(String patologias) {
        this.patologias = patologias;
    }

    public String getDataHora() {
        return data_hora;
    }

    public void setDataHora(String data_hora) {
        this.data_hora = data_hora;
    }

    public String getDataVisita() {
        return data_visita;
    }

    public void setDataVisita(String data_visita) {
        this.data_visita = data_visita;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }
}

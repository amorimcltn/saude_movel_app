package br.com.ifbavca.saudemovel;

import java.util.ArrayList;
import java.util.List;

import br.com.ifbavca.saudemovel.classes.Visita;

/**
 * Created by Cleiton on 16/10/2015.
 */
public class InstanciaUnica {

    private static InstanciaUnica ourInstance;
    private int idpaciente;
    private String token;
    private String nomeBairro;
    private String mode;
    private Double longitude, latitude, latitudeDest, longitudeDest;

    public synchronized static InstanciaUnica getInstance() {
        if (ourInstance == null){
            ourInstance = new InstanciaUnica();
        }
        return ourInstance;
    }

    private InstanciaUnica() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNomeBairro() {
        return nomeBairro;
    }

    public void setNomeBairro(String nomeBairro) {
        this.nomeBairro = nomeBairro;
    }

    public int getIdpaciente() {
        return idpaciente;
    }

    public void setIdpaciente(int idpaciente) {
        this.idpaciente = idpaciente;
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

    public Double getLatitudeDest() {
        return latitudeDest;
    }

    public void setLatitudeDest(Double latitudeDest) {
        this.latitudeDest = latitudeDest;
    }

    public Double getLongitudeDest() {
        return longitudeDest;
    }

    public void setLongitudeDest(Double longitudeDest) {
        this.longitudeDest = longitudeDest;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}

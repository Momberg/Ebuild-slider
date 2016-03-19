package tcc.ebuild_slider;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by momberg on 01/03/16.
 */
public class Obra {

    LatLng lat_lng;
    int id;
    double lat = 0, lng = 0;
    String nome= "";
    String TipoFase;
    String fase;
    String data = "";
    String rua = "";
    String bairro = "";
    String cidade = "";
    boolean marker = false;

    public void setObra(String nome, String data, String tipoFase, String fase, Double lat, Double lng){
        setNome(nome);
        setData(data);
        setTipoFase(tipoFase);
        setFase(fase);
        setLat(lat);
        setLng(lng);
    }

    public void setObraOnClick(String nome, String data, String rua, String bairro, String cidade, String tipoFase, String fase, Double lat, Double lng){
        setNome(nome);
        setData(data);
        setRua(rua);
        setBairro(bairro);
        setCidade(cidade);
        setTipoFase(tipoFase);
        setFase(fase);
        setLat(lat);
        setLng(lng);
    }

    public int getID(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoFase() {
        return TipoFase;
    }

    public void setTipoFase(String tipoFase) {
        TipoFase = tipoFase;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getLat(){
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng(){
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean getMarker(){
        return marker;
    }

    public void setMarker(boolean marker) {
        this.marker = marker;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
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

}

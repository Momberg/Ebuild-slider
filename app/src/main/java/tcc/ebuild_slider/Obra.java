package tcc.ebuild_slider;

public class Obra {

    int id;
    String lat = "", lng = "";
    String nome= "";
    String TipoFase;
    String fase;
    String data = "";
    String rua = "";
    String bairro = "";
    String cidade = "";
    boolean marker = false;

    public void setObra(String nome, String data, String tipoFase, String fase, String lat, String lng){
        setNome(nome);
        setData(data);
        setTipoFase(tipoFase);
        setFase(fase);
        setLat(lat);
        setLng(lng);
    }

    public void setObraOnClick(String nome, String data, String rua, String bairro, String cidade, String tipoFase, String fase, String lat, String lng){
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

    public void edit_obra(String nome, String data, String rua, String bairro, String cidade, String tipoFase, String fase){
        setNome(nome);
        setData(data);
        setRua(rua);
        setBairro(bairro);
        setCidade(cidade);
        setTipoFase(tipoFase);
        setFase(fase);
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

    public String getLat(){
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng(){
        return lng;
    }

    public void setLng(String lng) {
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

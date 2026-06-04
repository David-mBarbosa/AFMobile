package com.example.afmobile;

public class LivroGeolocalizado {
    private String id; // ID do documento no Firebase
    private String titulo;
    private String autores;
    private String anoPublicacao;
    private String editora;
    private double latitude;
    private double longitude;
    private String situacao; // biblioteca, livraria, viagem, etc.
    private String statusLeitura; // Quero ler, Lendo, Concluido
    private String observacao;

    // Construtor vazio necessário para o Firebase
    public LivroGeolocalizado() {}

    public LivroGeolocalizado(String titulo, String autores, String anoPublicacao, String editora,
                              double latitude, double longitude, String situacao, String statusLeitura, String observacao) {
        this.titulo = titulo;
        this.autores = autores;
        this.anoPublicacao = anoPublicacao;
        this.editora = editora;
        this.latitude = latitude;
        this.longitude = longitude;
        this.situacao = situacao;
        this.statusLeitura = statusLeitura;
        this.observacao = observacao;
    }

    // Getters e Setters para todos os campos (Obrigatório para o Firebase)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public String getAutores() { return autores; }
    public String getAnoPublicacao() { return anoPublicacao; }
    public String getEditora() { return editora; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getSituacao() { return situacao; }
    public String getStatusLeitura() { return statusLeitura; }
    public String getObservacao() { return observacao; }
    public void setStatusLeitura(String statusLeitura) { this.statusLeitura = statusLeitura; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
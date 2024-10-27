package com.example.biblioteca_repaso.classes;

public class Libro {
    private String titulo;
    private String autor;
    private int ano_publicacion;
    private boolean disponible;
    private String genero;

    public Libro(String titulo, String autor, int ano_publicacion, boolean disponible, String genero) {
        this.titulo = titulo;
        this.autor = autor;
        this.ano_publicacion = ano_publicacion;
        this.disponible = disponible;
        this.genero = genero;
    }

    public Libro(String titulo, String autor, boolean disponible){
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = disponible;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAno_publicacion() {
        return ano_publicacion;
    }

    public void setAno_publicacion(int ano_publicacion) {
        this.ano_publicacion = ano_publicacion;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "titulo='" + titulo + '\'' +
                ", autor=" + autor +
                ", ano_publicacion=" + ano_publicacion +
                ", disponible=" + disponible +
                ", genero='" + genero + '\'' +
                '}';
    }
}

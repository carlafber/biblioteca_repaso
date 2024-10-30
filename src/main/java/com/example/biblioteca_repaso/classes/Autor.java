package com.example.biblioteca_repaso.classes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Autor {
    private String nombre;
    private String nacionalidad;
    private LocalDate fecha_nacimiento;
    private List<String> generos;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Autor(String nombre, String nacionalidad, LocalDate fecha_nacimiento, List<String> generos) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.fecha_nacimiento = fecha_nacimiento;
        this.generos = generos;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public void setGeneros(List<String> generos) {
        this.generos = generos;
    }

    //serializar LocalDate a String
    public String getFecha_nacimiento_string() {
        return fecha_nacimiento != null ? fecha_nacimiento.format(DATE_FORMATTER) : null;
    }

    public String getGenerosString() {
        return String.join(", ", generos);
    }


    @Override
    public String toString() {
        return "Autor{" +
                "nombre='" + nombre + '\'' +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", fecha_nacimiento=" + getFecha_nacimiento_string() +
                ", generos=" + generos.toString() +
                '}';
    }
}

package com.example.biblioteca_repaso.classes;

import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Prestamo {
    private ObjectId id;
    private String libro;
    private Usuario usuario;
    private LocalDate fecha_prestamo;
    private LocalDate fecha_devolucion;
    private boolean devuelto;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Prestamo(String libro, Usuario usuario, LocalDate fecha_prestamo, LocalDate fecha_devolucion, boolean devuelto) {
        this.libro = libro;
        this.usuario = usuario;
        this.fecha_prestamo = fecha_prestamo;
        this.fecha_devolucion = fecha_devolucion;
        this.devuelto = devuelto;
    }

    public Prestamo(ObjectId id, String libro, Usuario usuario, LocalDate fecha_prestamo, LocalDate fecha_devolucion, boolean devuelto) {
        this.id = id;
        this.libro = libro;
        this.usuario = usuario;
        this.fecha_prestamo = fecha_prestamo;
        this.fecha_devolucion = fecha_devolucion;
        this.devuelto = devuelto;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFecha_prestamo() {
        return fecha_prestamo;
    }

    public void setFecha_prestamo(LocalDate fecha_prestamo) {
        this.fecha_prestamo = fecha_prestamo;
    }

    public LocalDate getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(LocalDate fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }

    public String getFecha_devolucion_string() {
        return fecha_devolucion != null ? fecha_devolucion.format(DATE_FORMATTER) : null;
    }

    public String getFecha_prestamo_string() {
        return fecha_prestamo != null ? fecha_prestamo.format(DATE_FORMATTER) : null;
    }

    @Override
    public String toString() {
        return "\"" + libro + "\", new Usuario(\"" + usuario.getNombre() + "\", \"" + usuario.getEmail() +
                "\"), LocalDate.parse(\"" + getFecha_prestamo() + "\", DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")), LocalDate.parse(\"" +
                getFecha_devolucion() + "\", DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")), " + devuelto;

        /*return "Prestamo{" +
                "libro=" + libro +
                ", usuario=" + usuario +
                ", fechaPrestamo=" + getFecha_prestamo_string() +
                ", fechaDevolucion=" + getFecha_devolucion_string() +
                ", devuelto=" + devuelto +
                '}';*/
    }
}

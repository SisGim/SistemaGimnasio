package models;

import java.time.LocalDateTime;

public class Asistencia {
    private int id;
    private String correoCliente;
    private LocalDateTime fechaHora;

    public Asistencia(int id, String correoCliente, LocalDateTime fechaHora) {
        this.id = id;
        this.correoCliente = correoCliente;
        this.fechaHora = fechaHora;
    }

    public Asistencia(String correoCliente, LocalDateTime fechaHora) {
        this(-1, correoCliente, fechaHora);
    }

    public int getId() {
        return id;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
}

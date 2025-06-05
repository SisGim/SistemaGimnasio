package models;

import javafx.beans.property.*;

public class HistorialMembresia {

    private final IntegerProperty idCliente = new SimpleIntegerProperty();
    private final StringProperty clienteEmail = new SimpleStringProperty();
    private final StringProperty tipoMembresia = new SimpleStringProperty();
    private final StringProperty fechaInicio = new SimpleStringProperty();
    private final StringProperty fechaFin = new SimpleStringProperty();
    private final DoubleProperty valorTotal = new SimpleDoubleProperty();

    public HistorialMembresia() {
        // Constructor vacío para uso con setters
    }

    public HistorialMembresia(int idCliente, String clienteEmail, String tipoMembresia, String fechaInicio, String fechaFin, double valorTotal) {
        this.idCliente.set(idCliente);
        this.clienteEmail.set(clienteEmail);
        this.tipoMembresia.set(tipoMembresia);
        this.fechaInicio.set(fechaInicio);
        this.fechaFin.set(fechaFin);
        this.valorTotal.set(valorTotal);
    }

    // Getters
    public int getIdCliente() {
        return idCliente.get();
    }

    public String getClienteEmail() {
        return clienteEmail.get();
    }

    public String getTipoMembresia() {
        return tipoMembresia.get();
    }

    public String getFechaInicio() {
        return fechaInicio.get();
    }

    public String getFechaFin() {
        return fechaFin.get();
    }

    public double getPrecio() {
        return valorTotal.get();
    }

    // Setters
    public void setIdCliente(int value) {
        this.idCliente.set(value);
    }

    public void setClienteEmail(String value) {
        this.clienteEmail.set(value);
    }

    public void setTipoMembresia(String value) {
        this.tipoMembresia.set(value);
    }

    public void setFechaInicio(String value) {
        this.fechaInicio.set(value);
    }

    public void setFechaFin(String value) {
        this.fechaFin.set(value);
    }

    public void setPrecio(double value) {
        this.valorTotal.set(value);
    }

    // Properties (para JavaFX TableView, etc.)
    public IntegerProperty idClienteProperty() {
        return idCliente;
    }

    public StringProperty clienteEmailProperty() {
        return clienteEmail;
    }

    public StringProperty tipoMembresiaProperty() {
        return tipoMembresia;
    }

    public StringProperty fechaInicioProperty() {
        return fechaInicio;
    }

    public StringProperty fechaFinProperty() {
        return fechaFin;
    }

    public DoubleProperty valorTotalProperty() {
        return valorTotal;
    }
}

package models;

import javafx.beans.property.*;

public class Membresia {

    private final IntegerProperty id;
    private final StringProperty tipo;          // Tipo de membresía
    private final IntegerProperty duracion;     // Duración en días
    private final DoubleProperty precioBase;    // Precio base por día

    // Constructor sin duración (para membresías globales)
    public Membresia(int id, String tipo, double precioBase) {
        this.id = new SimpleIntegerProperty(id);
        this.tipo = new SimpleStringProperty(tipo);
        this.precioBase = new SimpleDoubleProperty(precioBase);
        this.duracion = new SimpleIntegerProperty(1); // Valor por defecto para evitar nulls
    }

    // Constructor completo con duración personalizada
    public Membresia(int id, String tipo, double precioBase, int duracion) {
        this.id = new SimpleIntegerProperty(id);
        this.tipo = new SimpleStringProperty(tipo);
        this.precioBase = new SimpleDoubleProperty(precioBase);
        this.duracion = new SimpleIntegerProperty(duracion);
    }

    // Getters
    public int getId() {
        return id.get();
    }

    public String getTipo() {
        return tipo.get();
    }

    public int getDuracion() {
        return duracion.get();
    }

    public double getPrecioBase() {
        return precioBase.get();
    }

    // Setters
    public void setId(int id) {
        this.id.set(id);
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    public void setDuracion(int duracion) {
        this.duracion.set(duracion);
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase.set(precioBase);
    }

    // Propiedades JavaFX (para TableView)
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    public IntegerProperty duracionProperty() {
        return duracion;
    }

    public DoubleProperty precioBaseProperty() {
        return precioBase;
    }

    // Método para calcular el precio total
    public double calcularPrecio() {
        return getPrecioBase() * getDuracion();
    }
}

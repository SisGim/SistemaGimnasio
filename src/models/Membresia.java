package models;

import javafx.beans.property.*;

public class Membresia {

    private final IntegerProperty id;
    private final StringProperty tipo;          // Tipo de membresía
    private final IntegerProperty duracion;     // Duración en días
    private final DoubleProperty precioBase;    // Precio base por día

    // Constructor sin duración (por compatibilidad)
    public Membresia(int id, String tipo, double precioBase) {
        this.id = new SimpleIntegerProperty(id);
        this.tipo = new SimpleStringProperty(tipo);
        this.precioBase = new SimpleDoubleProperty(precioBase);
        this.duracion = new SimpleIntegerProperty(1); // Valor por defecto
    }

    // Constructor completo
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

    // ✅ Nuevo método para evitar error en ClienteUI
    public double getPrecio() {
        return calcularPrecio();
    }

    // Duración estimada en meses
    public int getDuracionMeses() {
        return Math.max(1, getDuracion() / 30); // Al menos 1 mes
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

    // Propiedades JavaFX
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

    // Cálculo total del precio
    public double calcularPrecio() {
        return getPrecioBase() * getDuracion();
    }

    // Mostrar en ComboBox
    @Override
    public String toString() {
        return getTipo() + " - $" + getPrecioBase() + " por " + getDuracion() + " día(s)";
    }
}

package models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Membresia {
    private SimpleIntegerProperty id;
    private SimpleStringProperty tipo; // tipo de membresia
    private SimpleIntegerProperty duracion; // duración en dias específica para cada cliente
    private SimpleDoubleProperty precioBase; // precio base de la membresía

    // Constructor
    public Membresia(int id, String tipo, double precioBase) {
        this.id = new SimpleIntegerProperty(id);
        this.tipo = new SimpleStringProperty(tipo);
        this.precioBase = new SimpleDoubleProperty(precioBase);
    }

    // Constructor para la membresia de un cliente
    public Membresia(int id, String tipo, double precioBase, int duracion) {
        this(id, tipo, precioBase);
        this.duracion = new SimpleIntegerProperty(duracion);
    }
    
        public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public double getPrecioBase() {
        return precioBase.get();
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase.set(precioBase);
    }

    public String getTipo() {
        return tipo.get();
    }

    public void setTipo(String tipo) { 
        this.tipo.set(tipo);
    }

    public int getDuracion() {
        return duracion.get();
    }

    public void setDuracion(int duracion) {
        this.duracion.set(duracion);
    }
  
    public double calcularPrecio() {
        return precioBase.get() * duracion.get(); // calcula el precio segun la duración
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleDoubleProperty precioBaseProperty() {
        return precioBase;
    }

    public SimpleStringProperty tipoProperty() {
        return tipo;
    }

    public SimpleIntegerProperty duracionProperty() {
        return duracion;
    }
}

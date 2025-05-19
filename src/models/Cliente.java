package models;

import javafx.beans.property.*;

public class Cliente {
    private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty telefono;
    private final StringProperty email;
    private final StringProperty membresia;
    private final StringProperty identificacion;

    // 📌 Constructor con identificación
    public Cliente(int id, String nombre, String telefono, String email, String membresia, String identificacion) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.telefono = new SimpleStringProperty(telefono);
        this.email = new SimpleStringProperty(email);
        this.membresia = new SimpleStringProperty(membresia);
        this.identificacion = new SimpleStringProperty(identificacion);
    }

    // 📌 Constructor sin identificación (opcional para compatibilidad)
    public Cliente(int id, String nombre, String telefono, String email, String membresia) {
        this(id, nombre, telefono, email, membresia, "");
    }

    // 📌 Getters y Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public StringProperty nombreProperty() { return nombre; }

    public String getTelefono() { return telefono.get(); }
    public void setTelefono(String telefono) { this.telefono.set(telefono); }
    public StringProperty telefonoProperty() { return telefono; }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    public String getMembresia() { return membresia.get(); }
    public void setMembresia(String membresia) { this.membresia.set(membresia); }
    public StringProperty membresiaProperty() { return membresia; }

    public String getIdentificacion() { return identificacion.get(); }
    public void setIdentificacion(String identificacion) { this.identificacion.set(identificacion); }
    public StringProperty identificacionProperty() { return identificacion; }
}

package models;
import javafx.beans.property.*;

public class Maquina {

    private final LongProperty id;
    private final StringProperty nombre;
    private final StringProperty grupoMuscular;
    private final StringProperty ubicacion;
    private final BooleanProperty enUso;
    //Constructor Vacío
    public Maquina() {
        this(0L, "", "", "", false);
    }
    //Constructor
    public Maquina(long id, String nombre, String grupoMuscular, String ubicacion, boolean enUso) {
        this.id = new SimpleLongProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.grupoMuscular = new SimpleStringProperty(grupoMuscular);
        this.ubicacion = new SimpleStringProperty(ubicacion);
        this.enUso = new SimpleBooleanProperty(enUso);
    }

    // Getters y Setters
    public long getId() {
        return id.get();
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getGrupoMuscular() {
        return grupoMuscular.get();
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular.set(grupoMuscular);
    }

    public String getUbicacion() {
        return ubicacion.get();
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion.set(ubicacion);
    }

    public boolean isEnUso() {
        return enUso.get();
    }

    public void setEnUso(boolean enUso) {
        this.enUso.set(enUso);
    }

    //Property para TableView
    public LongProperty idProperty() {
        return id;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public StringProperty grupoMuscularProperty() {
        return grupoMuscular;
    }

    public StringProperty ubicacionProperty() {
        return ubicacion;
    }

    public BooleanProperty enUsoProperty() {
        return enUso;
    }
}


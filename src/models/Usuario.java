package models;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String rol;
    private String password;
    private String telefono;
    private String identificacion;

    public Usuario(int id, String nombre, String email, String rol, String password, String telefono, String identificacion) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.password = password;
        this.telefono = telefono;
        this.identificacion = identificacion;
    }

    // Constructor sin teléfono e identificación (por compatibilidad si es necesario)
    public Usuario(int id, String nombre, String email, String rol, String password) {
        this(id, nombre, email, rol, password, null, null);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }

    public String getPassword() {
        return password;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }
}

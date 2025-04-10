package database;

import models.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public static void agregarCliente(Cliente cliente) {
        int nuevoID = obtenerMenorIDDisponible(); //Obtiene el ID más bajo globalmente, incluyendo 1 si está disponible
        //Sintaxis de la consulta para insertar un cliente
        String sql = "INSERT INTO clientes (id, nombre, telefono, email, membresia) VALUES (?, ?, ?, ?, ?)"; //Insertar información del cliente a la BDD
        //Conexión con la BBD por medio de JDBC
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, nuevoID);
            pstmt.setString(2, cliente.getNombre());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getEmail());
            pstmt.setString(5, cliente.getMembresia());
            pstmt.executeUpdate();
            //Confirmación de la adición del cliente
            System.out.println("Cliente agregado con ID: " + nuevoID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Consulta la lista de Clientes del Gimnasio
    public static List<Cliente> obtenerClientes() {
        List<Cliente> clientes = new ArrayList<>();
        //Sintaxis de la consulta para capturar a todos los clientes
        String sql = "SELECT * FROM clientes";
        //Conexión con la BDD
        try (Connection conn = Database.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            //Mientras haya un registro después del actual, captura los datos del cliente
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("membresia")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public static void actualizarCliente(Cliente cliente) {
        //Sintaxis de la Consulta para actualizar Cliente
        String sql = "UPDATE clientes SET nombre=?, telefono=?, email=?, membresia=? WHERE id=?";
        //Conexión con la BDD y cambia los datos del cliente
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getTelefono());
            pstmt.setString(3, cliente.getEmail());
            pstmt.setString(4, cliente.getMembresia());
            pstmt.setInt(5, cliente.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Función para eliminar un cliente de la lista
    public static void eliminarCliente(int id, String rolUsuario) {
        //Restricción de rol (solo si el rol es administrador puede ejecutar esta opción)
        if (!"Administrador".equals(rolUsuario)) {
            System.out.println("Acceso denegado: Solo los administradores pueden eliminar clientes.");
            return;
        }
        //Sintaxis de la Consulta
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            //Ejecuta un prepared statement para buscar el id insertado por el usuario en una variable llamada filasAfectadas
            int filasAfectadas = pstmt.executeUpdate();
            //Función de búsqueda según las filasAfectadas
            if (filasAfectadas > 0) {
                System.out.println("Cliente con ID " + id + " eliminado correctamente.");
            } else {
                System.out.println("No se encontró el cliente con ID " + id + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int obtenerMenorIDDisponible() {
        //Primero, se verifica si el ID 1 está libre
        String sqlCheckID1 = "SELECT COUNT(*) AS count FROM clientes WHERE id = 1";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sqlCheckID1); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next() && rs.getInt("count") == 0) {
                return 1; //Si el ID 1 no está ocupado, se usa
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si el ID 1 está ocupado, se usa el siguiente ID menor disponible
        String sqlFindMin = "SELECT MIN(t1.id + 1) AS nextID "
                + "FROM clientes t1 "
                + "LEFT JOIN clientes t2 ON t1.id + 1 = t2.id "
                + "WHERE t2.id IS NULL";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sqlFindMin); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next() && rs.getInt("nextID") > 0) {
                return rs.getInt("nextID"); //Usa el menor ID disponible en la tabla
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Si no hay espacios disponibles, se usa el siguiente ID mayor
        return obtenerMaxID() + 1;
    }

    //Método para obtener el ID máximo actual en la tabla
    public static int obtenerMaxID() {
        //Sintaxis de la consulta para obtener el ID máximo actual en la tabla
        String sql = "SELECT COALESCE(MAX(id), 0) AS maxID FROM clientes";
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            //Ejecutar la consulta hasta que encuentre el ID máximo actual en la tabla 
            if (rs.next()) {
                return rs.getInt("maxID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; //Si la tabla está vacía, empieza en 1
    }

}

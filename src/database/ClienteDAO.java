package database;

import models.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public static void agregarCliente(Cliente cliente) {
        int nuevoID = obtenerMenorIDDisponible();
        String sql = "INSERT INTO clientes (id, nombre, telefono, email, membresia, identificacion) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, nuevoID);
            pstmt.setString(2, cliente.getNombre());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getEmail());
            pstmt.setString(5, cliente.getMembresia());
            pstmt.setString(6, cliente.getIdentificacion());
            pstmt.executeUpdate();
            System.out.println("✅ Cliente agregado con ID: " + nuevoID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Cliente> obtenerClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection conn = Database.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("membresia"),
                        rs.getString("identificacion")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public static void actualizarCliente(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre=?, telefono=?, email=?, membresia=?, identificacion=? WHERE id=?";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getTelefono());
            pstmt.setString(3, cliente.getEmail());
            pstmt.setString(4, cliente.getMembresia());
            pstmt.setString(5, cliente.getIdentificacion());
            pstmt.setInt(6, cliente.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean actualizarClientePorCorreo(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre = ?, telefono = ?, identificacion = ? WHERE email = ?";
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getTelefono());
            pstmt.setString(3, cliente.getIdentificacion());
            pstmt.setString(4, cliente.getEmail());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Cliente obtenerClientePorCorreo(String correo) {
        String sql = "SELECT * FROM clientes WHERE email = ?";
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("membresia"),
                        rs.getString("identificacion")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean existeClientePorCorreo(String correo) {
        String sql = "SELECT COUNT(*) FROM clientes WHERE email = ?";
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void eliminarCliente(int id, String rolUsuario) {
        if (!"Administrador".equals(rolUsuario)) {
            System.out.println("⛔ Acceso denegado: Solo los administradores pueden eliminar clientes.");
            return;
        }

        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("🗑️ Cliente con ID " + id + " eliminado correctamente.");
            } else {
                System.out.println("⚠️ No se encontró el cliente con ID " + id + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int obtenerMenorIDDisponible() {
        String sqlCheckID1 = "SELECT COUNT(*) AS count FROM clientes WHERE id = 1";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sqlCheckID1); ResultSet rs = pstmt.executeQuery()) {
            if (rs.next() && rs.getInt("count") == 0) {
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sqlFindMin = "SELECT MIN(t1.id + 1) AS nextID " +
                "FROM clientes t1 " +
                "LEFT JOIN clientes t2 ON t1.id + 1 = t2.id " +
                "WHERE t2.id IS NULL";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sqlFindMin); ResultSet rs = pstmt.executeQuery()) {
            if (rs.next() && rs.getInt("nextID") > 0) {
                return rs.getInt("nextID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return obtenerMaxID() + 1;
    }

    public static int obtenerMaxID() {
        String sql = "SELECT COALESCE(MAX(id), 0) AS maxID FROM clientes";
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("maxID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

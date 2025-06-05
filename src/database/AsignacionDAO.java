package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDAO {

    /**
     * Asigna un cliente a un entrenador, si no está ya asignado.
     * @param correoEntrenador correo del entrenador
     * @param correoCliente correo del cliente
     * @return true si fue exitoso, false si ya estaba asignado o hubo error
     */
    public static boolean asignarCliente(String correoEntrenador, String correoCliente) {
        if (yaAsignado(correoEntrenador, correoCliente)) {
            return false;
        }

        String sql = "INSERT INTO asignaciones (ENTRENADOR_EMAIL, CLIENTE_EMAIL) VALUES (?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correoEntrenador);
            pstmt.setString(2, correoCliente);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si ya existe una asignación entre entrenador y cliente.
     */
    private static boolean yaAsignado(String correoEntrenador, String correoCliente) {
        String sql = "SELECT COUNT(*) FROM asignaciones WHERE ENTRENADOR_EMAIL = ? AND CLIENTE_EMAIL = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correoEntrenador);
            pstmt.setString(2, correoCliente);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Conservador: si hay error, asumimos que ya está asignado
        }
    }

    /**
     * Devuelve la lista de correos de clientes asignados a un entrenador.
     */
    public static List<String> obtenerClientesAsignados(String correoEntrenador) {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT CLIENTE_EMAIL FROM asignaciones WHERE ENTRENADOR_EMAIL = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correoEntrenador);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("CLIENTE_EMAIL"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Permite eliminar una asignación específica.
     */
    public static boolean eliminarAsignacion(String correoEntrenador, String correoCliente) {
        String sql = "DELETE FROM asignaciones WHERE ENTRENADOR_EMAIL = ? AND CLIENTE_EMAIL = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correoEntrenador);
            pstmt.setString(2, correoCliente);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

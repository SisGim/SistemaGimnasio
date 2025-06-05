package database;

import models.HistorialMembresia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistorialMembresiaDAO {

    public static List<HistorialMembresia> obtenerHistorial() {
        List<HistorialMembresia> historial = new ArrayList<>();
        String sql = "SELECT * FROM historial_membresias ORDER BY fecha_inicio DESC";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                HistorialMembresia h = new HistorialMembresia();
                h.setIdCliente(rs.getInt("id_cliente")); // ✅ CORREGIDO
                h.setClienteEmail(rs.getString("cliente_email"));
                h.setTipoMembresia(rs.getString("tipo_membresia"));
                h.setFechaInicio(rs.getString("fecha_inicio"));
                h.setFechaFin(rs.getString("fecha_fin"));
                h.setPrecio(rs.getDouble("valor_total"));
                historial.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historial;
    }

    public static List<HistorialMembresia> obtenerHistorialPorIdCliente(int idClienteBuscado) {
        List<HistorialMembresia> historial = new ArrayList<>();
        String sql = "SELECT * FROM historial_membresias WHERE id_cliente = ? ORDER BY fecha_inicio DESC"; // ✅ CORREGIDO

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idClienteBuscado);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                HistorialMembresia h = new HistorialMembresia();
                h.setIdCliente(rs.getInt("id_cliente")); // ✅ CORREGIDO
                h.setClienteEmail(rs.getString("cliente_email"));
                h.setTipoMembresia(rs.getString("tipo_membresia"));
                h.setFechaInicio(rs.getString("fecha_inicio"));
                h.setFechaFin(rs.getString("fecha_fin"));
                h.setPrecio(rs.getDouble("valor_total"));
                historial.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historial;
    }

    public static boolean insertarHistorialMembresia(HistorialMembresia registro) {
        String sql = "INSERT INTO historial_membresias (id_cliente, cliente_email, tipo_membresia, fecha_inicio, fecha_fin, valor_total) VALUES (?, ?, ?, ?, ?, ?)"; // ✅ CORREGIDO

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, registro.getIdCliente());
            pstmt.setString(2, registro.getClienteEmail());
            pstmt.setString(3, registro.getTipoMembresia());
            pstmt.setString(4, registro.getFechaInicio());
            pstmt.setString(5, registro.getFechaFin());
            pstmt.setDouble(6, registro.getPrecio());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

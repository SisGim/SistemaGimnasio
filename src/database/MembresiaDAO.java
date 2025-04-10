package database;

import models.Membresia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembresiaDAO {

    public static List<Membresia> obtenerMembresias() {
        List<Membresia> membresias = new ArrayList<>();
        String sql = "SELECT * FROM catalogo_membresias";

        try (Connection conn = Database.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Membresia membresia = new Membresia(
                        rs.getInt("id"),
                        rs.getString("tipo"),
                        rs.getDouble("precio_base")
                );
                membresias.add(membresia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membresias;
    }

    // Obtener una especifica
   public static Membresia obtenerMembresiaPorTipo(String tipo) {
    String sql = "SELECT * FROM catalogo_membresias WHERE tipo = ?";

    try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, tipo);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return new Membresia(
                    rs.getInt("id"),
                    rs.getString("tipo"),
                    rs.getDouble("precio_base")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    // Agregar membresia
    public static void agregarMembresia(Membresia membresia) {
        String sql = "INSERT INTO catalogo_membresias (tipo, precio_base) VALUES (?, ?)";

       try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, membresia.getTipo());
            pstmt.setDouble(2, membresia.getPrecioBase());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Actualizar una membresia
    public static void actualizarMembresia(Membresia membresia) {
        String sql = "UPDATE catalogo_membresias SET tipo = ?, precio_base = ? WHERE id = ?";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, membresia.getTipo());
            pstmt.setDouble(2, membresia.getPrecioBase());
            pstmt.setInt(3, membresia.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Eliminar
    public static void eliminarMembresia(String tipo) {
        String sql = "DELETE FROM catalogo_membresias WHERE tipo = ?";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

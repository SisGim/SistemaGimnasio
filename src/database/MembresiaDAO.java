package database;

import models.Membresia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembresiaDAO {

    public static List<Membresia> obtenerMembresias() {
        List<Membresia> membresias = new ArrayList<>();
        String sql = "SELECT * FROM catalogo_membresias";

        try (Connection conn = Database.connect(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Membresia membresia = new Membresia(
                        rs.getInt("id"),
                        rs.getString("tipo"),
                        rs.getDouble("precio_base"),
                        rs.getInt("duracion")
                );
                membresias.add(membresia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membresias;
    }

    public static Membresia obtenerMembresiaPorTipo(String tipo) {
        String sql = "SELECT * FROM catalogo_membresias WHERE tipo = ?";

        try (Connection conn = Database.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tipo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Membresia(
                        rs.getInt("id"),
                        rs.getString("tipo"),
                        rs.getDouble("precio_base"),
                        rs.getInt("duracion")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void agregarMembresia(Membresia membresia) {
        String sql = "INSERT INTO catalogo_membresias (tipo, precio_base, duracion) VALUES (?, ?, ?)";

        try (Connection conn = Database.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, membresia.getTipo());
            pstmt.setDouble(2, membresia.getPrecioBase());
            pstmt.setInt(3, membresia.getDuracion());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void actualizarMembresia(Membresia membresia) {
        String sql = "UPDATE catalogo_membresias SET tipo = ?, precio_base = ?, duracion = ? WHERE id = ?";

        try (Connection conn = Database.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, membresia.getTipo());
            pstmt.setDouble(2, membresia.getPrecioBase());
            pstmt.setInt(3, membresia.getDuracion());
            pstmt.setInt(4, membresia.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void eliminarMembresia(int id) {
        String sql = "DELETE FROM catalogo_membresias WHERE id = ?";

        try (Connection conn = Database.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

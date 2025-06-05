package database;

import models.Asistencia;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaDAO {

    public static void registrarAsistencia(Asistencia asistencia) {
        String sql = "INSERT INTO asistencias (correo_cliente, fecha_hora) VALUES (?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, asistencia.getCorreoCliente());
            pstmt.setString(2, asistencia.getFechaHora().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Asistencia> obtenerAsistenciasPorCliente(String correoCliente) {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencias WHERE correo_cliente = ? ORDER BY fecha_hora DESC";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correoCliente);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Asistencia(
                    rs.getInt("id"),
                    rs.getString("correo_cliente"),
                    LocalDateTime.parse(rs.getString("fecha_hora"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<Asistencia> obtenerTodasLasAsistencias() {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencias ORDER BY fecha_hora DESC";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Asistencia(
                    rs.getInt("id"),
                    rs.getString("correo_cliente"),
                    LocalDateTime.parse(rs.getString("fecha_hora"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}

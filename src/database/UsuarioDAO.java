package database;

import util.CorreoUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public static boolean registrarUsuario(String email, String password) {
        try (Connection conn = Database.connect()) {
            // Verificar si el email ya existe
            PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT COUNT(*) FROM usuarios WHERE email = ?"
            );
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Ya registrado
            }

            // Insertar nuevo usuario con rol 'cliente'
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO usuarios (email, password, rol) VALUES (?, ?, 'cliente')"
            );
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.executeUpdate();

            // Enviar correo de confirmación
            CorreoUtil.enviarConfirmacion(email);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarRol(String email, String nuevoRol) {
        try (Connection conn = Database.connect()) {
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE usuarios SET rol = ? WHERE email = ?"
            );
            stmt.setString(1, nuevoRol);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> obtenerUsuarios() {
        List<String> usuarios = new ArrayList<>();
        try (Connection conn = Database.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT email FROM usuarios");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                usuarios.add(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public static String obtenerRol(String email) {
        try (Connection conn = Database.connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT rol FROM usuarios WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("rol");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String verificarCredenciales(String email, String password) {
        String sql = "SELECT rol FROM usuarios WHERE email = ? AND password = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email.trim());
            pstmt.setString(2, password.trim());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("rol");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Credenciales incorrectas
    }
}

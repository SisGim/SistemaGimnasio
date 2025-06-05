package database;

import models.Cliente;
import models.Usuario;
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

            // Insertar nuevo usuario con datos incompletos
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO usuarios (email, password, rol, nombre, telefono, identificacion) VALUES (?, ?, 'Cliente', 'Por completar', 'Por completar', 'Por completar')"
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
            boolean actualizado = stmt.executeUpdate() > 0;

            // Inicializar campos incompletos si es necesario
            if (actualizado) {
                PreparedStatement initCampos = conn.prepareStatement(
                        "UPDATE usuarios SET nombre = 'Por completar', telefono = 'Por completar', identificacion = 'Por completar' WHERE email = ?"
                );
                initCampos.setString(1, email);
                initCampos.executeUpdate();

                if (nuevoRol.equalsIgnoreCase("Cliente")) {
                    // Crear cliente asociado si no existe
                    if (ClienteDAO.obtenerClientePorCorreo(email) == null) {
                        Cliente nuevoCliente = new Cliente(
                                0,
                                "Por completar",
                                "Por completar",
                                email,
                                "Básica",
                                "Por completar"
                        );
                        ClienteDAO.agregarCliente(nuevoCliente);
                        System.out.println("👤 Cliente agregado tras cambio de rol.");
                    }
                }
            }

            return actualizado;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarUsuarioPorCorreo(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, telefono = ?, identificacion = ? WHERE email = ?";
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getTelefono());
            pstmt.setString(3, usuario.getIdentificacion());
            pstmt.setString(4, usuario.getEmail());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // No eliminar, permite ver errores
            return false;
        }
    }

    public static Usuario obtenerUsuarioPorCorreo(String correo) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (Connection conn = Database.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("rol"),
                        rs.getString("password"),
                        rs.getString("telefono"),
                        rs.getString("identificacion")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

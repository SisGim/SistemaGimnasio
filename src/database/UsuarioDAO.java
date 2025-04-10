package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    //Contador para los intentos fallidos de inicio de sesión y tiempo de bloqueo
    private static int intentosFallidos = 0;
    private static long tiempoBloqueo = 0;
    //Función para autenticar el usuario al iniciar sesión
    public static String autenticarUsuario(String username, String password) {
        //Después del 3er intento fallido, el usuario se bloquea temporalmente
        if (intentosFallidos >= 3) {
            long tiempoActual = System.currentTimeMillis();
            if (tiempoActual < tiempoBloqueo) {
                System.out.println("Usuario bloqueado. Intente nuevamente más tarde.");
                return null;
            } else {
                intentosFallidos = 0; //Restablecer intentos
            }
        }
        //Sintaxis de la consulta para capturar el username y la contraseña del usuario
        String sql = "SELECT rol FROM usuarios WHERE username = ? AND password = ?";
        //Conexión con la BDD
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username.trim());
            pstmt.setString(2, password.trim());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("¡Inicio de sesión exitoso!");
                intentosFallidos = 0;
                return rs.getString("rol"); //Devuelve el rol (Administrador o Empleado)
            } else {
                System.out.println("Credenciales incorrectas.");
                intentosFallidos++;
                if (intentosFallidos >= 3) {
                    tiempoBloqueo = System.currentTimeMillis() + (5 * 60 * 1000); //Bloqueo de 5 minutos
                }
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String verificarCredenciales(String username, String password) {
        String sql = "SELECT rol FROM usuarios WHERE username = ? AND password = ?";

        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("rol"); //Devuelve el rol del usuario autenticado
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; //Devuelve null si las credenciales son incorrectas
    }

}

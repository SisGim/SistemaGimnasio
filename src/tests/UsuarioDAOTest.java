package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.sql.SQLException;
import java.util.List;

import models.Usuario;
import database.UsuarioDAO;
import database.TestDatabase;

public class UsuarioDAOTest {
    private static String testEmail = "test@example.com";
    private static String testRol = "Cliente";
    private static String nuevoRol = "Administrador";

    @BeforeAll
    public static void setup() {
        try {
            TestDatabase.initialize();
        } catch (SQLException e) {
            Assertions.fail("Failed to initialize database: " + e.getMessage());
        }
    }

    @BeforeEach
    public void setUp() {
        // Clean up any existing test data
        try {
            UsuarioDAO.eliminarUsuario(testEmail);
        } catch (Exception e) {
            // Ignore if user doesn't exist
        }
    }

    @Test
    public void testRegistrarUsuario() throws SQLException {
        UsuarioDAO.registrarUsuario(testEmail, testRol);
        
        Usuario usuario = UsuarioDAO.obtenerUsuarioPorEmail(testEmail);
        Assertions.assertNotNull(usuario);
        Assertions.assertEquals(testEmail, usuario.getEmail());
        Assertions.assertEquals(testRol, usuario.getRol());
    }

    @Test
    public void testActualizarRol() throws SQLException {
        UsuarioDAO.registrarUsuario(testEmail, testRol);
        UsuarioDAO.actualizarRol(testEmail, nuevoRol);
        
        String rolObtenido = UsuarioDAO.obtenerRol(testEmail);
        Assertions.assertEquals(nuevoRol, rolObtenido);
    }

    @Test
    public void testObtenerRol() throws SQLException {
        UsuarioDAO.registrarUsuario(testEmail, testRol);
        
        String rolObtenido = UsuarioDAO.obtenerRol(testEmail);
        Assertions.assertEquals(testRol, rolObtenido);
    }

    @Test
    public void testEliminarUsuario() throws SQLException {
        UsuarioDAO.registrarUsuario(testEmail, testRol);
        UsuarioDAO.eliminarUsuario(testEmail);
        
        Usuario usuario = UsuarioDAO.obtenerUsuarioPorEmail(testEmail);
        Assertions.assertNull(usuario);
    }

    @Test
    public void testObtenerUsuarioPorEmail() throws SQLException {
        UsuarioDAO.registrarUsuario(testEmail, testRol);
        
        Usuario usuario = UsuarioDAO.obtenerUsuarioPorEmail(testEmail);
        Assertions.assertNotNull(usuario);
        Assertions.assertEquals(testEmail, usuario.getEmail());
        Assertions.assertEquals(testRol, usuario.getRol());
    }

    @Test
    public void testObtenerUsuarios() throws SQLException {
        UsuarioDAO.registrarUsuario(testEmail, testRol);
        String email2 = "test2@example.com";
        UsuarioDAO.registrarUsuario(email2, testRol);
        
        List<Usuario> usuarios = UsuarioDAO.obtenerUsuarios();
        Assertions.assertNotNull(usuarios);
        Assertions.assertTrue(usuarios.size() >= 2);
    }

    @Test
    public void testCrearClienteConUsuario() throws SQLException {
        String nombre = "Test Cliente";
        String telefono = "123456789";
        String membresia = "Básica";
        String identificacion = "123456789";
        
        UsuarioDAO.registrarUsuario(testEmail, testRol);
        UsuarioDAO.crearClienteConUsuario(testEmail, nombre, telefono, membresia, identificacion);
        
        Usuario usuario = UsuarioDAO.obtenerUsuarioPorEmail(testEmail);
        Assertions.assertNotNull(usuario);
        Assertions.assertEquals(testEmail, usuario.getEmail());
        Assertions.assertEquals(testRol, usuario.getRol());
    }
}
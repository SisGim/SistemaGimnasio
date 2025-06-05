package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.sql.SQLException;
import java.util.List;

import models.Cliente;
import database.ClienteDAO;
import database.TestDatabase;

public class ClienteDAOTest {
    private static Cliente testCliente;

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
        testCliente = new Cliente(0, "Test Name", "123456789", "test@example.com", "Básica", "123456789");
    }

    @Test
    public void testAgregarCliente() throws SQLException {
        ClienteDAO.agregarCliente(testCliente);
        
        Cliente retrieved = ClienteDAO.obtenerClientePorCorreo(testCliente.getEmail());
        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals(testCliente.getEmail(), retrieved.getEmail());
    }

    @Test
    public void testActualizarCliente() throws SQLException {
        ClienteDAO.agregarCliente(testCliente);
        
        testCliente.setNombre("Updated Name");
        testCliente.setTelefono("987654321");
        testCliente.setMembresia("Premium");
        
        ClienteDAO.actualizarCliente(testCliente);
        
        Cliente updated = ClienteDAO.obtenerClientePorCorreo(testCliente.getEmail());
        Assertions.assertEquals("Updated Name", updated.getNombre());
        Assertions.assertEquals("987654321", updated.getTelefono());
        Assertions.assertEquals("Premium", updated.getMembresia());
    }

    @Test
    public void testEliminarCliente() throws SQLException {
        ClienteDAO.agregarCliente(testCliente);
        ClienteDAO.eliminarCliente(testCliente.getEmail());
        
        Cliente deleted = ClienteDAO.obtenerClientePorCorreo(testCliente.getEmail());
        Assertions.assertNull(deleted);
    }

    @Test
    public void testObtenerClientes() throws SQLException {
        ClienteDAO.agregarCliente(testCliente);
        
        List<Cliente> clientes = ClienteDAO.obtenerClientes();
        Assertions.assertNotNull(clientes);
        Assertions.assertTrue(clientes.size() >= 1);
    }
}


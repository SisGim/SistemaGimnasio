package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.sql.SQLException;
import java.util.List;

import models.Membresia;
import database.MembresiaDAO;

public class MembresiaDAOTest {
    private Membresia testMembresia;

    @BeforeEach
    public void setup() {
        try {
            TestDatabase.connect();
        } catch (SQLException e) {
            Assertions.fail("Database connection failed: " + e.getMessage());
        }
        testMembresia = new Membresia(
            0, // ID will be auto-generated
            "Test Membresia",
            100.0
        );
        testMembresia.setDuracion(1);
    }

    @Test
    public void testAgregarMembresia() throws SQLException {
        MembresiaDAO.agregarMembresia(testMembresia);
        
        List<Membresia> membresias = MembresiaDAO.obtenerMembresias();
        Assertions.assertTrue(membresias.stream().anyMatch(m -> m.getTipo().equals(testMembresia.getTipo())), "Membership should be found in list");
    }

    @Test
    public void testActualizarMembresia() throws SQLException {
        MembresiaDAO.agregarMembresia(testMembresia);
        Membresia retrieved = MembresiaDAO.obtenerMembresiaPorTipo(testMembresia.getTipo());
        Assertions.assertNotNull(retrieved);

        Membresia updated = new Membresia(
            retrieved.getId(),
            "Updated Type",
            50.0
        );
        updated.setDuracion(3);
        MembresiaDAO.actualizarMembresia(updated);

        Membresia finalMembresia = MembresiaDAO.obtenerMembresiaPorTipo(updated.getTipo());
        Assertions.assertNotNull(finalMembresia);
        Assertions.assertEquals(updated.getTipo(), finalMembresia.getTipo());
        Assertions.assertEquals(updated.getPrecioBase(), finalMembresia.getPrecioBase());
        Assertions.assertEquals(updated.getDuracion(), finalMembresia.getDuracion());

    }

    @Test
    public void testEliminarMembresia() throws SQLException {
        MembresiaDAO.agregarMembresia(testMembresia);
        Membresia retrieved = MembresiaDAO.obtenerMembresiaPorTipo(testMembresia.getTipo());
        Assertions.assertNotNull(retrieved);

        MembresiaDAO.eliminarMembresia(testMembresia.getTipo());
        Membresia deleted = MembresiaDAO.obtenerMembresiaPorTipo(testMembresia.getTipo());
        Assertions.assertNull(deleted);
    }

    @Test
    public void testObtenerMembresias() throws SQLException {
        MembresiaDAO.agregarMembresia(testMembresia);
        List<Membresia> membresias = MembresiaDAO.obtenerMembresias();
        Assertions.assertNotNull(membresias);
        Assertions.assertTrue(membresias.size() > 0);
        Assertions.assertTrue(membresias.stream().anyMatch(m -> m.getTipo().equals(testMembresia.getTipo())));
    }
}
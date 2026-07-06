/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.uam.educore.model.personas;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import edu.uam.educore.enums.TipoEmpleado;
import java.time.LocalDate;

public class EmpleadoTest {

    @Test
    void crear_empleado_correctamente() {
        Empleado e = new Empleado(
                1,
                "Ricardo",
                "Alvarez",
                "ricardo@uam.edu",
                500000,
                LocalDate.of(2024, 1, 1),
                TipoEmpleado.DOCENTE
        );

        // Verifica que el objeto se creó
        assertNotNull(e);

        // Verifica nombre
        assertEquals("Ricardo", e.getNombre());

        // Verifica tipo
        assertEquals("DOCENTE", e.getTipo());
    }

    @Test
    void getInfo_no_esta_vacio() {
        Empleado e = new Empleado(
                2,
                "Ana",
                "Lopez",
                "ana@uam.edu",
                300000,
                LocalDate.of(2023, 5, 10),
                TipoEmpleado.ADMINISTRATIVO
        );

        // Verifica que getInfo devuelve texto
        assertFalse(e.getInfo().isEmpty());
    }

    @Test
    void tipo_empleado_correcto() {
        Empleado e = new Empleado(
                3,
                "Carlos",
                "Ruiz",
                "carlos@uam.edu",
                400000,
                LocalDate.of(2022, 3, 15),
                TipoEmpleado.GUARDA
        );

        // 👇 ESTE ES EL CAMBIO CLAVE (String, no enum)
        assertEquals("GUARDA", e.getTipo());
    }
}

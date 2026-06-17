package edu.uam.educore.view;
//
import edu.uam.educore.controller.EmpleadoController;
import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.model.personas.Empleado;

import java.util.Scanner;

// Vista (pantalla) para empleados
public class EmpleadoView extends VistaBase {

    // Controller (cerebro del módulo)
    private final EmpleadoController controller;

    // Constructor
    public EmpleadoView(Scanner scanner, Repositorio<Empleado> repo) {
        super(scanner);
        this.controller = new EmpleadoController(repo);
    }

    // MÉTODO PRINCIPAL DEL MENÚ
    public void iniciar() {
        int opcion;

        do {
            System.out.println("\n----- MENÚ EMPLEADOS -----");
            System.out.println("1. Registrar empleado");
            System.out.println("2. Listar empleados");
            System.out.println("0. Salir");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    registrarEmpleado();
                    break;
                case 2:
                    listarEmpleados();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // REGISTRAR EMPLEADO
    private void registrarEmpleado() {
        String nombre = leerTexto("Nombre");
        String apellidos = leerTexto("Apellidos");
        String email = leerTexto("Email");
        double salario = leerDecimal("Salario");

        java.time.LocalDate fechaIngreso = leerFecha("Fecha ingreso (AAAA-MM-DD)");

        System.out.println("Tipo de empleado:");
        System.out.println("1. DOCENTE");
        System.out.println("2. ADMINISTRATIVO");
        System.out.println("3. GUARDA");
        System.out.println("4. MISCELANEO");
        System.out.println("5. MANTENIMIENTO");

        int opcion = leerEntero("Seleccione tipo: ");

        edu.uam.educore.enums.TipoEmpleado tipo = null;

        switch (opcion) {
            case 1:
                tipo = edu.uam.educore.enums.TipoEmpleado.DOCENTE;
                break;
            case 2:
                tipo = edu.uam.educore.enums.TipoEmpleado.ADMINISTRATIVO;
                break;
            case 3:
                tipo = edu.uam.educore.enums.TipoEmpleado.GUARDA;
                break;
            case 4:
                tipo = edu.uam.educore.enums.TipoEmpleado.MISCELANEO;
                break;
            case 5:
                tipo = edu.uam.educore.enums.TipoEmpleado.MANTENIMIENTO;
                break;
            default:
                mostrarError("Tipo inválido");
                return;
        }

        try {
            Empleado e = controller.registrar(
                    nombre,
                    apellidos,
                    email,
                    salario,
                    fechaIngreso,
                    tipo
            );

            mostrarMensaje("Empleado registrado:\n" + e.getInfo());

        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    //Lista empleado
    private void listarEmpleados() {
        try {
            java.util.List<Empleado> lista = controller.listar();

            if (lista.isEmpty()) {
                mostrarMensaje("No hay empleados registrados");
                return;
            }

            System.out.println("\n--- EMPLEADOS REGISTRADOS ---");

            for (Empleado e : lista) {
                System.out.println(e.getInfo());
            }

        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }
}
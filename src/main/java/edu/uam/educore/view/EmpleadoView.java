package edu.uam.educore.view;

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
      System.out.println("3. Buscar por ID");
      System.out.println("4. Actualizar empleado");
      System.out.println("5. Eliminar empleado");
      System.out.println("0. Salir");

      opcion = leerEntero("Seleccione una opción: ");

      switch (opcion) {
        case 1:
          registrarEmpleado();
          break;
        case 2:
          listarEmpleados();
          break;
        case 3:
          buscarEmpleado();
          break;
        case 4:
          actualizarEmpleado();
          break;
        case 5:
          eliminarEmpleado();
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

    // campos vacíos
    if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty()) {
      mostrarError("No se permiten campos vacíos");
      return;
    }

    // email básico
    if (!email.contains("@")) {
      mostrarError("Email inválido");
      return;
    }

    double salario = leerDecimal("Salario");

    // salario validacion
    if (salario < 0) {
      mostrarError("El salario no puede ser negativo");
      return;
    }

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
      Empleado e = controller.registrar(nombre, apellidos, email, salario, fechaIngreso, tipo);

      mostrarMensaje("Empleado registrado:\n" + e.getInfo());

    } catch (Exception ex) {
      mostrarError(ex.getMessage());
    }
  }

  // Lista empleado
  private void listarEmpleados() {
    try {
      java.util.List<Empleado> lista = controller.listar();

      if (lista.isEmpty()) {
        mostrarMensaje("No hay empleados registrados");
        return;
      }

      System.out.println("\n--- EMPLEADOS REGISTRADOS ---");

      for (Empleado e : lista) {
        System.out.println(e.getId() + " | " + e.getInfo());
      }

    } catch (Exception ex) {
      mostrarError(ex.getMessage());
    }
  }

  // buscar por ID
  private void buscarEmpleado() {

    int id = leerEntero("Ingrese el ID del empleado: ");

    try {
      Empleado e = controller.buscarPorId(id);

      if (e == null) {
        mostrarMensaje("No se encontró un empleado con ese ID");
      } else {
        System.out.println("\n--- EMPLEADO ENCONTRADO ---");
        System.out.println(e.getId() + " | " + e.getInfo());
      }

    } catch (Exception ex) {
      mostrarError(ex.getMessage());
    }
  }

  // actualizar
  private void actualizarEmpleado() {
    int id = leerEntero("ID del empleado a actualizar");

    try {
      Empleado existente = controller.buscarPorId(id);

      if (existente == null) {
        mostrarError("No existe empleado con ID " + id + ".");
        return;
      }

      System.out.println("\nDatos actuales:");
      System.out.println("  " + existente.getInfo());

      System.out.println("\nIngrese los nuevos datos:");
      String nombre = leerTexto("Nombre");
      String apellidos = leerTexto("Apellidos");
      String email = leerTexto("Email");
      // campos vacíos
      if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty()) {
        mostrarError("No se permiten campos vacíos");
        return;
      }

      // email básico
      if (!email.contains("@")) {
        mostrarError("Email inválido");
        return;
      }

      double salario = leerDecimal("Salario");

      // salario validacion
      if (salario < 0) {
        mostrarError("El salario no puede ser negativo");
        return;
      }
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

      Empleado actualizado =
          controller.actualizar(id, nombre, apellidos, email, salario, fechaIngreso, tipo);

      mostrarMensaje("Actualizado — " + actualizado.getInfo());

    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  // eliminar
  private void eliminarEmpleado() {
    int id = leerEntero("ID del empleado a eliminar");

    try {
      Empleado existente = controller.buscarPorId(id);

      if (existente == null) {
        mostrarError("No existe empleado con ID " + id + ".");
        return;
      }

      System.out.println("\nEmpleado encontrado:");
      System.out.println(existente.getId() + " | " + existente.getInfo());

      String confirmacion = leerTexto("¿Seguro que desea eliminarlo? (S/N)");

      if (!confirmacion.equalsIgnoreCase("S")) {
        mostrarMensaje("Operación cancelada");
        return;
      }

      controller.eliminar(id);

      mostrarMensaje("Empleado eliminado correctamente");

    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }
}

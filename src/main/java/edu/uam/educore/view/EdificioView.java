package edu.uam.educore.view;

import edu.uam.educore.controller.EdificioController;
import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.model.infraestructura.Edificio;
import java.util.Scanner;

public class EdificioView extends VistaBase {

  private final EdificioController controller;

  public EdificioView(Scanner scanner, Repositorio<Edificio> repo) {
    super(scanner);
    this.controller = new EdificioController(repo);
  }

  public void iniciar() {
    int opcion;
    do {
      System.out.println("\n--- MENÚ EDIFICIOS ---");
      System.out.println("1. Registrar edificio");
      System.out.println("2. Listar edificios");
      System.out.println("3. Buscar edificio por ID");
      System.out.println("4. Eliminar edificio");
      System.out.println("0. Salir");

      opcion = leerEntero("Seleccione una opción: ");

      switch (opcion) {
        case 1:
          registrarEdificio();
          break;

        case 2:
          System.out.println("Listar edificios (pendiente)");
          break;

        case 3:
          System.out.println("Buscar edificio por ID");
          break;

        case 4:
          System.out.println("Eliminar Edificio");
          break;

        case 0:
          System.out.println("Saliendo...");
          break;

        default:
          System.out.println("Opción inválida");
      }

    } while (opcion != 0);
  }

  // Registrar Edificio
  private void registrarEdificio() {
    String codigo = leerTexto("Código del edificio");
    String nombre = leerTexto("Nombre del edificio");

    if (codigo.isEmpty() || nombre.isEmpty()) {
      mostrarError("Código y nombre son obligatorios");
      return;
    }

    try {
      Edificio e = controller.registrar(codigo, nombre);

      mostrarMensaje(
          "Edificio registrado:\n" + e.getId() + " | " + e.getCodigo() + " | " + e.getNombre());

    } catch (Exception ex) {
      mostrarError(ex.getMessage());
    }
  }

  // Listar
  private void ListarEdificios() {
    try {
      java.util.List<Edificio> lista = controller.listar();

      if (lista.isEmpty()) {
        mostrarMensaje("No hay Edificios registrados");
        return;
      }

      System.out.println("\n--- EDIFICIOS REGISTRADOS ---");

      for (Edificio e : lista) {
        System.out.println(e.getId() + " | " + e.getInfo());
      }

    } catch (Exception ex) {
      mostrarError(ex.getMessage());
    }
  }

  // buscar edificio
  private void buscarEdificio() {
    int id = leerEntero("ID del edificio: ");

    try {
      Edificio e = controller.buscarPorId(id);

      if (e == null) {
        mostrarError("No existe edificio con ID " + id);
      } else {
        System.out.println("\n--- EDIFICIO ENCONTRADO ---");
        System.out.println(e.getId() + " | " + e.getInfo());

        // Aulas (por ahora vacías)
        System.out.println("Aulas: (ninguna registrada)");
      }

    } catch (Exception ex) {
      mostrarError(ex.getMessage());
    }
  }

  private void eliminarEdificio() {

    int id = leerEntero("ID del Edificio a eliminar");

    try {
      Edificio e = controller.buscarPorId(id);

      if (e == null) {
        mostrarError("No existe Edificio con ID " + id + ".");
        return;
      }

      System.out.println("\nEdificio encontrado:");
      System.out.println(e.getId() + " | " + e.getInfo());

      String confirmacion = leerTexto("¿Seguro que desea eliminarlo? (S/N)");

      if (!confirmacion.equalsIgnoreCase("S")) {
        mostrarMensaje("Operación cancelada");
        return;
      }

      controller.eliminar(id);

      mostrarMensaje("Edificio eliminado correctamente");

    } catch (Exception ex) {
      mostrarError(ex.getMessage());
    }
  }
}

package edu.uam.educore.view;

import edu.uam.educore.controller.EdificioController;
import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.infraestructura.Edificio;
import java.util.List;
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
      System.out.println("3. Buscar edificio (y listar aulas)");
      System.out.println("4. Eliminar edificio");
      System.out.println("5. Agregar aula");
      System.out.println("6. Listar aula");
      System.out.println("0. Salir");

      opcion = leerEntero("Seleccione una opción: ");

      switch (opcion) {
        case 1:
          registrarEdificio();
          break;
        case 2:
          listarEdificios();
          break;
        case 3:
          buscarEdificio();
          break;
        case 4:
          eliminarEdificio();
          break;
        case 5:
          agregarAula();
          break;
        case 6:
          listarAulas();
          break;
        case 7:
          eliminarAula();
          break;

        case 0:
          System.out.println("Saliendo...");
          break;
        default:
          System.out.println("Opción inválida");
      }
    } while (opcion != 0);
  }

  private void registrarEdificio() {
    String codigo = leerTexto("Código del edificio");
    String nombre = leerTexto("Nombre del edificio");
    if (codigo.isEmpty() || nombre.isEmpty()) {
      mostrarError("Código y nombre son obligatorios");
      return;
    }
    try {
      Edificio e = controller.registrar(codigo, nombre);
      mostrarMensaje("Edificio registrado:\n" + e.getId() + " | " + e.getInfo());
    } catch (Exception ex) {
      mostrarError(ex.getMessage());
    }
  }

  private void listarEdificios() {
    try {
      List<Edificio> lista = controller.listar();
      if (lista.isEmpty()) {
        mostrarMensaje("No hay edificios registrados");
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

  private void buscarEdificio() {
    int id = leerEntero("ID del edificio: ");
    try {
      Edificio e = controller.buscarPorId(id);
      if (e == null) {
        mostrarError("No existe edificio con ID " + id);
      } else {
        System.out.println("\n--- EDIFICIO ENCONTRADO ---");
        System.out.println(e.getId() + " | " + e.getInfo());
        List<Aula> aulas = e.getAulas();
        if (aulas.isEmpty()) {
          System.out.println("Aulas: (ninguna registrada)");
        } else {
          System.out.println("Aulas registradas:");
          for (Aula a : aulas) {
            System.out.println(
                " - ID: "
                    + a.getId()
                    + " | "
                    + a.getNumero()
                    + " | "
                    + a.getCapacidad()
                    + " pax | Tipo: "
                    + a.getTipo());
          }
        }
      }
    } catch (Exception ex) {
      mostrarError(ex.getMessage());
    }
  }

  private void eliminarEdificio() {
    int id = leerEntero("ID del edificio a eliminar: ");
    try {
      Edificio e = controller.buscarPorId(id);
      if (e == null) {
        mostrarError("No existe edificio con ID " + id);
        return;
      }
      System.out.println("\nEdificio encontrado: " + e.getInfo());
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

  private void agregarAula() {

    int edificioId = leerEntero("ID del edificio: ");
    String numero = leerTexto("Número del aula: ");
    int capacidad = leerEntero("Capacidad: ");
    String tipo = leerTexto("Tipo (REGULAR, LABORATORIO, AUDITORIO): ");

    try {
      controller.agregarAula(edificioId, numero, capacidad, tipo);
      mostrarMensaje("Aula agregada correctamente");
    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void listarAulas() {
    int edificioId = leerEntero("ID del edificio: ");

    try {
      Edificio edificio = controller.buscarPorId(edificioId);

      if (edificio == null) {
        mostrarError("No existe edificio con ID " + edificioId);
        return;
      }

      System.out.println("\n--- AULAS DEL EDIFICIO ---");
      System.out.println(edificio.getId() + " | " + edificio.getInfo());

      if (edificio.getAulas().isEmpty()) {
        mostrarMensaje("No hay aulas registradas en este edificio");
        return;
      }

      for (Aula aula : edificio.getAulas()) {
        System.out.println(aula.getInfo());
      }

    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }

  private void eliminarAula() {

    int edificioId = leerEntero("ID del edificio: ");

    try {
      Edificio edificio = controller.buscarPorId(edificioId);

      if (edificio == null) {
        mostrarError("No existe edificio con ID " + edificioId);
        return;
      }

      if (edificio.getAulas().isEmpty()) {
        mostrarMensaje("No hay aulas para eliminar");
        return;
      }

      System.out.println("\n--- AULAS DEL EDIFICIO ---");

      for (Aula aula : edificio.getAulas()) {
        System.out.println(aula.getInfo());
      }

      String numero = leerTexto("Número del aula a eliminar: ");

      Aula encontrada = null;

      for (Aula aula : edificio.getAulas()) {
        if (aula.getNumero().equals(numero)) {
          encontrada = aula;
          break;
        }
      }

      if (encontrada == null) {
        mostrarError("No se encontró el aula");
        return;
      }

      String confirmacion = leerTexto("¿Desea eliminarla? (S/N): ");

      if (!confirmacion.equalsIgnoreCase("S")) {
        mostrarMensaje("Operación cancelada");
        return;
      }

      edificio.getAulas().remove(encontrada);

      mostrarMensaje("Aula eliminada correctamente");

    } catch (Exception e) {
      mostrarError(e.getMessage());
    }
  }
}

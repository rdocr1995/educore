package edu.uam.educore.view;

import edu.uam.educore.controller.EdificioController;
import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.infraestructura.Edificio;
import edu.uam.educore.model.infraestructura.TipoAula;
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
      System.out.println("3. Buscar edificio por ID (y listar aulas)");
      System.out.println("4. Eliminar edificio");
      System.out.println("5. Agregar aula a edificio");
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
          agregarAulaAEdificio();
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
                " - " + a.getNumero() + " | " + a.getCapacidad() + " pax | Tipo: " + a.getTipo());
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

  private void agregarAulaAEdificio() {
    int idEdificio = leerEntero("ID del edificio al que desea agregar el aula: ");
    String numero = leerTexto("Número del aula: ");
    int capacidad = leerEntero("Capacidad del aula: ");

    System.out.println("Seleccione el tipo de aula:");
    for (int i = 0; i < TipoAula.values().length; i++) {
      System.out.println(i + ". " + TipoAula.values()[i]);
    }
    int tipoIdx = leerEntero("Opción: ");

    try {
      TipoAula tipo = TipoAula.values()[tipoIdx];
      controller.agregarAula(idEdificio, numero, capacidad, tipo);
      mostrarMensaje("Aula " + numero + " agregada exitosamente al edificio.");
    } catch (Exception ex) {
      mostrarError("Error: " + ex.getMessage());
    }
  }
}

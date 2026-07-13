package edu.uam.educore.view;

import edu.uam.educore.controller.SeccionController;
import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.model.academico.Seccion;
import edu.uam.educore.model.personas.Empleado;
import edu.uam.educore.model.personas.Estudiante;
import edu.uam.educore.model.infraestructura.Edificio;

import java.util.Scanner;

public class SeccionView extends VistaBase {

    private final SeccionController controller;

    public SeccionView(Scanner scanner,
                       Repositorio<Seccion> seccionRepo,
                       Repositorio<Empleado> empleadoRepo,
                       Repositorio<Estudiante> estudianteRepo,
                       Repositorio<Edificio> edificioRepo) {
        super(scanner);
        this.controller = new SeccionController(
                seccionRepo,
                empleadoRepo,
                estudianteRepo,
                edificioRepo
        );
    }
    
//MENU
   public void iniciar() {
    int opcion;

    do {
        System.out.println("\n--- MENÚ SECCIONES ---");
        System.out.println("1. Registrar sección");
        System.out.println("2. Agregar estudiante");
        System.out.println("3. Remover estudiante");
        System.out.println("4. Listar secciones");
        System.out.println("5. Eliminar seccion");
        System.out.println("0. Salir");

        opcion = leerEntero("Seleccione opción: ");

        switch (opcion) {
            case 1:
                registrarSeccion();
                break;
                
                case 2:
                    agregarEstudiante();
                break;
                
               case 3:
                    removerEstudiante();
                break;
                
                case 4:
                 listarSecciones();
                 break;
                
                 
                case 5:
                 eliminarSeccion();
                 break;
                
            case 0:
                System.out.println("Saliendo...");
                break;
                
            default:
                System.out.println("Opción inválida");
        }

    } while (opcion != 0);
}
   
   private void registrarSeccion() {
    try {
        String codigo = leerTexto("Código de la sección: ");
        String nombre = leerTexto("Nombre del curso: ");
        int aulaId = leerEntero("ID del aula: ");
        int docenteId = leerEntero("ID del docente: ");

        Seccion s = controller.registrar(codigo, nombre, aulaId, docenteId);

        mostrarMensaje("Sección registrada:\n" + s.getCodigo() + " | " + s.getNombre());

    } catch (Exception e) {
        mostrarError(e.getMessage());
    }
}
   
  private void agregarEstudiante() {

    int seccionId = leerEntero("ID de la sección: ");
    int estudianteId = leerEntero("ID del estudiante: ");

    try {

        controller.agregarEstudiante(
                seccionId,
                estudianteId
        );

        mostrarMensaje(
                "Estudiante agregado correctamente"
        );

    } catch (Exception e) {

        mostrarError(
                e.getMessage()
        );

    }
} 
  
 private void removerEstudiante() {
    try {
        int seccionId = leerEntero("ID de la sección: ");
        int estudianteId = leerEntero("ID del estudiante: ");

        controller.removerEstudiante(seccionId, estudianteId);

        mostrarMensaje("Estudiante removido correctamente");
    } catch (Exception e) {
        mostrarError(e.getMessage());
    }
} 

private void listarSecciones() {
    try {

        for (Seccion s : controller.listar()) {

            System.out.println(
                s.getCodigo()
                + " | "
                + s.getNombre()
                + " | Docente: "
                + s.getDocente().getNombre()
                + " | Aula: "
                + s.getAula().getNumero()
                + " | Estudiantes: "
                + s.getEstudiantes().size()
            );

        }

    } catch (Exception e) {
        mostrarError(e.getMessage());
    }
} 

private void eliminarSeccion() {
    int seccionId = leerEntero("ID de la sección: ");

    try {
        String confirmacion = leerTexto("¿Desea eliminar la sección? (S/N): ");

        if (!confirmacion.equalsIgnoreCase("S")) {
            mostrarMensaje("Operación cancelada");
            return;
        }

        controller.eliminar(seccionId);
        mostrarMensaje("Sección eliminada correctamente");

    } catch (Exception e) {
        mostrarError(e.getMessage());
    }
}


}
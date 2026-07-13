package edu.uam.educore.view;

import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.model.academico.Seccion;
import edu.uam.educore.model.infraestructura.Edificio;
import edu.uam.educore.model.personas.Empleado;
import edu.uam.educore.model.personas.Estudiante;
import java.util.Scanner;

public class AcademicoView extends VistaBase {

    private final EdificioView edificioView;
    private final SeccionView seccionView;

    public AcademicoView(
            Scanner scanner,
            Repositorio<Edificio> edificioRepo,
            Repositorio<Seccion> seccionRepo,
            Repositorio<Empleado> empleadoRepo,
            Repositorio<Estudiante> estudianteRepo) {

        super(scanner);

        this.edificioView = new EdificioView(scanner, edificioRepo);

        this.seccionView = new SeccionView(
                scanner,
                seccionRepo,
                empleadoRepo,
                estudianteRepo,
                edificioRepo);
    }

    public void iniciar() {

        int opcion;

        do {

            System.out.println("\n--- MENÚ ACADÉMICO ---");
            System.out.println("1. Gestión de Edificios");
            System.out.println("2. Gestión de Secciones");
            System.out.println("0. Salir");

            opcion = leerEntero("Seleccione opción: ");

            switch (opcion) {

                case 1:
                    edificioView.iniciar();
                    break;

                case 2:
                    seccionView.iniciar();
                    break;

                case 0:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }
}
package edu.uam.educore.model.academico;

// IMPORTS IMPORTANTES
import edu.uam.educore.model.personas.Empleado;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.personas.Estudiante;

import java.util.ArrayList;
import java.util.List;

public class Seccion {

    
    private int id;
    private String codigo;
    private String nombre;

    private Empleado docente;      // Profesor asignado
    private Aula aula;             // Aula donde se imparte


    private List<Estudiante> estudiantes = new ArrayList<>();

   
    public Seccion(int id, String codigo, String nombre, Empleado docente, Aula aula) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.docente = docente;
        this.aula = aula;
    }

    
    public int getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public Empleado getDocente() {
        return docente;
    }

    public Aula getAula() {
        return aula;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

   
    // Agregar estudiante (matricular)
    public void agregarEstudiante(Estudiante e) {
        estudiantes.add(e);
    }

    // Remover estudiante (quitar)
    public void removerEstudiante(Estudiante e) {
        estudiantes.remove(e);
    }

    // Listar estudiantes
    public List<Estudiante> listarEstudiantes() {
        return estudiantes;
    }
}
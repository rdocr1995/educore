package edu.uam.educore.controller;

import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.dao.SeccionRepoSql;
import edu.uam.educore.model.academico.Seccion;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.infraestructura.Edificio;
import edu.uam.educore.model.personas.Empleado;
import edu.uam.educore.model.personas.Estudiante;
import java.util.List;

public class SeccionController {

  // Repositorio principal de secciones
  private final Repositorio<Seccion> seccionRepo;

  // Repos usados para validar datos
  private final Repositorio<Empleado> empleadoRepo;
  private final Repositorio<Estudiante> estudianteRepo;
  private final Repositorio<Edificio> edificioRepo;

  // Constructor
  public SeccionController(
      Repositorio<Seccion> seccionRepo,
      Repositorio<Empleado> empleadoRepo,
      Repositorio<Estudiante> estudianteRepo,
      Repositorio<Edificio> edificioRepo) {
    this.seccionRepo = seccionRepo;
    this.empleadoRepo = empleadoRepo;
    this.estudianteRepo = estudianteRepo;
    this.edificioRepo = edificioRepo;
  }

  public Seccion registrar(String codigo, String nombre, int aulaId, int docenteId)
      throws Exception {

    // 1. Validar datos básicos
    if (codigo.isEmpty() || nombre.isEmpty()) {
      throw new IllegalArgumentException("Código y nombre son obligatorios");
    }

    // 2. Buscar empleado (docente)
    Empleado docente = empleadoRepo.buscarPorId(docenteId).orElse(null);

    if (docente == null) {
      throw new IllegalArgumentException("No existe empleado con ID " + docenteId);
    }

    // 3. Validar que sea DOCENTE
    if (!docente.getTipo().equals("DOCENTE")) {
      throw new IllegalArgumentException("El empleado no es de tipo DOCENTE");
    }

    // 4. Buscar aula recorriendo edificios
    Aula aulaEncontrada = null;

    for (Edificio edificio : edificioRepo.buscarTodos()) {
      for (Aula aula : edificio.getAulas()) {
        if (aula.getId() == aulaId) {
          aulaEncontrada = aula;
          break;
        }
      }
    }

    if (aulaEncontrada == null) {
      throw new IllegalArgumentException("No existe aula con ID " + aulaId);
    }

    // 5. Crear sección
    Seccion seccion = new Seccion(0, codigo, nombre, docente, aulaEncontrada);

    // 6. Guardar
    seccionRepo.guardar(seccion);

    return seccion;
  }

  // METODO AGREGAR ESTUDUANTE
  public void agregarEstudiante(int seccionId, int estudianteId) throws Exception {

    // Buscar sección
    Seccion seccion = seccionRepo.buscarPorId(seccionId).orElse(null);

    if (seccion == null) {
      throw new IllegalArgumentException("No existe sección con ID " + seccionId);
    }
    

    // Buscar estudiante
    Estudiante estudiante = estudianteRepo.buscarPorId(estudianteId).orElse(null);

    if (estudiante == null) {
      throw new IllegalArgumentException("No existe estudiante con ID " + estudianteId);
    }
    
    if (seccionRepo instanceof SeccionRepoSql repoSql) {
    repoSql.inscribirEstudiante(seccionId, estudianteId);
}

    
  }

  // REMOVER
  public void removerEstudiante(int seccionId, int estudianteId) throws Exception {

    Seccion seccion = seccionRepo.buscarPorId(seccionId).orElse(null);

    if (seccion == null) {
      throw new IllegalArgumentException("No existe sección con ID " + seccionId);
    }

    Estudiante estudiante = estudianteRepo.buscarPorId(estudianteId).orElse(null);

    if (estudiante == null) {
      throw new IllegalArgumentException("No existe estudiante con ID " + estudianteId);
    }

    seccion.removerEstudiante(estudiante);

    seccionRepo.actualizar(seccion);
  }

  public List<Seccion> listar() throws Exception {
    return seccionRepo.buscarTodos();
  }

  public Seccion actualizar(int id, String codigo, String nombre, int aulaId, int docenteId)
      throws Exception {

    // Buscar sección existente
    Seccion seccion = seccionRepo.buscarPorId(id).orElse(null);

    if (seccion == null) {
      throw new IllegalArgumentException("No existe sección con ID " + id);
    }

    // Buscar docente
    Empleado docente = empleadoRepo.buscarPorId(docenteId).orElse(null);

    if (docente == null) {
      throw new IllegalArgumentException("No existe empleado con ID " + docenteId);
    }

    // Buscar aula
    Aula aulaEncontrada = null;

    for (Edificio edificio : edificioRepo.buscarTodos()) {
      for (Aula aula : edificio.getAulas()) {
        if (aula.getId() == aulaId) {
          aulaEncontrada = aula;
          break;
        }
      }
    }

    if (aulaEncontrada == null) {
      throw new IllegalArgumentException("No existe aula con ID " + aulaId);
    }

    // Crear nueva sección actualizada
    Seccion actualizada = new Seccion(id, codigo, nombre, docente, aulaEncontrada);

    seccionRepo.actualizar(actualizada);

    return actualizada;
  }

  public void eliminar(int seccionId) throws Exception {

    Seccion seccion = seccionRepo.buscarPorId(seccionId).orElse(null);

    if (seccion == null) {
      throw new IllegalArgumentException("No existe sección con ID " + seccionId);
    }

    if (!seccion.getEstudiantes().isEmpty()) {
      throw new IllegalArgumentException(
          "No se puede eliminar la sección porque tiene estudiantes inscritos");
    }

    seccionRepo.eliminar(seccionId);
  }
}

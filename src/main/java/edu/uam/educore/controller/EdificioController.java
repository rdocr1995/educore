package edu.uam.educore.controller;

import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.infraestructura.Edificio;
import edu.uam.educore.model.infraestructura.TipoAula;
import java.util.List;
import java.util.Optional;

public class EdificioController {
  private final Repositorio<Edificio> repo;
  private int proximoId = 1;
  private int proximoIdAula = 1; // Contador global compartido para Aulas

  public EdificioController(Repositorio<Edificio> repo) {
    this.repo = repo;
  }

  public Edificio registrar(String codigo, String nombre) throws Exception {
    if (codigo.isEmpty() || nombre.isEmpty()) {
      throw new IllegalArgumentException("Código y nombre son obligatorios");
    }

    Edificio e = new Edificio(proximoId++, codigo, nombre);
    repo.guardar(e);
    return e;
  }

  public List<Edificio> listar() throws Exception {
    return repo.buscarTodos();
  }

  public Edificio buscarPorId(int id) throws Exception {
    Optional<Edificio> op = repo.buscarPorId(id);
    return op.orElse(null);
  }

  public void eliminar(int id) throws Exception {
    Edificio e = buscarPorId(id);

    if (e == null) {
      throw new IllegalArgumentException("No existe edificio con ID " + id);
    }

    if (!e.getAulas().isEmpty()) {
      throw new IllegalArgumentException(
          "No se puede eliminar el edificio porque tiene aulas registradas");
    }

    repo.eliminar(id);
  }

  // --- Nuevo método para agregar aulas ---

  public Aula agregarAula(int edificioId, String numero, int capacidad, TipoAula tipo)
      throws Exception {
    Edificio edificio = buscarPorId(edificioId);

    if (edificio == null) {
      throw new IllegalArgumentException("No existe el edificio con ID " + edificioId);
    }

    // Crear el aula con el ID global compartido
    Aula nuevaAula = new Aula(proximoIdAula++, numero, capacidad, tipo, edificio);
    edificio.agregarAula(nuevaAula);

    // Importante: como modificamos el edificio (que es una entidad guardada),
    // debemos actualizarlo en el repositorio (RNF-05)
    repo.actualizar(edificio);

    return nuevaAula;
  }
}

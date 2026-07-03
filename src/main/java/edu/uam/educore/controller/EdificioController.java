package edu.uam.educore.controller;

import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.model.infraestructura.Edificio;
import java.util.List;
import java.util.Optional;

public class EdificioController {

  private final Repositorio<Edificio> repo;
  private int proximoId = 1;

  public EdificioController(Repositorio<Edificio> repo) {
    this.repo = repo;
  }

  // REGISTRAR EDIFICIO
  public Edificio registrar(String codigo, String nombre) throws Exception {

    if (codigo.isEmpty() || nombre.isEmpty()) {
      throw new IllegalArgumentException("Código y nombre son obligatorios");
    }

    Edificio e = new Edificio(proximoId++, codigo, nombre);

    repo.guardar(e);

    return e;
  }

  // LISTAR EDIFICIO
  public List<Edificio> listar() throws Exception {
    return repo.buscarTodos();
  }

  // BUSCAR
  public Edificio buscarPorId(int id) throws Exception {
    Optional<Edificio> op = repo.buscarPorId(id);
    return op.orElse(null);
  }

  // ELIMINAR
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
}

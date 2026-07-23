package edu.uam.educore.controller;

import edu.uam.educore.dao.EdificioRepoSql;
import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.infraestructura.Edificio;
import edu.uam.educore.model.infraestructura.TipoAula;
import java.util.List;
import java.util.Optional;

public class EdificioController {
  private final Repositorio<Edificio> repo;
  private int proximoId = 1;
  private int proximoIdAula = 1;
  private int proximoAulaId = 1;
  private int proximoIdSeccion = 1;

  public EdificioController(Repositorio<Edificio> repo) {
    this.repo = repo;
  }

  public Edificio registrar(String codigo, String nombre) throws Exception {
    Edificio e = new Edificio(proximoId++, codigo, nombre);
    repo.guardar(e);
    return e;
  }

  public List<Edificio> listar() throws Exception {
    return repo.buscarTodos();
  }

  public Edificio buscarPorId(int id) throws Exception {
    return repo.buscarPorId(id).orElse(null);
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

  public Aula agregarAula(int edificioId, String numero, int capacidad, String tipo)
      throws Exception {

    Edificio edificio = buscarPorId(edificioId);

    if (edificio == null) {
      throw new IllegalArgumentException("No existe edificio con ID " + edificioId);
    }
    // AGREGAR
    TipoAula tipoAula = TipoAula.valueOf(tipo);

    Aula aula = new Aula(proximoAulaId++, numero, capacidad, tipoAula, edificio);

    edificio.agregarAula(aula);

    if (repo instanceof EdificioRepoSql repoSql) {
      repoSql.guardarAula(aula);
    }

    return aula;
  }

  public void eliminarAula(int idAula) throws Exception {
    boolean eliminada = false;
    for (Edificio e : repo.buscarTodos()) {
      Optional<Aula> aulaOpt = e.getAulas().stream().filter(a -> a.getId() == idAula).findFirst();

      if (aulaOpt.isPresent()) {
        e.getAulas().remove(aulaOpt.get());
        repo.actualizar(e);
        eliminada = true;
        break;
      }
    }
    if (!eliminada) throw new Exception("No se encontró un aula con ID " + idAula);
  }

  public Edificio actualizar(int id, String codigo, String nombre) throws Exception {

    Edificio e = buscarPorId(id);

    if (e == null) {
      throw new IllegalArgumentException("No existe edificio con ID " + id);
    }

    e.setCodigo(codigo);
    e.setNombre(nombre);

    repo.actualizar(e);

    return e;
  }
}

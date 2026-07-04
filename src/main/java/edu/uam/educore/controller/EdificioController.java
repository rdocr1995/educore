package edu.uam.educore.controller;

import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.infraestructura.Edificio;
import edu.uam.educore.model.infraestructura.Seccion;
import edu.uam.educore.model.infraestructura.TipoAula;
import java.util.List;
import java.util.Optional;

public class EdificioController {
  private final Repositorio<Edificio> repo;
  private int proximoId = 1;
  private int proximoIdAula = 1;
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
    repo.eliminar(id);
  }

  public Aula agregarAula(int edificioId, String numero, int capacidad, TipoAula tipo)
      throws Exception {
    Edificio edificio = buscarPorId(edificioId);
    if (edificio == null) throw new IllegalArgumentException("Edificio no existe");

    Aula nuevaAula = new Aula(proximoIdAula++, numero, capacidad, tipo, edificio);

    edificio.agregarAula(nuevaAula);
    repo.actualizar(edificio);
    return nuevaAula;
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

  public Seccion registrarSeccion(int idAula, String codigo, String nombre) throws Exception {
    Aula aulaEncontrada = null;
    Edificio edificioPadre = null;

    for (Edificio e : repo.buscarTodos()) {
      aulaEncontrada =
          e.getAulas().stream().filter(a -> a.getId() == idAula).findFirst().orElse(null);
      if (aulaEncontrada != null) {
        edificioPadre = e;
        break;
      }
    }

    if (aulaEncontrada == null) throw new IllegalArgumentException("Aula no encontrada");

    Seccion nuevaSeccion = new Seccion(proximoIdSeccion++, codigo, nombre, aulaEncontrada);
    aulaEncontrada.agregarSeccion(nuevaSeccion);
    repo.actualizar(edificioPadre);
    return nuevaSeccion;
  }
}

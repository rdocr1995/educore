package edu.uam.educore.dao;

import edu.uam.educore.model.academico.Seccion;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListaSeccionRepo extends Repositorio<Seccion> {

  private final List<Seccion> lista = new ArrayList<>();

  @Override
  public void guardar(Seccion seccion) throws Exception {
    lista.add(seccion);
  }

  @Override
  public void actualizar(Seccion seccion) throws Exception {}

  @Override
  public void eliminar(int id) throws Exception {
    lista.removeIf(s -> s.getId() == id);
  }

  @Override
  public Optional<Seccion> buscarPorId(int id) throws Exception {
    return lista.stream().filter(s -> s.getId() == id).findFirst();
  }

  @Override
  public List<Seccion> buscarTodos() throws Exception {
    return lista;
  }
}

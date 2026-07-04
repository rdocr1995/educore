package edu.uam.educore.model.infraestructura;

import java.util.ArrayList;
import java.util.List;

public class Edificio {
  private int id;
  private String codigo;
  private String nombre;
  // Asegúrate de que la lista sea de tipo Aula
  private List<Aula> aulas = new ArrayList<>();

  public Edificio(int id, String codigo, String nombre) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
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

  public List<Aula> getAulas() {
    return aulas;
  }

  // Este es el método que tu controlador está buscando y no encuentra
  public void agregarAula(Aula aula) {
    this.aulas.add(aula);
  }

  public String getInfo() {
    return codigo + " | " + nombre;
  }
}

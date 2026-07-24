package edu.uam.educore.model.infraestructura;

import java.util.ArrayList;
import java.util.List;

public class Edificio {
  private int id;
  private String codigo;
  private String nombre;

  private List<Aula> aulas = new ArrayList<>();

  public String getCodigo() {
    return codigo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  // Agrega el aula
  public void agregarAula(Aula aula) {
    aulas.add(aula);
  }

  public List<Aula> getAulas() {
    return aulas;
  }

  public Edificio(int id, String codigo, String nombre) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getInfo() {
    return codigo + " | " + nombre;
  }
}

package edu.uam.educore.model.infraestructura;

import java.util.ArrayList;
import java.util.List;

public class Edificio {

  private int id;
  private String codigo;
  private String nombre;

  // ver las aulas que contiene
  private List<Object> aulas = new ArrayList<>();

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

  public String getInfo() {
    return codigo + " | " + nombre;
  }

  public List<Object> getAulas() {
    return aulas;
  }
}

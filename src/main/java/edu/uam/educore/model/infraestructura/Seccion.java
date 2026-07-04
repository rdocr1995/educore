package edu.uam.educore.model.infraestructura;

public class Seccion {
  private int id;
  private String codigo;
  private String nombre;
  private Aula aula;

  public Seccion(int id, String codigo, String nombre, Aula aula) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
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

  public Aula getAula() {
    return aula;
  }
}

package edu.uam.educore.model.infraestructura;

import java.util.ArrayList;
import java.util.List;

public class Aula {
  private int id;
  private String numero;
  private int capacidad;
  private TipoAula tipo;
  // Esta es la relación de composición que asegura que el aula conozca su edificio
  private Edificio edificio;
  private List<Seccion> secciones = new ArrayList<>();

  public Aula(int id, String numero, int capacidad, TipoAula tipo, Edificio edificio) {
    this.id = id;
    this.numero = numero;
    this.capacidad = capacidad;
    this.tipo = tipo;
    this.edificio = edificio;
  }

  public int getId() {
    return id;
  }

  public String getNumero() {
    return numero;
  }

  public int getCapacidad() {
    return capacidad;
  }

  public TipoAula getTipo() {
    return tipo;
  }

  // El aula conoce su edificio
  public Edificio getEdificio() {
    return edificio;
  }

  public void agregarSeccion(Seccion seccion) {
    this.secciones.add(seccion);
  }

  public List<Seccion> getSecciones() {
    return secciones;
  }
}

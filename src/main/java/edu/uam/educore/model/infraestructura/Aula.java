package edu.uam.educore.model.infraestructura;

public class Aula {
  private int id;
  private String numero;
  private int capacidad;
  private TipoAula tipo;
  private Edificio edificio;

  // Este es el constructor que EdificioController está buscando
  public Aula(int id, String numero, int capacidad, TipoAula tipo, Edificio edificio) {
    this.id = id;
    this.numero = numero;
    this.capacidad = capacidad;
    this.tipo = tipo;
    this.edificio = edificio;
  }

  // Getters
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

  public Edificio getEdificio() {
    return edificio;
  }
}

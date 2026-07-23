package edu.uam.educore.dao;

import edu.uam.educore.db.Conexion;
import edu.uam.educore.db.ConfiguracionBD;
import edu.uam.educore.model.infraestructura.Aula;

import edu.uam.educore.model.infraestructura.TipoAula;
import edu.uam.educore.model.infraestructura.Edificio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EdificioRepoSql extends Repositorio<Edificio> {

  private final ConfiguracionBD config;

  public EdificioRepoSql(ConfiguracionBD config) {
    this.config = config;
  }

  private Connection abrir() throws Exception {
    return Conexion.getConnection(config.url(), config.usuario(), config.contrasena());
  }

  @Override
  public void guardar(Edificio e) throws Exception {

    String sql = "INSERT INTO edificio (codigo, nombre) " + "VALUES (?, ?)";

    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      ps.setString(1, e.getCodigo());
      ps.setString(2, e.getNombre());

      ps.executeUpdate();

      try (ResultSet claves = ps.getGeneratedKeys()) {

        if (claves.next()) {
          e.setId(claves.getInt(1));
        }
      }
    }
  }

  @Override
  public List<Edificio> buscarTodos() throws Exception {

    List<Edificio> lista = new ArrayList<>();

    String sql = "SELECT * FROM edificio";

    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {

        Edificio e = new Edificio(rs.getInt("id"), rs.getString("codigo"), rs.getString("nombre"));
String sqlAulas =
    "SELECT * FROM aula WHERE edificio_id = ?";

PreparedStatement psAula =
    con.prepareStatement(sqlAulas);

psAula.setInt(1, e.getId());

ResultSet rsAula =
    psAula.executeQuery();

while (rsAula.next()) {

    Aula aula =
        new Aula(
            rsAula.getInt("id"),
            rsAula.getString("numero"),
            rsAula.getInt("capacidad"),
            TipoAula.valueOf(rsAula.getString("tipo")),
            e);

    e.agregarAula(aula);
}
        lista.add(e);
      }
    }

    return lista;
  }

  @Override
  public Optional<Edificio> buscarPorId(int id) throws Exception {

    String sql = "SELECT * FROM edificio WHERE id=?";

    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)) {

      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {

          Edificio e =
              new Edificio(rs.getInt("id"), rs.getString("codigo"), rs.getString("nombre"));

          return Optional.of(e);
        }
      }
    }

    return Optional.empty();
  }

  @Override
  public void actualizar(Edificio e) throws Exception {

    String sql = "UPDATE edificio " + "SET codigo = ?, nombre = ? " + "WHERE id = ?";

    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)) {

      ps.setString(1, e.getCodigo());
      ps.setString(2, e.getNombre());
      ps.setInt(3, e.getId());

      ps.executeUpdate();
    }
  }

  @Override
  public void eliminar(int id) throws Exception {

    String sql = "DELETE FROM edificio WHERE id=?";

    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)) {

      ps.setInt(1, id);
      ps.executeUpdate();
    }
  }

  public void guardarAula(Aula aula) throws Exception {

    String sql =
        "INSERT INTO aula " + "(numero, capacidad, tipo, edificio_id) " + "VALUES (?, ?, ?, ?)";

    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      ps.setString(1, aula.getNumero());
      ps.setInt(2, aula.getCapacidad());
      ps.setString(3, aula.getTipo().name());
      ps.setInt(4, aula.getEdificio().getId());

      ps.executeUpdate();
    }
  }
  
  public void eliminarAula(int idAula) throws Exception {

    String sql = "DELETE FROM aula WHERE id = ?";

    try (
        Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {

        ps.setInt(1, idAula);
        ps.executeUpdate();
    }
}
}

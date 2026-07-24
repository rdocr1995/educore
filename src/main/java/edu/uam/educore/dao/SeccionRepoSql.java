package edu.uam.educore.dao;

import edu.uam.educore.db.Conexion;
import edu.uam.educore.db.ConfiguracionBD;
import edu.uam.educore.enums.TipoEmpleado;
import edu.uam.educore.model.academico.Seccion;
import edu.uam.educore.model.infraestructura.Aula;
import edu.uam.educore.model.infraestructura.Edificio;
import edu.uam.educore.model.infraestructura.TipoAula;
import edu.uam.educore.model.personas.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeccionRepoSql extends Repositorio<Seccion> {

  private final ConfiguracionBD config;

  public SeccionRepoSql(ConfiguracionBD config) {
    this.config = config;
  }

  private Connection abrir() throws Exception {
    return Conexion.getConnection(config.url(), config.usuario(), config.contrasena());
  }

  @Override
  public void guardar(Seccion s) throws Exception {

    String sql =
        "INSERT INTO seccion " + "(codigo, nombre, docente_id, aula_id) " + "VALUES (?, ?, ?, ?)";

    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      ps.setString(1, s.getCodigo());
      ps.setString(2, s.getNombre());
      ps.setInt(3, s.getDocente().getId());
      ps.setInt(4, s.getAula().getId());

      ps.executeUpdate();
    }
  }

  @Override
  public Optional<Seccion> buscarPorId(int id) throws Exception {

    String sql = "SELECT * FROM seccion WHERE id = ?";

    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)) {

      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {

          // Por ahora usamos objetos mínimos
          Empleado docente =
              new Empleado(
                  rs.getInt("docente_id"),
                  "",
                  "",
                  "",
                  0,
                  java.time.LocalDate.now(),
                  TipoEmpleado.DOCENTE);

          Edificio edificioDummy = new Edificio(0, "", "");

          Aula aula = new Aula(rs.getInt("aula_id"), "", 0, TipoAula.REGULAR, edificioDummy);

          Seccion s =
              new Seccion(
                  rs.getInt("id"), rs.getString("codigo"), rs.getString("nombre"), docente, aula);

          return Optional.of(s);
        }
      }
    }

    return Optional.empty();
  }

  @Override
  public List<Seccion> buscarTodos() throws Exception {

    List<Seccion> lista = new ArrayList<>();

    String sql = "SELECT * FROM seccion";

    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {

        Empleado docente =
            new Empleado(
                rs.getInt("docente_id"),
                "",
                "",
                "",
                0,
                java.time.LocalDate.now(),
                TipoEmpleado.DOCENTE);

        Edificio edificioDummy = new Edificio(0, "", "");

        Aula aula = new Aula(rs.getInt("aula_id"), "", 0, TipoAula.REGULAR, edificioDummy);

        Seccion s =
            new Seccion(
                rs.getInt("id"), rs.getString("codigo"), rs.getString("nombre"), docente, aula);

        lista.add(s);
      }
    }

    return lista;
  }

  @Override
  public void actualizar(Seccion s) throws Exception {
    String sql =
        "UPDATE seccion "
            + "SET codigo = ?, nombre = ?, docente_id = ?, aula_id = ? "
            + "WHERE id = ?";

    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)) {

      ps.setString(1, s.getCodigo());
      ps.setString(2, s.getNombre());
      ps.setInt(3, s.getDocente().getId());
      ps.setInt(4, s.getAula().getId());
      ps.setInt(5, s.getId());

      ps.executeUpdate();
    }
  }

  @Override
  public void eliminar(int id) throws Exception {
    String sql = "DELETE FROM seccion WHERE id = ?";

    try (Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, id);
      ps.executeUpdate();
    }
  }
  
 public void inscribirEstudiante(int seccionId, int estudianteId) throws Exception {

    String sql =
        "INSERT INTO matricula (estudiante_id, seccion_id) " +
        "VALUES (?, ?)";

    try (Connection con = abrir();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, estudianteId);
        ps.setInt(2, seccionId);

        ps.executeUpdate();
    }
}
}

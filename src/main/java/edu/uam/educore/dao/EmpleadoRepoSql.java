package edu.uam.educore.dao;

import edu.uam.educore.db.Conexion;
import edu.uam.educore.db.ConfiguracionBD;
import edu.uam.educore.model.personas.Empleado;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import edu.uam.educore.enums.TipoEmpleado;
import edu.uam.educore.model.personas.Empleado;
import java.util.ArrayList;
import java.util.List;





public class EmpleadoRepoSql extends Repositorio<Empleado> {

    private final ConfiguracionBD config;

    public EmpleadoRepoSql(ConfiguracionBD config) {
        this.config = config;
    }

    private Connection abrir() throws Exception {
        return Conexion.getConnection(
                config.url(),
                config.usuario(),
                config.contrasena()
        );
    }

    
    
    
    
    
  
   @Override
public void guardar(Empleado e) throws Exception {

    String sql =
        "INSERT INTO empleado " +
        "(nombre, apellidos, email, salario, fecha_ingreso, tipo) " +
        "VALUES (?, ?, ?, ?, ?, ?)";

    try (
        Connection con = abrir();
        PreparedStatement ps =
            con.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
            )
    ) {

        ps.setString(1, e.getNombre());
        ps.setString(2, e.getApellidos());
        ps.setString(3, e.getEmail());
        ps.setDouble(4, e.getSalario());

        ps.setDate(
            5,
            java.sql.Date.valueOf(e.getFechaIngreso())
        );

        ps.setString(
            6,
            e.getTipo()
        );

        ps.executeUpdate();

        try (ResultSet claves = ps.getGeneratedKeys()) {

            if (claves.next()) {

                e.setId(
                    claves.getInt(1)
                );
            }
        }
    }
}


@Override
public List<Empleado> buscarTodos() throws Exception {

    List<Empleado> lista = new ArrayList<>();

    String sql = "SELECT * FROM empleado";

    try (
        Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()
    ) {

        while (rs.next()) {

            Empleado e = new Empleado(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("email"),
                rs.getDouble("salario"),
                rs.getDate("fecha_ingreso").toLocalDate(),
                TipoEmpleado.valueOf(rs.getString("tipo"))
            );

            lista.add(e);
        }
    }

    return lista;
}

@Override
public Optional<Empleado> buscarPorId(int id) throws Exception {

    String sql = "SELECT * FROM empleado WHERE id = ?";

    try (
        Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {

        ps.setInt(1, id);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                Empleado e = new Empleado(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getString("email"),
                    rs.getDouble("salario"),
                    rs.getDate("fecha_ingreso").toLocalDate(),
                    TipoEmpleado.valueOf(rs.getString("tipo"))
                );

                return Optional.of(e);
            }
        }
    }

    return Optional.empty();
}


 @Override
public void actualizar(Empleado e) throws Exception {

    String sql =
        "UPDATE empleado " +
        "SET nombre = ?, apellidos = ?, email = ?, salario = ?, fecha_ingreso = ?, tipo = ? " +
        "WHERE id = ?";

    try (
        Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {

        ps.setString(1, e.getNombre());
        ps.setString(2, e.getApellidos());
        ps.setString(3, e.getEmail());
        ps.setDouble(4, e.getSalario());

        ps.setDate(
            5,
            java.sql.Date.valueOf(e.getFechaIngreso())
        );

        ps.setString(6, e.getTipo());

        ps.setInt(7, e.getId());

        ps.executeUpdate();
    }
}  

@Override
public void eliminar(int id) throws Exception {

    String sql = "DELETE FROM empleado WHERE id = ?";

    try (
        Connection con = abrir();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {

        ps.setInt(1, id);

        ps.executeUpdate();

    }
} 
  
    
}


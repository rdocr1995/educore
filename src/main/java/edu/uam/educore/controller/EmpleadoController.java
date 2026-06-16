package edu.uam.educore.controller;

import edu.uam.educore.dao.Repositorio;
import edu.uam.educore.model.personas.Empleado;
import java.util.Optional;

public class EmpleadoController {

    private final Repositorio<Empleado> repo;
    private int proximoId = 1;

    public EmpleadoController(Repositorio<Empleado> repo) {
        this.repo = repo;
    }

    public Empleado registrar(String nombre,String apellidos,String email,double salario,java.time.LocalDate fechaIngreso,edu.uam.educore.enums.TipoEmpleado tipo
    ) throws Exception {

        Empleado e = new Empleado(
                proximoId,
                nombre,
                apellidos,
                email,
                salario,
                fechaIngreso,
                tipo
        );

        repo.guardar(e);

        proximoId++;

        return e;
    }
    
    public java.util.List<Empleado> listar() throws Exception {
    return repo.buscarTodos();
}
public Empleado buscarPorId(int id) throws Exception {
    Optional<Empleado> resultado = repo.buscarPorId(id);

    if (resultado.isPresent()) {
        return resultado.get();
    }

    return null;
}
}

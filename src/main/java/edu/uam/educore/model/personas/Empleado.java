package edu.uam.educore.model.personas;

// Importamos el enum que creamos
import edu.uam.educore.enums.TipoEmpleado;

// Importamos la clase para manejar fechas
import java.time.LocalDate;

// Empleado HEREDA de Persona
public class Empleado extends Persona {

    private double salario;              // Salario mensual
    private LocalDate fechaIngreso;      // Fecha en que ingresó
    private TipoEmpleado tipo;           // Tipo de empleado (enum)

    public Empleado(int id, String nombre, String apellidos, String email,
                    double salario, LocalDate fechaIngreso, TipoEmpleado tipo) {

        // Llamamos al constructor de Persona (clase padre)
        super(id, nombre, apellidos, email);

        // Guardamos los valores propios del empleado
        this.salario = salario;
        this.fechaIngreso = fechaIngreso;
        this.tipo = tipo;
    }

    public double getSalario() {
        return salario;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public TipoEmpleado getTipoEmpleado() {
        return tipo;
    }



    // Devuelve el tipo de empleado como texto
    @Override
    public String getTipo() {
        return tipo.toString();
    }

    // Devuelve la información completa del empleado
    @Override
    public String getInfo() {
        return String.format(
                "[%s] %s %s | Salario: %.2f | Fecha ingreso: %s",
                getTipo(),
                getNombre(),
                getApellidos(),
                salario,
                fechaIngreso
        );
    }
    
    public void setSalario(double salario) {
    this.salario = salario;
}

public void setFechaIngreso(java.time.LocalDate fechaIngreso) {
    this.fechaIngreso = fechaIngreso;
}

public void setTipoEmpleado(edu.uam.educore.enums.TipoEmpleado tipo) {
    this.tipo = tipo;
}
}
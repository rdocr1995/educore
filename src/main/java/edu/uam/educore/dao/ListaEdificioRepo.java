package edu.uam.educore.dao;

import edu.uam.educore.model.infraestructura.Edificio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListaEdificioRepo extends Repositorio<Edificio> {

    private final List<Edificio> lista = new ArrayList<>();

    @Override
    public void guardar(Edificio e) throws Exception {
        lista.add(e);
    }

    @Override
    public void actualizar(Edificio e) throws Exception {
        // ya se actualiza en memoria
    }

    @Override
    public void eliminar(int id) throws Exception {
        lista.removeIf(e -> e.getId() == id);
    }

    @Override
    public Optional<Edificio> buscarPorId(int id) throws Exception {
        return lista.stream()
                .filter(e -> e.getId() == id)
                .findFirst();
    }

    @Override
    public List<Edificio> buscarTodos() throws Exception {
        return lista;
    }
}
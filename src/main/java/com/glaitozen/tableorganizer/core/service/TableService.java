package com.glaitozen.tableorganizer.core.service;

import com.glaitozen.tableorganizer.core.model.Table;
import com.glaitozen.tableorganizer.repository.TableRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TableService {

    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public Optional<Table> findById(String id) {
        return tableRepository.findById(id);
    }

    public Table findByName(String nom) {
        return tableRepository.findByNom(nom);
    }

    public List<String> getAllTableNames() {
        List<String> result = new ArrayList<>();
        tableRepository.findAll().forEach(t -> result.add(t.getNom()));
        return result;
    }

    public void save(Table table) {
        tableRepository.save(table);
    }

    public void delete(Table table) {
        tableRepository.delete(table);
    }
}

package com.glaitozen.tableorganizer.core.service;

import com.glaitozen.tableorganizer.core.mapper.h2.TableH2Mapper;
import com.glaitozen.tableorganizer.core.model.Table;
import com.glaitozen.tableorganizer.core.model.User;
import com.glaitozen.tableorganizer.repository.TableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TableService {

    private final TableRepository tableRepository;
    private final TableH2Mapper tableH2Mapper;

    public TableService(TableRepository tableRepository, TableH2Mapper tableH2Mapper) {
        this.tableRepository = tableRepository;
        this.tableH2Mapper = tableH2Mapper;

        sampleTest();
    }

    private void sampleTest() {
        User sduclos = new User("sduclos");
        Table expTable = new Table("Strahd", "D&D 5", sduclos);
        User pduclos = new User("pduclos");
        expTable.addJoueur(pduclos);
        expTable.addPropositions(Collections.singleton(LocalDate.now().plusDays(1)));

        save(expTable);
    }

    public Optional<Table> findById(String id) {
        return tableRepository.findById(id)
                .map(tableH2Mapper::mapToModel);
    }

    public Table findByName(String nom) {
        return tableH2Mapper.mapToModel(tableRepository.findByNom(nom));
    }

    public List<String> getAllTableNames() {
        List<String> result = new ArrayList<>();
        tableRepository.findAll().forEach(t -> result.add(t.getNom()));
        return result;
    }

    public void save(Table table) {
        tableRepository.save(tableH2Mapper.mapToDto(table));
    }

    public void delete(Table table) {
        tableRepository.delete(tableH2Mapper.mapToDto(table));
    }
}

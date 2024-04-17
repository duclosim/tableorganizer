package com.glaitozen.tableorganizer.repository;

import com.glaitozen.tableorganizer.core.model.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends CrudRepository<Table, String> {
    Table findByNom(String nom);
}

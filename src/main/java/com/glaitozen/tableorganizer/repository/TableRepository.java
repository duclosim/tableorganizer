package com.glaitozen.tableorganizer.repository;

import com.glaitozen.tableorganizer.repository.dto.TableH2Dto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends CrudRepository<TableH2Dto, String> {
    TableH2Dto findByNom(String nom);
}

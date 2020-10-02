package com.sap.i40aas.datamanager.persistence.repositories;

import com.sap.i40aas.datamanager.persistence.entities.SubmodelEntity;
import org.springframework.data.repository.CrudRepository;

public interface SubmodelRepository extends CrudRepository<SubmodelEntity, String> {
}

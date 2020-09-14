package com.sap.i40aas.datamanager.webService.services;

import com.sap.i40aas.datamanager.errorHandling.DuplicateResourceException;
import com.sap.i40aas.datamanager.persistence.entities.AssetAdministrationShellEntity;
import com.sap.i40aas.datamanager.persistence.repositories.AssetAdministrationShellRepository;
import identifiables.AssetAdministrationShell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.AASObjectsDeserializer;

import java.util.ArrayList;
import java.util.List;


@Service
public class AssetAdminShellObjectsService {

  private AssetAdminShellObjectsService assetAdminShellObjectsService;

  @Autowired
  private AssetAdministrationShellRepository aasRepo;


  public AssetAdministrationShell getAssetAdministrationShell(String id) {

    AssetAdministrationShellEntity aasEntityFound = aasRepo.findById(id).get();
    AssetAdministrationShell aasFound = AASObjectsDeserializer.Companion.deserializeAAS(aasEntityFound.getAasObj());
    return aasFound;
  }


  public List<AssetAdministrationShell> getAllAssetAdministrationShells() {
    List<AssetAdministrationShell> aasList = new ArrayList<>();
    aasRepo.findAll().forEach(assetAdministrationShellEntity -> {
      aasList.add(AASObjectsDeserializer.Companion.deserializeAAS(assetAdministrationShellEntity.getAasObj()));
    });
    return aasList;
  }

  public AssetAdministrationShell updateAssetAdministrationShell(String id, AssetAdministrationShell aas) {

    if (aasRepo.findById(id).isPresent()) {
      AssetAdministrationShellEntity sbE = new AssetAdministrationShellEntity(id, AASObjectsDeserializer.Companion.serializeAAS(aas));
      aasRepo.save(sbE);
      return aas;
    } else
      throw new java.util.NoSuchElementException();
  }

  public AssetAdministrationShell addAssetAdministrationShell(AssetAdministrationShell aas) {

    AssetAdministrationShellEntity sbE = new AssetAdministrationShellEntity(aas.getIdentification().getId(), AASObjectsDeserializer.Companion.serializeAAS(aas));
    aasRepo.save(sbE);
    return aas;

  }

  public AssetAdministrationShell createAssetAdministrationShell(String id, AssetAdministrationShell aas) throws DuplicateResourceException {
// if Id not present create else if already there throw error
    if (aasRepo.findById(id).isPresent() == false) {

      String aasSer = AASObjectsDeserializer.Companion.serializeAAS(aas);

      AssetAdministrationShellEntity sbE = new AssetAdministrationShellEntity(id, aasSer);
      aasRepo.save(sbE);
      return aas;
    } else
      throw new DuplicateResourceException();

  }

  public AssetAdministrationShell deleteAssetAdministrationShell(String id) {
    if (aasRepo.findById(id).isPresent()) {
      //find the Submodel so that it gets returned
      AssetAdministrationShellEntity aasEntityFound = aasRepo.findById(id).get();
      AssetAdministrationShell aasFound = AASObjectsDeserializer.Companion.deserializeAAS(aasEntityFound.getAasObj());

      aasRepo.deleteById(id);
      return aasFound;
    } else
      throw new java.util.NoSuchElementException();
  }

}

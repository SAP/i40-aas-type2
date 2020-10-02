package com.sap.i40aas.datamanager.web.mocktests;

import com.sap.i40aas.datamanager.errorHandling.RestResponseEntityExceptionHandler;
import com.sap.i40aas.datamanager.webService.controllers.SubmodelController;
import com.sap.i40aas.datamanager.webService.services.SubmodelObjectsService;
import identifiables.Submodel;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utils.AASObjectsDeserializer;
import utils.SampleSubmodelFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//using @WebMvcTest will tell Spring Boot to instantiate only the web layer and not the entire context.
@WebMvcTest(controllers = SubmodelController.class)
@ActiveProfiles("localtest")

public
class SubmodelControllerMockTest {
  public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

  @Autowired
  private MockMvc mockMvc;

  // mock the Service layer code for our unit tests
  @MockBean
  private SubmodelObjectsService submodelObjectsService;

  //private SubmodelObjectsService submodelObjectsServiceMock = Mockito.mock(SubmodelObjectsService.class);

  //activate exception handler
  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(submodelObjectsService)
      .setControllerAdvice(new RestResponseEntityExceptionHandler())
      .build();
  }

  @After
  public void reset_mocks() {
    Mockito.reset(submodelObjectsService);
  }

  @WithMockUser //for basic auth
  @Test
  public void getSumodelListShouldReturnListFromService() throws Exception {
    List<Submodel> submodels = new ArrayList<>(Arrays.asList(
      SampleSubmodelFactory.Companion.getSampleSubmodel("http://acplt.org/Submodels/Assets/TestAsset/Identification_1"),
      SampleSubmodelFactory.Companion.getSampleSubmodel("http://acplt.org/Submodels/Assets/TestAsset/Identification_2")
    ));

    //deserialized list of submodels that should be returned as response
    String sapleSerialized = AASObjectsDeserializer.Companion.serializeSubmodelList(submodels);

    when(submodelObjectsService.getAllSubmodels()).thenReturn(submodels);

    //expect as response a serialized list of submodelsd
    this.mockMvc.perform(get("/submodels"))
      .andDo(print()).andExpect(status().isOk())
      .andExpect(content().json(sapleSerialized));
  }

  @WithMockUser
  @Test
  public void getSumodelByIDShouldReturnASubmodelWithIdFromService() throws Exception {
    List<Submodel> submodels = new ArrayList<>(Arrays.asList(
      SampleSubmodelFactory.Companion.getSampleSubmodel("sampleSb_1"),
      SampleSubmodelFactory.Companion.getSampleSubmodel("sampleSb_2"),
      SampleSubmodelFactory.Companion.getSampleSubmodel("http://acplt.org/Submodels/Assets/TestAsset/Identification")
    ));

    String sampleShortId = "http://acplt.org/Submodels/Assets/TestAsset/Identification";
    String sampleSerialized = AASObjectsDeserializer.Companion.serializeSubmodel(submodels.get(2));

    when(submodelObjectsService.getSubmodel(sampleShortId)).thenReturn(submodels.get(2));

    //expect as response a serialized list of submodelsd
    this.mockMvc.perform(get("/submodels")
      .param("id", sampleShortId))
      .andDo(print()).andExpect(status().isOk())
      .andExpect(content().json(sampleSerialized));
  }

  @Test
  @WithMockUser
  public void getSumodelByIDShouldReturn404ErrorIfSubmodelNotFound() throws Exception {
    List<Submodel> submodels = new ArrayList<>(Arrays.asList(
      SampleSubmodelFactory.Companion.getSampleSubmodel("sampleSb_1"),
      SampleSubmodelFactory.Companion.getSampleSubmodel("sampleSb_2"),
      SampleSubmodelFactory.Companion.getSampleSubmodel("http://acplt.org/Submodels/Assets/TestAsset/Identification")
    ));

    String wrongId = "http://acplt.org/Submodels/Assets/TestAsset/Identification_NaM";
    //String sampleSerialized = AASDeserializer.Companion.serializeSubmodel(submodels.get(2));

    when(submodelObjectsService.getSubmodel(wrongId)).thenThrow(java.util.NoSuchElementException.class);

//        expect to return a 404 Error
    this.mockMvc.perform(get("/submodels")
      .param("id", wrongId))
      .andDo(print()).andExpect(status().is4xxClientError())
      .andExpect(content().string(containsString("Requested resource not found")));

  }

  @Test
  @WithMockUser
  public void putSumodelShouldReturnOKAndTheSubmodelObject() throws Exception {

    String id = "http://acplt.org/Submodels/Assets/TestAsset/Identification";
    Submodel sampleSubmodel = SampleSubmodelFactory.Companion.getSampleSubmodel(id);

    //String sampleSerialized = AASDeserializer.Companion.serializeSubmodel(submodels.get(2));
    String serializedSubmodel = AASObjectsDeserializer.Companion.serializeSubmodel(sampleSubmodel);

    when(submodelObjectsService.createSubmodel(anyString(), any(Submodel.class))).thenReturn(sampleSubmodel);

//        expect to return a 202 OK with the submodel as content
    this.mockMvc.perform(put("/submodels")
      .contentType(APPLICATION_JSON_UTF8)
      .content(serializedSubmodel)
      .param("id", id))
      .andDo(print())
      .andExpect(status().is2xxSuccessful())
      .andExpect(content().json(serializedSubmodel));

  }

  @Test
  @WithMockUser
  public void patchSumodelShouldReturnOKAndTheSubmodelObject() throws Exception {

    String id = "http://acplt.org/Submodels/Assets/TestAsset/Identification";
    Submodel sampleSubmodel = SampleSubmodelFactory.Companion.getSampleSubmodel(id);
    //String sampleSerialized = AASDeserializer.Companion.serializeSubmodel(submodels.get(2));
    String serializedSubmodel = AASObjectsDeserializer.Companion.serializeSubmodel(sampleSubmodel);

    when(submodelObjectsService.updateSubmodel(anyString(), any(Submodel.class))).thenReturn(sampleSubmodel);

    System.out.println("response is " + submodelObjectsService.updateSubmodel(id, sampleSubmodel));

//        expect to return a 202 OK with the submodel as content
    MvcResult result = this.mockMvc.perform(patch("/submodels")
      .contentType(APPLICATION_JSON_UTF8)
      .content(serializedSubmodel)
      .param("id", id))
      .andDo(print())
      .andExpect(status().is2xxSuccessful())
      .andExpect(content().json(serializedSubmodel, false))
      .andReturn();

//    TODO: check why the content of the response is empty
    String contentResponse = result.getResponse().getContentAsString();
//    org.assertj.core.api.Assertions.assertThat(contentResponse).isEqualTo(serializedSubmodel);
  }


  @Test
  @WithMockUser
  public void patchSumodelShouldReturn404ErrorWhenIdNotFound() throws Exception {

    String id = "http://acplt.org/Submodels/Assets/TestAsset/Identification";
    Submodel sampleSubmodel = SampleSubmodelFactory.Companion.getSampleSubmodel(id);
    //String sampleSerialized = AASDeserializer.Companion.serializeSubmodel(submodels.get(2));
    String serializedSubmodel = AASObjectsDeserializer.Companion.serializeSubmodel(sampleSubmodel);

    //throw Exception as if Element was not found
    when(submodelObjectsService.updateSubmodel(anyString(), any(Submodel.class))).thenThrow(new java.util.NoSuchElementException());

    MvcResult result = this.mockMvc.perform(patch("/submodels")
      .contentType(APPLICATION_JSON_UTF8)
      .content(serializedSubmodel)
      .param("id", id))
      .andExpect(status().is4xxClientError())
      .andExpect(content().string("Requested resource not found"))
      .andReturn();
  }


  @Test
  @WithMockUser
  public void deleteSumodelShouldReturnOKWhenSuccessful() throws Exception {

    String id = "http://acplt.org/Submodels/Assets/TestAsset/Identification";
    Submodel sampleSubmodel = SampleSubmodelFactory.Companion.getSampleSubmodel(id);
    //String sampleSerialized = AASDeserializer.Companion.serializeSubmodel(submodels.get(2));
    String serializedSubmodel = AASObjectsDeserializer.Companion.serializeSubmodel(sampleSubmodel);

    when(submodelObjectsService.deleteSubmodel(id)).thenReturn(sampleSubmodel);

    System.out.println("response is " + submodelObjectsService.updateSubmodel(id, sampleSubmodel));

//        expect to return a 202 OK with the submodel as content
    MvcResult result = this.mockMvc.perform(delete("/submodels")
      .param("id", id))
      .andDo(print())
      .andExpect(status().is2xxSuccessful())
      .andReturn();

  }


}

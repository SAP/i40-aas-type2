package com.sap.i40aas.datamanager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DataManagerApplication.class)
//@ActiveProfiles("localtest")

@AutoConfigureMockMvc
class DataManagerApplicationTests {

  @Autowired
  private MockMvc mockMvc;

    /*
    The @SpringBootTest annotation tells Spring Boot to look for a main configuration class
     (one with @SpringBootApplication, for instance) and use that to start a Spring application context.
      You can run this test in your IDE or on the command line (by running ./mvnw test or ./gradlew test),
      and it should pass.
     */

  @Test
  @WithMockUser
  public void shouldReturnDefaultMessage() throws Exception {
    this.mockMvc.perform(get("/health")).andDo(print()).andExpect(status().isOk())
      .andExpect(content().string(containsString("Server UP!")));
  }

  @Test
  void contextLoads() {
  }

}

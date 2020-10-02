package com.sap.i40aas.datamanager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataManagerApplication.class)
public class SpringContextManualTest {

  @Test
  public void whenSpringContextIsBootstrapped_thenNoExceptions() {
  }
}

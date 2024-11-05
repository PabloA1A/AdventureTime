package dev.pabloabad.adventuretime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class AdventuretimeApplicationTests {

	@Autowired
    private WebApplicationContext context;

    @Test
    void contextLoads() {
        MockMvcBuilders.webAppContextSetup(context).build();
    }

}

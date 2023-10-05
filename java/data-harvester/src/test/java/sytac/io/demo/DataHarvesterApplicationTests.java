package sytac.io.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sytac.io.demo.config.AppProperties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = DataHarvesterApplication.class)
class DataHarvesterApplicationTests {

	@Autowired
	private DataHarvesterApplication dataHarvesterApplication;

	@Autowired
	private AppProperties properties;

	@Test
	void contextLoads() {
		// This test is mainly for checking the application's configuration
		assertNotNull(dataHarvesterApplication);
		assertNotNull(properties);
	}

}

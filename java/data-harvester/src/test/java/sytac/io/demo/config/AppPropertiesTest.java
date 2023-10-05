package sytac.io.demo.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "streaming.host=testHost",
        "streaming.username=testUsername",
        "streaming.password=testPassword",
        "stopper.first-name=testFirstName",
        "stopper.duration-sec=10",
        "streaming.sytflix=testSytflix",
        "streaming.sytazon=testSytazon",
        "streaming.sysney=testSysney"
})
class AppPropertiesTest {
    @Autowired
    private AppProperties properties;

    @Test
    void getAllProperties() {
        Assertions.assertNotNull(properties.getStreamingHost());
        Assertions.assertNotNull(properties.getUsername());
        Assertions.assertNotNull(properties.getPassword());
        Assertions.assertNotNull(properties.getFirstNameSytac());
        Assertions.assertNotNull(properties.getDuration());
        Assertions.assertNotNull(properties.getSytflix());
        Assertions.assertNotNull(properties.getSytazon());
        Assertions.assertNotNull(properties.getSysney());
        System.out.println(properties);
    }
}
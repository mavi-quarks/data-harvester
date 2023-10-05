package sytac.io.demo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppProperties {
    @Value("${streaming.host}")
    private String streamingHost;
    @Value("${streaming.username}")
    private String username;
    @Value("${streaming.password}")
    private String password;

    @Value("${stopper.first-name}")
    private String firstNameSytac;

    @Value("${stopper.duration-sec}")
    private Integer duration;

    @Value("${streaming.sytflix}")
    private String sytflix;

    @Value("${streaming.sytazon}")
    private String sytazon;

    @Value("${streaming.sysney}")
    private String sysney;

    @Override
    public String toString() {
        return "AppProperties{" +
                "streamingHost='" + "***" + '\'' +
                ", username='" + "***" + '\'' +
                ", password='" + "***" + '\'' +
                ", firstNameSytac='" + firstNameSytac + '\'' +
                ", duration=" + duration +
                ", sytflix='" + sytflix + '\'' +
                ", sytazon='" + sytazon + '\'' +
                ", sysney='" + sysney + '\'' +
                '}';
    }
}

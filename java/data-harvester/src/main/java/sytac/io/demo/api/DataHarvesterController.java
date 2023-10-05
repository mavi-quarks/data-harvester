package sytac.io.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import sytac.io.demo.model.StreamResponseStatistics;
import sytac.io.demo.service.DataHarvesterIF;
import sytac.io.demo.service.DataHarvesterImpl;

@RestController
@RequestMapping(value = "dhm/v1")
public class DataHarvesterController {
    protected final DataHarvesterIF dataHarvester;

    @Autowired
    public DataHarvesterController(DataHarvesterImpl harvester) {
        this.dataHarvester = harvester;
    }

    /**
     * Get all the streaming data (user and show) gathered for 20 secs or 'til the 3rd occurrence of a
     * firstname 'Sytac' on either of the streams. The 3 streaming platforms are Sytflix, Sytazon & Sysney.
     * @return The StreamResponseStatistics
     */
    @GetMapping()
    public Mono<StreamResponseStatistics> collectData() {
        return dataHarvester.retrieveDataFromStreams();
    }
}

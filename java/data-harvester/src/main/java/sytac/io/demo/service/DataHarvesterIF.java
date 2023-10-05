package sytac.io.demo.service;

import reactor.core.publisher.Mono;
import sytac.io.demo.model.StreamResponseStatistics;

public interface DataHarvesterIF {

    /**
     * Retrieves all data collected from 3 sources: Sysney, Sytazon, Sytflix.
     * The streams of data are collected within a span of 20 seconds or until the 3rd occurrence
     * of first name 'Sytac', whichever comes first
     * @return JsonString
     */
    Mono<StreamResponseStatistics> retrieveDataFromStreams();

}

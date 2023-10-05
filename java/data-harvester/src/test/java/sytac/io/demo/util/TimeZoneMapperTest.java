package sytac.io.demo.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class TimeZoneMapperTest {

    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss.SSS";
    @Test
    void convertTimeToAmsterdamCETGood() {
        DateTimeFormatter dateFormatting = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String countryCode = "PH";

        String timeString = LocalDateTime.now().atZone(TimeZoneMapper.getZoneIdByCountryCode(countryCode)).format(dateFormatting);
        String actual = TimeZoneMapper.convertTimeToAmsterdamCET(timeString, countryCode);
        String[] date1 = actual.split("\\.");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime localTime = LocalDateTime.now();//LocalDateTime.parse(timeString, dateFormatting);
        ZoneId zoneId = TimeZoneMapper.getZoneIdByCountryCode("PH");
        ZonedDateTime countryDateTime = localTime.atZone(zoneId);

        // Convert the ZonedDateTime to Amsterdam CET timezone
        ZonedDateTime amsterdamCETDateTime = countryDateTime.withZoneSameInstant(ZoneId.of("Europe/Amsterdam"));
        String[] date2 = formatter.format(amsterdamCETDateTime).split("\\."); // remove the ms part
        Assertions.assertEquals(date2[0], date1[0]);
    }

    @Test
    void convertTimeToAmsterdamCETSameTimeZone() {

        String timeString = LocalDateTime.now().format(TestDataMaker.dateFormatting);
        String actual = TimeZoneMapper.convertTimeToAmsterdamCET(timeString, "NL");
        Assertions.assertNotNull(actual);
    }


    @Test
    void convertTimeToAmsterdamCETNoCountryCode() {
        String timeString = LocalDateTime.now().format(TestDataMaker.dateFormatting);
        String actual = TimeZoneMapper.convertTimeToAmsterdamCET(timeString, "");
        Assertions.assertNotNull(actual);
    }
}
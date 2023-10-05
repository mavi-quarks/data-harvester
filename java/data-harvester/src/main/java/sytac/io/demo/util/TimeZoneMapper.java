package sytac.io.demo.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TimeZoneMapper {
    private static final Map<String, ZoneId> COUNTRY_TIME_ZONES = new HashMap<>();
    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss.SSS";

    // Initialize the mapping in a static block
    static {
        COUNTRY_TIME_ZONES.put("US", ZoneId.of("America/New_York"));
        COUNTRY_TIME_ZONES.put("CA", ZoneId.of("America/Toronto"));
        COUNTRY_TIME_ZONES.put("PT", ZoneId.of("Europe/Lisbon"));
        COUNTRY_TIME_ZONES.put("RU", ZoneId.of("Europe/Moscow"));
        COUNTRY_TIME_ZONES.put("ID", ZoneId.of("Asia/Jakarta"));
        COUNTRY_TIME_ZONES.put("CN", ZoneId.of("Asia/Shanghai"));
        COUNTRY_TIME_ZONES.put("PH", ZoneId.of("Asia/Manila"));
        COUNTRY_TIME_ZONES.put("NL", ZoneId.of("Europe/Amsterdam"));
    }

    /**
     * Converts local date time 'dd-MM-yyyy HH:mm:ss.SSS' into Amsterdam CET
     * with zone id = Europe/Amsterdam
     */
    public static String convertTimeToAmsterdamCET(String timeString, String country) {
        DateTimeFormatter dateFormatting = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime localTime = LocalDateTime.parse(timeString, dateFormatting);
        ZoneId countryZone = getZoneIdByCountryCode(country);
        // Convert the LocalDateTime to a ZonedDateTime using the country's timezone
        ZonedDateTime countryDateTime = localTime.atZone(countryZone);

        // If the country zone is already Amsterdam CET, no need to convert
        if (countryZone.equals(getAmsterdamCETZone())) {
            return countryDateTime.format(dateFormatting);
        }
        // Convert the ZonedDateTime to Amsterdam CET timezone
        ZonedDateTime amsterdamCETDateTime = countryDateTime.withZoneSameInstant(getAmsterdamCETZone());
        return amsterdamCETDateTime.format(dateFormatting);
    }

    public static ZoneId getZoneIdByCountryCode(String countryCode) {
        // If there is no matching countryCode, always return the Amsterdam time
        return COUNTRY_TIME_ZONES.getOrDefault(countryCode, getAmsterdamCETZone());
    }

    /**
     * Return the ZoneId for Amsterdam CET
     */
    private static ZoneId getAmsterdamCETZone() {
        return ZoneId.of("Europe/Amsterdam");
    }
}

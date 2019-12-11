package uk.gov.dhsc.htbhf.dwp.converter;

import org.springframework.web.context.request.NativeWebRequest;
import uk.gov.dhsc.htbhf.dwp.http.HeaderName;

import java.time.LocalDate;

public class ConverterUtils {

    /**
     * Allows a null safe way of getting and formatting a date from the header in the given request.
     * The date is formatted to ISO-8601 format.
     *
     * @param webRequest The web request
     * @param dateHeader The key under which the date is stored
     * @return The date or null if the key doesn't exist in the map.
     */
    public static LocalDate nullSafeGetDate(NativeWebRequest webRequest, HeaderName dateHeader) {
        String dateValue = webRequest.getHeader(dateHeader.getHeader());
        if (dateValue != null) {
            return LocalDate.parse(dateValue);
        }
        return null;
    }

    /**
     * Provides a null safe way of getting an Integer from the headers in the WebRequest.
     *
     * @param webRequest    The web request
     * @param integerHeader The key for the Integer
     * @return The Integer or null if the key doesn't exist in the map
     */
    public static Integer nullSafeGetInteger(NativeWebRequest webRequest, HeaderName integerHeader) {
        String value = webRequest.getHeader(integerHeader.getHeader());
        if (value != null) {
            return Integer.valueOf(value);
        }
        return null;
    }
}

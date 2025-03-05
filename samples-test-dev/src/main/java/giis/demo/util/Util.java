package giis.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static Date isoStringToDate(String isoDateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(isoDateString);
        } catch (ParseException e) {
            throw new UnexpectedException("Formato ISO incorrecto para fecha: " + isoDateString);
        }
    }
}
package giis.demo.util;

import java.lang.reflect.InvocationTargetException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

    // Evitar la creación de instancias de la clase
    private Util() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Serializa una lista de objetos a formato JSON insertando saltos de línea entre cada elemento.
     * 
     * @param pojoClass Clase de los objetos a serializar.
     * @param pojoList Lista de objetos a serializar.
     * @param asArray si es true codifica los campos como un array.
     * @return el string que representa la lista serializada en JSON.
     */
    public static String serializeToJson(Class<?> pojoClass, List<?> pojoList, boolean asArray) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (asArray) {
                mapper.configOverride(pojoClass).setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.ARRAY));
                String value = mapper.writeValueAsString(pojoList);
                return value.replace("],", "],\n").replace("\"", ""); // Con saltos de línea y sin comillas.
            } else {
                return mapper.writeValueAsString(pojoList).replaceAll("},", "},\n"); // Con saltos de línea.
            }
        } catch (JsonProcessingException e) {
            throw new UnexpectedException(e);
        }
    }

    /**
     * Convierte una lista de objetos a formato CSV.
     * 
     * @param pojoList Lista de objetos a serializar.
     * @param fields Campos de cada objeto a incluir en el CSV.
     * @return el string que representa la lista serializada en CSV.
     */
    public static String pojosToCsv(List<?> pojoList, String[] fields) {
        return pojosToCsv(pojoList, fields, false, ",", "", "", "");
    }

    /**
     * Convierte una lista de objetos a formato CSV con varios parámetros de personalización.
     * 
     * @param pojoList Lista de objetos a serializar.
     * @param fields Campos de cada objeto a incluir en el CSV.
     * @param headers si es true, incluye una fila de cabeceras.
     * @param separator Caracter que separa cada columna.
     * @param begin Caracter a incluir al principio de cada línea.
     * @param end Caracter a incluir al final de cada línea.
     * @param nullAs Texto que se incluirá cuando el valor sea null.
     * @return el string que representa la lista serializada en CSV.
     */
    public static String pojosToCsv(List<?> pojoList, String[] fields, boolean headers, String separator, String begin, String end, String nullAs) {
        StringBuilder sb = new StringBuilder();
        if (headers) {
            addPojoLineToCsv(sb, null, fields, separator, begin, end, nullAs);
        }
        for (Object pojo : pojoList) {
            try {
                Map<String, String> objectAsMap = BeanUtils.describe(pojo);
                addPojoLineToCsv(sb, objectAsMap, fields, separator, begin, end, nullAs);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new UnexpectedException(e);
            }
        }
        return sb.toString();
    }

    private static void addPojoLineToCsv(StringBuilder sb, Map<String, String> objectAsMap, String[] fields, String separator, String begin, String end, String nullAs) {
        sb.append(begin);
        for (int j = 0; j < fields.length; j++) {
            String value = (objectAsMap == null) ? fields[j] : objectAsMap.getOrDefault(fields[j], nullAs);
            sb.append((j == 0 ? "" : separator) + value);
        }
        sb.append(end + "\n");
    }

    /**
     * Convierte una fecha representada como un string ISO a una fecha Java.
     * 
     * @param isoDateString la fecha en formato ISO.
     * @return la fecha convertida a tipo `Date`.
     */
    public static Date isoStringToDate(String isoDateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(isoDateString);
        } catch (ParseException e) {
            throw new UnexpectedException("Formato ISO incorrecto para fecha: " + isoDateString, e);
        }
    }

    /**
     * Convierte una fecha Java a un string en formato ISO.
     * 
     * @param javaDate la fecha de tipo `Date`.
     * @return la fecha convertida a formato ISO.
     */
    public static String dateToIsoString(Date javaDate) {
        if (javaDate == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(javaDate);
    }
}

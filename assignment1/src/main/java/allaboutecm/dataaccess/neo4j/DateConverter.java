package allaboutecm.dataaccess.neo4j;


import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements AttributeConverter<Date, String> {
    @Override
    public String toGraphProperty(Date value) {
        if (null == value) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Not a valid date");
        }

    }

    @Override
    public Date toEntityAttribute(String value) {
        if (null == value) {
            return null;
        }

        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Cannot convert string to Date:" + value);
        }
    }
}
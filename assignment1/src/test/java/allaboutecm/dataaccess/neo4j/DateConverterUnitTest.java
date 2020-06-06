package allaboutecm.dataaccess.neo4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class DateConverterUnitTest {
    private static DateConverter dc;

    @BeforeEach
    public void setUp(){
        dc = new DateConverter();
    }



    @Test
    public void toGraphPropertyNull() {
        assertEquals(null, dc.toGraphProperty(null));
    }

    @Test
    public void toGraphProperty() {
        Date date = new Date();
        dc.toGraphProperty(date);
    }

    @Test
    public void toEntityAttributeNull() {
        assertEquals(null, dc.toGraphProperty(null));
    }

    @Test
    public void toEntityAttribute() {
        dc.toEntityAttribute("1995-04-01 10:10:10");
    }

    @Test
    public void toEntityAttributeFail() {
        assertThrows(IllegalArgumentException.class, () -> dc.toEntityAttribute("asdf"));
    }
}
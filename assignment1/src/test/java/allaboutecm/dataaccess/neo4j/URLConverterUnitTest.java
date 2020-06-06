package allaboutecm.dataaccess.neo4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class URLConverterUnitTest {
    private static URLConverter uc;

    @BeforeEach
    public void setUp(){
        uc = new URLConverter();
    }
    @Test
    void toGraphProperty() throws MalformedURLException {
        URL url = new URL("https://www.google.com/");
        assertEquals("https://www.google.com/", uc.toGraphProperty(url));
    }

    @Test
    void toGraphPropertyNull() {
        assertEquals(null, uc.toGraphProperty(null));
    }


    @Test
    void toEntityAttribute() throws MalformedURLException {
        URL url = new URL("https://www.google.com/");
        assertEquals(url, uc.toEntityAttribute("https://www.google.com/"));
    }

    @Test
    void toEntityAttributeNull() {
        assertEquals(null, uc.toEntityAttribute(null));
    }

    @Test
    void toEntityAttributeException() {
        assertThrows(IllegalArgumentException.class, () -> uc.toEntityAttribute("asdf"));
    }
}
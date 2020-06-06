package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConcertsUnitTest {
    private Concerts concert;
    private Date date;
    private String name;
    private String location;
    private String country;
    private List<Musician> featuredMusicians;

    @BeforeEach
    void setUp() {
        date = new Date();
        name = "The Name";
        country = "Country";
        location = "Location";
        Musician m = new Musician("M n");
        featuredMusicians = Arrays.asList(m);
        concert = new Concerts(date, name);

    }

    @Test
    public void toStringTest() {
        assertEquals(("Concerts [date=" + date + ", name=The Name, location=null, country=null, featuredMusicians=[]]") , concert.toString());
    }

    @Test
    public void hashCodeTest() {
        concert.setCountry(country);
        concert.setLocation(location);
        concert.setFeaturedMusicians(featuredMusicians);
        final int prime = 31;
        int result = 1;
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((featuredMusicians == null) ? 0 : featuredMusicians.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        assertEquals(result, concert.hashCode());
    }

    @Test
    void equals() {
        assertTrue(concert == concert);
    }

    @Test
    void getDate() {
        assertEquals(date, concert.getDate());
    }

    @Test
    void setDate() {
        Date newDate = new Date();
        concert.setDate(newDate);
        assertEquals(newDate, concert.getDate());
    }

    @Test
    void getName() {
        assertEquals(name, concert.getName());
    }

    @Test
    void setName() {
        String newName = "New Name";
        concert.setName(newName);
        assertEquals(newName, concert.getName());
    }

    @Test
    void getLocation() {
        concert.setLocation(location);
        assertEquals(location, concert.getLocation());
    }


    @Test
    void getCountry() {
        concert.setCountry(country);
        assertEquals(country, concert.getCountry());
    }



    @Test
    void getFeaturedMusicians() {
        concert.setFeaturedMusicians(featuredMusicians);
        assertEquals(featuredMusicians, concert.getFeaturedMusicians());
    }


}
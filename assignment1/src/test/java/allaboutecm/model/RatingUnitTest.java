package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RatingUnitTest {

    private Rating rating;

    @BeforeEach
    public void setUp() {
        rating = new Rating(5, "Rolling Stone Magazine");
    }

    @Test
    @DisplayName("Source cannot be null")
    public void sourceCannotBeNull() {
        assertThrows(NullPointerException.class, () -> rating.setSource(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Source cannot be empty or blank")
    public void sourceCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> rating.setSource(arg));
    }

    @Test
    @DisplayName("Source cannot be different")
    public void sourceSameOrNot() {
        rating.setSource("Rolling Stone Magazine");
        assertEquals(rating.getSource(), "Rolling Stone Magazine");
    }

    @Test
    @DisplayName("Rating Cannot Be Greater Than 5")
    public void ratingCannotBeGreaterThan5() {
        assertThrows(IllegalArgumentException.class, () -> rating.setRatingScore(6));
    }

    @Test
    @DisplayName("Rating Cannot Be Lower Than 1")
    public void ratingCannotBeLowerThan1() {
        assertThrows(IllegalArgumentException.class, () -> rating.setRatingScore(0));
    }

    @Test
    @DisplayName("Upper limit boundary test failed for rating")
    public void ratingTestForUpperLimit() {
        rating.setRatingScore(5);
    }

    @Test
    @DisplayName("Lower limit boundary test failed for rating")
    public void ratingTestForLowerLimit() {
        rating.setRatingScore(1);
    }

    @Test
    @DisplayName("Rating cannot be different")
    public void ratingSameOrNot() {
        assertEquals(rating.getRatingScore(), 5);
    }

    @Test
    @DisplayName("Lower limit boundary test failed for rating")
    public void constructorRatingTestForLowerLimit() {
        rating = new Rating(1,  "Life Magazine");
    }

    @Test
    @DisplayName("Rating Cannot Be Greater Than 5")
    public void constructorRatingCannotBeGreaterThan5() {
        assertThrows(IllegalArgumentException.class, () -> new Rating(6,  "Life Magazine"));
    }

    @Test
    @DisplayName("Rating Cannot Be Lower Than 1")
    public void constructorRatingCannotBeLowerThan1() {
        assertThrows(IllegalArgumentException.class, () -> new Rating(0,  "Life Magazine"));
    }

    @Test
    @DisplayName("Upper limit boundary test failed for rating")
    public void constructorRatingTestForUpperLimit() {
        rating = new Rating(5,  "Life Magazine");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Constructor can't set empty source")
    //Constructor must not be able to set an empty record number, similar to setRecordNumber() above
    public void constructorSourceEmptyValidate(String arg) {
        assertThrows(IllegalArgumentException.class, () -> new Rating(5, arg));
    }

    @Test
    @DisplayName("Constructor can't set null source")
    //Constructor must not be able to set a null album name, similar to setAlbumName() above
    public void constructorSourceNullValidate() {
        assertThrows(NullPointerException.class, () -> new Rating(5, null));
    }



    @Test
    @DisplayName("Rating objects cannot be different")
    public void ratingsSameOrNot() {
        Rating rating1 = new Rating(5, "Rolling Stone Magazine");
        assertEquals(rating, rating1);
    }

    @Test
    @DisplayName("Equals should return a false in case of null")
    public void ratingEqualsNull(){
        assertEquals(rating.equals(null), false);
    }

    @Test
    @DisplayName("Equals should return a false when there are different objects")
    public void trackEqualsDifferentObject(){
        assertEquals(rating.equals(new Track("Test","4:11","Jazz",1)), false);
    }

    @Test
    @DisplayName("Equals should return a false when there are different values in objects")
    public void trackEqualsShouldBeFalse(){
        Rating rating1 = new Rating(1, "Rolling Stone Magazine");
        assertEquals(rating.equals(rating1), false);
    }

}

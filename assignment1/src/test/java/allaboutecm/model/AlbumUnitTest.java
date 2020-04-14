package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
//nupur is here
class AlbumUnitTest {
    //Ben was here
    private Album album;

    @BeforeEach
    public void setUp() {
        album = new Album(1975, "ECM 1064/65", "The Köln Concert"
               );

        List<Musician> musician= Arrays.asList(new Musician("Keith Jarrett"));
        Set<Musician> musicianSet=musician.stream().collect(Collectors.toSet());
    }


    @Test
    @DisplayName("Album name cannot be null")
    public void albumNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setAlbumName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Album name cannot be empty or blank")
    public void albumNameConnotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setAlbumName(arg));
    }

    @Test
    @DisplayName("album Name cannot be different")
    public void albumNameSameOrNot(){
        assertEquals(album.getAlbumName(),"The Köln Concert");
    }


    @Test
    @DisplayName("Album Record Number cannot be null")
    public void recordNumberCannotBeNull() {
        assertThrows(NullPointerException.class, () -> album.setRecordNumber(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Album Record Number cannot be empty or blank")
    public void recordNumberCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> album.setRecordNumber(arg));
    }

    @Test
    @DisplayName("record Number cannot be different")
    public void recordNumberSameOrNot(){
        assertEquals(album.getRecordNumber(),"ECM 1064/65");
    }


    @ParameterizedTest
    @DisplayName("Album release Year cannot be empty or zero")
    public void releaseYearCannotBeEmptyOrZero() {
        int arg=0;
        assertThrows(IllegalArgumentException.class, () -> album.setReleaseYear(arg));
    }








    @Test
    @DisplayName("record Number cannot be different")
    public void releaseYearSameOrNot(){
        assertEquals(album.getReleaseYear(),1975);
    }










    @Test
    public void sameNameAndNumberMeansSameAlbum() {
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");

        assertEquals(album, album1);
    }


}
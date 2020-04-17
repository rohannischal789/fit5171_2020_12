package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class MusicianUnitTest {
    private Musician musician;

    @BeforeEach
    public void setUp(){
        musician = new Musician("anything name");
    }

    @Test
    @DisplayName("Correctly match Musician name")
    public void matchMusicianName(){
        assertEquals(musician.getName(), "anything name");
    }

    @Test
    @DisplayName("Non-null name")
    public void nullMusicianNameError(){
        assertThrows(NullPointerException.class, () -> new Musician(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Musician name cannot be empty or blank")
    public void MusicianNameCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> new Musician(arg));
    }

    @Test
    @DisplayName("firstname lastname")
    public void substringTest(){
        assertThrows(IllegalArgumentException.class, () -> new Musician("Ben"));
    }


    @Test
    @DisplayName("album set test")
    public void albumsetTest(){
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album2 = new Album(1976, "ECM 1063/66", "The Köln Concert1");
        Album album3 = new Album(1977, "ECM 1065/67", "The Köln Concert2");
        Album albs[] = {album1, album2, album3};
        Set<Album> albumSet = new HashSet<>(Arrays.asList(albs));
        Set<Album> albumSet2 = new HashSet<>(Arrays.asList(albs));
        musician.setAlbums(albumSet);
        assertEquals(musician.getAlbums(),albumSet2);
    }

    @Test
    @DisplayName("Non-null albumSet")
    public void nullalbumSetError(){
        assertThrows(NullPointerException.class, () ->  musician.setAlbums(null));
    }

    @Test
    @DisplayName("Names do match")
    public void equalsmusicianNameMatch(){
        Musician musician2 = musician;
        assertEquals(musician.equals(musician2), true);
    }

    @Test
    @DisplayName("Names Do not match")
    public void equalsmusicianNameDoesNotMatch(){
        Musician musician3 = new  Musician("Nupur Ben");
        assertEquals(musician.equals(musician3), false);
    }

    @Test
    @DisplayName("Musicians Exactly match case = URL")
    public void equalsmusicianNameExactlyMatch(){
        try
        {
            Musician musician4 = new Musician("anything name");
            musician4.setMusicianUrl(new URL("https://google.com"));
            assertEquals(musician.equals(musician4), false);
        }
        catch(Exception e)
        {

        }
    }

    @Test
    @DisplayName("Musicians Exactly match case = Album Set")
    public void equalsmusicianNameExactlyMatchAlbum() {

        Musician musician5 = new Musician("anything name");
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album albs[] = {album1};
        Set<Album> albumSet = new HashSet<>(Arrays.asList(albs));
        musician5.setAlbums(albumSet);
        assertEquals(musician.equals(musician5), false);
    }

    @Test
    @DisplayName("URL correctly sets")
    public void URLSetTest(){
        try {
            URL theURL = new URL("https://www.google.com");
            musician.setMusicianUrl(theURL);
            assertEquals(musician.getMusicianUrl(), theURL);
        } catch (Exception e){
            //This will never throw because the URL is static and correct.
        }
    }

        //We had planned to write tests for the form of the URL, that it should start with http://, https:// or www. and that it should end with .XXX, .XXX.XX or .XX, where X is some character.
        //The URL class handles all of these behviours and does not require us to test, it is not possible for us to create a malformed URL to pass to the SetURL method.
    /*
    public void MalformedURLTest(){
        try {
            URL theURL = new URL("https://www.google.com");
            musician.setMusicianUrl(theURL);
            assertEquals(musician.getMusicianUrl(), theURL);
        } catch (Exception e){
            //This will never throw because the URL is static and correct.
            //This will never throw because the URL is static and correct.
        }
    }
    */


}

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
    @DisplayName("Musician Name does not set/get correctly")
    public void matchMusicianName(){
        assertEquals(musician.getName(), "anything name");
    }

    @Test
    @DisplayName("Musician Name is Null")
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
    @DisplayName("Name must match the form xxx xxx")
    public void substringTest(){
        assertThrows(IllegalArgumentException.class, () -> new Musician("Ben"));
    }


    @Test
    @DisplayName("Musician Albums does not set/get correctly")
    public void albumSetTest(){
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
    public void nullAlbumSetError(){
        assertThrows(NullPointerException.class, () ->  musician.setAlbums(null));
    }

    @Test
    @DisplayName("Musician equals does not correctly match")
    public void equalsMusicianNameMatch(){
        Musician musician2 = musician;
        assertEquals(musician.equals(musician2), true);
    }

    @Test
    @DisplayName("Musician equals reports a false true when Musician names do not match")
    public void equalsMusicianNameDoesNotMatch(){
        Musician musician3 = new  Musician("Nupur Ben");
        assertEquals(musician.equals(musician3), false);
    }

    @Test
    @DisplayName("Musician equals reports a false true when URLs do not match")
    public void equalsMusicianNameExactlyMatch(){
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
    @DisplayName("Musician equals reports a false true when Album Sets do not match")
    public void equalsmusicianNameExactlyMatchAlbum() {

        Musician musician5 = new Musician("anything name");
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album albs[] = {album1};
        Set<Album> albumSet = new HashSet<>(Arrays.asList(albs));
        musician5.setAlbums(albumSet);
        assertEquals(musician.equals(musician5), false);
    }

    @Test
    @DisplayName("URL did not correctly set.")
    public void URLSetTest(){
        try {
            URL theURL = new URL("https://www.google.com");
            musician.setMusicianUrl(theURL);
            assertEquals(musician.getMusicianUrl(), theURL);
        } catch (Exception e){
            //This will never throw because the URL is static and correct.
        }
    }
    //TODO new constraint on URL must be patterned correctly for ECM
    //TODO length check for name
    //TODO new attributes bio, artist external site, wikipage.

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

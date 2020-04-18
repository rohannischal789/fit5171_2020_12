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
    //We must be able to retrieve and match the name attribute.
    public void matchMusicianName(){
        assertEquals(musician.getName(), "anything name");
    }

    @Test
    @DisplayName("Musician Name is Null")
    //We must not be able to set a null name
    public void nullMusicianNameError(){
        assertThrows(NullPointerException.class, () -> new Musician(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Musician name cannot be empty or blank")
    //We must not be able to set an empty string or otherwise blank name. These strings taken from AlbumUnitTest
    public void MusicianNameCannotBeEmptyOrBlank(String arg) {
        assertThrows(IllegalArgumentException.class, () -> new Musician(arg));
    }

    @Test
    @DisplayName("Name must match the form xxx xxx")
    //Musician must have a name which consists of at least two strings of characters separated by a space
    public void substringTest(){
        assertThrows(IllegalArgumentException.class, () -> new Musician("Ben"));
    }

    @Test
    @DisplayName("Boundary Error, Name may be 100 characters long, but no longer")
    //Musicians names must be permitted to be 100 characters long, but no longer
    public void validButLongNameTest(){
        new Musician("a aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    }

    @Test
    @DisplayName("Names are capped to 100 characters")
    //Musician name may not be longer than 100 characters. The tested string is 101 characters.
    public void nameLengthTest(){
        assertThrows(IllegalArgumentException.class, () -> new Musician("a aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }


    @Test
    @DisplayName("Musician Albums does not set/get correctly")
    //The list of albums retrieved by getAlbum must match the input given to setAlbum.
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
    //The set of albums must not be equal to null
    public void nullAlbumSetError(){
        assertThrows(NullPointerException.class, () ->  musician.setAlbums(null));
    }

    @Test
    @DisplayName("Musician equals does not correctly match")
    //This is the base case for matching using .equals, two musicians with matching details must register as equal.
    public void equalsMusicianNameMatch(){
        Musician musician2 = musician;
        assertEquals(musician.equals(musician2), true);
    }

    @Test
    @DisplayName("Musician equals reports a false true when Musician names do not match")
    //Equals must report false when the Musician names do not match
    public void equalsMusicianNameDoesNotMatch(){
        Musician musician3 = new  Musician("Nupur Ben");
        assertEquals(musician.equals(musician3), false);
    }

    @Test
    @DisplayName("Musician equals reports a false true when URLs do not match")
    //Equals must be false when the URLs of the Musicians do not match
    public void equalsMusicianExactlyMatchURL(){
        try
        {
            Musician musician4 = new Musician("anything name");
            musician4.setMusicianUrl(new URL("https://www.ecm.com/something_band"));
            assertEquals(musician.equals(musician4), false);
        }
        catch(Exception e)
        {

        }
    }

    @Test
    @DisplayName("Musician equals reports a false true when Album Sets do not match")
    //Equals must be false when albums do not match
    public void equalsMusicianExactlyMatchAlbum() {

        Musician musician5 = new Musician("anything name");
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album albs[] = {album1};
        Set<Album> albumSet = new HashSet<>(Arrays.asList(albs));
        musician5.setAlbums(albumSet);
        assertEquals(musician.equals(musician5), false);
    }

    @Test
    @DisplayName("URL did not correctly set.")
    //Test that URL sets successfully
    public void URLSetTest(){
        try {
            URL theURL = new URL("https://www.ecm.com/something_band");
            musician.setMusicianUrl(theURL);
            assertEquals(musician.getMusicianUrl(), theURL);
        } catch (Exception e){
            //This will never throw because the URL is static and correct.
        }
    }

    @Test
    @DisplayName("URL must be on the ECM domain")
    //Test that URL may only be on the ECM domain
    public void URLvalidationTest(){
        try {
            URL theURL = new URL("https://www.google.com");
            assertThrows(IllegalArgumentException.class, () -> musician.setMusicianUrl(theURL));
        } catch (Exception e){

        }
    }

    @Test
    @DisplayName("Album Entry cannot be null")
    //Items in the list of albums may not be null
    public void nullAlbumsTest() {
        Album[] blankArray = {null, null};
        Set<Album> emptyItems = new HashSet<>(Arrays.asList(blankArray));
        assertThrows(NullPointerException.class, () -> musician.setAlbums(emptyItems));
    }

    //TODO new attributes bio, artist external site, wikipage.



}

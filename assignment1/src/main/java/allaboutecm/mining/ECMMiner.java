package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.model.*;
import com.google.common.collect.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Note that you can extend the Neo4jDAO class to make implementing this class easier.
 */
public class ECMMiner {
    private final DAO dao;

    public ECMMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     * Returns the most prolific musician in terms of number of albums released.
     *
     * @Param k the number of musicians to be returned.
     * @Param startYear, endYear between the two years [startYear, endYear].
     * When startYear/endYear is negative, that means startYear/endYear is ignored.
     */
    public List<Musician> mostProlificMusicians(int k, int startYear, int endYear) {
        Collection<Musician> musicians = dao.loadAll(Musician.class);
        Map<String, Musician> nameMap = Maps.newHashMap();
        for (Musician m : musicians) {
            nameMap.put(m.getName(), m);
        }

        ListMultimap<String, Album> multimap = MultimapBuilder.treeKeys().arrayListValues().build();
        ListMultimap<Integer, Musician> countMap = MultimapBuilder.treeKeys().arrayListValues().build();

        for (Musician musician : musicians) {
            Set<Album> albums = musician.getAlbums();
            for (Album album : albums) {
                boolean toInclude =
                        !((startYear > 0 && album.getReleaseYear() < startYear) ||
                                (endYear > 0 && album.getReleaseYear() > endYear));

                if (toInclude) {
                    multimap.put(musician.getName(), album);
                }
            }
        }

        Map<String, Collection<Album>> albumMultimap = multimap.asMap();

        for (Map.Entry<String,Collection<Album>> name : albumMultimap.entrySet()) {
            Collection<Album> albums = albumMultimap.get(name.getKey());
            int size = albums.size();
            countMap.put(size, nameMap.get(name.getKey()));
        }

        List<Musician> result = Lists.newArrayList();
        List<Integer> sortedKeys = Lists.newArrayList(countMap.keySet());
        sortedKeys.sort(Ordering.natural().reverse());
        for (Integer count : sortedKeys) {
            if (result.size() >= k) break;
            List<Musician> list = countMap.get(count);
            result.addAll(list);
        }

        return result;
    }

    /**
     * Most talented musicians by the number of different musical instruments they play
     *
     * @Param k the number of musicians to be returned.
     */
    public List<Musician> mostTalentedMusicians(int k) {
        if (k < 1) {
            throw new IllegalArgumentException();
        }
        //Let's grab all the musician instruments.
        Collection<MusicianInstrument> musicianInstruments = dao.loadAll(MusicianInstrument.class);
        if (k > musicianInstruments.size()) {
            throw new IllegalArgumentException();
        }
        Map<MusicianInstrument, Integer> countMap = Maps.newHashMap();

        for (MusicianInstrument m : musicianInstruments) {
            Integer instrumentCount = m.getMusicalInstruments().size();
            countMap.put(m, instrumentCount);
        }
        List<Map.Entry<MusicianInstrument, Integer>> sortList = new LinkedList<>(countMap.entrySet());

        Collections.sort(sortList, Comparator.comparing(Map.Entry::getValue));
        ArrayList resultList = new ArrayList();
        for (int i = sortList.size() - 1; i >= sortList.size() - k; i--) {
            resultList.add(sortList.get(i).getKey().getMusician());
        }

        return resultList;
    }

    /**
     * Musicians that collaborate the most widely, by the number of other musicians they work with on albums.
     *
     * @Param k the number of musicians to be returned.
     */

    public List<Musician> mostSocialMusicians(int k) {

        if (k < 1) {
            throw new IllegalArgumentException();
        }

        Collection<Musician> musicians = dao.loadAll(Musician.class);

        if (k > musicians.size()) {
            throw new IllegalArgumentException();
        }

        //Get the list of musicians.
        //I want a multimap, I'm not certain how the builder works but this is how to make one.
        SetMultimap<Musician, Musician> collaboratorMap = MultimapBuilder.hashKeys().hashSetValues().build();
        //Go through each musician
        for (Musician m : musicians) {
            //For each musician, loop through their albums
            Set<Album> thisAlbumSet = m.getAlbums();
            for (Album a : thisAlbumSet) {
                //For each album, loop through all musicians again to check if they have the same album in their album list.
                for (Musician m1 : musicians) {
                    if (m1.getAlbums().contains(a)) {
                        //If they do, record them as a collaborator.
                        collaboratorMap.put(m, m1);
                    }
                }
            }
        }
        //Let's make a hashmap to store Musicians against their collborator count, we don't care about the actual collaborators
        HashMap<Musician, Integer> countMap = Maps.newHashMap();
        for (Musician m : collaboratorMap.keys()) {
            //Store each musician against their collaborator count.
            countMap.put(m, collaboratorMap.get(m).size());
        }
        //Create a linked list to sort the entries.
        List<Map.Entry<Musician, Integer>> sortList = new LinkedList<>(countMap.entrySet());
        Collections.sort(sortList, Comparator.comparing(Map.Entry::getValue));

        ArrayList<Musician> returnList = new ArrayList<>();
        for (int i = sortList.size() - 1; i >= sortList.size() - k; i--) {
            returnList.add(sortList.get(i).getKey());
        }

        return returnList;
    }

    /**
     * Busiest year in terms of number of albums released.
     *
     * @Param k the number of years to be returned.
     */

    public List<Integer> busiestYears(int k) {
        //Load all albums:
        if (k < 1) {
            throw new IllegalArgumentException();
        }
        Collection<Album> albums = dao.loadAll(Album.class);
        Map<Integer, Integer> countMap = Maps.newHashMap();
        for (Album a : albums) {
            Integer year = a.getReleaseYear();
            if (null != countMap.get(year)) {
                countMap.put(year, countMap.get(year) + 1);
            } else {
                countMap.put(year, 1);
            }
        }
        //Create a map of Integer, Integer where the year is the key, and the value is the count of albums From your Integer/Album hashMap

        List<Map.Entry<Integer, Integer>> sortList = new LinkedList<>(countMap.entrySet());

        Collections.sort(sortList, Comparator.comparing(Map.Entry::getValue));
        //Create an arrayList of the top K years, and return it
        ArrayList resultList = new ArrayList();
        for (int i = sortList.size() - 1; i >= sortList.size() - k; i--) {
            resultList.add(sortList.get(i).getKey());
        }
        return resultList;
    }

    /**
     * Most similar albums to a give album. The similarity can be defined in a variety of ways.
     * For example, it can be defined over the musicians in albums, the similarity between names
     * of the albums & tracks, etc.
     *
     * @Param k the number of albums to be returned.
     * @Param album
     */

    public List<Album> mostSimilarAlbums(int k, Album album) {
        //If K is impossible throw exception
        if (k < 0) {
            throw new IllegalArgumentException();
        }
        Collection<Album> albums = dao.loadAll(Album.class);
        //Check if our given album is in the search space, if it isn't throw an exception, if it is, prune it from the search space.
        //We do this because an album is always going to get the highest similarity score for itself, but this isn't useful to know.
        if (albums.contains(album)) {
            albums.remove(album);
        } else {
            throw new IllegalArgumentException();
        }
        //If we're looking for more results than there are albums, throw exception
        if (k > albums.size()) {
            throw new IllegalArgumentException();
        }
        Map<Album, Integer> scoreMap = Maps.newHashMap();
        for (Album a : albums) {
            //Let's loop through each album and assign a score
            int score = 0;
            for (Musician m : album.getFeaturedMusicians()) {
                //If the two albums share any musicians, award two points for each.
                if (a.getFeaturedMusicians().contains(m)) {
                    score += 2;
                }
            }
            //If the albums were released in the same year, award a bonus point.
            if (album.getReleaseYear() == a.getReleaseYear()) {
                score += 1;
            }
            //Now add the score to our score map
            scoreMap.put(a, score);
        }
        //This is the structure we used to sort in previous examples, it will produce a list ordered smallest to largest.
        List<Map.Entry<Album, Integer>> sortList = new LinkedList<>(scoreMap.entrySet());

        Collections.sort(sortList, Map.Entry.comparingByValue());
        //Loop from the end of the list towards the beginning, back k entries.
        ArrayList resultList = new ArrayList();
        for (int i = sortList.size() - 1; i >= sortList.size() - k; i--) {
            resultList.add(sortList.get(i).getKey());
        }
        return resultList;
    }

    public List<Album> highestRatedAlbums(int k) {
        //If K is impossible throw exception
        if (k < 1) {
            throw new IllegalArgumentException();
        }
        Collection<Album> albums = dao.loadAll(Album.class);

        //If we're looking for more results than there are albums, throw exception
        if (k > albums.size()) {
            throw new IllegalArgumentException();
        }
        Map<Album, Float> ratingsMap = Maps.newHashMap();
        for (Album a : albums) {
            //Let's loop through each album and assign a score
            int total = 0;
            int count = 0;
            for (Rating r : a.getRatings()) {
                total += r.getRatingScore();
                count++;
            }
            //Now add the score to our score map
            if (count != 0)
                ratingsMap.put(a, (total /(float) count));
        }
        //This is the structure we used to sort in previous examples, it will produce a list ordered smallest to largest.
        List<Map.Entry<Album, Float>> sortList = new LinkedList<>(ratingsMap.entrySet());

        Collections.sort(sortList, Map.Entry.comparingByValue());
        //Loop from the end of the list towards the beginning, back k entries.
        ArrayList resultList = new ArrayList();
        for (int i = sortList.size() - 1; i >= sortList.size() - k; i--) {
            resultList.add(sortList.get(i).getKey());
        }
        return resultList;
    }

    public List<Album> mostSellingAlbums(int k) {
        //If K is impossible throw exception
        if (k < 1) {
            throw new IllegalArgumentException();
        }
        Collection<Album> albums = dao.loadAll(Album.class);

        //If we're looking for more results than there are albums, throw exception
        if (k > albums.size()) {
            throw new IllegalArgumentException();
        }
        Map<Album, Integer> ratingsMap = Maps.newHashMap();
        for (Album a : albums) {
            //Let's loop through each album and assign a score
            ratingsMap.put(a, a.getSales());
        }
        //This is the structure we used to sort in previous examples, it will produce a list ordered smallest to largest.
        List<Map.Entry<Album, Integer>> sortList = new LinkedList<>(ratingsMap.entrySet());

        Collections.sort(sortList, Comparator.comparing(Map.Entry::getValue));
        //Loop from the end of the list towards the beginning, back k entries.
        ArrayList resultList = new ArrayList();
        for (int i = sortList.size() - 1; i >= sortList.size() - k; i--) {
            resultList.add(sortList.get(i).getKey());
        }
        return resultList;
    }

    public List<Concerts> findNextConcerts(int k) {

        long today = new Date().getTime();

        Collection<Concerts> concerts = dao.loadAll(Concerts.class);
        List<Concerts> listOfSales = concerts.stream()
                .filter(s -> s.getDate().getTime() > today)
                .sorted((s1, s2) -> (int) (s1.getDate().getTime() - s2.getDate().getTime()))
                .collect(Collectors.toList());

        List<Concerts> result = Lists.newArrayList();
        Set<Concerts> set = Sets.newHashSet();
        Date lastTime = new Date();

        for (Concerts s : listOfSales) {
            if (result.size() >= k && !lastTime.equals(s.getDate())) break;
            if (!set.contains(s)) {
                result.add(s);
                set.add(s);
                lastTime = s.getDate();
            }
        }

        return result;
    }


}

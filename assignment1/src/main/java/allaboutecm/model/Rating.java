package allaboutecm.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.util.Objects;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

@NodeEntity
public class Rating extends Entity {

    @Property(name="ratingScore")
    private int ratingScore;

    @Property(name="source")
    private String source;

    public Rating(int ratingScore, String source) {
        notNull(source);
        notBlank(source);
        if (ratingScore <= 0 || ratingScore > 5){
            throw new IllegalArgumentException();
        }
        this.ratingScore = ratingScore;
        this.source = source;
    }

    public int getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(int ratingScore) {
        if (ratingScore <= 0 || ratingScore > 5){
        throw new IllegalArgumentException();
    }
        this.ratingScore = ratingScore;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        notNull(source);
        notBlank(source);
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return ratingScore == rating.ratingScore &&
                source.equals(rating.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ratingScore, source);
    }
}

package matchers;

import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Contains hamcrest matchers for matching some collection properties
 *
 * @author Tim Blok, Frans van Dijk, Yannick Mijsters, Ramon Onis, Tim Sonderen; University of Twente
 */
public final class CollectionMatchers {

    private CollectionMatchers() {
    }

    public static <T> Matcher<Collection<T>> sizeIs(final int i) {
        return new FeatureMatcher<Collection<T>, Integer>(equalTo(i), "size", "size") {
            @Override
            protected Integer featureValueOf(Collection<T> collection) {
                return collection.size();
            }
        };
    }

    public static <T> Matcher<Collection<T>> containsAll(final T[] items) {
        return new TypeSafeDiagnosingMatcher<Collection<T>>() {
            @Override
            protected boolean matchesSafely(Collection<T> collection, Description mismatchDescription) {
                Collection<T> col = new HashSet<>();
                col.addAll(Arrays.asList(items));
                col.removeAll(collection);
                mismatchDescription.appendText("but it did not contain ").appendValue(col.toArray());
                return collection.containsAll(Arrays.asList(items));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("should contain all items ").appendValue(items);
            }
        };
    }
}

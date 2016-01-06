import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.junit.Test;

import java.util.*;

public class Tests {

    private static final HashFunction[] FUNCTIONS = {
            Hashing.crc32(),
            Hashing.murmur3_128(),
            Hashing.sha1(),
            Hashing.md5()
    };

    @Test
    public void words() {
        for (HashFunction f : FUNCTIONS) {
            System.out.println(name(f));

            Map<HashCode, Set<String>> counts = new HashMap<>();

            Words.forEachWord(word -> {
                HashCode hash = f.newHasher().putUnencodedChars(word).hash();
                Set<String> count = counts.computeIfAbsent(hash, newHash -> new HashSet<>());

                count.add(word);
            });

            counts.values().stream().filter(set -> set.size() > 1).forEach(set -> System.out.println(set));
        }
    }

    @Test
    public void numbers() {
        for (HashFunction f : FUNCTIONS) {
            System.out.println(name(f));

            Map<HashCode, Set<Long>> counts = new HashMap<>();
            Random random = new Random();

            for (int i = 0; i < 1_000_000; i++) {
                long value = random.nextLong();
                HashCode hash = f.newHasher().putLong(value).hash();
                Set<Long> count = counts.computeIfAbsent(hash, newHash -> new HashSet<>());

                count.add(value);
            }

            counts.values().stream().filter(set -> set.size() > 1).forEach(set -> System.out.println(set));
        }
    }

    private static class WordNumber {
        public final String word;
        public final long number;

        private WordNumber(String word, long number) {
            this.word = word;
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WordNumber that = (WordNumber) o;
            return number == that.number &&
                   Objects.equals(word, that.word);
        }

        @Override
        public int hashCode() {
            return Objects.hash(word, number);
        }

        @Override
        public String toString() {
            return "(" + word + ", " + number + ")";
        }
    }

    @Test
    public void wordsWithNumbers() {
        for (HashFunction f : FUNCTIONS) {
            System.out.println(name(f));

            Map<HashCode, Set<WordNumber>> counts = new HashMap<>();
            Random random = new Random();

            Words.forEachWord(word -> {
                long number = random.nextLong();
                WordNumber value = new WordNumber(word, number);
                HashCode hash = f.newHasher().putUnencodedChars(word).putLong(number).hash();
                Set<WordNumber> count = counts.computeIfAbsent(hash, newHash -> new HashSet<>());

                count.add(value);
            });

            counts.values().stream().filter(set -> set.size() > 1).forEach(set -> System.out.println(set));
        }
    }

    private String name(HashFunction f) {
        return f.toString();
    }

    @Test
    public void wordAlwaysSame() {
        for (HashFunction f : FUNCTIONS) {
            System.out.println(name(f));

            Map<HashCode, Set<WordNumber>> counts = new HashMap<>();
            Random random = new Random();

            for (int i = 0; i < 235886; i++) {
                String word = "battological";
                long number = random.nextLong();
                WordNumber value = new WordNumber(word, number);
                HashCode hash = f.newHasher().putUnencodedChars(word).putLong(number).hash();
                Set<WordNumber> count = counts.computeIfAbsent(hash, newHash -> new HashSet<>());

                count.add(value);
            }

            counts.values().stream().filter(set -> set.size() > 1).forEach(set -> System.out.println(set));
        }
    }

    @Test
    public void numberAlwaysSame() {
        for (HashFunction f : FUNCTIONS) {
            System.out.println(name(f));

            Map<HashCode, Set<WordNumber>> counts = new HashMap<>();
            Random random = new Random();
            long number = random.nextLong();

            Words.forEachWord(word -> {
                WordNumber value = new WordNumber(word, number);
                HashCode hash = f.newHasher().putUnencodedChars(word).putLong(number).hash();
                Set<WordNumber> count = counts.computeIfAbsent(hash, newHash -> new HashSet<>());

                count.add(value);
            });

            counts.values().stream().filter(set -> set.size() > 1).forEach(set -> System.out.println(set));
        }
    }
}

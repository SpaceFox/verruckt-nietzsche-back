package fr.spacefox.verrucktnietzsche.data;

import io.vertx.core.impl.ConcurrentHashSet;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class Dictionary {

    private final Set<String> allWords = new ConcurrentHashSet<>();
    private final Set<String> goodWords = new ConcurrentHashSet<>();
    private final Set<String> badWords = new ConcurrentHashSet<>();

    public void load(List<String> standardWords, List<String> goodWordList, List<String> badWordList) {
        allWords.addAll(normalize(standardWords));
        goodWords.addAll(normalize(goodWordList));
        badWords.addAll(normalize(badWordList));
        // Just to be sure
        allWords.addAll(goodWords);
        allWords.addAll(badWords);
    }

    private Set<String> normalize(List<String> goodWordList) {
        return goodWordList.stream()
                .map(it -> it.toLowerCase(Locale.ENGLISH))
                .collect(Collectors.toSet());
    }

    public int size() {
        return allWords.size();
    }

    public boolean contains(String word) {
        return allWords.contains(word);
    }

    public boolean isGood(String word) {
        return goodWords.contains(word);
    }

    public boolean isBad(String word) {
        return badWords.contains(word);
    }
}

package fr.spacefox.verrucktnietzsche.service;

import fr.spacefox.verrucktnietzsche.data.Dictionary;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AnalyzerService {

    private static final Logger LOGGER = Logger.getLogger(AnalyzerService.class.getSimpleName());

    @ConfigProperty(name = "thresholds.goodevil") double goodEvilThreshold;

    @Inject
    Dictionary dictionary;

    public Balance analyze(List<String> sentence) {
        List<String> knownWords = sentence.stream()
                .filter(word -> dictionary.contains(word))
                .collect(Collectors.toList());
        long goodWords = knownWords.stream()
                .filter(word -> dictionary.isGood(word))
                .count();
        long badWords = knownWords.stream()
                .filter(word -> dictionary.isBad(word))
                .count();
        boolean negation = knownWords.stream()
                .filter("not"::equals)
                .count() % 2 == 1;
        long wordCount = knownWords.size();
        boolean isGood = negation != ((((double) goodWords) / wordCount) > goodEvilThreshold);
        boolean isBad =  negation != ((((double) badWords) / wordCount) > goodEvilThreshold);

        Balance balance = isGood
                ? (isBad ? Balance.PARADOXAL : Balance.GOOD)
                : (isBad ? Balance.EVIL : Balance.NEUTRAL);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debugv("{0} is {1} ({2} good words, {3} bad words)", sentence, balance, goodWords, badWords);
        }

        return balance;
    }
}

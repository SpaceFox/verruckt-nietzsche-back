package fr.spacefox.verrucktnietzsche.service;

import fr.spacefox.verrucktnietzsche.data.Dictionary;
import fr.spacefox.verrucktnietzsche.io.GoodEvil;
import fr.spacefox.verrucktnietzsche.io.GoodEvilResponse;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

@ApplicationScoped
public class GoodEvilService {

    private static final Logger LOGGER = Logger.getLogger(GoodEvilService.class.getSimpleName());

    private static final Map<Balance, GoodEvil[]> GOOD_EVIL_VALUES = new EnumMap<>(
            Map.ofEntries(
                    Map.entry(
                            Balance.NEUTRAL,
                            new GoodEvil[]{
                                    GoodEvil.GREATER_GOOD,
                                    GoodEvil.GOOD,
                                    GoodEvil.LESSER_GOOD,
                                    GoodEvil.BEYOND_GOOD,
                                    GoodEvil.GREATER_EVIL,
                                    GoodEvil.EVIL,
                                    GoodEvil.LESSER_EVIL,
                                    GoodEvil.BEYOND_EVIL,
                                    GoodEvil.BEYOND_GOOD_EVIL,
                                    GoodEvil.NOT_GOOD_NOR_EVIL,
                                    GoodEvil.NEUTRAL,
                                    GoodEvil.PARADOXAL,
                            }),
                    Map.entry(
                            Balance.GOOD,
                            new GoodEvil[]{
                                    GoodEvil.GREATER_GOOD,
                                    GoodEvil.GOOD,
                                    GoodEvil.LESSER_GOOD,
                                    GoodEvil.BEYOND_GOOD,
                                    GoodEvil.BEYOND_GOOD_EVIL,
                                    GoodEvil.NOT_GOOD_NOR_EVIL,
                                    GoodEvil.NEUTRAL,
                                    GoodEvil.PARADOXAL,
                            }),
                    Map.entry(
                            Balance.EVIL,
                            new GoodEvil[]{
                                    GoodEvil.GREATER_EVIL,
                                    GoodEvil.EVIL,
                                    GoodEvil.LESSER_EVIL,
                                    GoodEvil.BEYOND_EVIL,
                                    GoodEvil.BEYOND_GOOD_EVIL,
                                    GoodEvil.NOT_GOOD_NOR_EVIL,
                                    GoodEvil.NEUTRAL,
                                    GoodEvil.PARADOXAL,
                            }),
                    Map.entry(
                            Balance.PARADOXAL,
                            new GoodEvil[]{
                                    GoodEvil.BEYOND_GOOD_EVIL,
                                    GoodEvil.NOT_GOOD_NOR_EVIL,
                                    GoodEvil.NEUTRAL,
                                    GoodEvil.PARADOXAL,
                            }
                    )
            )
    );

    @ConfigProperty(name = "thresholds.gibberish") double gibberishThreshold;

    @Inject
    Dictionary dictionary;

    @Inject
    RequestsCheckService requestsCheckService;

    @Inject
    NormalizerService normalizerService;

    @Inject
    AnalyzerService analyzerService;

    public GoodEvilResponse goodOrEvil(String sentence) {
        GoodEvil goodEvil = computeGoodOrEvil(sentence);
        UUID requestId = requestsCheckService.computeId(goodEvil);
        return new GoodEvilResponse(sentence, goodEvil, requestId);
    }

    private GoodEvil computeGoodOrEvil(String sentence) {
        if (sentence == null || sentence.isBlank()) {
            return GoodEvil.EMPTY;
        }

        String normalizedSentence = normalizerService.normalize(sentence);

        final List<String> words = Arrays.stream(normalizedSentence.split("\\W"))
                .filter(word -> !word.isBlank())
                .collect(Collectors.toList());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Words : " + words);
        }

        float knownWordsRatio = computeKnownWordsRatio(words);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Known words ratio is " + knownWordsRatio);
        }
        if (knownWordsRatio < gibberishThreshold) {
            return GoodEvil.DONT_KNOW;
        }

        long key = computeKey(words);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("key for sentence is " + key);
        }

        Balance balance = analyzerService.analyze(words);

        final GoodEvil[] values = GOOD_EVIL_VALUES.get(balance);
        return values[(int) (key % values.length)];
    }

    private float computeKnownWordsRatio(List<String> words) {
        Set<String> uniqueWords = new HashSet<>(words);
        long knownWords = uniqueWords.stream()
                .filter(word -> dictionary.contains(word))
                .count();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(uniqueWords.size() + " different words, " + knownWords + " known in dictionary");
        }
        return ((float) knownWords) / uniqueWords.size();
    }

    private long computeKey(List<String> words) {
        CRC32 crc32 = new CRC32();
        for (String word : words) {
            crc32.update(word.getBytes());
        }
        return crc32.getValue();
    }
}

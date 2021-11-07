package fr.spacefox.verrucktnietzsche.service;

import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.Locale;

@ApplicationScoped
public class NormalizerService {

    private static final Logger LOGGER = Logger.getLogger(NormalizerService.class.getSimpleName());

    public String normalize(String s) {
        final String decontracted = decontract(normalizePunctuation(toLowerCase(s)));
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debugv("\"{0}\" is normalized as \"{1}\"", s, decontracted);
        }
        return decontracted;
    }

    private String toLowerCase(String s) {
        return s.toLowerCase(Locale.ENGLISH);
    }

    private String normalizePunctuation(String s) {
        return s.replace("’", "'");
    }

    private String decontract(String s) {
        return s.replace("'m", " am")
                .replace("'re", " are")
                .replace("'ve", " have")
                .replace("'ll", " will")
                .replace("aren't", "are not")
                .replace("can't", "cannot")
                .replace("couldn't", "could not")
                .replace("didn't", "did not")
                .replace("hasn't", "has not")
                .replace("haven't", "have not")
                .replace("it's", "it is")
                .replace("isn't", "is not")
                .replace("mustn't", "must not")
                .replace("shan't", "shall not")
                .replace("shouldn't", "should not")
                .replace("wasn't", "was not")
                .replace("weren't", "were not")
                .replace("won't", "will not")
                .replace("wouldn't", "would not")
                .replace("there's", "there is")
                .replace("where's", "where is")
                .replace("here's", "here is")
                .replace("what's", "what is")
                ;
    }
}

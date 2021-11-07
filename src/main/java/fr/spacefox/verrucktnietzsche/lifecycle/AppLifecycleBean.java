package fr.spacefox.verrucktnietzsche.lifecycle;

import fr.spacefox.verrucktnietzsche.data.Dictionary;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger LOGGER = Logger.getLogger(AppLifecycleBean.class.getSimpleName());

    @ConfigProperty(name = "dictionary.vocabulary.path") String dictionaryPathName;
    @ConfigProperty(name = "dictionary.goodwords.path") String goodWordsPathName;
    @ConfigProperty(name = "dictionary.badwords.path") String badWordsPathName;

    @Inject
    Dictionary dictionary;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");

        try {
            final Path dictionaryPath = Path.of(dictionaryPathName);
            final Path goodWordsPath = Path.of(goodWordsPathName);
            final Path badWordsPath = Path.of(badWordsPathName);
            LOGGER.infov("Loading {0}, {1} and {2}",
                    dictionaryPath.toAbsolutePath(), goodWordsPath.toAbsolutePath(), badWordsPath.toAbsolutePath());
            dictionary.load(
                    Files.readAllLines(dictionaryPath),
                    Files.readAllLines(goodWordsPath),
                    Files.readAllLines(badWordsPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("Dictionary loaded with " + dictionary.size() + " entries");
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }

}
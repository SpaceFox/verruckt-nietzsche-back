package fr.spacefox.verrucktnietzsche.service;

import fr.spacefox.verrucktnietzsche.io.GoodEvil;
import io.vertx.core.impl.ConcurrentHashSet;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class RequestsCheckService {

    private static final Logger LOGGER = Logger.getLogger(RequestsCheckService.class.getSimpleName());

    private final ConcurrentHashSet<UUID> knownIds = new ConcurrentHashSet<>();

    public UUID computeId(GoodEvil goodEvil) {
        if (goodEvil == GoodEvil.EMPTY || goodEvil == GoodEvil.DONT_KNOW) {
            return null;
        }
        UUID id = UUID.randomUUID();
        knownIds.add(id);
        return id;
    }

    public boolean consumes(UUID requestId) {
        return requestId != null && knownIds.remove(requestId);
    }
}

package fr.spacefox.verrucktnietzsche.io;

import java.util.UUID;

public class Opinion {

    private UUID requestId;
    private boolean correct;

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}

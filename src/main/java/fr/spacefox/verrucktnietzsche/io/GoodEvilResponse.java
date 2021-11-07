package fr.spacefox.verrucktnietzsche.io;

import java.util.UUID;

public class GoodEvilResponse {

    private String situation;
    private GoodEvil goodEvil;
    private UUID requestId;

    public GoodEvilResponse() {
    }

    public GoodEvilResponse(String situation, GoodEvil goodEvil, UUID requestId) {
        this.situation = situation;
        this.goodEvil = goodEvil;
        this.requestId = requestId;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public GoodEvil getGoodEvil() {
        return goodEvil;
    }

    public void setGoodEvil(GoodEvil goodEvil) {
        this.goodEvil = goodEvil;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }
}

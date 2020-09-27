package br.unip.tcc.eiaapp.DTO;

public class MessageDTO {

    private String text;
    private long timestamp;
    private boolean isWatson;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isWatson() {
        return isWatson;
    }

    public void setWatson(boolean watson) {
        isWatson = watson;
    }
}

package br.unip.tcc.eiaapp.DTO;

public class MessageDTO {

    private String text;
    private String timestamp;
    private String data;
    private boolean isWatson;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isWatson() {
        return isWatson;
    }

    public void setWatson(boolean watson) {
        isWatson = watson;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

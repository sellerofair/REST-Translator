package sellerofair.Translator;

public class Input {

    private final String from;
    private final String to;
    private final String text;

    Input(String from, String to, String text) {

        this.from = from;
        this.to = to;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Input{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

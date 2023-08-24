package flag;

public class StringVar {
    private String name;
    private String value;
    private String helpMessage;

    public StringVar(String name, String value, String helpMessage) {
        this.name = name;
        this.value = value;
        this.helpMessage = helpMessage;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    void setValue(String value) {
        this.value = value;
    }
}

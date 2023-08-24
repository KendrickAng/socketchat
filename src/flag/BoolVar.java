package flag;

public class BoolVar {
    private String name;
    private Boolean value;
    private String helpMessage;

    public BoolVar(String name, Boolean value, String helpMessage) {
        this.name = name;
        this.value = value;
        this.helpMessage = helpMessage;
    }

    public String getName() {
        return name;
    }

    public Boolean getValue() {
        return value;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    void setValue(Boolean value) {
        this.value = value;
    }
}

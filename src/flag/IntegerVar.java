package flag;

public class IntegerVar {
    private String name;
    private Integer value;
    private String helpMessage;

    public IntegerVar(String name, Integer value, String helpMessage) {
        this.name = name;
        this.value = value;
        this.helpMessage = helpMessage;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    void setValue(Integer value) {
        this.value = value;
    }
}

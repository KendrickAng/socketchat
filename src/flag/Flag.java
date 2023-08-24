package flag;

import java.util.HashMap;
import java.util.Map;

public class Flag {
    private static final Map<String, StringVar> strings = new HashMap<>();
    private static final Map<String, IntegerVar> integers = new HashMap<>();
    private static final Map<String, BoolVar> bools = new HashMap<>();
    private static final String FLAG_PREFIX = "--";

    public static StringVar string(String name, String defaultValue, String helpMessage) {
        StringVar ret = new StringVar(name, defaultValue, helpMessage);
        strings.put(name, ret);
        return ret;
    }

    public static IntegerVar integer(String name, Integer defaultValue, String helpMessage) {
        IntegerVar ret = new IntegerVar(name, defaultValue, helpMessage);
        integers.put(name, ret);
        return ret;
    }

    public static BoolVar bool(String name, Boolean defaultValue, String helpMessage) {
        BoolVar ret = new BoolVar(name, defaultValue, helpMessage);
        bools.put(name, ret);
        return ret;
    }

    public static void parse(String[] args) {
        for (String arg : args) {
            parseFlag(arg);
        }
    }

    private static void parseFlag(String token) {
        // flags must start with '--'
        if (!token.startsWith(FLAG_PREFIX)) {
            throw new IllegalArgumentException(String.format("Flag must start with '--', got %s", token));
        }

        // flags must be of form key=value
        String[] tokens = token
                .substring(FLAG_PREFIX.length())
                .split("=", 2);
        if (tokens.length != 2) {
            throw new IllegalArgumentException(String.format("Flag must be of form key=value, got %s", token));
        }

        String name = tokens[0];
        String value = tokens[1];

        // handle string flags
        if (strings.containsKey(name)) {
            StringVar var = strings.get(name);
            var.setValue(value);
        }

        // handle integer flags
        if (integers.containsKey(name)) {
            IntegerVar var = integers.get(name);

            int val = Integer.MIN_VALUE;
            try {
                val = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(String.format("Flag %s must be an integer, got %s", name, value));
            }
            var.setValue(val);
        }

        // handle bool flags
        if (bools.containsKey(name)) {
            BoolVar var = bools.get(name);

            boolean val = false;
            val = Boolean.parseBoolean(value);
            var.setValue(val);
        }
    }
}


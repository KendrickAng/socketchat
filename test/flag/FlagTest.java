package flag;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class FlagTest {
    @Test
    public void testStringFlag_succeedsWithSingleFlag() {
        StringVar nameFlag = Flag.string("name", "default", "help");
        String[] args = new String[]{"--name=John"};

        Flag.parse(args);

        assertEquals("John", nameFlag.getValue());
    }

    @Test
    public void testStringFlag_succeedsWithDefault() {
        StringVar nameFlag = Flag.string("name", "default", "help");
        String[] args = new String[]{};

        Flag.parse(args);

        assertEquals("default", nameFlag.getValue());
    }

    @Test
    public void testIntegerFlag_succeedsWithSingleFlag() {
        IntegerVar ageFlag = Flag.integer("age", 0, "help");
        String[] args = new String[]{"--age=20"};

        Flag.parse(args);

        assertEquals(20, ageFlag.getValue().intValue());
    }

    @Test
    public void testIntegerFlag_succeedsWithDefault() {
        IntegerVar ageFlag = Flag.integer("age", 0, "help");
        String[] args = new String[]{};

        Flag.parse(args);

        assertEquals(0, ageFlag.getValue().intValue());
    }
}

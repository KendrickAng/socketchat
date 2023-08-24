import flag.BoolVar;
import flag.Flag;
import flag.IntegerVar;
import flag.StringVar;

public class Main {
    public static void main(String[] args) {
        StringVar startFlag = Flag.string("start", "all", "'server', 'client', 'all'");
        System.out.println("start: " + startFlag.getValue());
    }
}
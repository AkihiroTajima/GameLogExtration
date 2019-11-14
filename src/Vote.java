import java.util.Arrays;
import java.util.List;

public class Vote {
    final String defaultValue = "def";
    String day = defaultValue;
    private String actType = defaultValue;

    private String from = defaultValue;
    String to = defaultValue;


    private List<String> elementList;

    boolean isOk() {
        for (String str : elementList) {
            if (str.equals(defaultValue)) {
                System.err.println("this Vote has default value: " + this.toString());
                System.exit(1);
                return false;
            }
        }
        return true;
    }

    public Vote(String[] line) {

        this.elementList = Arrays.asList(day, actType, from, to);
        if (line.length == this.elementList.size()) {
            this.day        = line[0];
            this.actType    = line[1];
            this.from       = line[2];
            this.to         = line[3];
        } else {
            System.err.println("Input line is " + line.length);
        }
        this.elementList = Arrays.asList(day, actType, from, to);
        this.isOk();
    }

    @Override
    public String toString() {
        return this.elementList.toString();
    }
}

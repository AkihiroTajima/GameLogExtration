import java.util.Arrays;
import java.util.List;

public class Attack {
    final String defaultValue = "def";
    private String day = defaultValue;
    private String actType = defaultValue;

    private String toID = defaultValue;
    private String outcome = defaultValue;

    private List<String> elementList;

    boolean isOk() {
        for (String str : elementList) {
            if (str.equals(defaultValue)) {
                System.err.println("this Attack has default value: " + this.toString());
                System.exit(1);
                return false;
            }
        }
        return true;
    }

    public Attack(String[] line) {

        this.elementList = Arrays.asList(day, actType, toID, outcome);
        if (line.length == this.elementList.size()) {
            this.day        = line[0];
            this.actType    = line[1];
            this.toID       = line[2];
            this.outcome    = line[3];
        } else {
            System.err.println("Input line is " + line.length);
        }
        this.elementList = Arrays.asList(day, actType, toID, outcome);
        this.isOk();
    }

    @Override
    public String toString() {
        return this.elementList.toString();
    }
}

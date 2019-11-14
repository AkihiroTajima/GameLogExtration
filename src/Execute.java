import java.util.Arrays;
import java.util.List;

public class Execute {
    final String defaultValue = "def";
    private String day = defaultValue;
    private String actType = defaultValue;

    private String targetID = defaultValue;
    String targetRole = defaultValue;

    private List<String> elementList;

    boolean isOk() {
        for (String str : elementList) {
            if (str.equals(defaultValue)) {
                System.err.println("this Execute has default value: " + this.toString());
                System.exit(1);
                return false;
            }
        }
        return true;
    }

    public Execute(String[] line) {

        this.elementList = Arrays.asList(day, actType, targetID, targetRole);
        if (line.length == this.elementList.size()) {
            this.day        = line[0];
            this.actType    = line[1];
            this.targetID   = line[2];
            this.targetRole = line[3];
        } else {
            System.err.println("Input line is " + line.length);
        }
        this.elementList = Arrays.asList(day, actType, targetID, targetRole);
        this.isOk();
    }

    @Override
    public String toString() {
        return this.elementList.toString();
    }
}

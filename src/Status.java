import java.util.Arrays;
import java.util.List;

public class Status {
    final String defaultValue = "def";
    private String day = defaultValue;
    private String actType = defaultValue;

    private String playerID = defaultValue;
    private String role = defaultValue;
    private String isSurvive = defaultValue;
    private String name = defaultValue;

    private List<String> elementList;

    boolean isOk() {
        for (String str : elementList) {
            if (str.equals(defaultValue)) {
                System.err.println("this Status has default value: " + this.toString());
                System.exit(1);
                return false;
            }
        }
        return true;
    }

    public Status(String[] line) {

        this.elementList = Arrays.asList(day, actType, playerID, role, isSurvive, name);
        if (line.length == this.elementList.size()) {
            this.day        = line[0];
            this.actType    = line[1];
            this.playerID   = line[2];
            this.role       = line[3];
            this.isSurvive  = line[4];
            this.name       = line[5];
        } else {
            System.err.println("Input line is " + line.length);
        }
        this.elementList = Arrays.asList(day, actType, playerID, role, isSurvive, name);
        this.isOk();
    }

    @Override
    public String toString() {
        return this.elementList.toString();
    }
}

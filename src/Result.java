import java.util.Arrays;
import java.util.List;

public class Result {
    final String defaultValue = "def";
    private String day = defaultValue;
    private String actType = defaultValue;

    private String numVillagers = defaultValue;
    private String numWolf = defaultValue;
    private String winner = defaultValue;

    private List<String> elementList;

    boolean isOk() {
        for (String str : elementList) {
            if (str.equals(defaultValue)) {
                System.err.println("this Result has default value: " + this.toString());
                System.exit(1);
                return false;
            }
        }
        return true;
    }

    public Result(String[] line) {

        this.elementList = Arrays.asList(day, actType, numVillagers, numWolf, winner);
        if (line.length == this.elementList.size()) {
            this.day            = line[0];
            this.actType        = line[1];
            this.numVillagers   = line[2];
            this.numWolf        = line[3];
            this.winner         = line[4];
        } else {
            System.err.println("Input line is " + line.length);
        }
        this.elementList = Arrays.asList(day, actType, numVillagers, numWolf, winner);
        this.isOk();
    }

    @Override
    public String toString() {
        return this.elementList.toString();
    }
}

import java.util.Arrays;
import java.util.List;

public class Talk {

    final String defaultValue = "def";
    String day = defaultValue;
    private String actType = defaultValue;

    String talkID = defaultValue;
    private String turnID = "0";
    String playerID = defaultValue;
    String content = defaultValue;

    private List<String> elementList;

    boolean isOk() {
        for (String str : elementList) {
            if (str.equals(defaultValue)) {
                System.err.println("this Talk has default value: " + this.toString());
                System.exit(1);
                return false;
            }
        }
        return true;
    }

    public Talk(String[] line) {

        this.elementList = Arrays.asList(day, actType, talkID, turnID, playerID, content);
        if (line.length == this.elementList.size()-1) {
            this.day        = line[0];
            this.actType    = line[1];
            this.talkID     = line[2];
            this.playerID   = line[4];
            this.content    = line[5];
        } else if (line.length == this.elementList.size()) {
            this.day        = line[0];
            this.actType    = line[1];
            this.talkID     = line[2];
            this.turnID     = line[3];
            this.playerID   = line[4];
            this.content    = line[5];
        } else {
            System.err.println("Input line is " + line.length);
        }
        this.elementList = Arrays.asList(day, actType, talkID, turnID, playerID, content);
        this.isOk();
    }

    @Override
    public String toString() {
        return this.elementList.toString();
    }
}

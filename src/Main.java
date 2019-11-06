
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    private final String pathTo15vList = "15vList.txt";
    private final String pathTo5vList = "5vList.txt";
    private  String root ;
    Util u = new Util();

    void writeTalkMap() {
        List<String> p = u.path2Line(pathTo5vList);
        Map<String, Integer> map = new HashMap<>();
        for (String path : p) {
            Log l = new Log(path);

            for (Talk t : l.talkList) {
                if (!map.containsKey(t.content)) {
                    map.put(t.content, 1);
                } else {
                    map.put(t.content, map.get(t.content)+1);
                }
            }
        }

        map.entrySet().stream()
                .sorted(java.util.Map.Entry.comparingByValue())
                .forEach(System.out::println);
    }

    Main(String rootInput) {
        this.root = rootInput;
//        u.writePathList(root);
    }

    public static void main(String[] args) {

        final String root = "/home/kenzi/pg/AIWolf/log";
//                String root = "/Users/nicolas/pg/aiwolf/log";

        Main m = new Main(root);

        

    }
}


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
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

    List<String> writeAllTopics(){
        List<String> res = new ArrayList<>();

        List<String> lines = u.path2Line("map.csv");

        for (String line : lines) {
            String elm =    line.split(" ")[0]
//                                .split("\(")[0]
                                .split(",")[0];
            if (elm.contains("REQUEST(")) continue;
            if (elm.contains("Agent")) continue;
            if (elm.contains("DAY")) continue;
            if (!res.contains(elm)) res.add(elm);
        }

        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get("topics.txt"));
            PrintWriter pw = new PrintWriter(bw, true);
            for (String str: res) {
                System.out.println(str);
                pw.println(str);
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }



    Main(String rootInput) {
        this.root = rootInput;
//        u.writePathList(root);
    }

    public static void main(String[] args) {

//        final String root = "/home/kenzi/pg/AIWolf/log";
                String root = "/Users/nicolas/pg/aiwolf/log";

        Main m = new Main(root);
        Log l = new Log(m.u.path2Line(m.pathTo5vList).get(0));

        int daynum = 1;
        int agent = 1;
        List<List<Talk>> map = l.talkMap();
        System.out.println(map.size());

        for (List<Talk> day : map) {
            for (Talk t : day) {
//                System.out.println(daynum + " : " + t.toString());
                if (t.playerID.equals(Integer.toString(agent))) {
                    System.out.println(t);
                    int talkID = Integer.parseInt(t.talkID);
                    for (int i = talkID-1; i >= 0 ; i--) {
                        if (talkID - i > 4) break;
                        Talk now = day.get(i);
                        if (now.playerID.equals(Integer.toString(agent))) break;
                        System.out.println("\t" + day.get(i));
                    }
                }
            }
            daynum++;
            System.out.println("======================================================================");
        }

    }
}


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
    Map<String, Integer> talkMap;
    Map<String, Integer> topicMap;
    Map<String, Integer> playerMap;
    Map<String, Integer> objectiveMap;
    Util u = new Util();

    Map<String, Integer> printTalkMap(String logPath) {
        List<String> p = u.path2Line(logPath);
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

        return map;
    }

    Map<String, Integer> getTalkID() {
        Map<String, Integer> res = new HashMap<>();

        Map<String, Integer> map = printTalkMap(pathTo5vList);
        for (String talk : map.keySet()) {
            if (!res.containsKey(talk.toString())) {
                res.put(talk.toString(), res.size());
            }
        }

        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("talkID.csv"));
             PrintWriter pw = new PrintWriter(bw, true)) {
            for (String talk : res.keySet()) {
                pw.println(talk + "," + res.get(talk));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    void printSituasionMap(String logPath) {
        Map<String , Integer> map = new HashMap<>();
        for (String path : u.path2Line(logPath)) {
            Log l = new Log(path);
            for (int i = 1; i<5; i++) {
                List<String> s = l.situation(i);
                for (String str : s) {
                    //System.out.println(i + " : " + str.split(",").length + " : " + str);
                    if (!map.containsKey(str)) {
                        map.put(str, 1);
                    } else {
                        map.put(str, map.get(str) + 1);
                    }
                }
                //System.out.println("--------------------------------------------------");
            }
        }


        // System.out.println(map.size());

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

    Map<String, Integer> getTopicID() {
        Map<String, Integer> res = new HashMap<>();

        List<String> topicList = writeAllTopics();

        topicList.add("Else");

        for (String topic : topicList) {
            res.put(topic, res.size());
        }

        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("topicID.csv"));
             PrintWriter pw = new PrintWriter(bw, true)) {
            Map<String, Integer> map = res;
            for (String topic : map.keySet()) {
                pw.println(topic + "," + map.get(topic));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    Map<String, Integer> writePlayerID() {
        Map<String, Integer> PlayerID = new HashMap<>();
        for (String path : u.path2Line(pathTo5vList)) {
            Log l = new Log(path);

            for (Status s : l.statusList) {
                if (!PlayerID.containsKey(s.name)) PlayerID.put(s.name, PlayerID.size());
            }
        }

        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("playerID.csv"));
             PrintWriter pw = new PrintWriter(bw, true)) {
            for (String key : PlayerID.keySet()) {
                pw.println(key + "," + PlayerID.get(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return PlayerID;
    }

    void writeData() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("out2.csv"));
             PrintWriter pw = new PrintWriter(bw, true)) {
            int i = 0;
            pw.println("logID,day,turn,playerID,talkID,topicID,Objective");
            for (String path : u.path2Line(pathTo5vList)) {
                System.out.println(path);
                for (String s : data(new Log(path), i)) {
                    pw.println(s);
                }
                System.out.println("--------------------------------------------------------------------------------------");
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<String> data(Log l, int logID) {
        List<String> res = new ArrayList<>();
        String wolfID = "-1";
        for (Status s : l.statusList) {
            if (s.role.equals("WEREWOLF")) wolfID = s.playerID;
        }
        List<List<Talk>> talklist = new ArrayList<>();
        List<List<Vote>> votelist = new ArrayList<>();
        List<List<Status>> statuslist = new ArrayList<>();
        talklist.add(new ArrayList<>());
        votelist.add(new ArrayList<>());
        statuslist.add(new ArrayList<>());
        for (Talk t : l.talkList) {
            if (Integer.parseInt(t.day) != talklist.size()) talklist.add(new ArrayList<>());
            talklist.get(Integer.parseInt(t.day)-1).add(t);
        }
        for (Vote v : l.voteList) {
            if (Integer.parseInt(v.day) != votelist.size()) votelist.add(new ArrayList<>());
            votelist.get(Integer.parseInt(v.day)-1).add(v);
        }
        for (Status s : l.statusList) {
            if (Integer.parseInt(s.day)+1 != statuslist.size()) statuslist.add(new ArrayList<>());
            statuslist.get(Integer.parseInt(s.day)).add(s);
        }

//        int day = 0;
//        for (List<Talk> tl : talklist) {
//            day++;
//            for (Talk t : tl) {
//                System.out.println(day + " : " + t);
//            }
//        }
//        day = 0;
//        for (List<Vote> vl : votelist) {
//            day++;
//            for (Vote v : vl) {
//                System.out.println(day + " : " + v);
//            }
//        }
//        int day = 0;
//        for (List<Status> sl : statuslist) {
//            for (Status s : sl) {
//                System.out.println(day + " : " + s);
//            }
//            day++;
//        }
        if (talklist.get(0).isEmpty() || votelist.get(0).isEmpty() || statuslist.get(0).isEmpty()) {
            System.err.println("S1");
            return res;
        }
        if (talklist.size() != votelist.size()) {
            System.err.println("S2");
            return res;
        }
        if (talklist.size() != l.executeList.size()) {
            System.err.println("S2_1");
            return res;
        }

        if (talklist.size() !=  statuslist.size()+2) {
            System.err.println("S3");
            //return;
        }
        if (wolfID.equals("-1")) {
            System.err.println("wolfID is not correcr");
            System.exit(1);
        }
//        System.out.println("Size of talkList = " + talklist.size());
//        System.out.println("Size of voteList = " + votelist.size());
//        System.out.println("Size of statusList = " + statuslist.size());

        for (int day = 0; day<talklist.size(); day++){
            double per;
            double numVote = votelist.get(day).size();
            double numVoteForWolf = 0;

            for (Vote v : votelist.get(day)) {
                if (v.to.equals(wolfID)) numVoteForWolf++;
            }
            per = numVoteForWolf/numVote;
            String isWolfExecuted = "0";
            if (l.executeList.get(day).targetRole.equals("WEREWOLF")) isWolfExecuted = "1";
            String text = "";
            for (Talk t : talklist.get(day)) {
                System.out.println(day+1 + " : " + t.content + "," + per);
                int topicID = topicMap.get("Else");
                for (String str : this.topicMap.keySet()) if (t.content.startsWith(str)) topicID = this.topicMap.get(str);
                text += t.content;
                text += " ";

                res.add(logID + "," +
                        t.day + "," +
                        t.turnID + "," +
                        this.playerMap.get(l.playerIDMap.get(t.playerID)) + "," +
                        this.talkMap.get(t.content) + "," +
                        topicID + "," +
                        this.objectiveMap.get(Double.toString(per)) );
            }
//            res.add(text + "," + isWolfExecuted);
        }

        return res;
    }

    Main(String rootInput) {
        this.root = rootInput;
//        u.writePathList(root);
//        this.talkMap = this.getTalkID();
//        this.topicMap = this.getTopicID();
//        this.playerMap = this.writePlayerID();
        this.talkMap = new HashMap<>();
        this.topicMap = new HashMap<>();
        this.playerMap = new HashMap<>();
        this.objectiveMap = new HashMap<>();
        for (String line : u.path2Line("talkID.csv")) {
            String[] data = line.split(",");
            this.talkMap.put(data[0], Integer.parseInt(data[1]));
        }
        for (String line : u.path2Line("topicID.csv")) {
            String[] data = line.split(",");
            this.topicMap.put(data[0], Integer.parseInt(data[1]));
        }
        for (String line : u.path2Line("playerID.csv")) {
            String[] data = line.split(",");
            this.playerMap.put(data[0], Integer.parseInt(data[1]));
        }
        for (String line : u.path2Line("objectiveMap.csv")) {
            String[] data = line.split(",");
            this.objectiveMap.put(data[0], Integer.parseInt(data[1]));
        }
    }

    public static void main(String[] args) {

//        final String root = "/home/kenzi/pg/AIWolf/log";
                String root = "/Users/nicolas/pg/aiwolf/log";

        Main m = new Main(root);
        Util u = new Util();

//        Log l = new Log(u.path2Line(m.pathTo5vList).get(0));
//
//        for (String[] line : l.log) {
//            System.out.println(Arrays.asList(line));
//        }

//        m.getTalkID();
        m.writeData();

    }
}

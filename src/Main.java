
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import static java.nio.file.StandardOpenOption.*;

public class Main {

    private final String pathTo15vList = "15vList.txt";
    private final String pathTo5vList = "5vList.txt";
    private  String root ;
    Map<String, Integer> talkMap;
    Map<String, Integer> topicMap;
    Map<String, Integer> playerMap;
    Map<String, Integer> objectiveMap;
    Map<String, Integer> roleMap;
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

    void getTalkData(Log l) {
        List<String> playerList = new ArrayList<>();
        l.statusList.stream().
                filter(s -> s.day.equals("0")).
                forEach(s -> playerList.add(s.playerID));

        for (String playerID : playerList) {
            String playerNAME = l.playerIDMap.get(playerID);
            String path = "talkdata/";
            path += playerNAME + ".csv";

            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path), APPEND, CREATE);
                 PrintWriter pw = new PrintWriter(bw, true)) {
                List<String> line = new ArrayList<>();
                List<String> codedLine = new ArrayList<>();
                l.talkList.stream().
                        filter(t -> t.playerID.equals(playerID)).
                        collect(Collectors.toList()).
                        forEach(t -> line.add(t.content));
                line.forEach(s -> codedLine.add(talkMap.get(s).toString()));
                pw.println(String.join(",", codedLine));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void writeTalkContent(Log l) {
        String path = "talkContentPart.txt";
        List<String> contentList = new ArrayList<>();
        l.talkList.forEach(t -> contentList.add(t.content));
        String line = String.join(" ", contentList);
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path), APPEND, CREATE);
             PrintWriter pw = new PrintWriter(bw, true)) {
            pw.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<InputTalkData> getInputDatabyDaybyPlayer(Log l) {
        List<InputTalkData> res = new ArrayList<>();

        for (int day = 1; day <= 2; day++) {
            for (String playerID : l.playerList) {
                String PlayerVoteFor = "-1";
                int numVoteforWolf = 0;
                for (Vote v : l.dayVote(Integer.toString(day))) {
                    if (v.from.equals(playerID)) PlayerVoteFor = v.to;
                    if (v.to.equals(l.WolfID)) numVoteforWolf++;
                }
                if (PlayerVoteFor.equals("-1")) continue;

                String playerNAME = l.playerIDMap.get(playerID);
                List<String> PlayerTalkList = new ArrayList<>();
                l.dayTalk(Integer.toString(day)).stream().
                        filter(t -> t.playerID.equals(playerID)).
                        collect(Collectors.toList()).
                        forEach(t -> PlayerTalkList.add(t.content));

                int tmp = Integer.parseInt(PlayerVoteFor);
                tmp--;
                PlayerVoteFor = Integer.toString(tmp);
                res.add(new InputTalkData(PlayerTalkList, playerNAME, PlayerVoteFor, l.roleMap.get(playerID)));
            }
        }

        return  res;
    }
    List<InputTalkData> getInputDatabyDay(Log l) {
        List<InputTalkData> res = new ArrayList<>();

        for (int day = 1; day <= 2; day++) {

            double numVoteforWolf = 0;
            int numArive = 0;
            if (l.dayVote(Integer.toString(day)).isEmpty()) continue;
            if (l.dayTalk(Integer.toString(day)).isEmpty()) continue;
            for (Status s :  l.dayStatus(Integer.toString(day))){
                if (s.isSurvive.equals("ALIVE")) numArive++;
            }
            for (Vote v : l.dayVote(Integer.toString(day))) {
                if (v.to.equals(l.WolfID)) numVoteforWolf++;
            }
            numVoteforWolf /= (double) l.dayVote(Integer.toString(day)).size() /numArive;

            res.add(new InputTalkData(l.dayTalk(Integer.toString(day)), numVoteforWolf));

        }

        return  res;
    }

    List<InputTalkData> getInputData(Log l) {
        List<InputTalkData> res = new ArrayList<>();

        for (String playerID : l.playerList) {
            String playerNAME = l.playerIDMap.get(playerID);
            List<String> PlayerTalkList = new ArrayList<>();
            l.talkList.stream().
                    filter(t -> t.playerID.equals(playerID)).
                    collect(Collectors.toList()).
                    forEach(t -> PlayerTalkList.add(t.content));
            res.add(new InputTalkData(PlayerTalkList, playerNAME, Integer.toString(roleMap.get(l.roleMap.get(playerID)))));
        }

        return  res;
    }

    int writeInputData(List<InputTalkData> input, int len) {
        String path = "detaByLength/" + Integer.toString(len) + ".txt";
        int cnt = 0;
        File file = new File(path);
        boolean empty = !file.exists() || file.length() == 0;
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path), CREATE, APPEND);
             PrintWriter pw = new PrintWriter(bw, true)) {
            if (empty) pw.println("text,Objective");
            for (InputTalkData in : input) {
                List<String> line = new ArrayList<>();
                for (String talk : in.talkList) {
                    line.add(talk);
                    if (line.size() == len) break;
                }
                if (line.size() != len) continue;

                pw.println(String.join(" ", line) + "," + in.getVoteTarget());
                cnt++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cnt;
    }
    void writeInputData(List<InputTalkData> input) {
        String path = "VoteForData.csv";
        int cnt = 0;
        File file = new File(path);
        boolean empty = !file.exists() || file.length() == 0;
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path), CREATE, APPEND);
             PrintWriter pw = new PrintWriter(bw, true)) {
            if (empty) pw.println("text,Objective");
            for (InputTalkData in : input) {
                pw.println(String.join(" ", in.talkList) + "," + in.getVoteTarget());
                cnt++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        this.roleMap= new HashMap<>();
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
        for (String line : u.path2Line("roleMap.csv")) {
            String[] data = line.split(",");
            this.roleMap.put(data[0], Integer.parseInt(data[1]));
        }
    }

    void writeKenziContent() {
        List<String> pathList = u.path2Line(pathTo5vList);
        List<String> kenziPathList = new ArrayList<>();
        for (String path : pathList) {
            for (Status s : new Log(path).statusList) {
                if (s.name.equals("kenzi")) kenziPathList.add(path);
            }
        }

        Map<String,Integer> contentMap = new HashMap<>();

        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("kenziContent.txt"), CREATE);
             PrintWriter pw = new PrintWriter(bw, true)) {
//            contentMap.entrySet().stream()
//                    .sorted(java.util.Map.Entry.comparingByValue())
//                    .forEach(c -> pw.println(c.getKey() + "," + c.getValue()));
            for (String path : kenziPathList) {
                List<String> kentTalk = new ArrayList<>();
                Log l = new Log(path);
                String kenziId = "-1";
                for (Status s : new Log(path).statusList) {
                    if (s.name.equals("kenzi")) kenziId = s.playerID;
                }
                if (kenziId.equals("-1")) continue;
                String finalKenziId = kenziId;
                l.talkList.stream().filter(t -> t.playerID.equals(finalKenziId)).forEach(t -> kentTalk.add(t.content));
                for (String c : kentTalk) {
                    if (!contentMap.containsKey(c)) contentMap.put(c,1);
                    else contentMap.put(c,contentMap.get(c)+1);
                }
                pw.println(String.join(", ", kentTalk));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

//        final String root = "/home/kenzi/pg/AIWolf/log";
                String root = "/Users/nicolas/pg/aiwolf/log";

        Main m = new Main(root);
        Util u = new Util();

//        u.writePathList("/home/kenzi/pg/setup/AIWolf11/log/");

//        Log l = new Log(u.path2Line(m.pathTo5vList).get(0));

//        l.log.forEach(line -> System.out.println(Arrays.asList(line)));

        List<String> pathList = u.path2Line(m.pathTo5vList);

//        pathList.forEach(p -> m.writeInputData(m.getInputDatabyDaybyPlayer(new Log(p))));

        for (int i = 1; i < 500; i++) {
            int sum = 0;
            for (String path : pathList) {
                sum += m.writeInputData(m.getInputDatabyDaybyPlayer(new Log(path)), i);
            }
            System.out.println(i);
            if (sum == 0) {
                System.err.println(i);
                break;
            }
        }
    }
}

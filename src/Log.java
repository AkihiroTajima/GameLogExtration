import java.io.File;
import java.util.*;

public class Log {
    String path = "";
    File f;
    Util u = new Util();
    List<String[]> log;

    List<String> playerList = new ArrayList<>();
    Map<String, String> playerIDMap = new HashMap<>();
    Map<String, String> roleMap = new HashMap<>();
    String WolfID;

    List<Talk> talkList = new ArrayList<>();
    List<Vote> voteList = new ArrayList<>();
    List<Attack> attackList = new ArrayList<>();
    List<AttackVote> attackVoteList = new ArrayList<>();
    List<Divine> divineList = new ArrayList<>();
    List<Execute> executeList = new ArrayList<>();
    List<Guard> guardList = new ArrayList<>();
    List<Result> resultList = new ArrayList<>();
    List<Status> statusList = new ArrayList<>();
    List<Whisper> whisperList = new ArrayList<>();

    boolean isCorrectPlayerNUmber = true;
    boolean isThereAnyTalk = true;

    Log(String inputFilePath) {
        this.path = inputFilePath;
        this.f = new File(path);
        this.log = u.path2SplitedLog(path);
        this.readLists();
        for (Status s : this.statusList) {
            if (!s.day.equals("0")) break;
            this.playerIDMap.put(s.playerID, s.name);
        }
        statusList.stream().
                filter(s -> s.day.equals("0")).
                forEach(s -> playerList.add(s.playerID));
        if (!(playerList.size() == 5 || playerList.size() == 15)) {
            isCorrectPlayerNUmber = false;
        }

        if (talkList.size() == 0) {
            isThereAnyTalk = false;
        }
        for (String id : playerList) {
            String role = "-1";
            for (Status s : statusList) {
                if (!s.day.equals("0")) break;
                if (s.playerID.equals(id)) this.roleMap.put(id,s.role);
                if (s.role.equals("WEREWOLF")) this.WolfID = s.playerID;
            }
        }
    }

    List<String> situation(int agent) {
        List<String> res = new ArrayList<>();

        int windowSize = 4; // [1,4(numplayer-1)]
        List<List<Talk>> map = this.talkMap();

        for (List<Talk> day : map) {
            for (Talk t : day) {
                if (t.playerID.equals(Integer.toString(agent))) {
                    //System.out.println(t);
                    String content = "";
                    int talkID = Integer.parseInt(t.talkID);
                    for (int i = talkID-1; i >= 0 ; i--) {
                        if (talkID - i > windowSize) break;
                        Talk now = day.get(i);
                        if (now.playerID.equals(Integer.toString(agent))) break;
                        //System.out.println("\t" + day.get(i));
                        if (!content.isEmpty())content += ":";
//                        System.out.println("content : "+day.get(i).content);
                        content += day.get(i).content;
                    }
                    if (!content.isEmpty()) res.add(content);
                }
            }
            //System.out.println("======================================================================");
        }

        return res;
    }

    public List<List<Talk>> talkMap(){
        List<List<Talk>> res = new ArrayList<>();

        res.add(new ArrayList<>());
        int nowday = 1;
        for (Talk t : talkList) {
            if (t.day.equals(Integer.toString(nowday))){
                res.get(res.size()-1).add(t);
            } else {
                nowday++;
                res.add(new ArrayList<>());
                res.get(res.size()-1).add(t);
            }
        }

        return res;
    }

    public List<Talk> dayTalk(String day) {
        List<Talk> res = new ArrayList<>();
        talkList.stream().
                filter(t -> t.day.equals(day)).
                forEach(res::add);
        return res;
    }
    public List<Status> dayStatus(String day) {
        List<Status> res = new ArrayList<>();
        statusList.stream().
                filter(t -> t.day.equals(day)).
                forEach(res::add);
        return res;
    }

    public List<Vote> dayVote(String day) {
        List<Vote> res = new ArrayList<>();
        voteList.stream().
                filter(t -> t.day.equals(day)).
                forEach(res::add);
        return res;
    }

    void readLists() {
        if (this.log.size() == 0) {
            System.err.println("log is empty");
            System.exit(1);
        }

        for (String[] line : this.log) {
            // System.out.println(Arrays.asList(line));
            if (line[1].equals("talk"))         this.talkList.add(new Talk(line));
            if (line[1].equals("vote"))         this.voteList.add(new Vote(line));
            if (line[1].equals("attack"))       this.attackList.add(new Attack(line));
            if (line[1].equals("attackVote"))   this.attackVoteList.add(new AttackVote(line));
            if (line[1].equals("divine"))       this.divineList.add(new Divine(line));
            if (line[1].equals("execute"))      this.executeList.add(new Execute(line));
            if (line[1].equals("guard"))        this.guardList.add(new Guard(line));
            if (line[1].equals("result"))       this.resultList.add(new Result(line));
            if (line[1].equals("status"))       this.statusList.add(new Status(line));
            if (line[1].equals("whisper"))      this.whisperList.add(new Whisper(line));
        }
    }
}

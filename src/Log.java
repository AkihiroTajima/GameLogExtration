import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Log {
    String path = "";
    File f;
    Util u = new Util();
    List<String[]> log;

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

    Log(String inputFilePath) {
        this.path = inputFilePath;
        this.f = new File(path);
        this.log = u.path2SplitedLog(path);
        this.readLists();
    }

    void situation(int agent) {
        List<String> res = new ArrayList<>();

        int windowSize = 4; // [1,4(numplayer-1)]
        List<List<Talk>> map = this.talkMap();
        int daynum = 1;
//        System.out.println(map.size());

        for (List<Talk> day : map) {
            for (Talk t : day) {
                if (t.playerID.equals(Integer.toString(agent))) {
                    System.out.println(t);
                    int talkID = Integer.parseInt(t.talkID);
                    for (int i = talkID-1; i >= 0 ; i--) {
                        if (talkID - i > windowSize) break;
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

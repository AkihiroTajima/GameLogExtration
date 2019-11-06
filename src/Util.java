import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Util {
    // input root directry
    // output pathlist of all log file
    List<String> getPathList(String ParentPath) throws Exception {
        System.out.println("searching log file from " + ParentPath + "...");
        ArrayList<String> res = new ArrayList<>();

        Stack<File> stack = new Stack<>();
        stack.add(new File(ParentPath));
        while (!stack.isEmpty()) {
            File item = stack.pop();
            if (item.isFile() && item.getName().endsWith(".log")) res.add(item.getAbsolutePath());

            if (item.isDirectory()) {
                System.out.println("Fine dir " + item.getName());
                for (File child : Objects.requireNonNull(item.listFiles())) stack.push(child);
            }
        }

        if (res.isEmpty()) {
            System.err.println("NO file found");
            throw new Exception();
        }



        return res;
    }

    // input root directry
    // output pathlist of all log file
    List<String> getPathList(String ParentPath, boolean print) throws Exception {
        ArrayList<String> res = new ArrayList<>();

        Stack<File> stack = new Stack<>();
        stack.add(new File(ParentPath));
        while (!stack.isEmpty()) {
            File item = stack.pop();
            if (item.isFile()) {
                if (item.length() != 0) res.add(item.getAbsolutePath());
            }

            if (item.isDirectory()) {
                for (File child : Objects.requireNonNull(item.listFiles())) stack.push(child);
            }
        }

        if (res.isEmpty()) {
            System.err.println("NO file found");
            throw new Exception();
        }

        if (print) {
            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("out.txt"));
                 PrintWriter pw = new PrintWriter(bw, true)) {
                for (String s : res) {
                    pw.println(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    // input log file
    // output number of player
    int countPlayer(File f) {
        if (f.length() == 0) {
            System.err.println("file is empty");
            return -1;
        }
        int cnt = 0;

        try (BufferedReader reader = new BufferedReader
                (new InputStreamReader
                        (new FileInputStream(f), StandardCharsets.UTF_8));){
            String str;
            while ((str = reader.readLine()) != null) {
                if (str.substring(0,8).equals("0,status")) {
                    cnt++;
                } else {
                    //System.out.println(cnt);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cnt;
    }

    // input root path
    // output pathlist devided by numbeer of player
    void writePathList(String pPath) {
        //create files has list of path devided by mnumber of player
        try {
            int cnt15 = 0;
            int cnt5 = 0;
            BufferedWriter bw15 = Files.newBufferedWriter(Paths.get("15vList.txt"));
            BufferedWriter bw5 = Files.newBufferedWriter(Paths.get("5vList.txt"));
            PrintWriter pw15 = new PrintWriter(bw15, true);
            PrintWriter pw5 = new PrintWriter(bw5, true);
            List<String> pathList = this.getPathList(pPath);
            int s = pathList.size();
            System.out.println("size of path list: " + s);
            int i = 0;
            for (String path : pathList) {
                File f = new File(path);

                if (f.length() == 0) continue;
                int out = this.countPlayer(f);

                if (out == 15) {
                    pw15.println(f.getAbsolutePath());
                    cnt15++;
                } else if (out == 5) {
                    pw5.println(f.getAbsolutePath());
                    cnt5++;
                } else {
                    System.err.println("unexpected count number " + out);
                    System.err.println("from " + f.getAbsolutePath());
                }
                String p = progress(i,s);
                if (Double.parseDouble(p) % 10 == 0) System.out.println(p + " %");
                i++;
            }

            System.out.println("count 15: " + cnt15);
            System.out.println("count  5: " + cnt5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // input path of log file
    // output log file splited by ","
    List<String[]> path2SplitedLog(String path) {
        List<String[]> res = new ArrayList<>();
        File f = new File(path);
        try (BufferedReader reader = new BufferedReader
                (new InputStreamReader
                        (new FileInputStream(f), StandardCharsets.UTF_8));){
            String str;
            while ((str = reader.readLine()) != null) {
                //System.out.println(Arrays.asList(splitLine(str)));
                res.add(splitLine(str));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    String[] splitLine(String line) {
        return line.split(",");
    }

    List<String> path2Line(String path) {
        File f = new File(path);
        List<String> res = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader
                (new InputStreamReader
                        (new FileInputStream(f), StandardCharsets.UTF_8));){
            String str;
            while ((str = reader.readLine()) != null) {
               res.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    List<Talk> getTalkList(String path) {
        Util u = new Util();
        List<Talk> talkList = new ArrayList<>();
        try {
            List<String[]> log = u.path2SplitedLog(path);

            for (String[] line : log) {
                //System.out.println(Arrays.asList(line));
                if (line[1].equals("talk")) talkList.add(new Talk(line));
            }

            //System.out.println(talkList.size());

            //for (Talk t : talkList) System.out.println(t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return talkList;
    }
    List<Vote> getVoteList(String path) {
        Util u = new Util();
        List<Vote> voteList = new ArrayList<>();
        try {
            List<String[]> log = u.path2SplitedLog(path);

            for (String[] line : log) {
                //System.out.println(Arrays.asList(line));
                if (line[1].equals("vote")) voteList.add(new Vote(line));
            }

            //System.out.println(talkList.size());

            //for (Talk t : talkList) System.out.println(t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return voteList;
    }
    List<Attack> getAttackList(String path) {
        Util u = new Util();
        List<Attack> attackList = new ArrayList<>();
        try {
            List<String[]> log = u.path2SplitedLog(path);

            for (String[] line : log) {
                //System.out.println(Arrays.asList(line));
                if (line[1].equals("attack")) attackList.add(new Attack(line));
            }

            //System.out.println(talkList.size());

            //for (Talk t : talkList) System.out.println(t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return attackList;
    }
    List<AttackVote> getAttackVoteList(String path) {
        Util u = new Util();
        List<AttackVote> attackVoteList = new ArrayList<>();
        try {
            List<String[]> log = u.path2SplitedLog(path);

            for (String[] line : log) {
                //System.out.println(Arrays.asList(line));
                if (line[1].equals("attackVote")) attackVoteList.add(new AttackVote(line));
            }

            //System.out.println(talkList.size());

            //for (Talk t : talkList) System.out.println(t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return attackVoteList;
    }
    List<Divine> getDivineList(String path) {
        Util u = new Util();
        List<Divine> divineList = new ArrayList<>();
        try {
            List<String[]> log = u.path2SplitedLog(path);

            for (String[] line : log) {
                //System.out.println(Arrays.asList(line));
                if (line[1].equals("divine")) divineList.add(new Divine(line));
            }

            //System.out.println(talkList.size());

            //for (Talk t : talkList) System.out.println(t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return divineList;
    }
    List<Execute> getExecuteList(String path) {
        Util u = new Util();
        List<Execute> executeList = new ArrayList<>();
        try {
            List<String[]> log = u.path2SplitedLog(path);

            for (String[] line : log) {
                //System.out.println(Arrays.asList(line));
                if (line[1].equals("execute")) executeList.add(new Execute(line));
            }

            //System.out.println(talkList.size());

            //for (Talk t : talkList) System.out.println(t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return executeList;
    }
    List<Guard> getGuardList(String path) {
        Util u = new Util();
        List<Guard> guardList = new ArrayList<>();
        try {
            List<String[]> log = u.path2SplitedLog(path);

            for (String[] line : log) {
                //System.out.println(Arrays.asList(line));
                if (line[1].equals("guard")) guardList.add(new Guard(line));
            }

            //System.out.println(talkList.size());

            //for (Talk t : talkList) System.out.println(t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return guardList;
    }
    List<Result> getResultList(String path) {
        Util u = new Util();
        List<Result> resultList = new ArrayList<>();
        try {
            List<String[]> log = u.path2SplitedLog(path);

            for (String[] line : log) {
                //System.out.println(Arrays.asList(line));
                if (line[1].equals("result")) resultList.add(new Result(line));
            }

            //System.out.println(talkList.size());

            //for (Talk t : talkList) System.out.println(t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
    List<Status> getStatusList(String path) {
        Util u = new Util();
        List<Status> statusList = new ArrayList<>();
        try {
            List<String[]> log = u.path2SplitedLog(path);

            for (String[] line : log) {
                //System.out.println(Arrays.asList(line));
                if (line[1].equals("status")) statusList.add(new Status(line));
            }

            //System.out.println(talkList.size());

            //for (Talk t : talkList) System.out.println(t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return statusList;
    }
    List<Whisper> getWhisperList(String path) {
        Util u = new Util();
        List<Whisper> whisperList = new ArrayList<>();
        try {
            List<String[]> log = u.path2SplitedLog(path);

            for (String[] line : log) {
                //System.out.println(Arrays.asList(line));
                if (line[1].equals("whisper")) whisperList.add(new Whisper(line));
            }

            //System.out.println(talkList.size());

            //for (Talk t : talkList) System.out.println(t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return whisperList;
    }

    String progress(int now, int lim) {
        return String.format("%.2f",(double) now/lim * 100);
    }
    void printProg(int now , int lim, int per) {
        String prog = progress(now,lim);
        if (Double.parseDouble(prog) % per == 0) System.out.println(prog + " %");
    }
}

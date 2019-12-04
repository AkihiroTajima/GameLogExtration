import java.util.List;

public class InputTalkData {
    List<String> talkList;
    List<Talk> talks;
    private String voteTarget;
    private String role;
    private String name;
    private double numVoteforWolf;

    public List<String> getTalkList() {
        return talkList;
    }

    public String getVoteTarget() {
        return voteTarget;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }



    public double getNumVoteforWolf() {
        return numVoteforWolf;
    }

    InputTalkData(List<String> inputTalkList, String inputName) {
        this.talkList = inputTalkList;
        this.name = inputName;
    }
    InputTalkData(List<Talk> inputtalks, double numVoteforWolf) {
        this.talks = inputtalks;
        this.numVoteforWolf = numVoteforWolf;
    }

    public void setTalkList(List<String> talkList) {
        this.talkList = talkList;
    }

    public void setTalks(List<Talk> talks) {
        this.talks = talks;
    }

    public void setVoteTarget(String voteTarget) {
        this.voteTarget = voteTarget;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumVoteforWolf(double numVoteforWolf) {
        this.numVoteforWolf = numVoteforWolf;
    }

    InputTalkData(List<String> inputTalkList, String inputName, double numVoteforWolf) {
        this.talkList = inputTalkList;
        this.name = inputName;
        this.numVoteforWolf = numVoteforWolf;
    }
    InputTalkData(List<String> inputTalkList, String inputName, String inputRole) {
        this.talkList = inputTalkList;
        this.name = inputName;
        this.role = inputRole;
    }
//    InputTalkData(List<String> inputTalkList, String inputName, String inputVote) {
//        this.talkList = inputTalkList;
//        this.name = inputName;
//        this.voteTarget = inputVote;
//    }
    InputTalkData(List<String> inputTalkList, String inputName, String inputVote, String inputRole) {
        this.talkList = inputTalkList;
        this.name = inputName;
        this.voteTarget = inputVote;
        this.role = inputRole;
    }

}

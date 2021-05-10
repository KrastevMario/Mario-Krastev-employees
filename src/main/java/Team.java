public class Team {
    private int firstEmpId;
    private int secondEmpId;
    private long timeWorkedTogether;

    public Team(int firstEmpId, int secondEmpId, long timeWorkedTogether){
        this.firstEmpId = firstEmpId;
        this.secondEmpId = secondEmpId;
        this.timeWorkedTogether = timeWorkedTogether;
    }

    public int getFirstEmpId() {
        return firstEmpId;
    }

    public void setFirstEmpId(int firstEmpId) {
        this.firstEmpId = firstEmpId;
    }

    public int getSecondEmpId() {
        return secondEmpId;
    }

    public void setSecondEmpId(int secondEmpId) {
        this.secondEmpId = secondEmpId;
    }

    public long getTimeWorkedTogether() {
        return timeWorkedTogether;
    }

    public void setTimeWorkedTogether(long timeWorkedTogether) {
        this.timeWorkedTogether = timeWorkedTogether;
    }

    public Team setTimeWorkedTogetherReturningTeam(long timeWorkedTogether) {
        this.timeWorkedTogether = timeWorkedTogether;
        return this;
    }
}

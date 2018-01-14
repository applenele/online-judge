package judge.beans;

public class SubmitBean {
    private int submitID;
    private String language;
    private int timeLimit;
    private int memLimit;
    private String runningFolder;
    private String testPointDataFolder;

    public SubmitBean() {
    }

    public SubmitBean(int submitID, String language, int timeLimit, int memLimit, String runningFolder, String testPointDataFolder) {
        this.submitID = submitID;
        this.language = language;
        this.timeLimit = timeLimit;
        this.memLimit = memLimit;
        this.runningFolder = runningFolder;
        this.testPointDataFolder = testPointDataFolder;
    }

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getMemLimit() {
        return memLimit;
    }

    public void setMemLimit(int memLimit) {
        this.memLimit = memLimit;
    }

    public String getRunningFolder() {
        return runningFolder;
    }

    public void setRunningFolder(String runningFolder) {
        this.runningFolder = runningFolder;
    }

    public String getTestPointDataFolder() {
        return testPointDataFolder;
    }

    public void setTestPointDataFolder(String testPointDataFolder) {
        this.testPointDataFolder = testPointDataFolder;
    }

    @Override
    public String toString() {
        return "SubmitBean{" +
                "submitID=" + submitID +
                ", language='" + language + '\'' +
                ", timeLimit=" + timeLimit +
                ", memLimit=" + memLimit +
                ", runningFolder='" + runningFolder + '\'' +
                ", testPointDataFolder='" + testPointDataFolder + '\'' +
                '}';
    }

    public String toJson() {
        String json = "{\"submitID\":%d,\"language\":\"%s\",\"timeLimit\":%d,\"memLimit\":%d,\"runningFolder\":\"%s\",\"testPointDataFolder\":\"%s\"}";
        return String.format(json, submitID, language, timeLimit, memLimit, runningFolder, testPointDataFolder);
    }
}

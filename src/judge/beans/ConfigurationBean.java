package judge.beans;

public class ConfigurationBean {
    /*judge server*/
    private String serverAddress;
    private int    serverPort;

    /*judge config*/
    private String testPointBaseDir;
    private String runningBaseDir;
    private int timeout;

    /*web config*/
    private int countPerPage;

    public ConfigurationBean(String serverAddress, int serverPort, String testPointBaseDir, String runningBaseDir, int timeout, int countPerPage) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.testPointBaseDir = testPointBaseDir;
        this.runningBaseDir = runningBaseDir;
        this.timeout = timeout;
        this.countPerPage = countPerPage;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getTestPointBaseDir() {
        return testPointBaseDir;
    }

    public void setTestPointBaseDir(String testPointBaseDir) {
        this.testPointBaseDir = testPointBaseDir;
    }

    public String getRunningBaseDir() {
        return runningBaseDir;
    }

    public void setRunningBaseDir(String runningBaseDir) {
        this.runningBaseDir = runningBaseDir;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getCountPerPage() {
        return countPerPage;
    }

    public void setCountPerPage(int countPerPage) {
        this.countPerPage = countPerPage;
    }

    @Override
    public String toString() {
        return "ConfigurationBean{" +
                "serverAddress='" + serverAddress + '\'' +
                ", serverPort=" + serverPort +
                ", testPointBaseDir='" + testPointBaseDir + '\'' +
                ", runningBaseDir='" + runningBaseDir + '\'' +
                ", timeout=" + timeout +
                ", countPerPage=" + countPerPage +
                '}';
    }
}

/*
{
    "serverAddress":"127.0.0.0.1",
    "serverPort":2345,
    "testPointBaseDir":"/s/s/s/s",
    "runningBaseDir":"/s/s/s",
    "timeout":23,
    "countPerPage":23
}
*/
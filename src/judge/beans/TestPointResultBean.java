package judge.beans;

/**
 * Created by xanarry on 18-1-8.
 */
public class TestPointResultBean {

    // {"testPointID":1,"timeConsume":234,"memConsume":3421,"returnVal":0,"result":"accepted"},

    private int testPointID;
    private int timeConsume;
    private int memConsume;
    private String result;

    public TestPointResultBean() {
    }

    public TestPointResultBean(int testPointID, int timeConsume, int memConsume, String result) {
        this.testPointID = testPointID;
        this.timeConsume = timeConsume;
        this.memConsume = memConsume;
        this.result = result;
    }

    public int getTestPointID() {
        return testPointID;
    }

    public void setTestPointID(int testPointID) {
        this.testPointID = testPointID;
    }

    public int getTimeConsume() {
        return timeConsume;
    }

    public void setTimeConsume(int timeConsume) {
        this.timeConsume = timeConsume;
    }

    public int getMemConsume() {
        return memConsume;
    }

    public void setMemConsume(int memConsume) {
        this.memConsume = memConsume;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "TestPointResultBean{" +
                "testPointID=" + testPointID +
                ", timeConsume=" + timeConsume +
                ", memConsume=" + memConsume +
                ", result='" + result + '\'' +
                '}';
    }
}

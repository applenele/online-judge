package judge;

/**
 * Created by xanarry on 18-1-8.
 */
public class TestPointResultBean {

    // {"testPointID":1,"timeConsume":234,"memConsume":3421,"returnVal":0,"result":"accepted"},

    private int testPointID;
    private int timeConsume;
    private int memConsume;
    private int returnVal;
    private String result;

    public TestPointResultBean() {
    }

    public TestPointResultBean(int testPointID, int timeConsume, int memConsume, int returnVal, String result) {
        this.testPointID = testPointID;
        this.timeConsume = timeConsume;
        this.memConsume = memConsume;
        this.returnVal = returnVal;
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

    public int getReturnVal() {
        return returnVal;
    }

    public void setReturnVal(int returnVal) {
        this.returnVal = returnVal;
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
                ", returnVal=" + returnVal +
                ", result='" + result + '\'' +
                '}';
    }
}

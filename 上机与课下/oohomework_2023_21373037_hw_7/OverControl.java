public class OverControl {
    private int totalPersonRequest;
    private int hasDone;

    private int endFlag;

    public OverControl() {
        this.totalPersonRequest = 0;
        this.hasDone = 0;
        endFlag = 0;
    }

    public int getEndFlag() {
        return endFlag;
    }

    public void setEndFlag() {
        this.endFlag = 1;
    }

    public synchronized void addRequest() {
        totalPersonRequest++;
    }

    public synchronized void hasDoneRequest() {
        hasDone++;
    }

    public synchronized boolean canOver() {
        return hasDone == totalPersonRequest && endFlag == 1;
    }
}

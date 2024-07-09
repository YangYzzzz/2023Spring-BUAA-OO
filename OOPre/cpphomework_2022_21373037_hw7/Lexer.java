
public class Lexer {
    private String data;
    private String now = null; //{ } [ ] : String
    private int position = 0;
    private int contentFlag = 0;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void read() {
        char[] a = data.toCharArray();
        StringBuilder flag = new StringBuilder();
        this.contentFlag = 0;
        for (int i = position; i < data.length(); i++) {
            if (a[i] == '{') {
                now = "{";
                position = i + 1;
                return;
            } else if (a[i] == '}') {
                now = "}";
                position = i + 1;
                return;
            } else if (a[i] == '[') {
                now = "[";
                position = i + 1;
                return;
            } else if (a[i] == ']') {
                now = "]";
                position = i + 1;
                return;
            } else if (a[i] == '\"') { //寻找字符串
                i++;
                for (; a[i] != '\"'; i++) {
                    flag.append(a[i]);
                }
                now = flag.toString();
                position = i + 1;
                return;
            } else if (a[i] >= '0' && a[i] <= '9') { //寻找数字 ,逗号无所谓   ##
                for (; a[i] != ','; i++) {
                    if (a[i] == ']' || a[i] == '}') {
                        position = i;
                        break;
                    }
                    flag.append(a[i]);
                }
                now = flag.toString();
                position = i;
                return;
            } else if (a[i] == 't') {
                position = i + 4;
                now = "true";
                return;
            } else if (a[i] == 'n') {
                position = i + 4;
                now = "null";
                contentFlag = 1;
                return;
            } else if (a[i] == 'f') {
                position = i + 5;
                now = "false";
                return;
            }
        }
    }

    public int getContentFlag() {
        return contentFlag;
    }

    public void setContentFlag(int contentFlag) {
        this.contentFlag = contentFlag;
    }
}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OkTest {
    public static boolean ok1(HashMap<Integer, Integer> before,
                       HashMap<Integer, Integer> after, int limit) {
        for (Map.Entry<Integer, Integer> entry : before.entrySet()) {
            if (entry.getValue() >= limit) {
                if (!after.containsKey(entry.getKey())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean ok2(HashMap<Integer, Integer> before, HashMap<Integer, Integer> after) {
        for (Map.Entry<Integer, Integer> entry : after.entrySet()) {
            if (!before.containsKey(entry.getKey()) ||
                    before.get(entry.getKey()) != entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    public static boolean ok3(HashMap<Integer, Integer> before,
                       HashMap<Integer, Integer> after, int limit) {
        int cnt = 0;
        for (Map.Entry<Integer, Integer> entry : before.entrySet()) {
            if (entry.getValue() >= limit) {
                cnt++;
            }
        }
        return cnt == after.size();
    }

    public static boolean ok5(HashMap<Integer, Integer> beforeMes, HashMap<Integer, Integer>
            afterMes, HashMap<Integer, Integer> afterEmoji) {
        for (Map.Entry<Integer, Integer> entry : beforeMes.entrySet()) {
            if (entry.getValue() != null && afterEmoji.containsKey(entry.getValue())) {
                if (!afterMes.containsKey(entry.getKey()) ||
                        afterMes.get(entry.getKey()) != entry.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean ok6(HashMap<Integer, Integer> beforeMes,
                              HashMap<Integer, Integer> afterMes) {
        for (Map.Entry<Integer, Integer> entry : beforeMes.entrySet()) {
            if (entry.getValue() == null) {
                if (!afterMes.containsKey(entry.getKey()) || afterMes.get(entry.getKey()) != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean ok7(HashMap<Integer, Integer> beforeMes, HashMap<Integer, Integer>
            afterMes, HashMap<Integer, Integer> afterEmoji) {
        int cnt = 0;
        for (Map.Entry<Integer, Integer> entry : beforeMes.entrySet()) {
            if (entry.getValue() == null || afterEmoji.containsKey(entry.getValue())) {
                cnt++;
            }
        }
        return cnt == afterMes.size();
    }

    public static int deleteColdEmojiOKTest(int limit, ArrayList<HashMap<Integer, Integer>>
            beforeData, ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        HashMap<Integer, Integer> beforeEmoji = beforeData.get(0);
        HashMap<Integer, Integer> beforeMessage = beforeData.get(1);
        HashMap<Integer, Integer> afterEmoji = afterData.get(0);
        HashMap<Integer, Integer> afterMessage = afterData.get(1);
        if (!ok1(beforeEmoji, afterEmoji, limit)) {
            return 1;
        } else if (!ok2(beforeEmoji, afterEmoji)) {
            return 2;
        } else if (!ok3(beforeEmoji, afterEmoji, limit)) {
            return 3; //4保证对
        } else if (!ok5(beforeMessage, afterMessage, afterEmoji)) {
            return 5;
        } else if (!ok6(beforeMessage, afterMessage)) {
            return 6;
        } else if (!ok7(beforeMessage, afterMessage, afterEmoji)) {
            return 7;
        } else if (result != afterEmoji.size()) {
            return 8;
        }
        return 0;
    }
}

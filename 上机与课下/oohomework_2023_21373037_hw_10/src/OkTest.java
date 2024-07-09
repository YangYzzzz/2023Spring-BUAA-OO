import java.util.HashMap;
import java.util.Map;

public class OkTest {
    public static boolean ok20and21(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                    HashMap<Integer, HashMap<Integer, Integer>> afterData, int id) {
        HashMap<Integer, Integer> hashMap1 = beforeData.get(id);
        HashMap<Integer, Integer> hashMap2 = afterData.get(id); //after里面有的 -> before一定全有
        for (Map.Entry<Integer, Integer> entry : hashMap2.entrySet()) {
            if (!hashMap1.containsKey(entry.getKey()) ||
                    !hashMap1.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public static int ok2and3(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                       HashMap<Integer, HashMap<Integer, Integer>> afterData, int id1, int id2) {
        for (Integer integer : beforeData.keySet()) {
            if (!afterData.containsKey(integer)) {
                return 2;
            }
            if (integer != id1 && integer != id2 &&
                    !beforeData.get(integer).equals(afterData.get(integer))) {
                return 3;
            }
        }
        return 0;
    }

    public static boolean ok11and12(HashMap<Integer, HashMap<Integer, Integer>> before,
                           HashMap<Integer, HashMap<Integer, Integer>> after, int id1, int id2) {
        for (Integer i : after.get(id1).keySet()) {
            if (i != id2 && !after.get(id1).get(i).equals(before.get(id1).get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean ok13and14and18and19(HashMap<Integer, HashMap<Integer, Integer>>
                                                      data, int id) {
        int keyNum = 0;
        int valueNum = 0;
        for (Map.Entry<Integer, Integer> entry : data.get(id).entrySet()) {
            if (entry.getKey() != null) {
                keyNum++;
            }
            if (entry.getValue() != null) {
                valueNum++;
            }
        }
        return keyNum == valueNum;
    }

    public static int modifyRelationOkTest(int id1, int id2, int value, HashMap<Integer, HashMap
            <Integer, Integer>> beforeData, HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        //副作用 改动内层hashmap的value 可能
        int tmp;
        if ((!beforeData.containsKey(id1) || !beforeData.containsKey(id2) || id1 == id2
                || !beforeData.get(id1).containsKey(id2)) && !beforeData.equals(afterData)) {
            return -1;
        } else if ((!beforeData.containsKey(id1) || !beforeData.containsKey(id2) || id1 == id2
                || !beforeData.get(id1).containsKey(id2)) && beforeData.equals(afterData)) {
            return 0;
        } else if (beforeData.size() != afterData.size()) {
            return 1;
        } else if ((tmp = ok2and3(beforeData, afterData, id1, id2)) != 0) {
            return tmp; //包含2和3
        } else if (beforeData.get(id1).get(id2) + value > 0) {
            if (!afterData.get(id1).containsKey(id2) || !afterData.get(id2).containsKey(id1)) {
                return 4;
            } else if (!(afterData.get(id1).get(id2) == beforeData.get(id1).get(id2) + value)) {
                return 5;
            } else if (!(afterData.get(id2).get(id1) == beforeData.get(id2).get(id1) + value)) {
                return 6;
            } else if (!(afterData.get(id1).size() == beforeData.get(id1).size())) {
                return 7;
            } else if (!(afterData.get(id2).size() == beforeData.get(id2).size())) {
                return 8;
            } else if (!afterData.get(id1).keySet().equals(beforeData.get(id1).keySet())) {
                return 9; //包含9，10
            } else if (!afterData.get(id2).keySet().equals(beforeData.get(id2).keySet())) {
                return 10;
            } else if (!ok11and12(beforeData, afterData, id1, id2)) {
                return 11;
            } else if (!ok11and12(beforeData, afterData, id2, id1)) {
                return 12;
            } else if (!ok13and14and18and19(afterData, id1)) { //默认1314一定对
                return 13;
            } else if (!ok13and14and18and19(afterData, id2)) {
                return 14;
            } else {
                return 0;
            }
        } else {
            if (afterData.get(id1).containsKey(id2) || afterData.get(id2).containsKey(id1)) {
                return 15;
            } else if (beforeData.get(id1).size() != afterData.get(id1).size() + 1) {
                return 16;
            } else if (beforeData.get(id2).size() != afterData.get(id2).size() + 1) {
                return 17;
            } else if (!ok13and14and18and19(afterData, id1)) { //默认1314一定对
                return 18;
            } else if (!ok13and14and18and19(afterData, id2)) {
                return 19;
            } else if (!ok20and21(beforeData, afterData, id1)) {
                return 20;
            } else if (!ok20and21(beforeData, afterData, id2)) {
                return 21;
            } else {
                return 0;
            }
        }
    }
}

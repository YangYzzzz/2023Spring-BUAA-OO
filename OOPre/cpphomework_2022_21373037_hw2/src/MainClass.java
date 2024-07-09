package src;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//list.add();  list.remove(返回布尔 or 返回删的东西)； list.set(index,e); list.get(index);
public class MainClass {

    private static ArrayList<Adventurer> adventurers = new ArrayList<>(); //静态变量对这个类共享
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int mark;
        int count;
        count = sc.nextInt();
        for (; count > 0; count--) {
            mark = sc.nextInt();
            switch (mark) {
                case 1:
                    operation1();
                    break;
                case 2:
                    operation2();
                    break;
                case 3:
                    operation3();
                    break;
                case 4:
                    operation4();
                    break;
                case 5:
                    operation5();
                    break;
                case 6:
                    operation6();
                    break;
                case 7:
                    operation7();
                    break;
                case 8:
                    operation8();
                    break;
                case 9:
                    operation9();
                    break;
                default:
                    break;
            }
        }
    }

    public static void operation1() {
        //Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        String name = sc.next();
        HashMap<Integer, Equipment> equipments = new HashMap<>();
        Adventurer adventurer = new Adventurer(id, name, equipments, 100.0, 0, 0);
        adventurers.add(adventurer);
    }

    public static void operation2() {
        int advId = sc.nextInt();
        int equipmentType = sc.nextInt();
        int equId = sc.nextInt();
        String name = sc.next();
        long price = sc.nextLong();
        switch (equipmentType) {
            case 1:
                setBottle(advId, equId, name, price);
                break;
            case 2:
                setHealingPotion(advId, equId, name, price);
                break;
            case 3:
                setExpBottle(advId, equId, name, price);
                break;
            case 4:
                setSword(advId, equId, name, price);
                break;
            case 5:
                setRareSword(advId, equId, name, price);
                break;
            case 6:
                setEpicSword(advId, equId, name, price);
                break;
            default:
                break;
        }
    }

    public static void setBottle(int advId, int id, String name, long price) {
        double capacity = sc.nextDouble();
        Equipment bottle = new Bottle(id, name, price, capacity, true);
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                item.getEquipments().put(id, bottle);
            }
        }
    }

    public static void setHealingPotion(int advId, int id, String name, long price) {
        double capacity = sc.nextDouble();
        double efficiency = sc.nextDouble();
        Equipment healingPotion = new HealingPotion(id, name, price, capacity, true, efficiency);
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                item.getEquipments().put(id, healingPotion);
            }
        }
    }

    public static void setExpBottle(int advId, int id, String name, long price) {
        double capacity = sc.nextDouble();
        double expRatio = sc.nextDouble();
        Equipment expBottle = new ExpBottle(id, name, price, capacity, true, expRatio);
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                item.getEquipments().put(id, expBottle);
            }
        }
    }

    public static void setSword(int advId, int id, String name, long price) {
        double sharpness = sc.nextDouble();
        Equipment sword = new Sword(id, name, price, sharpness);
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                item.getEquipments().put(id, sword);
            }
        }
    }

    public static void setRareSword(int advId, int id, String name, long price) {
        double sharpness = sc.nextDouble();
        double extraExpBonus = sc.nextDouble();
        Equipment rareSword = new RareSword(id, name, price, sharpness, extraExpBonus);
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                item.getEquipments().put(id, rareSword);
            }
        }
    }

    public static void setEpicSword(int advId, int id, String name, long price) {
        double sharpness = sc.nextDouble();
        double evolveRatio = sc.nextDouble();
        Equipment epicSword = new EpicSword(id, name, price, sharpness, evolveRatio);
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                item.getEquipments().put(id, epicSword);
            }
        }
    }

    public static void operation3() {
        int advId = sc.nextInt();
        int equId = sc.nextInt();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                for (HashMap.Entry<Integer, Equipment> entry : item.getEquipments().entrySet()) {
                    if (entry.getValue().getId() == equId) {
                        entry.getValue().setFlag(1);
                    }
                }
            }
        }
    }

    public static void operation7() {
        int advId = sc.nextInt();
        int equId = sc.nextInt();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                for (HashMap.Entry<Integer, Equipment> entry : item.getEquipments().entrySet()) {
                    if (entry.getValue().getId() == equId) {
                        entry.getValue().printf();
                    }
                }
            }
        }
    }

    public static void operation4() {
        int advId = sc.nextInt();
        BigInteger totalPrice = new BigInteger("0");
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                for (HashMap.Entry<Integer, Equipment> entry : item.getEquipments().entrySet()) {
                    if (entry.getValue().getFlag() == 0) {
                        totalPrice = totalPrice.add(BigInteger.valueOf(
                                entry.getValue().getPrice()));
                    }
                }
                System.out.println(totalPrice);
            }
        }
    }

    public static void operation5() {
        int advId = sc.nextInt();
        long maxPrice = 0L;
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                for (HashMap.Entry<Integer, Equipment> entry : item.getEquipments().entrySet()) {
                    if (entry.getValue().getPrice() > maxPrice && entry.getValue().getFlag() == 0) {
                        maxPrice = entry.getValue().getPrice();
                    }
                }
                System.out.println(maxPrice);
            }
        }
    }

    public static void operation6() {
        int advId = sc.nextInt();
        int cnt = 0;
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                for (HashMap.Entry<Integer, Equipment> entry : item.getEquipments().entrySet()) {
                    if (entry.getValue().getFlag() == 0) {
                        cnt++;
                    }
                }
                System.out.println(cnt);
            }
        }
    }

    public static void operation8() {
        int advId = sc.nextInt();
        int equId = sc.nextInt();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                for (HashMap.Entry<Integer, Equipment> entry : item.getEquipments().entrySet()) {
                    if (entry.getValue().getId() == equId) {
                        item.use(entry.getValue());
                    }
                }
            }
        }
    }

    public static void operation9() {
        int advId = sc.nextInt();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                System.out.println("The adventurer's id is " + item.getId() + ", name is " +
                        item.getName() + ", health is " + item.getHealth() + ", exp is " +
                        item.getExp() + ", money is " + item.getMoney() + ".");
            }
        }
    }
}

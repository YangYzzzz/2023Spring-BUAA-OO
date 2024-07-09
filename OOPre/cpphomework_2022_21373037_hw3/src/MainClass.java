package src;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//list.add();  list.remove(返回布尔 or 返回删的东西)； list.set(index,e); list.get(index);
public class MainClass {

    private static HashMap<String, ArrayList<Adventurer>> versionControl = new HashMap<>();
    private static ArrayList<Adventurer> version1 = new ArrayList<>(); //静态变量对这个类共享
    private static ArrayList<Adventurer> adventurers;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        versionControl.put("1", version1);
        adventurers = version1; //浅拷贝 引用
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
                case 10:
                    operation10();
                    break;
                case 11:
                    operation11();
                    break;
                case 12:
                    adventurers = operation12();
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
        ArrayList<Commodity> commodities = new ArrayList<>();
        Adventurer adventurer = new Adventurer(id, name, commodities, 100.0, 0, 0);
        adventurers.add(adventurer);
    }

    public static void operation2() {
        int advId = sc.nextInt();
        int commoditiesType = sc.nextInt();
        int equId = sc.nextInt();
        String name = sc.next();
        long price = sc.nextLong();
        switch (commoditiesType) {
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
        Bottle bottle = new Bottle(id, name, BigInteger.valueOf(price), capacity, true);
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                item.getCommodities().add(bottle);
            }
        }
    }

    public static void setHealingPotion(int advId, int id, String name, long price) {
        double capacity = sc.nextDouble();
        double efficiency = sc.nextDouble();
        HealingPotion healingPotion = new HealingPotion(id, name, BigInteger.valueOf(price),
                capacity, true, efficiency);
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                item.getCommodities().add(healingPotion);
            }
        }
    }

    public static void setExpBottle(int advId, int id, String name, long price) {
        double capacity = sc.nextDouble();
        double expRatio = sc.nextDouble();
        ExpBottle expBottle = new ExpBottle(id, name, BigInteger.valueOf(price),
                capacity, true, expRatio);
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                item.getCommodities().add(expBottle);
            }
        }
    }

    public static void setSword(int advId, int id, String name, long price) {
        double sharpness = sc.nextDouble();
        Sword sword = new Sword(id, name, BigInteger.valueOf(price), sharpness);
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                item.getCommodities().add(sword);
            }
        }
    }

    public static void setRareSword(int advId, int id, String name, long price) {
        double sharpness = sc.nextDouble();
        double extraExpBonus = sc.nextDouble();
        RareSword rareSword = new RareSword(id, name, BigInteger.valueOf(price),
                sharpness, extraExpBonus);
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                item.getCommodities().add(rareSword);
            }
        }
    }

    public static void setEpicSword(int advId, int id, String name, long price) {
        double sharpness = sc.nextDouble();
        double evolveRatio = sc.nextDouble();
        EpicSword epicSword = new EpicSword(id, name, BigInteger.valueOf(price),
                sharpness, evolveRatio);
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                item.getCommodities().add(epicSword);
            }
        }
    }

    public static void operation3() {
        //System.out.println("3!   ");
        int advId = sc.nextInt();
        int equId = sc.nextInt();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                for (int i = 0; i < item.getCommodities().size(); i++) {
                    if (item.getCommodities().get(i).getId() == equId) {
                        item.getCommodities().remove(i);
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
                for (Commodity items : item.getCommodities()) {
                    if (items.getId() == equId) {
                        items.printf();
                    }
                }
            }
        }
    }

    public static void operation4() {
        //System.out.println("!23");
        int advId = sc.nextInt();
        BigInteger totalPrice = new BigInteger("0");
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                //System.out.println("!"+item.getCommodities().size());
                for (Commodity items : item.getCommodities()) {
                    //System.out.println("!1"+item.getPrice());
                    totalPrice = totalPrice.add(
                            items.getPrice());
                }
                System.out.println(totalPrice);
            }
        }
    }

    public static void operation5() {
        int advId = sc.nextInt();
        BigInteger maxPrice = new BigInteger("0");
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                for (Commodity items : item.getCommodities()) {
                    if (items.getPrice().compareTo(maxPrice) == 1) {
                        maxPrice = items.getPrice();
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
                for (Commodity items : item.getCommodities()) {
                    cnt++;
                }
                System.out.println(cnt);
            }
        }
    }

    public static void operation8() {
        int advId = sc.nextInt();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                item.use(item);
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

    public static void operation10() {
        //System.out.println("10!   ");
        int advId1 = sc.nextInt();
        //System.out.println(adv_Id1);
        int advId2 = sc.nextInt();
        for (Adventurer item : adventurers) {
            if (advId1 == item.getId()) {
                //System.out.println("#"+item.getCommodities().size());
                for (Adventurer items : adventurers) {
                    if (advId2 == items.getId()) {
                        item.getCommodities().add(items);
                    }
                }
            }
        }
        //System.out.println("end");
    }

    public static void operation11() {
        String branchName = sc.next(); //对此刻adventure数据深拷贝
        ArrayList<Adventurer> versionNew = new ArrayList<>();
        HashMap<Integer, Adventurer> support = new HashMap<>();
        for (Adventurer item : adventurers) {
            Adventurer adventurer = new Adventurer();
            versionNew.add(copyAdventurer(item, adventurer, support));
        }
        versionControl.put(branchName, versionNew);
        adventurers = versionNew;
    }

    public static Bottle copyBottle(Bottle sample, Bottle shadow) {
        shadow.setId(sample.getId());
        shadow.setName(sample.getName());
        shadow.setFilled(sample.isFilled());
        //shadow.setFilled(true);
        shadow.setPrice(sample.getPrice());
        shadow.setCapacity(sample.getCapacity());
        return shadow;
    }

    public static Sword copySword(Sword sample, Sword shadow) {
        shadow.setId(sample.getId());
        shadow.setName(sample.getName());
        shadow.setSharpness(sample.getSharpness());
        shadow.setPrice(sample.getPrice());
        return shadow;
    }

    public static ExpBottle copyExpBottle(ExpBottle sample, ExpBottle shadow) {
        shadow.setId(sample.getId());
        shadow.setName(sample.getName());
        shadow.setFilled(sample.isFilled());
        //shadow.setFilled(true);
        shadow.setPrice(sample.getPrice());
        shadow.setCapacity(sample.getCapacity());
        shadow.setExpRatio(sample.getExpRatio());
        return shadow;
    }

    public static HealingPotion copyHealingPotion(HealingPotion sample, HealingPotion shadow) {
        shadow.setId(sample.getId());
        shadow.setName(sample.getName());
        shadow.setFilled(sample.isFilled());
        //shadow.setFilled(true);
        shadow.setPrice(sample.getPrice());
        shadow.setCapacity(sample.getCapacity());
        shadow.setEfficiency(sample.getEfficiency());
        return shadow;
    }

    public static EpicSword copyEpicSword(EpicSword sample, EpicSword shadow) {
        shadow.setId(sample.getId());
        shadow.setName(sample.getName());
        shadow.setSharpness(sample.getSharpness());
        shadow.setPrice(sample.getPrice());
        shadow.setEvolveRatio(sample.getEvolveRatio());
        return shadow;
    }

    public static RareSword copyRareSword(RareSword sample, RareSword shadow) {
        shadow.setId(sample.getId());
        shadow.setName(sample.getName());
        shadow.setSharpness(sample.getSharpness());
        shadow.setPrice(sample.getPrice());
        shadow.setExtraExpBonus(sample.getExtraExpBonus());
        return shadow;
    }

    public static Adventurer copyAdventurer(Adventurer sample, Adventurer shadow,
                                            HashMap<Integer, Adventurer> support) {
        for (HashMap.Entry<Integer, Adventurer> entry : support.entrySet()) {
            if (entry.getValue().getId() == sample.getId()) {
                return entry.getValue(); //返回一个浅拷贝引用
            }
        }
        ArrayList<Commodity> commodities = new ArrayList<>();
        shadow.setId(sample.getId());
        shadow.setName(sample.getName());
        shadow.setMoney(sample.getMoney());
        shadow.setExp(sample.getExp());
        shadow.setHealth(sample.getHealth());
        for (Commodity items : sample.getCommodities()) {
            if (items instanceof Adventurer) {
                Adventurer newCommodity = new Adventurer();
                newCommodity = copyAdventurer((Adventurer) items, newCommodity, support);
                commodities.add(newCommodity);
            } else if (items instanceof ExpBottle) {
                ExpBottle newCommodity = new ExpBottle();
                newCommodity = copyExpBottle((ExpBottle) items, newCommodity);
                commodities.add(newCommodity);
            } else if (items instanceof HealingPotion) {
                HealingPotion newCommodity = new HealingPotion();
                newCommodity = copyHealingPotion((HealingPotion) items, newCommodity);
                commodities.add(newCommodity);
            } else if (items instanceof EpicSword) {
                EpicSword newCommodity = new EpicSword();
                newCommodity = copyEpicSword((EpicSword) items, newCommodity);
                commodities.add(newCommodity);
            } else if (items instanceof RareSword) {
                RareSword newCommodity = new RareSword();
                newCommodity = copyRareSword((RareSword) items, newCommodity);
                commodities.add(newCommodity);
            } else if (items instanceof Bottle) {
                Bottle newCommodity = new Bottle();
                newCommodity = copyBottle((Bottle) items, newCommodity);
                commodities.add(newCommodity);
            } else {
                Sword newCommodity = new Sword();
                newCommodity = copySword((Sword) items, newCommodity);
                commodities.add(newCommodity);
            }
        }
        shadow.setCommodities(commodities);
        support.put(sample.getId(), shadow);
        return shadow;
    }

    public static ArrayList<Adventurer> operation12() {
        String branchName = sc.next();
        for (HashMap.Entry<String, ArrayList<Adventurer>> entry : versionControl.entrySet()) {
            if (entry.getKey().equals(branchName)) {
                adventurers = entry.getValue();
            }
        }
        return adventurers;
    }
}
/*
21
1 2 Co20ocvblT
1 30 Al8QnWnkS7
1 91 pqWY5UNcm4
2 91 1 26 6DlfOJGzfY 74 96.3964
2 2 6 35 yv58Ec49pK 2 65.161 68.6988
2 2 1 71 FEw7siBqbW 64 66.534
2 91 2 44 OLy4CqtmrO 45 60.135 13.2503
2 30 1 56 H2EvYaqUXD 0 64.7676
2 91 6 65 Wjsn3jVy6E 60 20.1061 23.1743
2 2 2 28 0WnMAYPzUH 37 27.0554 10.4833
10 30 91
10 91 2
3 30 56
4 30
5 91
6 2
7 2 28
8 2
8 91
8 30
9 2
 */
package domain;

import java.math.BigInteger;
import java.util.ArrayList;
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
                case 10:
                    operation10();
                    break;
                case 11:
                    operation11();
                    break;
                case 12:
                    operation12();
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
        ArrayList<Bottle> bottles = new ArrayList<>();
        domain.Adventurer adventurer = new Adventurer(id, name, bottles);
        adventurers.add(adventurer);
    }

    public static void operation2() {
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        String name = sc.next();
        long price = sc.nextLong();
        double capacity = sc.nextDouble();
        //是否装满为true
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                Bottle bottle = new Bottle(botId, name, price, capacity, true);
                ArrayList<Bottle> bottles = item.getBottles();
                bottles.add(bottle);
                item.setBottles(bottles);
            }
        }
    }

    public static void operation3() {
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                ArrayList<Bottle> bottles = item.getBottles();
                for (int i = 0; i < bottles.size(); i++) {
                    if (bottles.get(i).getId() == botId) {
                        bottles.remove(bottles.get(i));
                    }
                }
            }
        }
    }

    public static void operation4() {
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        long price = sc.nextLong();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                ArrayList<Bottle> bottles = item.getBottles();
                for (Bottle items : bottles) {
                    if (items.getId() == botId) {
                        items.setPrice(price);
                    }
                }
            }
        }
    }

    public static void operation5() {
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        String filled = sc.next();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                ArrayList<Bottle> bottles = item.getBottles();
                for (Bottle items : bottles) {
                    if (items.getId() == botId) {
                        items.setFilled(filled.equals("true"));
                    }
                }
            }
        }
    }

    public static void operation6() {
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                ArrayList<Bottle> bottles = item.getBottles();
                for (Bottle items : bottles) {
                    if (items.getId() == botId) {
                        System.out.println(items.getName());
                    }
                }
            }
        }
    }

    public static void operation7() {
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                ArrayList<Bottle> bottles = item.getBottles();
                for (Bottle items : bottles) {
                    if (items.getId() == botId) {
                        System.out.println(items.getPrice());
                    }
                }
            }
        }
    }

    public static void operation8() {
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                ArrayList<Bottle> bottles = item.getBottles();
                for (Bottle items : bottles) {
                    if (items.getId() == botId) {
                        System.out.println(items.getCapacity());
                    }
                }
            }
        }
    }

    public static void operation9() {
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                ArrayList<Bottle> bottles = item.getBottles();
                for (Bottle items : bottles) {
                    if (items.getId() == botId) {
                        System.out.println(items.isFilled());
                    }
                }
            }
        }
    }

    public static void operation10() {
        int advId = sc.nextInt();
        int botId = sc.nextInt();
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                ArrayList<Bottle> bottles = item.getBottles();
                for (Bottle items : bottles) {
                    if (items.getId() == botId) {
                        System.out.println("The bottle's id is " + items.getId() + ", name is "
                                + items.getName() + ", capacity is " + items.getCapacity() +
                                ", filled is " + items.isFilled() + ".");
                    }
                }
            }
        }
    }

    public static void operation11() {
        int advId = sc.nextInt();
        BigInteger totalPrice = new BigInteger("0");
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                ArrayList<Bottle> bottles = item.getBottles();
                for (Bottle items : bottles) {
                    totalPrice = totalPrice.add(BigInteger.valueOf(items.getPrice()));
                }
                System.out.println(totalPrice);
            }
        }
    }

    public static void operation12() {
        int advId = sc.nextInt();
        long maxPrice = 0L;
        for (Adventurer item : adventurers) {
            if (item.getId() == advId) {
                ArrayList<Bottle> bottles = item.getBottles();
                for (Bottle items : bottles) {
                    if (items.getPrice() > maxPrice) {
                        maxPrice = items.getPrice();
                    }
                }
                System.out.println(maxPrice);
            }
        }
    }
}

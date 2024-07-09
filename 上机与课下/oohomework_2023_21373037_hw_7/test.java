public class test {
    public static void fun(int[] a) {
        a[0] = 100;
    }
    public static void main(String[] args) {
        int[] a = new int[1];
        fun(a);
        System.out.println(a[0]);
    }
}

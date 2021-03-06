package test;

import java.util.*;

public class HesitantFuzzyLinguisticTermSet {
    private static String[] linguisticTermSet = {"nothing", "very bad", "bad", "medium", "good", "very good", "perfect"};
    private static int atLeastIndex = getIndex("good", linguisticTermSet);
    private static int atMostIndex = getIndex("bad", linguisticTermSet);
    private static int leftBetween = getIndex("medium", linguisticTermSet);
    private static int rightBetween = getIndex("very good", linguisticTermSet);

    public static void main(String[] args) {
        //获取atLeast集合
        String[] atLeast = getAtLeast(atLeastIndex, linguisticTermSet);
        System.out.println("atLeast = " + Arrays.toString(atLeast));
        //获取atMost集合
        String[] atMost = getAtMost(atMostIndex, linguisticTermSet);
        System.out.println("atMost = " + Arrays.toString(atMost));
        //获取between集合
        String[] between = getBetween(leftBetween, rightBetween, linguisticTermSet);
        System.out.println("Between = " + Arrays.toString(between));
        System.out.println("===================================================================================");
        //获取atLeast的T集合
        List<Double> T_atLeast = atLeastGetT(atLeast);
        System.out.print("T_atLeast = ");
        printList(T_atLeast);
        //获取atMost的T集合
        List<Double> T_atMost = atMostGetT(atMost);
        System.out.print("T_atMost = ");
        printList(T_atMost);
        //获取between的T集合
        List<Double> T_between = betweenGetT(between);
        System.out.print("T_between = ");
        printList(T_between);
        System.out.println("===================================================================================");
        //获取四种情况的α
        double α_atLeast = (double) atLeastIndex / (double) (linguisticTermSet.length - 1);
        double α_atMost = (double) atMostIndex / (double) (linguisticTermSet.length - 1);
        double α1_between = (double) (linguisticTermSet.length - 1 - (rightBetween - leftBetween)) / (double) (linguisticTermSet.length - 2);
        double α2_between = (double) ((rightBetween - leftBetween) - 1) / (double) (linguisticTermSet.length - 2);
        System.out.println("α_atLeast = " + α_atLeast);
        System.out.println("α_atMost = " + α_atMost);
        System.out.println("α1_between = " + α1_between);
        System.out.println("α2_between = " + α2_between);
        System.out.println("===================================================================================");
        //计算atLeast的权重W2的方法，并通过OWA算子获取atLeast的b，然后获取atLeast的所有要求的值
        List<Double> W2_atLeast = getW2(T_atLeast, α_atLeast);
        System.out.print("W2_atLeast = ");
        printList(W2_atLeast);
        System.out.print("atLeast时模糊包络为：");
        double a1 = getMin_a(T_atLeast);
        System.out.printf("[a1 = %.2f, ", a1);
        double b1 = getOWA(T_atLeast, W2_atLeast);
        System.out.printf("b1 = %.2f, ", b1);
        double c1 = 1.0;
        System.out.printf("c1 = %.2f, ", c1);
        double d1 = getMax_d(T_atLeast);
        System.out.printf("d1 = %.2f]\n", d1);
        System.out.println("===================================================================================");
        //计算atMost的权重W1的方法，并通过OWA算子获取atMost的c，然后获取atMost的所有要求的值
        List<Double> W1_atMost = getW1(T_atMost, α_atMost);
        System.out.print("W1_atMost = ");
        printList(W1_atMost);
        System.out.print("atMost时模糊包络为：");
        double a2 = getMin_a(T_atMost);
        System.out.printf("[a2 = %.2f, ", a2);
        double b2 = 0.0;
        System.out.printf("b2 = %.2f, ", b2);
        double c2 = getOWA(T_atMost, W1_atMost);
        System.out.printf("c2 = %.2f, ", c2);
        double d2 = getMax_d(T_atMost);
        System.out.printf("d2 = %.2f]\n", d2);
        System.out.println("===================================================================================");
        //计算between时，获取四种权重并求得a,b,c,d的方法
        getValueOfBetween(T_between, α1_between, α2_between);
    }

    //根据传入的字符串，获取相应的索引的方法，因为后续的很多计算都是基于该字符串在数组中的索引
    public static int getIndex(String str, String[] linguisticTermSet) {
        for (int i = 0; i < linguisticTermSet.length; i++) {
            if (str == linguisticTermSet[i]) {
                return i;
            } else {
                continue;
            }
        }
        return 0;
    }

    //传入一个已知的beginIndex，获取atLeast的数组
    public static String[] getAtLeast(int beginIndex, String[] linguisticTermSet) {
        String[] atLeast = new String[linguisticTermSet.length - beginIndex];
        for (int i = 0, j = beginIndex; i < linguisticTermSet.length - beginIndex; i++, j++) {
            atLeast[i] = linguisticTermSet[j];
        }
        return atLeast;
    }

    //传入一个已知的endIndex，获取atMost数组
    public static String[] getAtMost(int endIndex, String[] linguisticTermSet) {
        String[] atMost = new String[endIndex + 1];
        for (int i = 0; i <= endIndex; i++) {
            atMost[i] = linguisticTermSet[i];
        }
        return atMost;
    }

    //传入一个已知的beginIndex和endIndex，获取between数组
    public static String[] getBetween(int beginIndex, int endIndex, String[] linguisticTermSet) {
        String[] between = new String[endIndex - beginIndex + 1];
        for (int i = 0, j = beginIndex; i <= endIndex - beginIndex; i++, j++) {
            between[i] = linguisticTermSet[j];
        }
        return between;
    }

    //atLeast获取T的方法
    public static List<Double> atLeastGetT(String[] atLeast) {
        List<Double> list = new ArrayList<>();
        if (leftBetween == 0) {
            list.add(0.0);
        } else {
            list.add((1 / (double) (linguisticTermSet.length - 1)) * (atLeastIndex - 1));
        }
        for (int i = 0; i <= linguisticTermSet.length - 1 - atLeastIndex; i++) {
            list.add((1 / (double) (linguisticTermSet.length - 1)) * (atLeastIndex + i));
        }
        list.add(1.0);  //右索引必为0，那么最后一个元素为1
        return list;
    }

    //atMost获取T的方法
    public static List<Double> atMostGetT(String[] atMost) {
        List<Double> list = new ArrayList<>();
        list.add(0.0);  //左索引必为0，那么第一个元素为0
        for (int i = 0; i <= atMostIndex - 0; i++) {  //按照between获取T的方法修改
            list.add((1 / (double) (linguisticTermSet.length - 1)) * (0 + i));
        }
        if (atMostIndex == linguisticTermSet.length - 1) {  //如果右索引为数组中最后一个元素的索引，那么list中最后一个元素必为0
            list.add(1.0);
        } else {  //添加Ar的通式
            list.add((1 / (double) (linguisticTermSet.length - 1)) * (atMostIndex + 1));
        }
        return list;
    }

    //between获取T的方法
    public static List<Double> betweenGetT(String[] between) {
        List<Double> list = new ArrayList<>();
        if (leftBetween == 0) {  //如果左索引为0，那么第一个元素必为0
            list.add(0.0);
        } else {  //如果左索引不为0，则按照以下公式添加，这也是添加Al的通式
            list.add((1 / (double) (linguisticTermSet.length - 1)) * (leftBetween - 1));
        }
        for (int i = 0; i <= rightBetween - leftBetween; i++) {  //添加Am的通式
            list.add((1 / (double) (linguisticTermSet.length - 1)) * (leftBetween + i));
        }
        if (rightBetween == linguisticTermSet.length - 1) {  //如果右索引为数组中最后一个元素的索引，那么list中最后一个元素必为0
            list.add(1.0);
        } else {  //添加Ar的通式
            list.add((1 / (double) (linguisticTermSet.length - 1)) * (rightBetween + 1));
        }
        return list;
    }

    //计算atMost的权重W1的方法
    public static List<Double> getW1(List<Double> T, double α) {
        // n = T.size() - 2 ，去头去尾
        List<Double> list = new ArrayList<>();
        list.add(α);
        for (int i = 2; i <= T.size() - 2 - 1; i++) {
            list.add(α * Math.pow((1 - α), i - 1));
        }
        list.add(Math.pow((1 - α), T.size() - 2 - 1));
        return list;
    }

    //计算atLeast的权重W2的方法
    public static List<Double> getW2(List<Double> T, double α) {
        // n = T.size() - 2 ，去头去尾
        List<Double> list = new ArrayList<>();
        list.add(Math.pow(α, T.size() - 2 - 1));
        for (int i = 2; i <= T.size() - 2 - 1; i++) {
            list.add((1 - α) * Math.pow(α, T.size() - 2 - i));
        }
        list.add(1 - α);
        return list;
    }

    //between情况下，求值的方法
    public static void getValueOfBetween(List<Double> T_between, double α1_between, double α2_between) {
        //获取a和d
        double a3 = getMin_a(T_between);
        double d3 = getMax_d(T_between);
        if ((leftBetween + rightBetween) % 2 == 0) {
            //在这里面计算对应的w1,w2,以及b,c
            List<Double> W1_betweenEven = getW1_betweenEven(α2_between);
            List<Double> W2_betweenEven = getW2_betweenEven(α1_between);
            System.out.print("W1_betweenEven = ");
            printList(W1_betweenEven);
            System.out.print("W2_betweenEven = ");
            printList(W2_betweenEven);

            //偶数时计算b
            double b3_even = getb_betweenEven(T_between, W2_betweenEven);
            //偶数时计算c
            double c3_even = getc_betweenEven(T_between, W1_betweenEven);

            System.out.print("between时模糊包络为：");
            System.out.printf("[a3 = %.2f, ", a3);
            System.out.printf("b3 = %.2f, ", b3_even);
            System.out.printf("c3 = %.2f, ", c3_even);
            System.out.printf("d3 = %.2f]\n", d3);
        }
        //计算between时，当leftBetween+rightBetween为奇数时
        if ((leftBetween + rightBetween) % 2 != 0) {
            double b3_odd;
            double c3_odd;
            if (leftBetween + 1 == rightBetween) {
                b3_odd = T_between.get(1);
                c3_odd = T_between.get(2);
            } else {
                //在这里面计算对应的w1,w2,以及b,c
                List<Double> W1_betweenOdd = getW1_betweenOdd(α2_between);
                List<Double> W2_betweenOdd = getW2_betweenOdd(α1_between);
                System.out.print("W1_betweenOdd = ");
                printList(W1_betweenOdd);
                System.out.print("W2_betweenOdd = ");
                printList(W2_betweenOdd);
                //奇数时计算b
                b3_odd = getb_betweenOdd(T_between, W2_betweenOdd);
                //奇数时计算c
                c3_odd = getc_betweenOdd(T_between, W1_betweenOdd);
            }
            System.out.print("between时模糊包络为：");
            System.out.printf("[a3 = %.2f, ", a3);
            System.out.printf("b3 = %.2f, ", b3_odd);
            System.out.printf("c3 = %.2f, ", c3_odd);
            System.out.printf("d3 = %.2f]\n", d3);
        }
    }

    //计算between时，当leftBetween+rightBetween为偶数时计算权重W1_betweenEven的方法
    public static List<Double> getW1_betweenEven(double α2) {
        // 总元素个数为 = (rightBetween - leftBetween + 2) / 2
        List<Double> list = new ArrayList<>();
        list.add(α2);
        //一共有(rightBetween - leftBetween + 2) / 2个元素，去头去尾，需要循环添加的剩下(rightBetween - leftBetween + 2) / 2 - 2个元素
        for (int i = (rightBetween - leftBetween + 2) / 2 - 2; i >= 1; i--) {
            list.add(α2 * Math.pow((1 - α2), (rightBetween - leftBetween - 2 * i) / 2));
        }
        list.add(Math.pow((1 - α2), (rightBetween - leftBetween) / 2));
        return list;
    }

    //计算between时，当leftBetween+rightBetween为偶数时计算权重W2_betweenEven的方法
    public static List<Double> getW2_betweenEven(double α1) {
        // 总元素个数为 = (rightBetween - leftBetween + 2) / 2
        List<Double> list = new ArrayList<>();
        list.add(Math.pow(α1, (rightBetween - leftBetween) / 2));
        //一共有(rightBetween - leftBetween + 2) / 2个元素，去头去尾，需要循环添加的剩下(rightBetween - leftBetween + 2) / 2 - 2个元素
        for (int i = 1; i <= (rightBetween - leftBetween + 2) / 2 - 2; i++) {
            list.add((1 - α1) * Math.pow(α1, (rightBetween - leftBetween - 2 * i) / 2));
        }
        list.add(1 - α1);
        return list;
    }

    //计算between时，当leftBetween+rightBetween为奇数时计算权重W1_betweenOdd的方法
    public static List<Double> getW1_betweenOdd(double α2) {
        // 总元素个数为 = (rightBetween - leftBetween + 1) / 2
        List<Double> list = new ArrayList<>();
        list.add(α2);
        //一共有(rightBetween - leftBetween + 1) / 2个元素，去头去尾，需要循环添加的剩下(rightBetween - leftBetween + 1) / 2 - 2个元素
        for (int i = (rightBetween - leftBetween + 1) / 2 - 2; i >= 1; i--) {
            list.add(α2 * Math.pow((1 - α2), (rightBetween - leftBetween - 2 * i - 1) / 2));
        }
        list.add(Math.pow((1 - α2), (rightBetween - leftBetween - 1) / 2));
        return list;
    }

    //计算between时，当leftBetween+rightBetween为偶数时计算权重W2_betweenEven的方法
    public static List<Double> getW2_betweenOdd(double α2) {
        // 总元素个数为 = (rightBetween - leftBetween + 2) / 2
        List<Double> list = new ArrayList<>();
        list.add(Math.pow(α2, (rightBetween - leftBetween - 1) / 2));
        //一共有(rightBetween - leftBetween + 2) / 2个元素，去头去尾，需要循环添加的剩下(rightBetween - leftBetween + 2) / 2 - 2个元素
        for (int i = 1; i <= (rightBetween - leftBetween + 2) / 2 - 2; i++) {
            list.add((1 - α2) * Math.pow(α2, (rightBetween - leftBetween - 2 * i - 1) / 2));
        }
        list.add(1 - α2);
        return list;
    }

    //计算between时，当leftBetween+rightBetween为偶数时计算b的方法
    public static double getb_betweenEven(List<Double> T_between, List<Double> W2_betweenEven) {
        //由于T_between的第一个元素和最后一个元素是用不上的，所以这里要注意-2的作用
        //这里的思路是：顺序遍历Index从1开始，倒序遍历Index从T_between.size() - 2是没问题
        //关键在于可以移动多少个步长，注意到(rightBetween + leftBetween) / 2 - leftBetween = rightBetween - (rightBetween + leftBetween) / 2
        //就是可以移动的步长，所以退出循环的条件：
        //顺序时 1 + (rightBetween + leftBetween) / 2 - leftBetween
        //逆序时 T_between.size() - 2 - (rightBetween + leftBetween) / 2 - leftBetween
        List<Double> tempList = new ArrayList<>();
        for (int i = 1 + (rightBetween - leftBetween) / 2; i >= 1; i--) {
            tempList.add(T_between.get(i));
        }
        double OWA = 0;
        for (int i = 0; i < W2_betweenEven.size(); i++) {
            OWA += W2_betweenEven.get(i) * tempList.get(i);
        }
        return OWA;
    }

    //计算between时，当leftBetween+rightBetween为偶数时计算c的方法
    public static double getc_betweenEven(List<Double> T_between, List<Double> W1_betweenEven) {
        List<Double> tempList = new ArrayList<>();
        for (int i = T_between.size() - 2; i >= T_between.size() - 2 - (rightBetween - leftBetween) / 2; i--) {
            tempList.add(T_between.get(i));
        }
        double OWA = 0;
        for (int i = 0; i < W1_betweenEven.size(); i++) {
            OWA += W1_betweenEven.get(i) * tempList.get(i);
        }
        return OWA;
    }

    //计算between时，当leftBetween+rightBetween为奇数时计算b的方法
    public static double getb_betweenOdd(List<Double> T_between, List<Double> W2_betweenOdd) {
        List<Double> tempList = new ArrayList<>();
        for (int i = 1 + (rightBetween - leftBetween - 1) / 2; i >= 1; i--) {
            tempList.add(T_between.get(i));
        }
        double OWA = 0;
        for (int i = 0; i < W2_betweenOdd.size(); i++) {
            OWA += W2_betweenOdd.get(i) * tempList.get(i);
        }
        return OWA;
    }

    //计算between时，当leftBetween+rightBetween为奇数时计算c的方法
    public static double getc_betweenOdd(List<Double> T_between, List<Double> W1_betweenOdd) {
        List<Double> tempList = new ArrayList<>();
        for (int i = i = T_between.size() - 2; i >= T_between.size() - 2 - (rightBetween - leftBetween - 1) / 2; i--) {
            tempList.add(T_between.get(i));
        }
        double OWA = 0;
        for (int i = 0; i < W1_betweenOdd.size(); i++) {
            OWA += W1_betweenOdd.get(i) * tempList.get(i);
        }
        return OWA;
    }

    //计算atLeast和atMost时，通过OWA算子计算b或c的方法
    public static double getOWA(List<Double> T, List<Double> W) {
        List<Double> tempList = new ArrayList<>();
        for (int i = T.size() - 2; i >= 1; i--) {
            tempList.add(T.get(i));
        }
        double OWA = 0;
        for (int i = 0; i < W.size(); i++) {
            OWA += W.get(i) * tempList.get(i);
        }
        return OWA;
    }

    //获取最小值的方法
    public static double getMin_a(List<Double> list) {
        Collections.sort(list);
        return list.get(0);
    }

    //获取最大值的方法
    public static double getMax_d(List<Double> list) {
        Collections.sort(list);
        return list.get(list.size() - 1);
    }

    //格式化打印ArrayList的方法
    public static void printList(List<Double> list) {
        System.out.print("[");
        for (int i = 0; i < list.size() - 1; i++) {
            System.out.printf("%.2f, ", list.get(i));
        }
        System.out.printf("%.2f", list.get(list.size() - 1));
        System.out.println("]");
    }
}

package org.pp.util;

import org.apache.commons.io.FileUtils;
import org.pp.filter.DefaultBuildConditionFilter;
import org.pp.model.RemainderByThreeEnum;
import org.pp.model.RowData;
import org.pp.spider.FetchUtil;
import practice.util.DataUtils;

import javax.swing.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 19-3-16
 * Time: 下午6:57
 * To change this template use File | Settings | File Templates.
 */
public class NumberUtil {

    public static final DecimalFormat DF2 = new DecimalFormat("00");
    private static final DecimalFormat DF = new DecimalFormat("000000");
    private static final TreeSet<Integer> numberSet = new TreeSet<>();
    /**
     * 尾数表
     */
    private static final TreeMap<Integer, ArrayList<Integer>> tailNumMap = new TreeMap<Integer, ArrayList<Integer>>();
    /**
     * 尾数对应表
     */
    private static final TreeMap<Integer, Integer> tailNumPairMap = new TreeMap<Integer, Integer>();
    private static final int[] arr1 = new int[]{1, 2, 3, 4, 5};
    private static final int[] arr2 = new int[]{6, 7, 8, 9, 10};
    private static final int[] arr3 = new int[]{11, 12, 13, 14, 15};
    private static final int[] arr4 = new int[]{16, 17, 18, 19, 20};
    private static final int[] arr5 = new int[]{21, 22, 23, 24, 25};
    private static final int[] arr6 = new int[]{26, 27, 28, 29, 30};
    private static final int[] arr7 = new int[]{31, 32, 33, 34, 35};
    private static String classPath = "";
    private static Set<Integer> jiaoNumberSet = new HashSet<>();
    private static Set<Integer> weiNumberSet = new HashSet<>();

    static {
//        String jarFilePath = NumberUtil.class.getProtectionDomain().getCodeSource().getLocation().toString();
//        // URL Decoding
//        try {
//            classPath = java.net.URLDecoder.decode(jarFilePath, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        classPath = ClassLoader.getSystemClassLoader().getResource("").getFile();
        for (int i = 1; i <= 35; i++) {
            numberSet.add(i);
            // 首位差 出现尾数
            Integer x = i % 10;
            if (tailNumMap.get(x) == null) {
                ArrayList<Integer> set = new ArrayList<Integer>();
                set.add(i);
                tailNumMap.put(x, set);
            } else {
                tailNumMap.get(x).add(i);
            }
        }

        // 尾数对应表
        tailNumPairMap.put(0, 5);
        tailNumPairMap.put(1, 6);
        tailNumPairMap.put(2, 7);
        tailNumPairMap.put(3, 8);
        tailNumPairMap.put(4, 9);
        tailNumPairMap.put(5, 0);
        tailNumPairMap.put(6, 1);
        tailNumPairMap.put(7, 2);
        tailNumPairMap.put(8, 3);
        tailNumPairMap.put(9, 4);

        int[] jiaoArray = new int[]{1, 6, 8, 11, 15, 16, 21, 22, 26, 29, 31};
        jiaoNumberSet = intArrayToTreeSet(jiaoArray);
        int[] weiArray = new int[]{1, 2, 3, 4, 5, 6, 7, 12, 13, 18, 19, 24, 25, 30, 31, 32, 33, 34, 35};
        weiNumberSet = intArrayToTreeSet(weiArray);
    }

    public static TreeMap<Integer, ArrayList<Integer>> getTailNumMap() {
        return tailNumMap;
    }

    public static TreeMap<Integer, Integer> getTailNumPairMap() {
        return tailNumPairMap;
    }

    public static boolean isBig(int num) {
        return (num >= 18 && num <= 35);
    }

    /**
     * 奇偶性判断
     *
     * @param a
     * @return 奇数为true
     */
    public static boolean isOdd(int a) {
        if (a <= 0) {
            throw new IllegalArgumentException("参数必须为正整数。");
        }
        if ((a & 1) != 1) {
            return false;
        }
        return true;
    }

    /**
     * <pre>
     * 用于判断一个数是否为素数，若为素数，返回true,否则返回false
     * 质数是除了1和它本身之外，不能被其他数整除的正整数，又称素数。
     * 质数和合数的区别在于因数的个数，质数只有2个因数，合数有多于2个因数。
     * 除1,0以外不是质数的正整数就是合数。
     * "0"“1”既不是质数也不是合数。
     * </pre>
     *
     * @param a 输入的值
     * @return true、false
     */
    public static boolean isPrime(int a) {
        boolean flag = true;
        if (a < 2) { // 指定1为质数
            return true;
        } else {
            for (int i = 2; i <= Math.sqrt(a); i++) {
                if (a % i == 0) { // 若能被整除，则说明不是素数，返回false
                    flag = false;
                    break;// 跳出循环
                }
            }
        }
        return flag;
    }


    public static int getTail(int num) {
        return num % 10;
    }

    /******************************************************************************************************************/

    /**
     * 求和值
     *
     * @param numbers
     * @return
     */
    public static int sum(int... numbers) {
        int sum = 0;
        for (int i : numbers) {
            sum += i;
        }
//        System.out.println("和值：" + sum);
        return sum;
    }

    /**
     * 计算ac值 值的大小代表随机性
     *
     * @param numbers
     */
    public static int ac(int... numbers) {
        Arrays.sort(numbers);
//        System.out.println("号码：" + Arrays.toString(numbers));
        int len = numbers.length;
        int maxAC = len * (len - 1) / 2 - (len - 1);
        List<Integer> resultList = new ArrayList<Integer>();
        int repeatCount = 0;
        for (int i = 0; i < len; i++) {
            int tmp1 = numbers[i];
            for (int j = i + 1; j < len; j++) {
                int tmp2 = numbers[j];
                int result = tmp2 - tmp1;
                resultList.add(result);
//                System.out.println(tmp2 + "-" + tmp1 + "=" + result);
            }
        }
        Collections.sort(resultList);
//        System.out.println("差值列表：" + resultList);
        int resultListLen = resultList.size();
        for (int i = 0; i < resultListLen; i++) {
            int num = resultList.get(i);
            for (int j = i + 1; j < resultListLen; j++) {
                if (resultList.get(j) == num) {
                    repeatCount++; // 重复差值加1
//                    System.out.println("重复差值：" + num);
                }
            }
        }
        int ac = maxAC - repeatCount;
//        System.out.println("AC最大值：" + maxAC);
//        System.out.println("AC值：" + ac);
        return ac;
    }

    public static List<Integer> getShouWeiChaNumList(int[] numbers) {
        if (numbers.length != 5) {
            throw new IllegalArgumentException("前区号码个数必须为5");
        }
        int kuaDu = (numbers[4] - numbers[0]) % 10;
        int kuaDuPair = getTailNumPairMap().get(kuaDu);
        List<Integer> set1 = getTailNumMap().get(kuaDu);
        List<Integer> set2 = getTailNumMap().get(kuaDuPair);
        List<Integer> set0 = new ArrayList<>(8);
        set0.addAll(set1);
        set0.addAll(set2);
        return set0;
    }

    /**
     * 大号数
     *
     * @param originFrontNumber
     * @return
     */
    public static int getBigNumberCount(int... originFrontNumber) {
        int count = 0;
        for (int i = 0; i < originFrontNumber.length; i++) {
            if (isBig(originFrontNumber[i])) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取偶数个数
     *
     * @return
     */
    public static int getOddCount(int... originFrontNumber) {
        int count = 0;
        for (int i = 0; i < originFrontNumber.length; i++) {
            if (isOdd(originFrontNumber[i])) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取素数的个数
     *
     * @return
     */
    public static int getPrimerCount(int... originFrontNumber) {
        int count = 0;
        for (int i = 0; i < originFrontNumber.length; i++) {
            if (isPrime(originFrontNumber[i])) {
                count++;
            }
        }
        return count;
    }

    /**
     * 012路比
     *
     * @param originFrontNumber
     * @return
     */
    public static String getRemainderByThreeCount(int... originFrontNumber) {
        int x = 0, y = 0, z = 0;
        for (int i = 0; i < originFrontNumber.length; i++) {
            int t = originFrontNumber[i] % 3;
            if (t == 0) {
                x++;
            } else if (t == 1) {
                y++;
            } else {
                z++;
            }
        }
        StringBuilder str0 = new StringBuilder("");
        str0.append(x).append(":").append(y).append(":").append(z);
        return str0.toString();
    }

    /**
     * 本期的三区比
     *
     * @param numbers
     * @return
     */
    public static String areaRatioByThree(int... numbers) {
        int x = 0;
        int y = 0;
        int z = 0;
        for (int i : numbers) {
            // 三分区
            if (i <= 12) {
                x++;
            }
            if (i > 12 && i <= 24) {
                y++;
            }
            if (i > 24) {
                z++;
            }

        }
        StringBuilder str0 = new StringBuilder("");
        str0.append(x).append(":").append(y).append(":").append(z);
        return str0.toString();
    }

    public static String getBigNumRatio(int... numbers) {
        int bigNumber = getBigNumberCount(numbers);
        return bigNumber + ":" + (5 - bigNumber);
    }

    public static String getOddNumRatio(int... numbers) {
        int oddCount = getOddCount(numbers);
        return oddCount + ":" + (5 - oddCount);
    }

    public static String getPrimerRatio(int... numbers) {
        int primerCount = getPrimerCount(numbers);
        return primerCount + ":" + (5 - primerCount);
    }

    /**
     * **************************************************************************************************************
     */
    public static int[] stringArrToIntArray(String[] arrs) {
        int[] ints = new int[arrs.length];
        for (int i = 0; i < arrs.length; i++) {
            ints[i] = Integer.parseInt(arrs[i]);
        }
        return ints;
    }

    public static TreeSet<Integer> stringArrToTreeSet(String[] arrs) {
        TreeSet<Integer> set = new TreeSet<Integer>();
        for (int i = 0; i < arrs.length; i++) {
            set.add(DataUtils.asInt(arrs[i]));
        }
        return set;
    }

    public static String treeSetToString(TreeSet<Integer> set) {
        StringBuilder sb = new StringBuilder("");
        for (Integer num : set) {
            sb.append(DF2.format(num)).append(",");
        }

        return sb.substring(0, sb.length() - 1).toString();
    }

    public static String IntArrayToString(int[] numArray) {
        StringBuilder sb = new StringBuilder("");
        for (int num : numArray) {
            sb.append(DF2.format(num)).append(",");
        }
        return sb.toString();
    }

    public static String IntArrayToString2(int[] numArray) {
        StringBuilder sb = new StringBuilder("");
        for (int num : numArray) {
            sb.append(DF2.format(num)).append(" ");
        }
        return sb.substring(0, sb.length() - 1).toString();
    }

    public static TreeSet<Integer> intArrayToTreeSet(int[] arrs) {
        TreeSet<Integer> set = new TreeSet<Integer>();
        for (int i = 0; i < arrs.length; i++) {
            set.add(arrs[i]);
        }
        return set;
    }

    public static void writeFile(List<int[]> filterResult, String fileName) {
        try {
            File file = new File(classPath + fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int[] nums : filterResult) {
                String content = IntArrayToString(nums).replaceAll("\\[| |]", "").replaceAll(",", " ");
                bw.write(content);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile2(List<RowData> filterResult, String fileName) {
        try {
            File file = new File(classPath + fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            int len = filterResult.size();
            for (int i = 0; i < len; i++) {
                RowData nums = filterResult.get(i);
                String content = nums.toString().replaceAll("\\[| |]", "").replaceAll(",", " ");
//                bw.write(DF.format(i + 1) + "  ");
                bw.write(content);
                bw.newLine();
            }
            bw.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendFile(List<int[]> filterResult, String fileName) {
        try {
            File file = new File(classPath + fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file, true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int[] nums : filterResult) {
                String content = IntArrayToString(nums).replaceAll("\\[| |]", "").replaceAll(",", " ");
                bw.write(content);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入文件
     *
     * @param fileName
     * @param filterResult
     */
    public static void writeFile(String fileName, Object... filterResult) {
        try {
            File file = new File(classPath + fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (Object nums : filterResult) {
                String content = nums.toString();
                bw.write(content);
                bw.newLine();
            }
            bw.flush();
            bw.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * list转换成int数组
     *
     * @param list
     * @return
     */
    public static int[] collectionToIntArray(Collection<Integer> list) {
        int len = list.size();
        int[] arr = new int[len];
        Iterator<Integer> iterator = list.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Integer num = iterator.next();
            arr[index++] = num;
        }
        return arr;
    }

    /**
     * 判断一个数的尾数大小
     *
     * @param i 正整数
     * @return true 就是大
     */
    public static boolean bigTail(int i) {
        return (i % 10 >= 5);
    }

    /**
     * 求两个数的平均值
     *
     * @param a
     * @param b
     * @return
     */
    public static int avg(int a, int b) {
        return ((a & b) + ((a ^ b) >> 1));

    }

    public static Map<Integer, Integer> statisticalNumber(String fileName) {
        Map<Integer, Integer> numMap = new TreeMap<Integer, Integer>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = null;
            //按行读，并把每次读取的结果保存在line字符串中
            while ((line = reader.readLine()) != null) {
                if (line.matches("\\s*") || line.startsWith("#")) {
                    continue;
                }
                String[] arr = line.split(" ");
                for (String i : arr) {
                    Integer num = DataUtils.asInt(i);
                    if (!numMap.containsKey(num)) {
                        numMap.put(num, 1);
                    } else {
                        numMap.put(num, (numMap.get(num) + 1));
                    }
                }

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("出现异常");
        }
        return numMap;
    }

    public static int getAreaByThree(int num) {
        if (num >= 1 && num <= 12) {
            return 1;
        } else if (num >= 13 && num <= 24) {
            return 2;
        } else/* (num >= 25 && num <= 35) */ {
            return 3;
        }
    }

    public static int getRemainderByThree(int num) {
        int index = num % 3;
        if (index == 0) {
            return 0;
        } else if (index == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    public static int loadNumber(JTextPane textPane, String fileName) {
        File file = null;
        file = new File(classPath + fileName);
        FileReader fileReader = null;
        int result = 0;
        try {
            fileReader = new FileReader(file);
            char[] allChar = new char[(int) file.length()];
            int hasRead = 0;
            while ((hasRead = fileReader.read(allChar)) > 0) {
            }

            result = allChar.length;
            textPane.replaceSelection(new String(allChar, 0, result));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static int[] kuaDuDianWei(int num) {
        if (Arrays.binarySearch(arr1, num) >= 0) {
            return arr1;
        } else if (Arrays.binarySearch(arr2, num) >= 0) {
            return arr2;
        } else if (Arrays.binarySearch(arr3, num) >= 0) {
            return arr3;
        } else if (Arrays.binarySearch(arr4, num) >= 0) {
            return arr4;
        } else if (Arrays.binarySearch(arr5, num) >= 0) {
            return arr5;
        } else if (Arrays.binarySearch(arr6, num) >= 0) {
            return arr6;
        } else {
            return arr7;
        }
    }

    public static void doUpdateFile() {
        String fileName = ConfigUtil.getLoadNumber();
        File file = null;
        file = new File(classPath + fileName);
        byte[] bytes = FileUtil.readFile(file);
        String userDir = System.getProperty("user.dir");
        String writeFilePath = userDir + File.separator + "resource" + File.separator + fileName;
        FileUtil.writeFile(bytes, writeFilePath);
    }

    public static void fillEditorPane(JEditorPane panel, String fileName) {
        FileReader fileReader = null;
        try {
            File file = new File(classPath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileReader = new FileReader(file);
            panel.read(fileReader, "");
            panel.scrollRectToVisible(panel.getVisibleRect());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String format(int num) {
        return DF2.format(num);
    }

    public static int[] getLatestNum() {
        return getNumberBack(1);
    }

    public static int[] getSecondLatestNum() {
        return getNumberBack(2);
    }

    public static int[] getNumberBack(int index) {
        ArrayList<int[]> arrayList = DefaultBuildConditionFilter.getInstance().getArrayList();
        int length = arrayList.size();
        int[] latestLine = arrayList.get(length - index);
        return latestLine;
    }

    public static Set<Integer> getNumberSet() {
        return Collections.unmodifiableSet(numberSet);
    }

    public static Set<Integer> getJiaoNumberSet() {
        return Collections.unmodifiableSet(jiaoNumberSet);
    }

    public static Set<Integer> getWeiNumberSet() {
        return Collections.unmodifiableSet(weiNumberSet);
    }

    /**
     * 获取本期前区号码的尾所对应的号码
     *
     * @return
     */
    public static List<Integer> getLatestTailNumber() {
        int[] originFrontNumber = getLatestNum();
        int[] frontNumber = new int[5];
        System.arraycopy(originFrontNumber, 1, frontNumber, 0, 5);
        Set<Integer> set = new HashSet<Integer>(25);
        for (int num : frontNumber) {
            set.add(NumberUtil.getTail(num));
        }
        List<Integer> numbers = new ArrayList<Integer>();
        for (Integer i : set) {
            numbers.addAll(NumberUtil.getTailNumMap().get(i));
        }
        return numbers;
    }

    /**
     * 获取本期前区号码的尾所对应的号码
     *
     * @return
     */
    public static Set<Integer> getLatestLinHaoNumber() {
        int[] originFrontNumber = getLatestNum();
        int[] frontNumber = new int[5];
        System.arraycopy(originFrontNumber, 1, frontNumber, 0, 5);
        Set<Integer> xlNum = new TreeSet<>();
        for (int i = 0; i < frontNumber.length; i++) {
            int num = frontNumber[i];
            if (num - 1 >= 1) {
                xlNum.add(num - 1);
            }
            if (num + 1 <= 35) {
                xlNum.add(num + 1);
            }
        }
        return xlNum;
    }

    public static final String getClassPath() {
        if (classPath.endsWith(".jar")) {
//            classPath = classPath + "!/";
        }
        return classPath;
    }

    /********************************************数字属性****************************************************/
    /**
     * 除三余数
     *
     * @param num
     * @return 除三余数
     */
    public static RemainderByThreeEnum getRemainderByThreeEnum(int num) {
        int index = num % 3;
        if (index == 0) {
            return RemainderByThreeEnum.ZERO;
        } else if (index == 1) {
            return RemainderByThreeEnum.ONE;
        } else {
            return RemainderByThreeEnum.TWO;
        }
    }
}

package com.hand.demo.security;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * @author guanghui.liu
 */
public class PasswordManager {

    private String salt;
    private String defaultPassword = "111111";

    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * 加盐MD5处理.
     */
    public String encode(String rawPass) {
        Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
        return passwordEncoder.encodePassword(rawPass, salt);
    }

    /**
     * 调试用main函数-实际产生的对象还是autowire自动产生的.
     * 
     * @param args
     */
    // public static void main(String[] args) {
    // PasswordManager pm = new PasswordManager();
    // pm.setSalt("hoshi");
    // System.out.println(pm.encode("111111"));
    //
    // String s1 = "Programming";
    // String s2 = new String("Programming");
    // String s3 = "Program";
    // String s4 = "ming";
    // String s5 = "Program" + "ming";
    // String s6 = s3 + s4;
    // System.out.println(s1 == s2);
    // System.out.println(s1 == s5);
    // System.out.println(s1 == s6);
    // System.out.println(s1 == s6.intern());
    // System.out.println(s2 == s2.intern());

    /**
     * 给定一个整数数组a，该数组已经排好序（非降序排序），再给定一个整数target，写程序提供一个方法，
     * 找出target在a中出现的位置（数组下标）k，并返回k；如果在数组a中没有target出现，
     * 则必然有一个位置n可以插入，插入之后依然保持数组有序，找出这个位置n并返回-1*(n+1)。
     */
    public static void main(String[] args) {
        /*
         * Integer[] array = new Integer[] { 1, 2, 4, 4, 6, 7, 8 }; int num = 0;
         * int index = binarySearch(array, num); if (index >= 0) {
         * System.out.println("已找到,index=" + index); } else { int insertIndex =
         * -index - 1; System.out.println("未找到,将在如下位置插入:index=" + insertIndex);
         * // 插入 List<Integer> list = new ArrayList<Integer>(array.length);
         * Collections.addAll(list, array); List<Integer> left = list.subList(0,
         * insertIndex); left.add(num); left.addAll(list.subList(insertIndex +
         * 1, list.size())); System.out.println("插入后的结果:" + left.toString()); }
         */
        System.out.println(FNVHash1("liuguanghui"));

    }

    // 二分查找
    public static int binarySearch(Integer[] array, int num) {
        int low = 0;
        int upper = array.length - 1;
        while (low <= upper) {
            int mid = (upper + low) / 2;
            if (array[mid] < num) {
                low = mid + 1;
            } else if (array[mid] > num) {
                // upper = ((mid % 2 == 0) ? (mid - 2) : (mid - 1)) - 1;
                upper = mid - 1;
            } else
                return mid;
        }
        return -(upper + 1 + 1);
    }

    /**
     * 加法hash
     * 
     * @param key
     *            字符串
     * @param prime
     *            一个质数
     * @return hash结果
     */
    public static int additiveHash(String key, int prime) {
        int hash, i;
        for (hash = key.length(), i = 0; i < key.length(); i++)
            hash += key.charAt(i);
        return (hash % prime);
    }

    /**
     * 改进的32位FNV算法1
     * 
     * @param data
     *            字符串
     * @return int值
     */
    public static int FNVHash1(String data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < data.length(); i++)
            hash = (hash ^ data.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }
}

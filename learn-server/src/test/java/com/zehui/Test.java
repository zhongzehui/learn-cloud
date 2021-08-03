package com.zehui;

public class Test {
    public static void main(String[] args) {
        System.out.println(Long.MAX_VALUE);
        /*String a22 = new String("222");
        System.out.println(a22.hashCode());
        System.out.println("20000".hashC/*ode());
        System.out.println(Integer.toBinaryString(47653682>>>16));

        System.out.println(getHash(a22 ));*/
    }

    static final int getHash(Object key) {
        if (key == null){
            return 0;
        }else{
            int  h = key.hashCode() ;
            System.out.println(" key 的hashcode"+ h);
            int result = h  ^ (h >>> 16);
            System.out.println("返回的结果是：" + result);
            return result;
        }

    }
}

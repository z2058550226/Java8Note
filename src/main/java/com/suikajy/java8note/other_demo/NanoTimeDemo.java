package com.suikajy.java8note.other_demo;

/**
 * System.nanoTime()这个函数会返回一个系统时间，但这个时间是没有什么意义的，它的单位是纳秒
 * 这个函数的用处在于统计计算机执行速度。两次System.nanoTime()可以得出很精准的运行时间差。
 */
public class NanoTimeDemo {
    
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        createStringBuilder();
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println(estimatedTime);  //1s=1000毫秒=1000000微秒=1000000000纳秒
    }

    /**
     * 测试StringBuilder执行一万次增加和清除字符串所花时间
     */
    private static void createStringBuilder() {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= 100000; i++){
            sb.append("Hello World");
            sb.delete(0, sb.length());
        }

    }
    
}

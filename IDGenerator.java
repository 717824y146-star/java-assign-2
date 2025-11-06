package util;

import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {
    private static final AtomicInteger c = new AtomicInteger(1);
    public static String next(String prefix){
        return prefix + c.getAndIncrement();
    }
}
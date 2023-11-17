package com.emily.infrastructure.test.test;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * @author Emily
 * @since Created in 2022/5/31 11:21 上午
 */
public class CafeineTest {
    private static final Cache<String, Boolean> CACHE = Caffeine.newBuilder()
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) {
        for (int i = 0; i < 10001; i++) {
            CACHE.put(i + "", Boolean.TRUE);
        }
        System.out.println(CACHE.getIfPresent(1 + ""));
        System.out.println(CACHE.getIfPresent(1000 + ""));
        System.out.println(CACHE.getIfPresent(10000 + ""));
    }
}

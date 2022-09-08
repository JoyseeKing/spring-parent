package com.emily.infrastructure.test.controller;

import com.emily.infrastructure.common.utils.json.JSONUtils;
import com.emily.infrastructure.core.helper.SystemNumberHelper;
import com.emily.infrastructure.redis.factory.RedisDbFactory;
import com.emily.infrastructure.redis.helper.RedisDbHelper;
import com.google.common.collect.Maps;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @program: spring-parent
 * @description: 缓存测试
 * @author: Emily
 * @create: 2021/07/14
 */
@RestController
@RequestMapping("api/redis")
public class RedisController {
    @GetMapping("info/{section}")
    public Properties getInfo(@PathVariable("section") String section) {
        Properties properties = RedisDbFactory.getStringRedisTemplate().getConnectionFactory().getConnection().info(section);
        return properties;
    }

    @GetMapping("get1")
    public String get1() {

        RedisDbFactory.getStringRedisTemplate().opsForValue().set("test", "123", 12, TimeUnit.MINUTES);
        RedisDbFactory.getStringRedisTemplate().opsForValue().set("test66", "123", 12);
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("te", 12);
        dataMap.put("te2", 12);
        dataMap.put("te3", "哈哈");
        RedisDbFactory.getRedisTemplate().opsForValue().set("test1", dataMap, 1, TimeUnit.MINUTES);
        //RedisDbFactory.getRedisTemplate("one").opsForValue().set("one", "adf", 1, TimeUnit.MINUTES);
        return RedisDbFactory.getStringRedisTemplate("default").opsForValue().get("test");
    }

    @GetMapping("get2")
    public Object get2() {
        RedisDbFactory.getRedisTemplate("test").opsForValue().set("test", "123", 12, TimeUnit.MINUTES);
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("te", 12);
        dataMap.put("te2", 12);
        dataMap.put("te3", "年好吗");
        RedisDbFactory.getRedisTemplate("test").opsForValue().set("test1", dataMap, 1, TimeUnit.MINUTES);
        //RedisDbFactory.getRedisTemplate("one").opsForValue().set("one", dataMap, 1, TimeUnit.MINUTES);
        return RedisDbFactory.getRedisTemplate("test").opsForValue().get("test");
    }

    @GetMapping("pool")
    public String pool() {
        List<RedisClientInfo> list = RedisDbFactory.getRedisTemplate().getClientList();
        System.out.println(list.size());
        list.stream().forEach(redisClientInfo -> {
            System.out.println(JSONUtils.toJSONPrettyString(redisClientInfo));
        });
        return "success";
    }

    @GetMapping("get")
    public String indicator() {
        return RedisDbFactory.getStringRedisTemplate().opsForValue().get("test");
    }

    @GetMapping("roll")
    public void roll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                 /*   try {
                        Thread.sleep(100);
                    } catch (Exception e) {

                    }*/
                    long start = System.currentTimeMillis();
                    RedisDbFactory.getStringRedisTemplate("test").opsForValue().set("roll_test", "123:" + System.currentTimeMillis());
                    long time = System.currentTimeMillis() - start;
                    System.out.println("--------roll----------" + time);
                }
            }
        }).start();
    }

    @GetMapping("hash")
    public void hash(@RequestParam("code") String code) {
        String key = RedisDbHelper.getKey(SystemNumberHelper.getSystemNumber(), code);
        StringRedisTemplate stringRedisTemplate = RedisDbFactory.getStringRedisTemplate();
        stringRedisTemplate.opsForHash().put(key, "accountCode" + code, code);
    }

    @GetMapping("lock")
    public Boolean lock(@RequestParam("code") String code) {
        String key = RedisDbHelper.getKey(SystemNumberHelper.getSystemNumber(), "123");
        StringRedisTemplate stringRedisTemplate = RedisDbFactory.getStringRedisTemplate();
        return stringRedisTemplate.opsForValue().setIfAbsent(key, code, 10, TimeUnit.SECONDS);
    }

    @GetMapping("setBit")
    public Long setBit(@RequestParam("accountCode") Long accountCode, @RequestParam("date") String date, @RequestParam("offset") Long offset) {
        String key = RedisDbHelper.getKey(SystemNumberHelper.getSystemNumber(), "bloom", date, accountCode + "");
        StringRedisTemplate stringRedisTemplate = RedisDbFactory.getStringRedisTemplate();
        Boolean flag = stringRedisTemplate.opsForValue().setBit(key, offset, true);
        System.out.println(flag);
        return stringRedisTemplate.execute((RedisCallback<Long>) connection -> connection.bitCount(key.getBytes()));
    }

    @GetMapping("getBit")
    public Boolean getBit(@RequestParam("accountCode") Long accountCode, @RequestParam("date") String date, @RequestParam("offset") Long offset) {
        String key = RedisDbHelper.getKey(SystemNumberHelper.getSystemNumber(), "bloom", date, accountCode + "");
        StringRedisTemplate stringRedisTemplate = RedisDbFactory.getStringRedisTemplate();
        return stringRedisTemplate.execute((RedisCallback<Boolean>) connection -> connection.getBit(key.getBytes(), offset));
    }

    @GetMapping("batch")
    public void batchBit(@RequestParam("accountCode") Long accountCode) {
        String key = RedisDbHelper.getKey(SystemNumberHelper.getSystemNumber(), "bloom", accountCode + "");
        StringRedisTemplate stringRedisTemplate = RedisDbFactory.getStringRedisTemplate();
        for (int i = 0; i < 10000; i++) {
           // stringRedisTemplate.opsForValue().bitField(key, new BitFieldSubCommands(sd));
        }
    }

    public static void main(String[] args) {
        String voiceCerfUrl = null;
        String mobileNo = null;
        String verCode = null;
        String url = String.format("%s/AEPOBService/ob?phone=%s&verifyCode=%s", voiceCerfUrl, mobileNo, verCode);
        System.out.println(url);
    }
}

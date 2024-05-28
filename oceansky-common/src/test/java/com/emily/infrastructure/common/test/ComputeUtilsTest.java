package com.emily.infrastructure.common.test;

import com.emily.infrastructure.common.ComputeUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;

/**
 * 计算工具类单元测试
 *
 * @author :  Emily
 * @since :  2024/4/30 9:11 PM
 */
public class ComputeUtilsTest {
    @Test
    public void getEffectiveValue() {
        Assertions.assertNull(ComputeUtils.getEffectiveValue(null));
        Assertions.assertEquals(ComputeUtils.getEffectiveValue(""), "");
        Assertions.assertEquals(ComputeUtils.getEffectiveValue(" "), " ");
        Assertions.assertEquals(ComputeUtils.getEffectiveValue("3.14159265777777"), "3.14159265777777");
        Assertions.assertEquals(ComputeUtils.getEffectiveValue("3.141590000000"), "3.14159");
        Assertions.assertEquals(ComputeUtils.getEffectiveValue("3.0000"), "3");
    }

    @Test
    public void round() {
        String s = ComputeUtils.round("", 2, RoundingMode.HALF_UP, "0.00");
        System.out.println(s);
        Assertions.assertEquals(ComputeUtils.round(null, 2, RoundingMode.HALF_UP, "0.00"), "0.00");
        Assertions.assertEquals(ComputeUtils.round("", 2, RoundingMode.HALF_UP, "0.00"), "0.00");
        Assertions.assertEquals(ComputeUtils.round("", 2, RoundingMode.HALF_UP, null), "");
        Assertions.assertEquals(ComputeUtils.round(" ", 2, RoundingMode.HALF_UP, "0.00"), "0.00");
        Assertions.assertEquals(ComputeUtils.round("3.230000", 2, RoundingMode.HALF_UP, "0.00"), "3.23");
        Assertions.assertEquals(ComputeUtils.round("3.230000", 4, RoundingMode.HALF_UP, "0.00"), "3.2300");
        Assertions.assertEquals(ComputeUtils.round(" 3.230000 ", 4, RoundingMode.HALF_UP, "0.00"), "3.2300");
        Assertions.assertEquals(ComputeUtils.round(" 3.230000 ", 4), "3.2300");
        Assertions.assertEquals(ComputeUtils.round(" 3.230000 ", 4, null), "3.2300");
        Assertions.assertEquals(ComputeUtils.round("", 4, "0.00"), "0.00");
    }

    @Test
    public void toPercentage() {
        Assertions.assertEquals(ComputeUtils.toPercentage("", 2, "0.1"), "0.1");
        Assertions.assertEquals(ComputeUtils.toPercentage(null, 3, "0.1"), "0.1");
        Assertions.assertEquals(ComputeUtils.toPercentage("1.23", 3, "0.1"), "123.000%");
        Assertions.assertEquals(ComputeUtils.toPercentage("1.23", 0, "0.1"), "123%");
    }
}

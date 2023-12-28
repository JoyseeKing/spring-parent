package com.emily.infrastructure.test.plugin.valid;

import com.emily.infrastructure.autoconfigure.valid.annotation.*;
import com.emily.infrastructure.date.DatePatternInfo;

/**
 * @author :  Emily
 * @since :  2023/12/24 1:41 PM
 */
public class ValidReq {
    @IsLocalTime(message = "日期格式不正确1", pattern = DatePatternInfo.HH_MM_SS, required = false)
    private String name;
    @IsInclude(includeString = {"1", "2", "3", ""}, message = "年龄不正确", required = false)
    private String age;
    @IsInclude(includeInt = {4, 5, 6, 2147483647}, message = "高度不正确")
    private int height;
    @IsInclude(includeLong = {1, 2147483648L}, message = "id不正确")
    private long id;
    @IsInclude(includeDouble = {1.0, 2.0}, message = "价格不正确")
    private double price;
    @IsDouble(message = "交易价格不正确", required = false)
    private String tradePrice;
    @IsInt(message = "购买数量不正确", required = false)
    private String buyAmount;
    @IsLong(message = "总金额不正确", required = false)
    private String totalAmount;
    @IsBigDecimal(message = "金额不正确", required = false)
    private String bigDecimal;
    @IsSuffix(suffixes = {"21", "22"}, message = "用户名不正确", required = false)
    private String username;
    @IsPrefix(prefixes = {"10", "20"}, message = "账号不正确", required = false)
    private String accountCode;

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(String bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(String buyAmount) {
        this.buyAmount = buyAmount;
    }

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

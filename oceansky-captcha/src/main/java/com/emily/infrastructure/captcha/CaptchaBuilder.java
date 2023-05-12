package com.emily.infrastructure.captcha;

/**
 * @Description :  图形验证码建造器
 * @Author :  姚明洋
 * @CreateDate :  Created in 2023/5/4 10:18 AM
 */
public class CaptchaBuilder {
    /**
     * 验证码
     */
    private String code;
    /**
     * 验证码图片
     */
    private byte[] image;

    public CaptchaBuilder code(String code) {
        this.code = code;
        return this;
    }

    public CaptchaBuilder image(byte[] image) {
        this.image = image;
        return this;
    }

    public Captcha build() {
        return new Captcha(this.code, this.image);
    }
}

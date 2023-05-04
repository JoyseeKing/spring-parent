package com.emily.infrastructure.common.captcha;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @author Emily
 * @Description 图形验证码
 * @See Also: 获取字体名称 String[] names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
 * @See Also: 获取字体对象 Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
 */
public class CaptchaUtils {
    /**
     * 数字
     */
    private static final String[] DIGIT = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    /**
     * 字母
     */
    private static final String[] LETTER = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    /**
     * 字母数字
     */
    private static final String[] ALPHANUMERIC = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    /**
     * 图片格式
     */
    public static final String FORMAT_NAME = "JPEG";

    /**
     * @param width    画板宽度
     * @param height   画板高度
     * @param count    验证码个数
     * @param fontSize 字体大小
     * @return
     * @Description: 绘制数字图形验证码，不带干扰线
     */
    public static Captcha createDigit(int width, int height, int count, int fontSize) throws IOException {
        return createDigit(width, height, count, fontSize, false, null);
    }

    /**
     * @param width     画板宽度
     * @param height    画板高度
     * @param count     验证码个数
     * @param fontSize  字体大小
     * @param line      是否画干扰线
     * @param lineCount 干扰线数量
     * @return
     * @Description: 绘制数字图形验证码
     */
    public static Captcha createDigit(int width, int height, int count, int fontSize, boolean line, Integer lineCount) throws IOException {
        /**
         * 字体名又分为两大类：中文字体名：宋体，楷体，黑体等；
         * 　　　　　　　　　　英文字体名：Arial,Times New Roman等；
         */
        Font font = new Font("Times New Roman", Font.ITALIC, fontSize);
        // 绘制的字符串
        String[] code = getDrawCode(CaptchaType.DIGIT, count);
        return create2D(width, height, code, font, line, lineCount);
    }

    /**
     * @param width    画板宽度
     * @param height   画板高度
     * @param count    验证码个数
     * @param fontSize 字体大小
     * @return
     * @throws IOException
     * @Description: 绘制字母图形验证码
     */
    public static Captcha createLetter(int width, int height, int count, int fontSize) throws IOException {
        return createLetter(width, height, count, fontSize, false, null);
    }

    /**
     * @param width     画板宽度
     * @param height    画板高度
     * @param count     验证码个数
     * @param fontSize  字体大小
     * @param line      是否画干扰线
     * @param lineCount 干扰线数量
     * @return
     * @throws IOException
     * @Description: 绘制字母图形验证码
     */
    public static Captcha createLetter(int width, int height, int count, int fontSize, boolean line, Integer lineCount) throws IOException {
        /**
         * 字体名又分为两大类：中文字体名：宋体，楷体，黑体等；
         * 　　　　　　　　　　英文字体名：Arial,Times New Roman等；
         */
        Font font = new Font("Times New Roman", Font.ITALIC, fontSize);
        // 绘制的字符串
        String[] code = getDrawCode(CaptchaType.LETTER, count);
        return create2D(width, height, code, font, line, lineCount);
    }

    /**
     * @param width    画板宽度
     * @param height   画板高度
     * @param count    验证码个数
     * @param fontSize 字体大小
     * @return
     * @throws IOException
     * @Description: 绘制数字字母图形验证码
     */
    public static Captcha createAlphanumeric(int width, int height, int count, int fontSize) throws IOException {
        return createAlphanumeric(width, height, count, fontSize, false, null);
    }

    /**
     * @param width     画板宽度
     * @param height    画板高度
     * @param count     验证码个数
     * @param fontSize  字体大小
     * @param line      是否画干扰线
     * @param lineCount 干扰线数量
     * @return
     * @throws IOException
     * @Description: 绘制数字字母图形验证码
     */
    public static Captcha createAlphanumeric(int width, int height, int count, int fontSize, boolean line, Integer lineCount) throws IOException {
        /**
         * 字体名又分为两大类：中文字体名：宋体，楷体，黑体等；
         * 　　　　　　　　　　英文字体名：Arial,Times New Roman等；
         */
        Font font = new Font("Times New Roman", Font.ITALIC, fontSize);
        // 绘制的字符串
        String[] code = getDrawCode(CaptchaType.ALPHANUMERIC, count);
        return create2D(width, height, code, font, line, lineCount);
    }

    /**
     * @param width     画板宽度
     * @param height    画板高度
     * @param code      验证码
     * @param font      字体
     * @param line      是否绘制干扰线
     * @param lineCount 干扰线条数
     * @return 生成的图形验证码对象
     * @throws IOException
     * @Description: 生成图形验证码
     */
    public static Captcha create2D(int width, int height, String[] code, Font font, boolean line, Integer lineCount) throws IOException {
        // 创建空白图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图片画笔
        Graphics graphic = image.getGraphics();
        // 绘制矩形背景(实心矩形)
        graphic.fillRect(0, 0, width, height);
        /**
         * Font.PLAIN（普通）
         * Font.BOLD（加粗）
         * Font.ITALIC（斜体）
         * Font.BOLD+ Font.ITALIC（粗斜体）
         */
        graphic.setFont(font);
        // 画图形验证码
        drawCode(graphic, width, height, code);
        // 画干扰线
        if (line) {
            drawLine(graphic, width, height, lineCount);
        }
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        ImageIO.write(image, FORMAT_NAME, bas);
        return new CaptchaBuilder().code(StringUtils.join(code)).image(bas.toByteArray()).build();

    }

    /**
     * @param graphic 画板对象
     * @param width   画板宽度
     * @param height  画板高度
     * @param code    图形验证码字符串
     * @Description 画图形验证码
     */
    protected static void drawCode(Graphics graphic, int width, int height, String[] code) {
        // 计算文字长度，计算居中的x点坐标，即字符串左边位置
        FontMetrics fm = graphic.getFontMetrics(graphic.getFont());
        // 字符串宽度
        int textWidth = fm.stringWidth(StringUtils.join(code));
        // 字符串左侧x坐标
        int x = (width - textWidth) / 2;
        /**
         *
         *  文本高度=lead+ascent+descent
         ***************************************************************
         *  lead 字体前导|行间距
         ***************************************************************
         *
         *
         *  ascent 字体升序
         *
         *
         **************************************************************** baseline
         *
         *  descent 字体降序
         *
         ****************************************************************
         *
         * 上图就是四线格，文本占用的有效高度就是ascent+descent
         * 居中计算规则就是：(画板的高度height-文本的有效高度（ascent+descent）)/2获得文本非占用空间的一半，由于绘制字符串是以基线为准，所以再加上ascent后绘制字符串以基线为准在一条线上并且居中
         *
         */
        int y = (height - (fm.getAscent() + fm.getDescent())) / 2 + fm.getAscent();
        for (int i = 0; i < code.length; i++) {
            // 设置随机颜色
            graphic.setColor(new Color(RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255)));
            // 绘制字符串
            graphic.drawString(code[i], x + (textWidth / code.length) * i, y);
        }
    }

    /**
     * 获取绘制的验证码
     *
     * @param type  字符串类型
     * @param count 字符个数
     * @return 字符串数组
     */
    protected static String[] getDrawCode(CaptchaType type, int count) {
        String[] code = new String[count];
        Random ran = new Random();
        for (int i = 0; i < count; i++) {
            switch (type) {
                case DIGIT:
                    code[i] = DIGIT[ran.nextInt(DIGIT.length)];
                    break;
                case LETTER:
                    code[i] = LETTER[ran.nextInt(LETTER.length)];
                    break;
                default:
                    code[i] = ALPHANUMERIC[ran.nextInt(ALPHANUMERIC.length)];
                    break;

            }
        }
        return code;
    }

    /**
     * @param graphic   画板对象
     * @param width     画板宽度
     * @param height    画板高度
     * @param lineCount 干扰线条数
     * @Description 绘制干扰线
     */
    protected static void drawLine(Graphics graphic, int width, int height, Integer lineCount) {
        // 画干扰线
        for (int i = 0; i < lineCount; i++) {
            // 设置随机颜色
            graphic.setColor(new Color(RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255)));
            // 随机画线
            graphic.drawLine(RandomUtils.nextInt(0, width), RandomUtils.nextInt(0, height), RandomUtils.nextInt(0, width), RandomUtils.nextInt(0, height));
        }
    }
}

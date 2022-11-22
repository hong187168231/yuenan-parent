package com.indo.common.utils.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 国际化工具类
 */
@Component
public class MessageUtils {

    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    private static final Logger logger = LoggerFactory.getLogger(MessageUtils.class);
    /**
     * 获取单个国际化翻译值
     */
    public static String get(String msgKey,String countryCode) {
        try {
            String lang = "";
            String country = "";
            switch (countryCode) {
                case "IN":
                    lang = "en";
                    country = "IN";
                    break;
                case "EN":
                    lang = "en";
                    country = "US";
                    break;
                case "CN":
                    lang = "zh";
                    country = "CN";
                    break;
                case "VN":
                    lang = "vi";
                    country = "VN";
                    break;
                case "TW":
                    lang = "zh";
                    country = "TW";
                    break;
                case "TH":
                    lang = "th";
                    country = "TH";
                    break;
                case "ID":
                    lang = "in";
                    country = "ID";
                    break;
                case "MY":
                    lang = "ms";
                    country = "MY";
                    break;
                case "KR":
                    lang = "ko";
                    country = "KR";
                    break;
                case "JP":
                    lang = "ja";
                    country = "JP";
                    break;
                default:
                    lang = "en";
                    country = "US";
                    break;
            }
            Locale locale = new Locale(lang,country);
            logger.info("获取单个国际化翻译值msgKey:{},countryCode：{},lang：{},country：{},locale：{}", msgKey, countryCode,lang,country,locale);
            ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
            //return new String(bundle.getString(msgKey).getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
            return bundle.getString(msgKey);
        } catch (Exception e) {
            e.printStackTrace();
            return msgKey;
        }
    }
}

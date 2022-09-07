package com.indo.common.utils.i18n;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

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
                case "EN":
                    lang = "en";
                    country = "US";
                case "CN":
                    lang = "zh";
                    country = "CN";
                case "VN":
                    lang = "vi";
                    country = "VN";
                case "TW":
                    lang = "zh";
                    country = "TW";
                case "TH":
                    lang = "th";
                    country = "TH";
                case "ID":
                    lang = "in";
                    country = "ID";
                case "MY":
                    lang = "ms";
                    country = "MY";
                    countryCode = "ms_MY";
                case "KR":
                    lang = "ko";
                    country = "KR";
                case "JP":
                    lang = "ja";
                    country = "JP";
                default:
                    lang = "en";
                    country = "US";
            }
            Locale locale = new Locale(lang,country);
            ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
            return bundle.getString(msgKey);
        } catch (Exception e) {
            e.printStackTrace();
            return msgKey;
        }
    }
}

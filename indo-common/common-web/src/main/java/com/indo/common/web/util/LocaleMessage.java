package com.indo.common.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 国际化messageSource
 *
 * @author choimroc
 * @since 2019/9/29
 */
@Component
public class LocaleMessage {

    private static MessageSource messageSource;

    @Autowired
    public LocaleMessage(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public  static String getV(String code) {
        return getMessage(code, new Object[]{});
    }

    public  static String getMessage(String code) {
        return getMessage(code, new Object[]{});
    }

    public static String getMessage(String code, String defaultMessage) {
        return getMessage(code, new Object[]{}, defaultMessage);
    }

    public static String getMessage(String code, String defaultMessage, Locale locale) {
        return getMessage(code, new Object[]{}, defaultMessage, locale);
    }

    public static  String getMessage(String code, Locale locale) {
        return getMessage(code, new Object[]{}, "", locale);
    }

    public static String getMessage(String code, Object[] args) {
        return getMessage(code, args, "");
    }

    public static String getMessage(String code, Object[] args, Locale locale) {
        return getMessage(code, args, "", locale);
    }

    public static String getMessage(String code, Object[] args, String defaultMessage) {
        Locale locale = LocaleContextHolder.getLocale();
        return getMessage(code, args, defaultMessage, locale);
    }

    public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

}

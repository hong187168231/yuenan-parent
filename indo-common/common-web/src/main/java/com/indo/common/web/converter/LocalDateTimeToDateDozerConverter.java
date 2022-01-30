//package com.indo.common.web.converter;
//import com.github.dozermapper.core.DozerConverter;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Date;
//
//public class LocalDateTimeToDateDozerConverter extends DozerConverter<LocalDateTime, Date> {
//
//
//    public LocalDateTimeToDateDozerConverter() {
//        super(LocalDateTime.class, Date.class);
//    }
//
//    @Override
//    public Date convertTo(LocalDateTime source, Date destination) {
//        Date convertToDate = Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
//        return convertToDate;
//    }
//
//    @Override
//    public LocalDateTime convertFrom(Date source, LocalDateTime destination) {
//        LocalDateTime dateTime = LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
//        return dateTime;
//    }
//}

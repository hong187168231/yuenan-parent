package com.live.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 复制相关的工具类
 * @Date 2019/10/28
 **/
@Slf4j
public class CopyUtils {

    /**
     * 拷贝相同的属性
     * @param source
     * @param dest
     * @throws Exception
     */
    public static void conver(Object source, Object dest) throws Exception {
        // 获取属性
        BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(),Object.class);
        PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();

        BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(),Object.class);
        PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();

        try {
            for (int i = 0; i < sourceProperty.length; i++) {

                for (int j = 0; j < destProperty.length; j++) {

                    if (sourceProperty[i].getName().equals(destProperty[j].getName())  && sourceProperty[i].getPropertyType() == destProperty[j].getPropertyType()) {
                        // 调用source的getter方法和dest的setter方法
                        destProperty[j].getWriteMethod().invoke(dest,sourceProperty[i].getReadMethod().invoke(source));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("复制属性失败",e);
        }
    }


    /**
     * 拷贝相同的属性
     * @param source
     * @param dest
     * @throws Exception
     */
    public static void converNotNull(Object source, Object dest) throws Exception {
        // 获取属性
        BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(),Object.class);
        PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();

        BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(),Object.class);
        PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();

        try {
            for (int i = 0; i < sourceProperty.length; i++) {

                for (int j = 0; j < destProperty.length; j++) {

                    if (sourceProperty[i].getName().equals(destProperty[j].getName())  && sourceProperty[i].getPropertyType() == destProperty[j].getPropertyType()) {
                        // 调用source的getter方法和dest的setter方法
                        if(sourceProperty[i].getReadMethod().invoke(source) != null) {
                            destProperty[j].getWriteMethod().invoke(dest, sourceProperty[i].getReadMethod().invoke(source));
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("复制属性失败",e);
        }
    }

    /**
     * 拷贝相同的属性
     * @param source
     * @param dest
     * @throws Exception
     */
    public static void converNotNull(Object source, Object dest,String... excludeArgs) throws Exception {
        // 获取属性
        BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(),Object.class);
        PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();

        BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(),Object.class);
        PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();
        List<String> list= Arrays.asList(excludeArgs);
        try {
            for (int i = 0; i < sourceProperty.length; i++) {

                for (int j = 0; j < destProperty.length; j++) {
                    if(list.contains(sourceProperty[i].getName())) {
                        log.info("修改属性失败，禁止修改{}",sourceProperty[i].getName());
                        break;
                    }else{
                        if (sourceProperty[i].getName().equals(destProperty[j].getName()) && sourceProperty[i].getPropertyType() == destProperty[j].getPropertyType()) {
                            // 调用source的getter方法和dest的setter方法
                            if (sourceProperty[i].getReadMethod().invoke(source) != null) {
                                destProperty[j].getWriteMethod().invoke(dest, sourceProperty[i].getReadMethod().invoke(source));
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("复制属性失败",e);
        }
    }

}

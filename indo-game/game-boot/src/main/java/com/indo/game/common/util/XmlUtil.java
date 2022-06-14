package com.indo.game.common.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlUtil {
    private static final Logger logger = LoggerFactory.getLogger(new XmlUtil().getClass());
    /**
     * xml 转 对象
     * @param clazz 输出对象
     * @param xmlStr xml
     * @return object
     */
    public static Object convertXmlStrToObject(Class clazz, String xmlStr) {
        logger.info("convertXmlStrToObject输入 {} ", xmlStr);
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            // 进行将Xml转成对象的核心接口
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(xmlStr);
            xmlObject = unmarshaller.unmarshal(sr);
            logger.info("convertXmlStrToObject输出 {} ", JSONObject.toJSONString(xmlObject));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlObject;
    }

    /**
     * JavaBean转换成xml
     *
     * @param obj obj
     * @param encoding encoding
     * @return String
     */
    public static Object convertToXml(Object obj, String encoding, boolean format) {
        StringWriter writer = null;
        try {
            logger.info("convertToXml输入 {} ", JSONObject.toJSONString(obj));
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
//            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT,true);
            writer = new StringWriter();
            writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            marshaller.marshal(obj, writer);
            logger.info("convertToXml输出 {} ", writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return writer;
    }

//    public static void main(String[] args) {
//        SaCallbackResp saLoginResp = new SaCallbackResp();
//        saLoginResp.setError(0);
//        saLoginResp.setUsername("aaa");
//        saLoginResp.setAmount(BigDecimal.valueOf(1.111));
//        saLoginResp.setCurrency("CNY");
//        System.out.println(XmlUtil.convertToXml(saLoginResp, "UTF-8", false));
//    }
}

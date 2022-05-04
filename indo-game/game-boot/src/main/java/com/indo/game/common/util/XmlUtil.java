package com.indo.game.common.util;

import com.indo.game.pojo.dto.sa.SaCallbackResp;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

public class XmlUtil {
    /**
     * xml 转 对象
     * @param clazz 输出对象
     * @param xmlStr xml
     * @return object
     */
    public static Object convertXmlStrToObject(Class clazz, String xmlStr) {
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            // 进行将Xml转成对象的核心接口
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(xmlStr);
            xmlObject = unmarshaller.unmarshal(sr);
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
    public static String convertToXml(Object obj, String encoding, boolean format) {
        String result = null;
        StringWriter writer = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
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
        return result;
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

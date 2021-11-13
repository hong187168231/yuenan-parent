package com.indo.game.utils;

import com.indo.game.game.RedisBaseUtil;
import com.indo.game.common.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


/**
 * AG FTP文件下载工具
 */
public class AGFTPUtil {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private FTPClient ftpClient = new FTPClient();

    /**
     * 根据文件名称获取文件流
     *
     * @return
     * @throws IOException
     */
    public void initFtpClient(String userName, String passWord, String ip, int port, String defDir, boolean isBefore) {
        try {
            connectFTPServer(userName, passWord, ip, port, defDir);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                closeConnect();
                throw new IOException("failed connect to the FTP Server:" + ip);
            }
            String dateDir = getDateDir();
            if (isBefore) {
                dateDir = getFormatDateBefore(-1);
            }
            ftpClient.changeWorkingDirectory(defDir + dateDir);
        } catch (FTPConnectionClosedException e) {
            log.error("ftp连接被关闭！", e);
        } catch (Exception e) {
            log.error("download file from ftp failed!", e);
        }
    }

    /**
     * 连接ftp服务器
     */
    private void connectFTPServer(String userName, String passWord, String ip, int port, String defDir) throws FTPConnectionClosedException, Exception {
        if (!ftpClient.isConnected()) {
            int reply;
            try {
                ftpClient = new FTPClient();
                ftpClient.connect(ip, port);
                ftpClient.login(userName, passWord);
                reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                }
            } catch (FTPConnectionClosedException e) {
                log.error("服务器IP：" + ip + "没有连接数！", e);
                throw e;
            } catch (Exception e) {
                log.error("连接ftp服务器" + ip + "失败", e);
                throw e;
            }
        }
    }

    /**
     * 关闭连接
     */
    public void closeConnect() {
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            log.error("ftp连接关闭失败！", e);
        }
    }

    /**
     * 根据当前时间生成文件目录
     *
     * @return String
     */
    public String getDateDir() {
        return getGMTTime("yyyyMMdd");
    }

    /**
     * 解析AG XML属性
     *
     * @param xmlTag
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public NamedNodeMap getXmlAttr(String xmlTag)
            throws SAXException, IOException, ParserConfigurationException {
        StringReader sr = new StringReader(xmlTag);
        InputSource is = new InputSource(sr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);
        NamedNodeMap resultMap = doc.getElementsByTagName("row").item(0).getAttributes();
        // getNamedItem("info").getTextContent()
        return resultMap;
    }

    /**
     * 获取文件内容的总行数。
     *
     * @param isr
     * @return
     * @throws IOException
     */
    public int getTotalLines(InputStreamReader isr) throws IOException {
        LineNumberReader reader = new LineNumberReader(isr);
        String content = reader.readLine();
        int lines = 0;
        while (content != null) {
            lines++;
            content = reader.readLine();
        }
        reader.close();
        isr.close();
        return lines;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    //public List<String> lineNumberReader(String userName, String passWord, String ip, int port, String defDir, RedisTemplate redisTemplate, boolean isBefore) {
    public List<String> lineNumberReader(String userName, String passWord, String ip, int port, String defDir, boolean isBefore) {
        InputStream streamLine = null;
        InputStream streamData = null;
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        InputStreamReader readerLine = null;
        InputStreamReader readerData = null;
        LineNumberReader LineNumberReader = null;
        List<String> list = new ArrayList<String>();
        try {
            log.info("agftp begin defDir {}", defDir);
            initFtpClient(userName, passWord, ip, port, defDir, isBefore);
            // ftpClient.setDefaultTimeout(60);
            // ftp文件获取文件
            for (String fileName : ftpClient.listNames()) {
                // 处理 YYYYMMDD0000.xml 状况，会在前一天的文件夹当中，只读取0000.xml文件
                log.info("agftp begin fileName {}", fileName);
                if (isBefore && fileName.indexOf("0000") < 0) {
                    continue;
                }
                int startNumber = 0;
                //String startNumberStr = String.valueOf(redisTemplate.opsForValue().get(Constants.AG_CAGENT_VALUE + fileName));
                String startNumberStr = null;
                if (RedisBaseUtil.hasKey(Constants.AG_CAGENT_VALUE + fileName)) {
                    Object o = RedisBaseUtil.get(Constants.AG_CAGENT_VALUE + fileName);
                    if (null != o) {
                        startNumberStr = String.valueOf(o);
                        log.info("====startNumberStr===={}", startNumberStr);
                    }
                    if (StringUtils.isNotBlank(startNumberStr) && !"null".equals(startNumberStr) && !"NULL".equals(startNumberStr)) {
                        startNumber = Integer.parseInt(startNumberStr);
                    }
                }
                inputStream = ftpClient.retrieveFileStream(fileName);
                if (inputStream != null) {
                    baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) > -1) {
                        baos.write(buffer, 0, len);
                    }
                    baos.flush();
                    // 获取行号
                    streamLine = new ByteArrayInputStream(baos.toByteArray());
                    // 读取
                    streamData = new ByteArrayInputStream(baos.toByteArray());
                    readerLine = new InputStreamReader(streamLine);
                    readerData = new InputStreamReader(streamData);
                    LineNumberReader = new LineNumberReader(readerData);
                    int lines = getTotalLines(readerLine);
                    // redisTemplate.opsForValue().set(Constants.AG_CAGENT_VALUE + fileName, lines);
                    // 隔天失效
                    String keyName = Constants.AG_CAGENT_VALUE + fileName;
                    if (defDir.equals(Constants.AG_FTP_LAST_DIR)) {
                        keyName = keyName + Constants.AG_FTP_LAST_DIR;
                    }
                    //redisTemplate.opsForValue().set(keyName, lines, 1450, TimeUnit.MINUTES);
                    RedisBaseUtil.set(keyName, lines, 1450, TimeUnit.MINUTES);
                    if (startNumber < 0 || startNumber > lines) {
                        inputStream.close();
                        ftpClient.completePendingCommand();
                        continue;
                    }
                    String line = LineNumberReader.readLine();
                    lines = 0;
                    while (line != null) {
                        lines++;
                        if (startNumber <= lines) {
                            list.add(line);
                            log.info("list line line {} ", line);
                        }
                        line = LineNumberReader.readLine();
                    }
                }
                log.info("agftp inputStream fileName {}", fileName);
                if (inputStream == null) {
                    break;
                } else {
                    inputStream.close();
                    ftpClient.completePendingCommand();
                }
                log.info("agftp end fileName {}", fileName);
            }
        } catch (Exception e) {
            log.error("拉取ag--xml文件出现异常", e);
        } finally {
            try {
                closeConnect();

                if (LineNumberReader != null) {
                    LineNumberReader.close();
                }
                if (streamLine != null) {
                    streamLine.close();
                }
                if (streamData != null) {
                    streamData.close();
                }
                if (readerData != null) {
                    readerData.close();
                }
                if (readerLine != null) {
                    readerLine.close();
                }
                if (baos != null) {
                    baos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("startupOrStopXsnotice occur error.", e.getMessage());
            }
        }
        return list;
    }

    public static String getGMTTime(String format) {
        return getFormatedDateString(-4, format);
    }

    public static String getFormatedDateString(float timeZoneOffset, String format) {
        if (timeZoneOffset > 13 || timeZoneOffset < -12) {
            timeZoneOffset = 0;
        }
        int newTime = (int) (timeZoneOffset * 60 * 60 * 1000);
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(timeZone);
        return sdf.format(new Date());
    }

    public String getFormatDateBefore(int date) throws ParseException {
        String today = getDateDir();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        calendar.setTime(simpleDateFormat.parse(today));
        calendar.add(Calendar.DATE, date);
        return simpleDateFormat.format(calendar.getTime());
    }
}

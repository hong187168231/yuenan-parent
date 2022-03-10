package com.indo.game.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class RandomGUID extends Object {
    protected final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(getClass());

    public String valueBefore = "";
    public String valueAfter = "";
    private static Random myRand;
    private static SecureRandom mySecureRand;

    private static String s_id;
    private static final int PAD_BELOW = 0x10;
    private static final int TWO_BYTES = 0xFF;

    /*
     * Static block to take care of one time secureRandom seed.
     * It takes a few seconds to initialize SecureRandom.  You might
     * want to consider removing this static block or replacing
     * it with a "time since first loaded" seed to reduce this time.
     * This block will run only once per JVM instance.
     */

    static {
        mySecureRand = new SecureRandom();
        long secureInitializer = mySecureRand.nextLong();
        myRand = new Random(secureInitializer);
        try {
            s_id = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }


    /*
     * Default constructor.  With no specification of security option,
     * this constructor defaults to lower security, high performance.
     */
    public RandomGUID() {
        getRandomGUID(false);
    }

    /*
     * Constructor with security option.  Setting secure true
     * enables each random number generated to be cryptographically
     * strong.  Secure false defaults to the standard Random function seeded
     * with a single cryptographically strong random number.
     */
    public RandomGUID(boolean secure) {
        getRandomGUID(secure);
    }

    /*
     * Method to generate the random GUID
     */
    private void getRandomGUID(boolean secure) {
        MessageDigest md5 = null;
        StringBuffer sbvalueBefore = new StringBuffer(128);

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error: " + e);
        }

        try {
            long time = System.currentTimeMillis();
            long rand = 0;

            if (secure) {
                rand = mySecureRand.nextLong();
            } else {
                rand = myRand.nextLong();
            }
            sbvalueBefore.append(s_id);
            sbvalueBefore.append(":");
            sbvalueBefore.append(Long.toString(time));
            sbvalueBefore.append(":");
            sbvalueBefore.append(Long.toString(rand));

            valueBefore = sbvalueBefore.toString();
            md5.update(valueBefore.getBytes());

            byte[] array = md5.digest();
            StringBuffer sb = new StringBuffer(32);
            for (int j = 0; j < array.length; ++j) {
                int b = array[j] & TWO_BYTES;
                if (b < PAD_BELOW)
                    sb.append('0');
                sb.append(Integer.toHexString(b));
            }

            valueAfter = sb.toString();

        } catch (Exception e) {
            logger.error("Error:" + e);
        }
    }

    /*
     * Convert to the standard format for GUID
     * (Useful for SQL Server UniqueIdentifiers, etc.)
     * Example: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
     */
    public String toString() {
        String raw = valueAfter.toUpperCase();
        StringBuffer sb = new StringBuffer(64);
        sb.append(raw.substring(0, 8));
        sb.append("-");
        sb.append(raw.substring(8, 12));
        sb.append("-");
        sb.append(raw.substring(12, 16));
        sb.append("-");
        sb.append(raw.substring(16, 20));
        sb.append("-");
        sb.append(raw.substring(20));

        return sb.toString();
    }



}

package com.tcg.api.core.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.tcg.api.core.exception.ConnectionException;

public class HttpUtils {

private static final String DEFAULT_ENCODING = "UTF-8";
	
	private transient StringEntity          entity;
    private final transient HttpRequestBase request;

    private HttpUtils(final HttpRequestBase request, final StringEntity entity) {
        this.request = request;
        this.entity  = entity;
    }
    
    private HttpUtils(final HttpRequestBase request) {
    	this.request = request;
    }
    
    public static HttpUtils newPost(final String url, final String entity) throws UnsupportedEncodingException {
        final HttpRequestBase base     = new HttpPost(url);
        final HttpUtils template = new HttpUtils(base, new StringEntity(entity, "application/x-www-form-urlencoded; charset=UTF-8", DEFAULT_ENCODING));

        return template;
    }
    
    public static HttpUtils newGet(final String url) {
    	final HttpRequestBase base = new HttpGet(url);
    	final HttpUtils template = new HttpUtils(base);
    	return template;
    }
    
    public String execute() {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
		HttpConnectionParams.setSoTimeout(httpParams, 15000);	
		
        final HttpClient client     = new DefaultHttpClient(httpParams);
        final HttpClient httpclient = UntrustedHTTPsWrapper.wrap(client);

        if ((entity != null) && (request instanceof HttpEntityEnclosingRequestBase)) {
            ((HttpEntityEnclosingRequestBase) request).setEntity(entity);
        }
        HttpResponse response;

        try {
            response = httpclient.execute(request);
        } catch (ClientProtocolException ex) {
            throw new ConnectionException("IO error!", ex);
        } catch (IOException ex) {
            throw new ConnectionException("client protocol error!", ex);
        }

        final int status = response.getStatusLine().getStatusCode();
		if (status != HttpStatus.SC_OK) {
			throw new RuntimeException(
					"Http fail, status=" + status + ", reason=" + response.getStatusLine().getReasonPhrase(), null);
		}

        try {
            final HttpEntity  result = response.getEntity();
            final InputStream iscont = result.getContent();
            final byte[]      bscont = IOUtils.toByteArray(iscont);
            // Assume UTF-8, seems OK
            final String      str    = new String(bscont, "UTF-8");
            if (str == null || str.isEmpty()) {
            	throw new RuntimeException("Server returns HTTP 200 with empty string.", null);
            }
            return str;
        } catch (IOException ex) {
            throw new ConnectionException("get content error!", ex);
        } catch (IllegalStateException ex) {
            throw new RuntimeException("http invalid state!", ex);
        }
    }

    private static class UntrustedHTTPsWrapper {
        @SuppressWarnings("deprecation")
		public static HttpClient wrap(HttpClient base) {
            try {
                SSLContext       ctx = SSLContext.getInstance("TLS");
                X509TrustManager tm  = new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
                    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[] {};
                    }
                };

                ctx.init(null, new TrustManager[] { tm }, null);

                SSLSocketFactory ssf = new SSLSocketFactory(ctx);

                ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                ClientConnectionManager ccm = base.getConnectionManager();
                SchemeRegistry          srg = ccm.getSchemeRegistry();

                srg.register(new Scheme("https", ssf, 443));

                return new DefaultHttpClient(ccm, base.getParams());
            } catch (Exception ex) {
                ex.printStackTrace();

                return null;
            }
        }
    }
}

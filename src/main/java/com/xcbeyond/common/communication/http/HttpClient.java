package com.xcbeyond.common.communication.http;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import javax.net.ssl.*;
import java.io.*;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Http、Https客户端
 * @Auther: xcbeyond
 * @Date: 2019/1/17 18:03
 */
public class HttpClient {

    /**
     * 请求超时时间
     */
    private static final int TIME_OUT = 120000;

    /**
     * Https请求
     */
    private static final String HTTPS = "https";

    /**
     * 发送JSON格式参数POST请求
     * @param url 请求路径
     * @param params JSON格式请求参数
     * @return 服务器响应对象
     * @throws IOException
     */
    public static Response post(String url, String params) throws IOException {
        return doPostRequest(url, null, null, params);
    }

    /**
     * 字符串参数post请求
     * @param url 请求URL地址
     * @param param 请求参数Map
     * @return 服务器响应对象
     * @throws IOException
     */
    public static Response post(String url, Map<String, String> param) throws IOException {
        return doPostRequest(url, param, null, null);
    }

    /**
     * 带上传文件的post请求
     * @param url 请求URL地址
     * @param param 请求字符串参数集合
     * @param fileMap 请求文件参数集合
     * @return 服务器响应对象
     * @throws IOException
     */
    public static Response post(String url, Map<String, String> param, Map<String, File> fileMap) throws IOException {
        return doPostRequest(url, param, fileMap, null);
    }

    /**
     * 执行post请求
     * @param url 请求URL地址
     * @param paramMap 请求字符串参数Map
     * @param fileMap 请求文件参数集合
     * @return 服务器响应对象
     * @throws IOException
     */
    private static Response doPostRequest(String url, Map<String, String> paramMap, Map<String, File> fileMap, String jsonParams) throws IOException {
        if (null == url || url.isEmpty()) {
            throw new RuntimeException("The request URL is blank.");
        }

        Connection connection = initConn(url, Connection.Method.POST);

        // 收集上传文件输入流，最终全部关闭
        List<InputStream> inputStreamList = null;
        try {
            // 添加文件参数
            if (null != fileMap && !fileMap.isEmpty()) {
                inputStreamList = new ArrayList<InputStream>();
                InputStream in = null;
                File file = null;
                Set<Entry<String, File>> set = fileMap.entrySet();
                for (Entry<String, File> e : set) {
                    file = e.getValue();
                    in = new FileInputStream(file);
                    inputStreamList.add(in);
                    connection.data(e.getKey(), file.getName(), in);
                }
            }

            // 设置请求体为JSON格式内容
            else if (null != jsonParams && !jsonParams.isEmpty()) {
                connection.header("Content-Type", "application/json;charset=UTF-8");
                connection.requestBody(jsonParams);
            }

            // 普通表单提交方式
            else {
                connection.header("Content-Type", "application/x-www-form-urlencoded");
            }

            // 添加字符串类参数
            if (null != paramMap && !paramMap.isEmpty()) {
                connection.data(paramMap);
            }

            Response response = connection.execute();
            return response;
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {// 关闭上传文件的输入流
            if (null != inputStreamList) {
                for (InputStream in : inputStreamList) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Get请求
     * @param url    请求URL
     * @param param  请求参数Map
     * @return  服务器响应对象
     * @throws IOException
     */
    public static Response get(String url, Map<String, String> param) throws IOException {
        if (null == url || url.isEmpty()) {
            throw new RuntimeException("The request URL is blank.");
        }

        if (null != param && !param.isEmpty()) {
            StringBuilder sb = new StringBuilder(url);
            if (url.indexOf("?") == -1) {
                sb.append("?");
            }
            sb.append(map2UrlParam(param));

            url = sb.toString();
        }

        Connection connection = initConn(url, Connection.Method.GET);
        Response response = connection.execute();

        return response;
    }

    /**
     * Put请求
     * @param url   请求URL
     * @param param 请求参数Map
     * @return  服务器响应对象
     * @throws Exception
     */
    public static Response put(String url, Map<String, String> param) throws Exception {
        if (null == url || url.isEmpty()) {
            throw new RuntimeException("The request URL is blank.");
        }

        Connection connection = initConn(url, Connection.Method.PUT);
        connection.data(param);

        Response response = connection.execute();
        return response;
    }

    /**
     * 将请求参数map转换为Url参数字符串
     * @param param 请求参数map
     * @return Url参数字符串 格式为：key1=value1&key2=value2
     * @throws UnsupportedEncodingException
     */
    private static String map2UrlParam(Map<String, String> param) throws UnsupportedEncodingException {
        if (null == param || param.isEmpty()) {
            return null;
        }
        StringBuffer url = new StringBuffer();
        boolean isfist = true;
        for (Entry<String, String> entry : param.entrySet()) {
            if (isfist) {
                isfist = false;
            } else {
                url.append("&");
            }
            url.append(entry.getKey()).append("=");
            String value = entry.getValue();
            if (null != value && !"".equals(value)) {
                url.append(URLEncoder.encode(value, "UTF-8"));
            }
        }
        return url.toString();

    }

    /**
     * 初始化请求连接
     * @param url
     * @param requestMethod
     * @return
     */
    private static Connection initConn(String url, Connection.Method requestMethod) {
        // 如果是Https请求
        if (url.startsWith(HTTPS)) {
            getTrust();
        }

        Connection connection = Jsoup.connect(url);
        connection.method(requestMethod);
        connection.timeout(TIME_OUT);
        connection.ignoreHttpErrors(true);
        connection.ignoreContentType(true);

        return connection;
    }

    /**
     * 获取服务器信任
     */
    private static void getTrust() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[] { new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package sklad.faurecia.sk.scanner.api;

import android.content.Context;

import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sklad.faurecia.sk.scanner.utils.Constants;
import sklad.faurecia.sk.scanner.utils.Utils;

public class HttpMethods {

    public static final int POST = 0;
    public static final int GET = 1;
    public static final int DOWNLOAD = 2;
    public static final int PUT = 3;

//    private String _ApiUri = "http://10.20.30.124:9999/Epicklist";
    private String _ApiUri = "http://10.192.81.15:9999/Epicklist";

    private Map<String, Object> callParamsMap = new HashMap<>();

    /**
     * Default constructor
     */
    public HttpMethods(Context context) {
        super();
    }

    /**
     * POST REQUEST
     *
     * @param httpclient
     * @param callUri
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws ParseException
     * @throws JSONException
     */
    @SuppressWarnings("unchecked")
    public Integer makePOSTrequest(HttpClient httpclient, String callUri)
            throws ClientProtocolException, IOException, ParseException,
            JSONException {

        HttpPost httppost = new HttpPost(_ApiUri + callUri);

        printOutRequest(httppost);

        MultipartEntity reqEntity=  new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        // Add aditional parameters
        for (String key : callParamsMap.keySet()) {

            if (callParamsMap.get(key) instanceof String) {

                String value = (String) callParamsMap.get(key);
                reqEntity.addPart(key, new StringBody(value, Charset.forName("UTF-8")));
                Utils.appendLog(Constants.TAG_RETRIEVER, key + " = " + value);

            } else if (callParamsMap.get(key) instanceof List<?>) {

                for (String value : (List<String>) callParamsMap.get(key)) {

                    reqEntity.addPart(key, new StringBody(value, Charset.forName("UTF-8")));
                    Utils.appendLog(Constants.TAG_RETRIEVER, key + " = " + value);
                }
            }
        }
        httppost.setEntity(reqEntity);

        // Execute HTTP Post Request
        HttpResponse responseBody = httpclient.execute(httppost);
        String response = EntityUtils.toString(responseBody.getEntity());

        printOutResponse(responseBody, response);

        return responseBody.getStatusLine().getStatusCode();
    }

    /**
     * GET REQUEST
     *
     * @param httpclient
     * @param callUri
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws ParseException
     * @throws JSONException
     */
    public Integer makeGETrequest(HttpClient httpclient, String callUri)
            throws ClientProtocolException, IOException, ParseException,
            JSONException {

        HttpGet httpget = new HttpGet(_ApiUri + callUri);

        printOutRequest(httpget);

        HttpResponse responseBody = httpclient.execute(httpget);
        String response = EntityUtils.toString(responseBody.getEntity());

        printOutResponse(responseBody, response);

        return responseBody.getStatusLine().getStatusCode();
    }

    private void printOutRequest(HttpMessage httpRequest) {

        Utils.appendLog(Constants.TAG_RETRIEVER,
                ((HttpRequestBase) httpRequest).getRequestLine().toString());
        for (Header header : httpRequest.getAllHeaders()) {
            Utils.appendLog(Constants.TAG_RETRIEVER, header.toString());

        }

    }

    private void printOutResponse(HttpResponse responseBody, String response) {
        Utils.appendLog(Constants.TAG_RETRIEVER, responseBody.getStatusLine()
                .toString());
        for (Header header : responseBody.getAllHeaders()) {

            Utils.appendLog(Constants.TAG_RETRIEVER, header.toString());
        }

        Utils.appendLog(Constants.TAG_RETRIEVER,
                "---------------- ResponseBody --------------------");
        Utils.appendLog(Constants.TAG_RETRIEVER, response);
    }

    public void addCallParam(String paramName, String value) {
        callParamsMap.put(paramName, value);
    }

    public void addCallParam(HashMap<String, Object> params) {
        if (params != null)
            callParamsMap.putAll(params);
    }

    public void clearCallParams() {
        callParamsMap.clear();
    }

}

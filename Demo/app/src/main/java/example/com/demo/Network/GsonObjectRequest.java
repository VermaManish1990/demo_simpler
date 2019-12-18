package example.com.demo.Network;

import android.content.Context;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import example.com.demo.database.CacheORM;
import example.com.demo.utils.StringUtil;

public class GsonObjectRequest<T> extends JsonRequest<T> {
    private static final String TAG = "GsonObjectRequest";
    private final Gson mGson;
    private final Class<T> mClazz;
    private Context mCtx;
    private NetworkUpdateListener<T> mListener;
    private NetworkUpdateListener<T> mTempListener;
    private GsonObjectRequest mTempReq;
    public int reqType;
    public String mUrl;
    private boolean cache;
    private String response;
    //    final Handler handler = new Handler();
    private TimerTask timerTask;
    private Timer timer;
    private boolean isResponseDelivered = false;
    private boolean isTimeOut = false;
    private static final int TIME_OUT_IN_MILLISEC = 500;

    private int failCount = 10;

    public GsonObjectRequest(int method, int reqType, String url, String jsonPayload, Class<T> clazz, NetworkUpdateListener listener) {
        this(method, reqType, url, new HashMap<String, String>(), jsonPayload, clazz, listener);
        mListener = listener;
        this.reqType = reqType;
        this.mUrl = url;

    }

    public GsonObjectRequest(int method, int type, String url, Map<String, String> mRequestHeaders, String jsonPayload, Class<T> clazz, NetworkUpdateListener listener) {
        this(method, url, type, mRequestHeaders, jsonPayload, clazz, listener, new Gson());
        mListener = listener;
        this.reqType = type;
        this.mUrl = url;
    }


    public GsonObjectRequest(Context ctx, int method, int type, String url, Map<String, String> mRequestHeaders, String jsonPayload, Class<T> clazz, NetworkUpdateListener listener) {
        this(method, url, type, mRequestHeaders, jsonPayload, clazz, listener, new Gson());
        mListener = listener;
        this.reqType = type;
        mCtx = ctx;
        this.mUrl = url;
    }


    public GsonObjectRequest(int method, String url, String jsonPayload, Class<T> clazz, NetworkUpdateListener listener, Gson gson) {
        this(method, url, 1, null, jsonPayload, clazz, listener, gson);
        mListener = listener;

    }


    public GsonObjectRequest(int method, String url, int type, Map<String, String> mRequestHeaders, String jsonPayload, Class<T> clazz, NetworkUpdateListener listener, Gson gson) {
        super(method, url, mRequestHeaders, jsonPayload, listener);
        mListener = listener;
        this.mClazz = clazz;
        mGson = gson;
        reqType = type;
        this.mUrl = url;

        // TODO :: start timer only for selected request.
        if (cache) {
            startTimer();
        }

    }

    private void startTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    public void run() {
                        if (!isResponseDelivered) {
                            isTimeOut = true;
                            mListener.onErrorResponse(new VolleyError("Timeout Error"));
                            timerTask.cancel();
                            timer.cancel();
                        } else {
                            isTimeOut = false;
                            stoptimertask();
                        }
                    }
                });
            }
        };
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        startTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, TIME_OUT_IN_MILLISEC, 100000);
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    public GsonObjectRequest(int method, int type, String url, Map<String, String> mRequestHeaders, String jsonPayload, Class<T> clazz, NetworkUpdateListener listener, Gson gson) {
        this(method, url, type, mRequestHeaders, jsonPayload, clazz, listener, gson);
        mListener = listener;
        this.reqType = type;
        this.mUrl = url;
    }


    @Override
    protected void deliverResponse(T response) {
        if (!isTimeOut) {
            mListener.onResponse(response);
        }

    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        isResponseDelivered = true;
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if (getBody() != null && getBody().length > 0) {
                String body = new String(getBody(), "UTF-8");
            }

            if (response.statusCode == 200 || response.statusCode == 201) {

                Map<String, String> responseHeaders = response.headers;
                String etag = responseHeaders.get("ETag");
                // Cache in DB
                if (cache) {
                    CacheORM.insertCache(mCtx, getUrl(), json);
                }
                if (!StringUtil.isNullOrEmpty(etag)) {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    return Response.success(mGson.fromJson(json, mClazz), cacheEntry);
                } else {
                    if (StringUtil.isNullOrEmpty(json)) {
                        json = "{\"status\":\"SUCCESS\"}";
                    }
                    return Response.success(mGson.fromJson(json, mClazz), null);

                }

            } else if (response.statusCode == 304) {
                return Response.success(mGson.fromJson(json, mClazz), null);
            } else if (response.statusCode == 204) {
                return Response.error(new VolleyError());
            } else {
                return Response.error(new VolleyError());
            }
//            return Response.success(mGson.fromJson(json, mClazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return Response.error(new VolleyError(new NetworkResponse(response.data)));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new VolleyError(new NetworkResponse(response.data)));
        }
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        return mRequestHeaders;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

}
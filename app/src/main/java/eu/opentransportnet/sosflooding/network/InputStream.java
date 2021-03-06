package eu.opentransportnet.sosflooding.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ilmars Svilsts
 */
public class InputStream extends Request<byte[]> {
    private final Response.Listener<byte[]> mListener;
    private Map<String, String> mParams;
    //Map for direct access to headers
    public Map<String, String> responseHeaders ;

    public InputStream(int post, String mUrl,Response.Listener<byte[]> listener,
                                    Response.ErrorListener errorListener, HashMap<String, String> params) {

        super(post, mUrl, errorListener);
        //Don't use cache
        setShouldCache(false);
        mListener = listener;
        mParams=params;
    }

    @Override
    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return mParams;
    };


    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

        //Initialize local responseHeader map with received header
        responseHeaders = response.headers;

        //Passes response data
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }
}
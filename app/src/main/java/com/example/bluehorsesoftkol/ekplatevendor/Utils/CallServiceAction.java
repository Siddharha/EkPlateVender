package com.example.bluehorsesoftkol.ekplatevendor.Utils;

import android.content.Context;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bluehorsesoftkol.ekplatevendor.interfaces.BackgroundActionInterface;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rahul on 9/28/2015.
 */
public class CallServiceAction {
    private Context context;
    public BackgroundActionInterface actionInterface;
    private JSONObject errorNodeMain;

    public CallServiceAction(Context context){
        this.context = context;
    }

    public void requestVersionApi(JSONObject jsonObjParams, String apiUrl){
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                ConstantClass.BASE_URL + apiUrl,
                jsonObjParams,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        actionInterface.onCompleted(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(context, "Error in server connection.", Toast.LENGTH_LONG).show();
                            errorNodeMain = new JSONObject();
                            JSONObject jsonErrNode = new JSONObject();
                            jsonErrNode.put("errCode", "-1");
                            jsonErrNode.put("errMsg", "Error in server connection.");
                            errorNodeMain.put("errNode", jsonErrNode);
                            actionInterface.onCompleted(errorNodeMain);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", "Custom-Agent 1.0");
                actionInterface.onStarted();
                return headers;
            }
        };
        queue.add(myRequest);
    }

    public void requestVersionApi1(String apiUrl){
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST,
                ConstantClass.BASE_URL + apiUrl,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        actionInterface.onCompleted(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(context, "Error in server connection.", Toast.LENGTH_LONG).show();
                            errorNodeMain = new JSONObject();
                            JSONObject jsonErrNode = new JSONObject();
                            jsonErrNode.put("errCode", "-1");
                            jsonErrNode.put("errMsg", "Error in server connection.");
                            errorNodeMain.put("errNode", jsonErrNode);
                            actionInterface.onCompleted(errorNodeMain);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", "Custom-Agent 1.0");
                actionInterface.onStarted();
                return headers;
            }
        };
        queue.add(myRequest);
    }
}

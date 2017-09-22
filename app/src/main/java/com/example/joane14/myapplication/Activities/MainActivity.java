package com.example.joane14.myapplication.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Model.FbUSer;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;


public class    MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    EditText username, pass;
    AccessTokenTracker accessTokenTracker;
    private static String userId, email, name, pictureFile, gender, mUsername, mPassword;
    Bundle mbundle;
    Button mLogin;
    User passModel;
    TextView mBtnRegister;



    public void registerUser(){
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SPUtility.getSPUtil(this).contains("USER_OBJECT")){
            Log.d("inside", "contain SPUtility");
            User spUser = new User();
            spUser = (User) SPUtility.getSPUtil(MainActivity.this).getObject("USER_OBJECT", User.class);
            Intent intent = new Intent(MainActivity.this, LandingPage.class);
            Bundle b = new Bundle();
            b.putSerializable("userModel", spUser);
            b.putBoolean("fromRegister", false);
            intent.putExtra("user",b);
//                    intent.putExtra("recommned", b);
            SPUtility.getSPUtil(MainActivity.this).putObject("USER_OBJECT ", spUser);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "Contain", Toast.LENGTH_SHORT).show();
        }else{
            Log.d("inside", "empty SPUtility");
            Toast.makeText(MainActivity.this, "Empty", Toast.LENGTH_SHORT).show();
        }

        username = (EditText) findViewById(R.id.etUsername);
        pass = (EditText) findViewById(R.id.etPassword);


        passModel = new User();

        mLogin = (Button) findViewById(R.id.btnLogIn);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

//        register();
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsername = username.getText().toString();
                mPassword = pass.getText().toString();
                login(view);
            }
        });
        mBtnRegister = (TextView) findViewById(R.id.btnRegister);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                registerUser();
                Intent intent = new Intent(MainActivity.this, LocationChooser.class);
                startActivity(intent);
            }

        });
        mbundle = new Bundle();
        if (isLoggedIn()) {
            Log.d("Logged in", "True");

            FbUSer userFB;
            PrefUtil prefUtil = PrefUtil.getPrefUtilInstance(this);
            userFB = prefUtil.getLoggedInFacebookUserDetails();
            mbundle.putString("email", userFB.getEmail());
            mbundle.putString("name", userFB.getName());
            mbundle.putString("userId", userFB.getUserId());
            mbundle.putString("gender", userFB.getGender());
            Intent intent = new Intent(MainActivity.this, LandingPage.class);
            intent.putExtra("ProfileBundle", mbundle);
            startActivity(intent);
//            getUserData();
        } else {
            Log.d("Logged in", "False");
        }


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(MainActivity.this, "Log in success", Toast.LENGTH_SHORT).show();

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject user, GraphResponse response) {
                                        Log.d("User Data", response.toString());
                                        Log.d("User json object", user.toString());
                                        try {
                                            Log.d("User id", user.getString("id").toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            Log.d("inside try ctch", "");

                                            email = user.getString("email").toString();
                                            name = user.getString("name").toString();
                                            userId = user.getString("id").toString();
                                            gender = user.getString("gender").toString();

                                            Log.d("name", name);
                                            Log.d("gender", gender);
                                            Log.d("email", email);
                                            Log.d("userId", userId);

                                            mbundle.putString("email", email);
                                            mbundle.putString("name", name);
                                            mbundle.putString("userId", userId);
                                            mbundle.putString("gender", gender);

                                            PrefUtil.getPrefUtilInstance(MainActivity.this)
                                                    .saveFacebookUserInfo(name, email, gender, userId);

                                            SPUtility.getSPUtil(MainActivity.this).putObject("USER_OBJECT", user);

                                            Intent intent = new Intent(MainActivity.this, LandingPage.class);
                                            intent.putExtra("ProfileBundle", mbundle);
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, name, email, gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this, "Log in cancelled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(MainActivity.this, "Log in error", Toast.LENGTH_SHORT).show();

                    }
                });


    }


    public void login(View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://192.168.1.8:8080/Koobym/user/login";
//        String URL = Constants.WEB_SERVICE_URL +"user/login";
        User user = new User();
        user.setUsername(mUsername);
        user.setPassword(mPassword);
        Log.d("Inside", user.toString());
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(user);
        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Inside ", "On response");
                if(response == null || response.length() == 0){
                    Log.d("Response", "Null");

                    AlertDialog.Builder dialogAlert = new AlertDialog.Builder(MainActivity.this);
                    dialogAlert.setMessage("Wrong password or username.");
                    dialogAlert.setTitle("Error Message");
                    dialogAlert.setPositiveButton("Okay", null);
                    dialogAlert.setCancelable(true);
                    dialogAlert.create().show();
                }else{
                    Intent intent = new Intent(MainActivity.this, LandingPage.class);
                    User user = gson.fromJson(response, User.class);
                    Log.d("Response", response);
                    Bundle b = new Bundle();
                    b.putSerializable("userModel", user);
                    b.putBoolean("fromRegister", false);
                    intent.putExtra("user",b);
//                    intent.putExtra("recommned", b);
                    SPUtility.getSPUtil(MainActivity.this).putObject("USER_OBJECT", user);
                    startActivity(intent);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void getUserData() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log.v("LoginActivity", response.toString());

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();

    }


}

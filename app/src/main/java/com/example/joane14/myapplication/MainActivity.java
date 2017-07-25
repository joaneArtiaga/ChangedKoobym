package com.example.joane14.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    AccessTokenTracker accessTokenTracker;
    private static String userId, email, name, pictureFile, gender;
    Bundle mbundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        mbundle = new Bundle();
        if (isLoggedIn()) {
            Log.d("Logged in", "True");
            Intent intent = new Intent(MainActivity.this, LandingPage.class);
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
//                        getUserData();


                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject user, GraphResponse response) {
                                        Log.d("User Data", response.toString());
                                        Log.d("User json object", user.toString());
                                        try {
                                            email = user.getString("email");
                                            name = user.getString("name");
                                            userId = user.getString("id");
                                            gender = user.getString("gender");

                                            Log.d("name", email);
                                            Log.d("gender", gender);
                                            Log.d("email", email);
                                            Log.d("userId", userId);

                                            mbundle.putString("email", email);
                                            mbundle.putString("name", name);
                                            mbundle.putString("userId", userId);
                                            mbundle.putString("gender", gender);
                                            Log.d("inside try ctch","");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        AccessToken accessToken;

                                        final Profile profile = Profile.getCurrentProfile();

                                        if ((user != null) && (profile != null)) {

                                            accessToken = AccessToken.getCurrentAccessToken();

                                            if (accessToken.getDeclinedPermissions().isEmpty()) {
                                                try {
                                                    String email = user.get("email").toString();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }else{
                                                Log.d("denied permissions", accessToken.getDeclinedPermissions().toString());
                                            }


                                        }
/*

                                        try {
                                            new GraphRequest(
                                                    AccessToken.getCurrentAccessToken(),
                                                    "/" + object.getString("id"),
                                                    null,
                                                    HttpMethod.GET,
                                                    new GraphRequest.Callback() {
                                                        public void onCompleted(GraphResponse response) {
                                                            Log.d("user data new", response.toString());
                                                        }
                                                    }
                                            ).executeAsync();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
*/
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, name, email, gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                        Intent intent = new Intent(MainActivity.this, LandingPage.class);
                        intent.putExtras(mbundle);
//                        startActivity(intent);
//                        Log.d("name", mbundle.getString("name"));
//                        Log.d("gender", mbundle.getString("gender"));
//                        Log.d("email", mbundle.getString("email"));
//                        Log.d("userId", mbundle.getString("userId"));
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(MainActivity.this, "Log in cancelled", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(MainActivity.this, "Log in error", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    public void login(View view){
        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile, email"));
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

package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.ProfileActivity;
import com.example.joane14.myapplication.Adapters.RecyclerAdapterShowBook;
import com.example.joane14.myapplication.Model.Author;
import com.example.joane14.myapplication.Model.Book;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ShowBooksFrag extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Book> bookList;
    Book bookObject;
    User userObkect;
    String bookStatDes, bookDateBought;


    private OnFragmentInteractionListener mListener;

    public ShowBooksFrag() {
        // Required empty public constructor
    }


    public static ShowBooksFrag newInstance() {
        ShowBooksFrag fragment = new ShowBooksFrag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("OnCreate Fragment", "inside");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_show_books, container, false);

        bookObject = new Book();
        bookList = new ArrayList<Book>();

        userObkect = new User();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        String resultString = getArguments().getString("searchResult");
        this.userObkect = (User) getArguments().getSerializable("userProfile");

        if(resultString.length()!=0){
            Log.d("searchResult", resultString);
            try {
                JSONObject obj = new JSONObject(resultString);
                JSONArray items = obj.getJSONArray("items");

                for(int init = 0; init< items.length(); init++){
                    bookObject = new Book();
                    JSONObject arrayObject = items.getJSONObject(init);

                    obj = arrayObject.getJSONObject("volumeInfo");


                    bookObject.setBookTitle(arrayObject.getJSONObject("volumeInfo").getString("title"));
                    if(obj.has("description")){
                        bookObject.setBookDescription(obj.getString("description"));
                    }else{
                        bookObject.setBookDescription("No Description");
                    }

                    if(obj.has("authors")){
                        JSONArray arrJson = obj.getJSONArray("authors");
                        List<Author> arr = new ArrayList<Author>();
                        Author auth;
                        for(int i = 0; i < arrJson.length(); i++) {
                            auth = new Author();
                            auth.setAuthorFName(arrJson.getString(i));
                            auth.setAuthorLName("");
                            arr.add(auth);
                        }

                        bookObject.setBookAuthor(arr);
                    }else{
                        bookObject.setBookAuthor(new ArrayList<Author>());
                    }

                    if(obj.has("imageLinks")){
                        bookObject.setBookFilename(obj.getJSONObject("imageLinks").getString("thumbnail"));
                    }
                    else{
                        bookObject.setBookFilename("");
                    }
                    bookObject.setBookOriginalPrice(1.1f);
                    if(obj.has("publishedDate")) {
                        bookObject.setBookPublishedDate(obj.getString("publishedDate"));
                    }else{
                        bookObject.setBookPublishedDate("N/A");
                    }
//                    bookObject.setBookFilename();
//                    bookObject.setBookId(arrayObject.getJSONObject(""));

//                    bookObject.setBookFilename(obj.getJSONObject("imageLinks").getString("thumbnail"));
                    bookList.add(bookObject);

                    Log.d("Title",arrayObject.getJSONObject("volumeInfo").getString("title"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mAdapter = new RecyclerAdapterShowBook(bookList);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((RecyclerAdapterShowBook) mAdapter).setOnItemClickListener(new RecyclerAdapterShowBook
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Toast.makeText(getContext() ,bookList.get(position).getBookTitle(), Toast.LENGTH_SHORT).show();

                bookObject = bookList.get(position);

                if(userObkect!=null){
                    Log.d("userObject", "not null");
                }else{
                    Log.d("userObject", "null");
                }

                if(bookObject!=null){
                    Log.d("bookObject", "not null");
                }else{
                    Log.d("bookObject", "null");
                }

                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);


                final EditText statDescription = new EditText(getContext());
                statDescription.setHint("Book State Description");
                layout.addView(statDescription);

                final EditText etBought = new EditText(getContext());
                layout.addView(etBought);
                etBought.setHint("Date Bought (YYYY-MM-DD)");
                alert.setView(layout);

                alert.setTitle("You chose "+bookList.get(position).getBookTitle());


                alert.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //What ever you want to do with the value
//                        Editable YouEditTextValue = edittext.getText();
//                        //OR
//                        String YouEditTextValue

                        Log.d("Inside", "onClickPositiveButton");
                        bookStatDes = statDescription.getText().toString();
                        Log.d("Stat Description", bookStatDes);
                        bookDateBought = etBought.getText().toString();
                        Log.d("Date Bought", bookDateBought);
                        addBook(userObkect, bookObject, bookStatDes, bookDateBought);

                    }
                });

                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d("Inside", "onClickNegativeButton");
                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void addBook(User userObj, Book bookObj, String bookStatDes, String bookDateBought) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        String URL = "http://192.168.1.134:8080/Koobym/bookOwner/add";
        String URL = Constants.WEB_SERVICE_URL+"bookOwner/add";
        BookOwnerModel bookOwnerObj = new BookOwnerModel();
        bookOwnerObj.setBookObj(bookObj);
        bookOwnerObj.setUserObj(userObj);
        bookOwnerObj.setDateBought(bookDateBought);
        bookOwnerObj.setStatusDescription(bookStatDes);
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookOwnerObj);
        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_VOLLEY", response);
                BookOwnerModel bookOwner = gson.fromJson(response, BookOwnerModel.class);
                Log.i("LOG_VOLLEY", bookOwner.getDateBought());
                Log.i("LOG_VOLLEY", bookOwner.getStatusDescription());
                Log.i("LOG_VOLLEY", bookOwner.getBookObj().toString());
                Log.i("LOG_VOLLEY", bookOwner.getUserObj().toString());
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

    /*private ArrayList<Book> getDataSet() {
        ArrayList results = new ArrayList<Book>();
        for (int index = 0; index < 20; index++) {
            Book objBook = new Book();
            results.add(index, objBook);
        }
        return results;
    }*/
}

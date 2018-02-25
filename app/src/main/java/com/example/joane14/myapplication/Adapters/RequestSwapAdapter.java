package com.example.joane14.myapplication.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.AuctionHeader;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by Joane14 on 20/10/2017.
 */

public class RequestSwapAdapter extends BaseAdapter {

    private Context context;
    List<SwapHeader> bookOwnerModelList;
    BookOwnerModel bookOwnerModel;
    private LayoutInflater mInflater;
    RatingBar mRating;


    public RequestSwapAdapter(Context context, List<SwapHeader> bookOwnerModelList){
        this.context = context;
        this.bookOwnerModelList = bookOwnerModelList;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount(){
        return bookOwnerModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_shelf, null);
        }

        bookOwnerModel = bookOwnerModelList.get(position).getSwapDetail().getBookOwner();

        ImageView bookPic = (ImageView) convertView.findViewById(R.id.displayBookPic);
        TextView bookTitle = (TextView) convertView.findViewById(R.id.lpBookTitle);
        TextView bookAuthor = (TextView) convertView.findViewById(R.id.lpAuthor);
        TextView statusBook = (TextView) convertView.findViewById(R.id.ratingStatusBook);
        TextView bookPrice = (TextView) convertView.findViewById(R.id.lpSwapPrice);
        LinearLayout statusLinear = (LinearLayout) convertView.findViewById(R.id.status_ll);

        bookPrice.setVisibility(View.GONE);
        mRating = (RatingBar) convertView.findViewById(R.id.rating_bookRating);
        Log.d("inside", "PrefferedAdapter");
        bookTitle.setText(bookOwnerModel.getBookObj().getBookTitle());

        statusLinear.setVisibility(View.GONE);
        statusBook.setVisibility(View.GONE);
//        statusBook.setText(bookOwnerModel.getStatus());
//        if(bookOwnerModelList.get(position).getStatus().equals("Complete")){
//            statusBook.setText("Complete");
//            statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRent));
//        }else {
//            statusBook.setText("Rejected");
//            statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAuction));
//        }

        String author = " ";
        if(bookOwnerModel.getBookObj().getBookAuthor().size()!=0){
            for(int init=0; init<bookOwnerModel.getBookObj().getBookAuthor().size(); init++){
                if(!(bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                    author+=bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                    if(!(bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                        author+=bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorLName();
                        if(init+1<bookOwnerModel.getBookObj().getBookAuthor().size()){
                            author+=", ";
                        }
                    }
                }
            }
        }else{
            author="Unknown Author";
        }
        bookAuthor.setText(author);

        Glide.with(context).load(bookOwnerModel.getBookObj().getBookFilename()).centerCrop().into(bookPic);

        mRating.setRating(Float.parseFloat(String.valueOf(bookOwnerModel.getRate())));

        return convertView;
    }

    public static void makeTextViewResizable(final TextView textView, final int maxLine, final String expandText, final boolean viewMore){
        Log.d("makeTextViewResizable","inside");
        if(textView.getTag() == null){
            textView.setTag(textView.getText());
        }

        ViewTreeObserver treeObserver = textView.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndText;
                ViewTreeObserver observer = textView.getViewTreeObserver();

                observer.removeGlobalOnLayoutListener(this);

                if(maxLine == 0){
                    lineEndText = textView.getLayout().getLineEnd(0);
                    text = textView.getText().subSequence(0, lineEndText - expandText.length()-1)+" "+ expandText;
                }else if(maxLine > 0 && textView.getLineCount() >= maxLine){
                    lineEndText = textView.getLayout().getLineEnd(maxLine - 1);
                    text = textView.getText().subSequence(0, lineEndText - expandText.length()-1)+" "+ expandText;
                }else{
                    lineEndText = textView.getLayout().getLineEnd(textView.getLayout().getLineCount()-1);
                    text = textView.getText().subSequence(0, lineEndText)+" "+ expandText;
                }

                textView.setText(text);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                textView.setText(addClickablePartTextViewResizable(Html.fromHtml(textView.getText().toString()), textView, lineEndText, expandText,
                        viewMore), TextView.BufferType.SPANNABLE);

            }
        });


    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    Log.d("spannable", "inside");
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        makeTextViewResizable(tv, 3, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public void getRatings(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
        Log.d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_RATINGS+bookOwnerModel.getBookOwnerId();
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        final RentalHeader rentalHeader =new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseRequestReceived", response);

                Float fl = Float.parseFloat(response);

                mRating.setRating(fl);
                Log.d("RatingAdapter: "+bookOwnerModel.getBookObj().getBookTitle(), String.valueOf(fl));
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

}
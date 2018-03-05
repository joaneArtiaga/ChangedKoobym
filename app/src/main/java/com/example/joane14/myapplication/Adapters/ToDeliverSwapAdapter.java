package com.example.joane14.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.BookActActivity;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.ProfileActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.VolleyUtil;
import com.example.joane14.myapplication.Model.BookActivity;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.SwapHeaderDetail;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.joane14.myapplication.Adapters.BookChooserAdapter.d;

/**
 * Created by Joane14 on 03/02/2018.
 */

public class ToDeliverSwapAdapter extends RecyclerView.Adapter<ToDeliverSwapAdapter.BookHolder> {

    public List<SwapHeader> bookList;
    public Activity context;

    @Override
    public ToDeliverSwapAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.swap_book_activity_item, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("ToDeliverSwapAdapter","inside");
        ToDeliverSwapAdapter.BookHolder dataObjectHolder = new ToDeliverSwapAdapter.BookHolder(this.context, view);


        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public ToDeliverSwapAdapter(List<SwapHeader> myDataset) {
        bookList = myDataset;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ToDeliverSwapAdapter.BookHolder holder, final int position) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currDAte = sdf.format(c);

        d("swapHeader", bookList.get(position).toString());
//        Log.d("CurrentDate: "+currDAte, "ReturnDate: "+bookList.get(position).getSwapHeader().getDateDelivered());
        if(currDAte.equals(bookList.get(position).getDateDelivered())){
            holder.mRate.setImageResource(R.drawable.checkbookact);
        }else{
            holder.mRate.setImageResource(R.drawable.notrate);
        }

        holder.mNotify.setVisibility(View.GONE);

        holder.mBookTitle.setText(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookTitle());
        holder.mBookDate.setText(bookList.get(position).getDateTimeStamp());
        holder.mRenterName.setText(bookList.get(position).getRequestedSwapDetail().getBookOwner().getUserObj().getUserFname()+" "+bookList.get(position).getRequestedSwapDetail().getBookOwner().getUserObj().getUserLname());
        if(bookList.get(position).getDateDelivered()==null){
            Log.d("EndDateDeliverSwap", "walay sulod");
        }else{
            holder.mDate.setText(bookList.get(position).getDateDelivered());
        }

        if(bookList.get(position).getMeetUp()==null){

        }else{
            holder.mTime.setText(bookList.get(position).getMeetUp().getUserDayTime().getTime().getStrTime());
            holder.mLocation.setText(bookList.get(position).getMeetUp().getLocation().getLocationName());
        }

       Glide.with(context).load(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);

        holder.mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userModelPass", bookList.get(position).getRequestedSwapDetail().getBookOwner().getUserObj());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.mRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currDAte.equals(bookList.get(position).getDateDelivered())){
                    final AlertDialog ad = new AlertDialog.Builder(context).create();
                    ad.setTitle("Confirmation");
                    ad.setMessage("Will notify "+bookList.get(position).getRequestedSwapDetail().getBookOwner().getUserObj().getUserFname()+" "+bookList.get(position).getRequestedSwapDetail().getBookOwner().getUserObj().getUserFname()+" that the book has been delivered.");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setDelivered(bookList.get(position));
                            Intent intent = new Intent(context, BookActActivity.class);
                            context.startActivity(intent);
                        }
                    });
                    ad.show();
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Alert!");
                    alertDialog.setMessage("Not yet time for delivery.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });

        List<SwapHeaderDetail> shd = new ArrayList<SwapHeaderDetail>();
        List<SwapHeaderDetail> newShd = new ArrayList<SwapHeaderDetail>();

        shd = bookList.get(position).getSwapHeaderDetail();
        for(int init=0; init<shd.size(); init++){
            if(shd.get(init).getSwapType().equals("Requestor")){
                newShd.add(shd.get(init));
            }
        }

        final SwapRequestAdapter adapter = new SwapRequestAdapter(context, newShd);

        holder.ly.setAdapter(adapter);



    }

    private void setDelivered(SwapHeader swapHeaderMod){
        String URL = Constants.SET_DELIVERED_SWAP+swapHeaderMod.getSwapHeaderId();

        Log.d("setDeliveredURL", URL);
        Log.d("setDelivered", swapHeaderMod.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("setDeliveredResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) ;
        VolleyUtil.volleyRQInstance(context).add(stringRequest);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mBookTitle, mRenterName, mBookDate, mLocation, mTime, mDate;
        ImageView mIvBookImg;
        ListView ly;
        ImageButton mProfile, mNotify, mRate;
        SwapHeader swapHeaderObj;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mBookTitle = (TextView) itemView.findViewById(R.id.bookTitleBA);
            mDate = (TextView) itemView.findViewById(R.id.dateBA);
            mRenterName = (TextView) itemView.findViewById(R.id.renterNameBA);
            mBookDate = (TextView) itemView.findViewById(R.id.bookDateBA);
            mLocation = (TextView) itemView.findViewById(R.id.locationBA);
            mTime = (TextView) itemView.findViewById(R.id.timeBA);
            mIvBookImg = (ImageView) itemView.findViewById(R.id.ivBookBA);
            mProfile = (ImageButton) itemView.findViewById(R.id.profileBA);
            mNotify = (ImageButton) itemView.findViewById(R.id.notifyBA);
            mRate = (ImageButton) itemView.findViewById(R.id.rateButtonBA);

            ly = (ListView) itemView.findViewById(R.id.listSwap);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swapHeaderObj = new SwapHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    swapHeaderObj = ToDeliverSwapAdapter.this.bookList.get(position);
                    if(swapHeaderObj==null){
                        Log.d("deliverSwap", "is null");
                    }else{
                        Log.d("deliverSwap", swapHeaderObj.toString());
                    }
//                    intent.putExtra("ViewBook", "fromAdapter");
//                    bundle.putSerializable("View", rentalDetailObj);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
                }
            });
        }

    }

    public String getDetails(int position){
        String result = "";

        result = bookList.get(position).toString();



        return result;

    }
}

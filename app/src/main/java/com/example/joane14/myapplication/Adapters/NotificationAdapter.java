package com.example.joane14.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.HistoryActivity;
import com.example.joane14.myapplication.Activities.MeetUpChooser;
import com.example.joane14.myapplication.Activities.MyShelf;
import com.example.joane14.myapplication.Activities.NotificationAct;
import com.example.joane14.myapplication.Activities.SwapMeetUpChooser;
import com.example.joane14.myapplication.Activities.TransactionActivity;
import com.example.joane14.myapplication.Activities.ViewBookAct;
import com.example.joane14.myapplication.Activities.ViewBookActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.BookOwnerReview;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserNotification;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.util.Log.d;

/**
 * Created by Joane14 on 12/10/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.BookHolder> {

    public List<UserNotification> userNotificationList;
    BookOwnerReview bookOwnerReview;
    SwapHeader swapHeaderModelOut;
    String message = "";

    public Activity context;

    @Override
    public NotificationAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_notification, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter", "inside");
        NotificationAdapter.BookHolder dataObjectHolder = new NotificationAdapter.BookHolder(this.context, view);


        Log.d("RequestAdapter", String.valueOf(userNotificationList.size()));
        return dataObjectHolder;
    }

    public NotificationAdapter(List<UserNotification> myDataset) {
        userNotificationList = myDataset;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(NotificationAdapter.BookHolder holder, final int position) {

        UserNotification userNotification = userNotificationList.get(position);

        String dateStr = "";
        Calendar c = Calendar.getInstance();
        @SuppressLint({"NewApi", "LocalSuppress"})
        DateFormat format = new android.icu.text.SimpleDateFormat("yyyy-MM-dd");
        dateStr = format.format(c.getTime());

        swapHeaderModelOut = new SwapHeader();
        if (userNotification.getActionName().equals("rental")) {
            if (userNotificationList.get(position).getActionStatus().equals("request") || userNotificationList.get(position).getActionStatus().equals("Request")) {
                message = userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname() + " " + userNotification.getActionStatus() + "ed your book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " to be rented.";
            } else if (userNotification.getActionStatus().equals("Approved")) {
                message = userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname() + " " + userNotification.getActionStatus() + " your request of the book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " to be rented.";
            } else if (userNotification.getActionStatus().equals("Confirm")) {
                message = userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname() + " " + userNotification.getActionStatus() + "ed the meet up location and time with the book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " to be rented.";
            }else if(userNotification.getActionStatus().equals("Due")){
                message = "The book you rented "+userNotification.getBookActionPerformedOn().getBookObj().getBookTitle()+" is already due, " +
                        "your deposit will be deducted.";
            }
        } else if (userNotification.getActionName().equals("swap")) {
            if (userNotificationList.get(position).getActionStatus().equals("request") || userNotificationList.get(position).getActionStatus().equals("Request")) {
                getSwapHeader(position, userNotification);
            } else if (userNotification.getActionStatus().equals("Approved")) {
                message = userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname() + " " + userNotification.getActionStatus() + " your request of the book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " to be swapped.";
            } else if (userNotification.getActionStatus().equals("Confirm")) {
                message = userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname() + " " + userNotification.getActionStatus() + "ed the meet up location and time with the book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " to be swapped.";
            } else if (userNotification.getActionStatus().equals("ToGive")) {
                message = "The book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " which is to be swapped should be delivered today, " + dateStr + " to " + userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname();
            } else if (userNotification.getActionStatus().equals("ToReceive")) {
                message = "The book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " was delivered  by " + userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname();
            } else if (userNotification.getActionStatus().equals("Rejected")) {
                message = "The book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " was Rejected by " + userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname();
            } else if (userNotification.getActionStatus().equals("Complete")) {
                message = "The transaction of the book that you want to " + userNotification.getActionName() + " is completed";
            }
        }


        holder.mMessage.setText(message);
        Picasso.with(context).load(userNotificationList.get(position).getUserPerformer().getImageFilename()).fit().into(holder.mPerformer);
        Picasso.with(context).load(userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookFilename()).fit().into(holder.mBookPerformed);

        if (userNotification.getRead() == true) {
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            Log.d("cardRead", "true");
        } else if (userNotification.getRead() == false) {
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorMint));
            Log.d("cardRead", "true");
        }
    }
    public void getSwapHeader(final int position, final UserNotification userNotification) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

        String URL = Constants.GET_SWAP_DETAIL + userNotificationList.get(position).getActionId();

        d("getSwapHeader", URL);
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        final SwapHeader swapHeader = new SwapHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapHeader);


        d("LOG_VOLLEY", mRequestBody);
        final User finalUser = user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("swapHeaderResponseId", response);
                SwapHeader swapHeaderMod = gson.fromJson(response, SwapHeader.class);
                swapHeaderModelOut = swapHeaderMod;
                message = userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname() + " " + userNotification.getActionStatus() + "ed your book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " to be swapped with your book "+swapHeaderModelOut.getSwapDetail().getBookOwner().getBookObj().getBookTitle()+".";
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
    public int getItemCount() {
        return userNotificationList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mMessage;
        ImageView mPerformer, mBookPerformed;
        CardView mCardView;
        Context context;


        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mMessage = (TextView) itemView.findViewById(R.id.notifMessage);
            mPerformer = (ImageView) itemView.findViewById(R.id.notifPerformer);
            mBookPerformed = (ImageView) itemView.findViewById(R.id.notifBookPerformer);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    rentalHeaderObj = new RentalHeader();
//                    Bundle bundle = new Bundle();
//                    int position = getAdapterPosition();
//                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
//                    intent.putExtra("ViewBook", "fromAdapter");
//                    bundle.putSerializable("View", rentalDetailObj);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
                    int position = getAdapterPosition();
                    Log.d("AdapterNotif", userNotificationList.get(position).getUserPerformer().getUserFname());

                    User user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
                    if (user.getUserId() == userNotificationList.get(position).getUser().getUserId()) {

                        if (userNotificationList.get(position).getActionName().equals("rental")) {
                            Log.d("RentalAdapter", "Inside");

                            if (userNotificationList.get(position).getActionStatus().equals("Request")) {
                                createDialog(position);
                            } else if (userNotificationList.get(position).getActionStatus().equals("Approved")) {
                                Log.d("ApprovedNotif", "Inside");
                                getRentalHeader(position, "meetUp");
                                updateReceive(position, "Confirm");
                                getRead(position);
                            } else if (userNotificationList.get(position).getActionStatus().equals("Confirm")) {
                                getRead(position);
                            }

                        } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                            Log.d("SwapAdapter", "Inside");
                            if (userNotificationList.get(position).getActionName().equals("swap")) {

                                if (userNotificationList.get(position).getActionStatus().equals("Request")) {
                                    acceptSwap(position);
                                } else if (userNotificationList.get(position).getActionStatus().equals("Approved")) {
                                    Log.d("ApprovedNotif", "Inside");
                                    getSwapHeader(position, "meetUp");
                                    updateReceive(position, "Confirm");
                                    getRead(position);
                                } else if (userNotificationList.get(position).getActionStatus().equals("Confirm")) {
                                    Log.d("ConfirmNotif", "Inside");
                                    getRead(position);
//                                    getSwapHeader(position, "summ");
                                    toDeliverDialog(position);
                                } else if (userNotificationList.get(position).getActionStatus().equals("ToReceive")) {
                                    Log.d("ToReceiveNotif", "Inside");
                                    toGiveDialog(position);
                                    getRead(position);
                                } else if (userNotificationList.get(position).getActionStatus().equals("Complete")) {
                                    Log.d("CompletedNotif", "Inside");
                                    getRead(position);
                                    getSwapHeader(position, "change");
                                }
                            }
                        }
                    } else {

                        if (userNotificationList.get(position).getActionName().equals("rental")) {
                            getRentalHeader(position, "view");
                        } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                            getSwapHeader(position, "view");
                        }
                    }

                }
            });
        }


        public void updateBookOwner(int position, final boolean bool, SwapHeader swapHeader) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
            String status = "", URL = "";

            if (bool == true) {
                URL = Constants.UPDATE_BOOK_OWNER + "/" + swapHeader.getSwapDetail().getBookOwner().getBookOwnerId() + "/" + swapHeader.getRequestedSwapDetail().getBookOwner().getUserObj().getUserId();
            } else {
                URL = Constants.UPDATE_BOOK_OWNER + "/" + swapHeader.getRequestedSwapDetail().getBookOwner().getBookOwnerId() + "/" + swapHeader.getSwapDetail().getBookOwner().getUserObj().getUserId();
            }

            Log.d("Update SwapHeader URL", URL);


            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(swapHeader);


            Log.d("LOG_VOLLEY_swapHeaderUD", mRequestBody);
            Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("RequestReceivedStatus", response);
                    if (bool == false) {
                        Intent intent = new Intent(context, HistoryActivity.class);
                        context.startActivity(intent);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                    error.printStackTrace();
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

        public void meetUpSumm(final int position, SwapHeader swapHeaderSumm) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("MeetUp Summary");
            alertDialogBuilder.setMessage("Date:\t" +swapHeaderSumm.getDateDelivered()+
                    "\n\nDay:\t"+swapHeaderSumm.getMeetUp().getUserDayTime().getDay().getStrDay()+
                    "\n\nTime:\t"+swapHeaderSumm.getMeetUp().getUserDayTime().getTime().getStrTime()+
                    "\n\nLocation:\t"+swapHeaderSumm.getMeetUp().getLocation().getLocationName());
            alertDialogBuilder.setPositiveButton("Okay",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            getRead(position);
                            if (userNotificationList.get(position).getActionName().equals("rental")) {
                                updateReceive(position, "Approved");
                            } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                                updateSwap(position, "ToReceive");
                            }
                            Intent intent = new Intent(context, NotificationAct.class);
                            context.startActivity(intent);
                        }
                    });
//            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    getRead(position);
//                    updateReceive(position, "Rejected");
//                    Intent intent = new Intent(context, NotificationAct.class);
//                    context.startActivity(intent);
//                }
//            });

            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        public void toDeliverDialog(final int position) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("Did you deliver the book " + userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookTitle() + " to " + userNotificationList.get(position).getUserPerformer().getUserFname() + " " + userNotificationList.get(position).getUserPerformer().getUserLname() + " on time?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            getRead(position);
                            if (userNotificationList.get(position).getActionName().equals("rental")) {
                                updateReceive(position, "Approved");
                            } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                                updateSwap(position, "ToReceive");
                            }
                            Intent intent = new Intent(context, NotificationAct.class);
                            context.startActivity(intent);
                        }
                    });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getRead(position);
                    updateReceive(position, "Rejected");
                    Intent intent = new Intent(context, NotificationAct.class);
                    context.startActivity(intent);
                }
            });

            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        public void toGiveDialog(final int position) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("Did you receive the book " + userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookTitle() + " that was delivered by " + userNotificationList.get(position).getUserPerformer().getUserFname() + " " + userNotificationList.get(position).getUserPerformer().getUserLname() + "?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            getRead(position);
                            if (userNotificationList.get(position).getActionName().equals("rental")) {
                                Log.d("InsideGiveDialog","rental");
                                updateReceive(position, "Approved");
                            } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                                updateSwap(position, "Complete");
                                Log.d("InsideGiveDialog","swap");
                            }
                            Intent intent = new Intent(context, NotificationAct.class);
                            context.startActivity(intent);
                        }
                    });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getRead(position);
                    updateReceive(position, "Rejected");
                    Intent intent = new Intent(context, NotificationAct.class);
                    context.startActivity(intent);
                }
            });

            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        public void createDialog(final int position) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Request");
            alertDialogBuilder.setMessage("Do you want to approve " + userNotificationList.get(position).getUserPerformer().getUserFname() + " " + userNotificationList.get(position).getUserPerformer().getUserLname() + "'s " + userNotificationList.get(position).getActionStatus() + "?");
            alertDialogBuilder.setPositiveButton("Approve",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            getRead(position);
                            if (userNotificationList.get(position).getActionName().equals("rental")) {
                                updateReceive(position, "Approved");
                            } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                                updateSwap(position, "Approved");
                            }
                            Intent intent = new Intent(context, NotificationAct.class);
                            context.startActivity(intent);
                        }
                    });
            alertDialogBuilder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getRead(position);
                    updateReceive(position, "Rejected");
                    Intent intent = new Intent(context, NotificationAct.class);
                    context.startActivity(intent);
                }
            });

            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        public void acceptSwap(final int position) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Swap Request");
            alertDialogBuilder.setMessage("Do you want to accept " + userNotificationList.get(position).getUserPerformer().getUserFname() + " " + userNotificationList.get(position).getUserPerformer().getUserLname() + "'s " + userNotificationList.get(position).getActionStatus() + "?");
            alertDialogBuilder.setPositiveButton("Accept",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            getRead(position);
                            if (userNotificationList.get(position).getActionName().equals("rental")) {
                                updateReceive(position, "Approved");
                            } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                                updateSwap(position, "Approved");
                            }
                            Intent intent = new Intent(context, NotificationAct.class);
                            context.startActivity(intent);
                        }
                    });
            alertDialogBuilder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getRead(position);
                    updateReceive(position, "Rejected");
                    Intent intent = new Intent(context, NotificationAct.class);
                    context.startActivity(intent);
                }
            });

            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        @SuppressLint("NewApi")
        public void updateSwap(final int position, final String status) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";
            String nextDateStr = "";

            Calendar c = Calendar.getInstance();
            @SuppressLint({"NewApi", "LocalSuppress"})
            DateFormat format = new android.icu.text.SimpleDateFormat("yyyy-MM-dd");
            nextDateStr = format.format(c.getTime());

            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
            String URL = Constants.UPDATE_SWAP_HEADER + "/" + status + "/" + userNotificationList.get(position).getActionId() + "/" + nextDateStr;

            Log.d("UpdateSwapHeaderURL", URL);


            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(userNotificationList);


            Log.d("LOG_VOLLEY_swapHeaderUD", mRequestBody);
            Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("UpdateSwapHeader", response);
                    SwapHeader swapHeaderMod = gson.fromJson(response, SwapHeader.class);

//                    getSwapHeader(position, "view");

                    Intent intent = new Intent(context, NotificationAct.class);
                    context.startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                    error.printStackTrace();
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

        @SuppressLint("NewApi")
        public void updateReceive(final int position, String status) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";
            String nextDateStr = "";

            Calendar c = Calendar.getInstance();
            @SuppressLint({"NewApi", "LocalSuppress"})
            DateFormat format = new android.icu.text.SimpleDateFormat("yyyy-MM-dd");
            nextDateStr = format.format(c.getTime());

            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
            String URL = Constants.UPDATE_RENTAL_HEADER + "/" + userNotificationList.get(position).getActionId() + "/" + status + "/" + nextDateStr;

            Log.d("UpdateRentalHeaderURL", URL);


            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(userNotificationList);


            Log.d("LOG_VOLLEY_swapHeaderUD", mRequestBody);
            Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("UpdateRentalHeader", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                    error.printStackTrace();
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

        public void getRead(int position) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
            d("UserIdReceive", String.valueOf(user.getUserId()));
            String URL = Constants.PUT_USER_READ + userNotificationList.get(position).getUserNotificationId();

            d("CountURL", URL);
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

            final RentalHeader rentalHeader = new RentalHeader();

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(rentalHeader);


            d("LOG_VOLLEY", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("userNotificationRead", response);
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

        public void getRentalHeader(final int position, final String status) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

            String URL = Constants.GET_RENTAL_DETAIL + userNotificationList.get(position).getActionId();

            d("getRentalHeader", URL);
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

            final RentalHeader rentalHeader = new RentalHeader();

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(rentalHeader);


            d("LOG_VOLLEY", mRequestBody);
            final User finalUser = user;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("rentalHeaderResponseId", response);
                    RentalHeader rentalHeaderMod = gson.fromJson(response, RentalHeader.class);

                    if (status.equals("view")) {
                        Intent intent = new Intent(context, ViewBookAct.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("viewBook", rentalHeaderMod.getRentalDetail());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else if (status.equals("meetUp")) {

                        if (finalUser.getUserId() == userNotificationList.get(position).getUser().getUserId()) {
                            Intent intent = new Intent(context, MeetUpChooser.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("rentalHeader", rentalHeaderMod);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
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

        public void getSwapHeader(final int position, final String status) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

            String URL = Constants.GET_SWAP_DETAIL + userNotificationList.get(position).getActionId();

            d("getSwapHeader", URL);
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

            final SwapHeader swapHeader = new SwapHeader();

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(swapHeader);


            d("LOG_VOLLEY", mRequestBody);
            final User finalUser = user;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("swapHeaderResponseId", response);
                    SwapHeader swapHeaderMod = gson.fromJson(response, SwapHeader.class);

                    if (status.equals("view")) {
                        Intent intent = new Intent(context, ViewBookAct.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("swapBook", swapHeaderMod.getSwapDetail());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else if (status.equals("meetUp")) {
                        if (finalUser.getUserId() == userNotificationList.get(position).getUser().getUserId()) {
                            Intent intent = new Intent(context, SwapMeetUpChooser.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("swapHeader", swapHeaderMod);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    } else if (status.equals("change")) {
                        updateBookOwner(position, true, swapHeaderMod);
                        updateBookOwner(position, false, swapHeaderMod);
                    }else if(status.equals("summ")){
                        meetUpSumm(position, swapHeaderMod);
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


    }

}


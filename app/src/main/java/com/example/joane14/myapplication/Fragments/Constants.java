package com.example.joane14.myapplication.Fragments;

/**
 * Created by Joane14 on 03/08/2017.
 */

public class Constants {


    public static final String WEB_SERVICE_URL = "http://104.197.4.32:8080/Koobym/";

    public static final String IMAGE_URL = "http://104.197.4.32:8080/Koobym/image/%s";

    public static final String RENT_BY_ID = "http://104.197.4.32:8080/Koobym/rentalDetail/rentById/";

    public static final String GOOGLE_API_SEARCH_URL = "https://www.googleapis.com/books/v1/volumes?q=intitle:%s";

    public static final String ISBN_SEARCH_URL = "http://isbndb.com/api/v2/json/IX7LA6KI/books?q=%s";

    public static final String GOOGLE_API_SEARCH_URL_ISBN = "https://www.googleapis.com/books/v1/volumes?q=isbn:%s";

    public static final String POST_RENTAL_HEADER = "http://104.197.4.32:8080/Koobym/rentalHeader/add";

    public static final String POST_SWAP_HEADER = "http://104.197.4.32:8080/Koobym/swapHeader/add";

    public static final String GET_RENTAL_HEADER = "http://104.197.4.32:8080/Koobym/rentalHeader/all";

    public static final String GET_RENTAL_HEADER_BY_ID = "http://104.197.4.32:8080/Koobym/rentalHeader/rentalById/";

    public static final String GET_RENTAL_HEADER_MY_REQUEST = "http://104.197.4.32:8080/Koobym/rentalHeader/myRequestById/";

    public static final String GET_TRANSACTION_TO_RETURN_RENTER = "http://104.197.4.32:8080/Koobym/rentalHeader/toReturnByIdRenter/";

    public static final String GET_TRANSACTION_TO_RECEIVE_RENTER = "http://104.197.4.32:8080/Koobym/rentalHeader/toReceiveByIdRenter/";

    public static final String GET_TRANSACTION_TO_RECEIVE_SWAP = "http://104.197.4.32:8080/Koobym/swapHeader/toReceiveById/";

    public static final String GET_TRANSACTION_TO_RECEIVE_OWNER = "http://104.197.4.32:8080/Koobym/rentalHeader/toReceiveByIdOwner/";

    public static final String GET_TRANSACTION_COMPLETE_RENTER = "http://104.197.4.32:8080/Koobym/rentalHeader/completeByIdRenter/";

    public static final String GET_TRANSACTION_COMPLETE_OWNER = "http://104.197.4.32:8080/Koobym/rentalHeader/completeByIdOwner/";

    public static final String GET_TRANSACTION_TO_DELIVER= "http://104.197.4.32:8080/Koobym/rentalHeader/toDeliverById/";

    public static final String UPDATE_RENTAL_HEADER= "http://104.197.4.32:8080/Koobym/rentalHeader/updateStatus";

    public static final String UPDATE_SWAP_HEADER= "http://104.197.4.32:8080/Koobym/swapHeader/updateStatus";

    public static final String GET_SWAP_DETAILS_ALL= "http://104.197.4.32:8080/Koobym/swapDetail/all";

    public static final String GET_REQUEST_RECEIVED = "http://104.197.4.32:8080/Koobym/rentalHeader/requestReceivedById/";

    public static final String GET_MY_SWAP = "http://104.197.4.32:8080/Koobym/swapDetail/mySwapBooks/";

    public static final String RECOMMEND_SWAP_BOOK = "http://104.197.4.32:8080/Koobym/swapDetail/mySwapBooksPrice";

    public static final String UPLOAD_IMAGE = "http://104.197.4.32:8080/Koobym/image/upload";


}

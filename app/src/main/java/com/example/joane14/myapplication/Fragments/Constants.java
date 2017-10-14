package com.example.joane14.myapplication.Fragments;

/**
 * Created by Joane14 on 03/08/2017.
 */

public class Constants {


    public static final String WEB_SERVICE_URL = "http://104.197.4.32:8080/Koobym/";

    public static final String IMAGE_URL = "http://104.197.4.32:8080/Koobym/image/%s";

    public static final String RENT_BY_ID = "http://104.197.4.32:8080/Koobym/rentalDetail/rentById/";

    public static final String GOOGLE_API_SEARCH_URL = "https://www.googleapis.com/books/v1/volumes?q=%s";

    public static final String ISBN_SEARCH_URL = "http://isbndb.com/api/v2/json/IX7LA6KI/books?q=%s";

    public static final String ISBN_SEARCH_PRICES = "http://isbndb.com/api/v2/json/IX7LA6KI/prices/%s";

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

    public static final String UPDATE_BOOK_OWNER= "http://104.197.4.32:8080/Koobym/bookOwner/updateBookOwner";

    public static final String UPDATE_SWAP_HEADER= "http://104.197.4.32:8080/Koobym/swapHeader/updateStatus";

    public static final String GET_SWAP_DETAILS_ALL= "http://104.197.4.32:8080/Koobym/swapDetail/getAllSwap";

    public static final String GET_REQUEST_RECEIVED = "http://104.197.4.32:8080/Koobym/rentalHeader/requestReceivedById/";

    public static final String GET_MY_SWAP = "http://104.197.4.32:8080/Koobym/swapDetail/mySwapBooks/";

    public static final String RECOMMEND_SWAP_BOOK = "http://104.197.4.32:8080/Koobym/swapDetail/mySwapBooksPrice";

    public static final String UPLOAD_IMAGE = "http://104.197.4.32:8080/Koobym/image/upload";

    public static final String POST_SWAP_COMMENT_DETAIL ="http://104.197.4.32:8080/Koobym/swapCommentDetail/add";

    public static final String GET_TO_APPROVE ="http://104.197.4.32:8080/Koobym/swapHeader/toApproveById";

    public static final String GET_COMPLETE_SWAP ="http://104.197.4.32:8080/Koobym/swapHeader/completeById";

    public static final String GET_TO_DELIVER_SWAP ="http://104.197.4.32:8080/Koobym/swapHeader/toDeliverById";

    public static final String POST_BOOK_OWNER_REVIEW ="http://104.197.4.32:8080/Koobym/bookOwnerReview/add";

    public static final String POST_BOOK_OWNER_RATE ="http://104.197.4.32:8080/Koobym/bookOwnerRating/add";

    public static final String POST_USER_RATE ="http://104.197.4.32:8080/Koobym/userRating/add";

    public static final String GET_BOOK_REVIEWS ="http://104.197.4.32:8080/Koobym/bookOwnerReview/getAllReviewOfBookOwner";

    public static final String GET_USER_REVIEWS ="http://104.197.4.32:8080/Koobym/userRating/getAllUserRatingsOf";

    public static final String GET_MY_BOOKS ="http://104.197.4.32:8080/Koobym/bookOwner/myBooksById";

    public static final String POST_RENTAL_DETAIL ="http://104.197.4.32:8080/Koobym/rentalDetail/add";

    public static final String POST_SWAP_DETAIL ="http://104.197.4.32:8080/Koobym/swapDetail/add";

    public static final String POST_BOOK_OWNER ="http://104.197.4.32:8080/Koobym/bookOwner/add";

    public static final String POST_BOOK ="http://104.197.4.32:8080/Koobym/book/addNewBook";

    public static final String GET_USER_RATING ="http://104.197.4.32:8080/Koobym/userRating/getAverageRatingOfUser";

    public static final String GET_BOOK_RATING ="http://104.197.4.32:8080/Koobym/bookOwnerRating/getAverageRatingOfBookOwner";

    public static final String CHECK_FB_USER ="http://104.197.4.32:8080/Koobym/user/checkFbUser";

    public static final String GET_REJECTED_BY_OWNER ="http://104.197.4.32:8080/Koobym/rentalHeader/rejectedByIdOwner";

    public static final String GET_REJECTED_BY_RENTER ="http://104.197.4.32:8080/Koobym/rentalHeader/rejectedByIdRenter";

}

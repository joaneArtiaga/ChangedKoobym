package com.example.joane14.myapplication.Fragments;

/**
 * Created by Joane14 on 03/08/2017.
 */

public class Constants {


    public static final String WEB_SERVICE_URL = "http://104.198.152.85/Koobym/";

    public static final String IMAGE_URL = WEB_SERVICE_URL + "image/%s";

    public static final String RENT_BY_ID = WEB_SERVICE_URL + "rentalDetail/rentById/";

    public static final String GOOGLE_API_SEARCH_URL = "https://www.googleapis.com/books/v1/volumes?q=%s";

    public static final String ISBN_SEARCH_URL = "http://isbndb.com/api/v2/json/IX7LA6KI/books?q=%s";

    public static final String ISBN_SEARCH_PRICES = "http://isbndb.com/api/v2/json/IX7LA6KI/prices/%s";

    public static final String GOOGLE_API_SEARCH_URL_ISBN = "https://www.googleapis.com/books/v1/volumes?q=isbn:%s";

    public static final String POST_RENTAL_HEADER = WEB_SERVICE_URL + "rentalHeader/add";

    public static final String POST_MEET_UP = WEB_SERVICE_URL + "meetUp/add";

    public static final String POST_SWAP_HEADER = WEB_SERVICE_URL + "swapHeader/add";

    public static final String POST_AUCTION_HEADER = WEB_SERVICE_URL + "auctionHeader/add";

    public static final String GET_RENTAL_HEADER_MY_REQUEST = WEB_SERVICE_URL + "rentalHeader/myRequestById/";

    public static final String GET_TRANSACTION_TO_RETURN_RENTER = WEB_SERVICE_URL + "rentalHeader/toReturnByIdRenter/";

    public static final String GET_TRANSACTION_TO_RECEIVE_RENTER = WEB_SERVICE_URL + "rentalHeader/toReceiveByIdRenter/";

    public static final String GET_TRANSACTION_TO_RECEIVE_SWAP = WEB_SERVICE_URL + "swapHeader/toReceiveById/";

    public static final String GET_TRANSACTION_TO_RECEIVE_OWNER = WEB_SERVICE_URL + "rentalHeader/toReceiveByIdOwner/";

    public static final String GET_TRANSACTION_COMPLETE_RENTER = WEB_SERVICE_URL + "rentalHeader/completeByIdRenter/";

    public static final String GET_TRANSACTION_COMPLETE_OWNER = WEB_SERVICE_URL + "rentalHeader/completeByIdOwner/";

    public static final String GET_TRANSACTION_TO_DELIVER = WEB_SERVICE_URL + "rentalHeader/toDeliverById/";

    public static final String UPDATE_RENTAL_HEADER = WEB_SERVICE_URL + "rentalHeader/updateStatus";

    public static final String UPDATE_BOOK_OWNER = WEB_SERVICE_URL + "bookOwner/updateBookOwner";

    public static final String UPDATE_BOOK_OWNER_1 = WEB_SERVICE_URL + "bookOwner/update";

    public static final String UPDATE_SWAP_HEADER = WEB_SERVICE_URL + "swapHeader/updateStatus";

    public static final String GET_SWAP_DETAILS_ALL = WEB_SERVICE_URL + "swapDetail/getAllSwap";

    public static final String GET_REQUEST_RECEIVED = WEB_SERVICE_URL + "rentalHeader/requestReceivedById/";

    public static final String GET_MY_SWAP = WEB_SERVICE_URL + "swapDetail/mySwapBooks/";

    public static final String GET_MY_SWAP_RECOMMENDED = WEB_SERVICE_URL + "swapDetail/mySwapBooksPrice/";

    public static final String RECOMMEND_SWAP_BOOK = WEB_SERVICE_URL + "swapDetail/mySwapBooksPrice/";

    public static final String UPLOAD_IMAGE = WEB_SERVICE_URL + "image/upload";

    public static final String POST_SWAP_COMMENT_DETAIL = WEB_SERVICE_URL + "swapCommentDetail/add";

    public static final String POST_AUCTION_COMMENT_DETAIL = WEB_SERVICE_URL + "auctionCommentDetail/add";

    public static final String GET_TO_APPROVE = WEB_SERVICE_URL + "swapHeader/toApproveById";

    public static final String GET_TO_APPROVED = WEB_SERVICE_URL + "swapHeader/approvedById/";

    public static final String GET_COMPLETE_SWAP = WEB_SERVICE_URL + "swapHeader/completeById";

    public static final String GET_TO_DELIVER_SWAP = WEB_SERVICE_URL + "swapHeader/toDeliverById";

    public static final String POST_BOOK_OWNER_REVIEW = WEB_SERVICE_URL + "bookOwnerReview/add";

    public static final String POST_BOOK_OWNER_RATE = WEB_SERVICE_URL + "bookOwnerRating/add";

    public static final String POST_USER_RATE = WEB_SERVICE_URL + "userRating/add";

    public static final String GET_BOOK_REVIEWS = WEB_SERVICE_URL + "bookOwnerReview/getAllReviewOfBookOwner";

    public static final String GET_USER_REVIEWS = WEB_SERVICE_URL + "userRating/getAllUserRatingsOf";

    public static final String GET_MY_BOOKS = WEB_SERVICE_URL + "bookOwner/myBooksById";

    public static final String POST_RENTAL_DETAIL = WEB_SERVICE_URL + "rentalDetail/add";

    public static final String POST_RENTAL_DETAIL_1 = WEB_SERVICE_URL + "rentalDetail/setBookOwnerAsRental";

    public static final String POST_SWAP_DETAIL = WEB_SERVICE_URL + "swapDetail/add";

    public static final String POST_SWAP_DETAIL_1 = WEB_SERVICE_URL + "swapDetail/setBookOwnerAsSwap";

    public static final String POST_BOOK_OWNER = WEB_SERVICE_URL + "bookOwner/add";

    public static final String POST_BOOK = WEB_SERVICE_URL + "book/addNewBook";

    public static final String GET_USER_RATING = WEB_SERVICE_URL + "userRating/getAverageRatingOfUser";

    public static final String GET_BOOK_RATING = WEB_SERVICE_URL + "bookOwnerRating/getAverageRatingOfBookOwner";

    public static final String CHECK_FB_USER = WEB_SERVICE_URL + "user/checkFbUser";

    public static final String GET_REJECTED_BY_OWNER = WEB_SERVICE_URL + "rentalHeader/rejectedByIdOwner";

    public static final String GET_REJECTED_BY_RENTER = WEB_SERVICE_URL + "rentalHeader/rejectedByIdRenter";

    public static final String GET_REJECTED_BY_SWAP = WEB_SERVICE_URL + "swapHeader/rejectedOwner";

    public static final String CHECK_EXIST = WEB_SERVICE_URL + "rentalHeader/checkExist";

    public static final String GET_RENTERS = WEB_SERVICE_URL + "rentalHeader/getRentalDetail";

    public static final String INCREMENT_BOOK_OWNER = WEB_SERVICE_URL + "bookOwner/increment";

    public static final String GET_COMPLETE_SWAP_HEADER = WEB_SERVICE_URL + "swapHeader/completeById/";

    public static final String GET_RATINGS = WEB_SERVICE_URL + "bookOwnerReview/getAverageRatingOfBookOwner/";

    public static final String GET_COUNT = WEB_SERVICE_URL + "rentalHeader/getCount/";

    public static final String GET_USER_NOTIFICATION = WEB_SERVICE_URL + "userNotification/notificationForUser/";

    public static final String PUT_USER_READ = WEB_SERVICE_URL + "userNotification/notificationRead/";

    public static final String GET_RENTAL_DETAIL = WEB_SERVICE_URL + "rentalHeader/get/";

    public static final String GET_SWAP_DETAIL = WEB_SERVICE_URL + "swapHeader/get/";

    public static final String GET_ALL_BOOK_OWNER = WEB_SERVICE_URL + "bookOwner/all/";

    public static final String GET_BOOK_OWNER_RENTAL_DETAIL = WEB_SERVICE_URL + "rentalDetail/getRentalDetail/";

    public static final String GET_BOOK_OWNER_SWAP_DETAIL = WEB_SERVICE_URL + "swapDetail/getSwapDetail/";

    public static final String GET_BOOK_OWNER_AUCTION_DETAIL = WEB_SERVICE_URL + "auctionDetail/getAuctionDetail/";

    public static final String GET_RECOMMENDATION = WEB_SERVICE_URL + "bookOwner/suggestedBooks/";

    public static final String GET_SEARCH_RESULT = WEB_SERVICE_URL + "bookOwner/searchBooks/";

    public static final String PUT_RENTAL_HEADER = WEB_SERVICE_URL + "rentalHeader/update";

    public static final String PUT_SWAP_HEADER = WEB_SERVICE_URL + "swapHeader/update";

    public static final String GET_BOOK_ACVTIVITY_REQUEST = WEB_SERVICE_URL + "bookOwner/bookActivity/requests/";

    public static final String GET_BOOK_ACTIVITY_MY_BOOKS = WEB_SERVICE_URL + "bookOwner/bookActivity/own/";

    public static final String POST_AUCTION_DETAIL_1 = WEB_SERVICE_URL + "auctionDetail/setBookOwnerAsAuction";

    public static final String GET_AUCTION_BID = WEB_SERVICE_URL + "auctionCommentDetail/getAuctionCommentDetailsOfAuctionDetail/";

}

package com.samarth.memesmagic.util

object Constants {
    const val PARTIALLY_CLICKABLE_TEXT_TAG = "Click"

    private const val API_VERSION = "/v1"
    const val MEME_GITHUB_API_BASE_URL = "https://meme-api.herokuapp.com"
    const val IMAGE_FLIP_BASE_URL = "https://api.imgflip.com"
    const val MEME_MAKER_BASE_URL = "http://alpha-meme-maker.herokuapp.com"
    const val BASE_URL_DEPLOYMENT = "https://memesmagic.herokuapp.com"
    const val BASE_URL_LOCAL = "http://192.168.43.33:8081"
    const val BASE_URL = BASE_URL_LOCAL                  //  <-------- change this
    const val BASE_URL_WS = "$BASE_URL$API_VERSION/ws"
    const val RECONNECT_INTERVAL = 3000L


    const val USERS = "$API_VERSION/user"
    const val POSTS = "$API_VERSION/posts"
    const val FEED = "$API_VERSION/feed"
    const val COMMENTS = "$API_VERSION/comments"
    const val REWARDS = "$API_VERSION/rewards"

    const val JWT_TOKEN_KEY = "JwtToken"
    const val FCM_TOKEN_KEY = "FcmToken"
    const val EMAIL_KEY = "emailKey"
    const val REWARD_ID_KEY = "rewardId"
    const val YEAR_REWARD_ID = "yearRewardId"
    const val DATA_PREFERENCES_NAME_FOR_TOKEN = "tokens"


    const val MAXIMUM_MEME_MAKER_PAGE_NUMBER = 11
    const val NO_MEME = "no_meme"

    const val BUCKET_OBJECT_URL_PREFIX = "https://memebucket143419-staging.s3.ap-south-1.amazonaws.com/public/"

    const val FCM_TYPE_FOLLOWER_ADDED = "FCM_TYPE_FOLLOWER_ADDED"

    const val BEARER = "Bearer"
    const val NETWORK_UNKNOWN_PROBLEM = "Some Problem Occurred!"


    const val TYPE_LIKE_POST = "TYPE_LIKE_POST"
    const val TYPE_JOIN_SERVER_HANDSHAKE = "TYPE_JOIN_SERVER_HANDSHAKE"
    const val TYPE_PRIVATE_CHAT_MESSAGE = "TYPE_PRIVATE_CHAT_MESSAGE"
    const val TYPE_DISCONNECT_REQUEST = "TYPE_DISCONNECT_REQUEST"
    const val TYPE_MESSAGE_RECEIVED = "TYPE_MESSAGE_RECEIVED"
    const val TYPE_MESSAGE_SEEN = "TYPE_MESSAGE_SEEN"
    const val TYPE_MESSAGE_RECEIVED_ON_SERVER_ACK = "TYPE_MESSAGE_RECEIVED_ON_SERVER_ACK"
    const val TYPE_DELETE_MESSAGE = "TYPE_DELETE_MESSAGE"



    const val CHAT_MESSAGES_LIMIT = 100





}

object Screens{

    const val LANDING_SCREEN = "landing_page"
    const val REGISTER_SCREEN = "register_screen"
    const val LOGIN_SCREEN = "login_screen"
    const val HOME_SCREEN = "home_screen"
    const val SPLASH_SCREEN = "splash_screen"

    const val HOME_FEED = "$HOME_SCREEN/feed"
    const val HOME_SEARCH = "$HOME_SCREEN/search"
    const val HOME_CREATE = "$HOME_SCREEN/create"
    const val HOME_REWARDS = "$HOME_SCREEN/rewards"
    const val HOME_PROFILE = "$HOME_SCREEN/profile"

    const val EDIT_SCREEN = "${HOME_CREATE}/edit"
    const val NEW_POST_DETAILS_AND_UPLOAD = "$EDIT_SCREEN/upload"

    const val COMMENT_SCREEN = "comments_screen"
    const val ANOTHER_USER_PROFILE_SCREEN = "another_user_profile"
    const val EDIT_PROFILE_SCREEN = "edit_profile"
    const val SINGLE_POST_SCREEN = "single_post"

    const val CHAT_ROOMS_LIST_SCREEN = "CHAT_ROOMS_LIST_SCREEN"
    const val CHAT_ROOM_SCREEN = "CHAT_ROOM_SCREEN"
    const val FIND_ANOTHER_USER_FOR_CHAT = "FIND_ANOTHER_USER_FOR_CHAT"

    val posts = mutableListOf<String>()

    const val MAX_PASSWORD_LENGTH = 12
    const val MIN_PASSWORD_LENGTH = 6
}


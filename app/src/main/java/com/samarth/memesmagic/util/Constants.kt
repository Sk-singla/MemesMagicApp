package com.samarth.memesmagic.util

import androidx.navigation.NavController

object Constants {
    const val PARTIALLY_CLICKABLE_TEXT_TAG = "Click"

    const val IMAGE_FLIP_BASE_URL = "https://api.imgflip.com"
    const val MEME_MAKER_BASE_URL = "http://alpha-meme-maker.herokuapp.com"
    const val BASE_URL = "https://memesmagic.herokuapp.com"

    private const val API_VERSION = "/v1"
    const val USERS = "$API_VERSION/user"
    const val POSTS = "$API_VERSION/posts"
    const val FEED = "$API_VERSION/feed"

    const val JWT_TOKEN_KEY = "JwtToken"
    const val EMAIL_KEY = "emailKey"
    const val DATA_PREFERENCES_NAME_FOR_TOKEN = "tokens"


    const val MAXIMUM_MEME_MAKER_PAGE_NUMBER = 11
    const val NO_MEME = "no_meme"


    /**
     * todo -> create post component
     * todo -> create home page -> ( topbar, lazycolumn, bottombar )
     * todo -> logout
     * todo -> fetch posts from server
     * todo -> create post
     * todo -> editing,
     * todo -> Main stuff -> Memes related!
     */






}

object Screens{

    const val LANDING_SCREEN = "landing_page"
    const val REGISTER_SCREEN = "register_screen"
    const val LOGIN_SCREEN = "login_screen"
    const val HOME_SCREEN = "home_screen"

    const val HOME_FEED = "$HOME_SCREEN/feed"
    const val HOME_SEARCH = "$HOME_SCREEN/search"
    const val HOME_CREATE = "$HOME_SCREEN/create"
    const val HOME_REWARDS = "$HOME_SCREEN/rewards"
    const val HOME_PROFILE = "$HOME_SCREEN/profile"


}
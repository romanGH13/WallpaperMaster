package com.trehub.wallpapermaster.utils;

public class Constants {

    public final static String LOG_TAG = "myLogs";

    public final static String BASE_URL = "https://api.themoviedb.org/3/";
    public final static String IMG_URL = "https://image.tmdb.org/t/p/w500/";
    public final static String GOOGLE_API_KEY = "AIzaSyCCAgSHgDEZ3MkoR_4wyglsHklpWKEK6UI";
    public final static String MOVIE_DB_API_KEY = "88f01b7ee873a5292f113fd60f62d809";
    public final static String LANG = "ru";
    public final static String PARAMS = "?api_key=" + MOVIE_DB_API_KEY + "&language=" + LANG;

    public final static String ERROR_TOP = "Не удалось обновить список \"Топ\"";
    public final static String ERROR_POPULAR = "Не удалось обновить список \"Популярные\"";
    public final static String ERROR_NOW_PLAYING = "Не удалось обновить список \"Сейчас\"";
    public final static String ERROR_UPCOMING = "Не удалось обновить список \"Скоро\"";
    public final static String ERROR_NET = "Отсутствует интернет соединение.";
    public final static String ERROR_NULL = "К сожалению, в базе отсутствуют необходимые данные.";
    public final static String ERROR = "Возникла ошибка. Попробуйте, пожалуйста, позже.";

    public final static String MOVIE_ID = "id";
    public final static String LIST_ID = "listId";

    public final static int TOP = 1;
    public final static int POPULAR = 2;
    public final static int UPCOMING = 3;
    public final static int NOW_PLAYING = 4;

    public final static int CARD_ITEM = 0;
    public final static int PROGRESS_ITEM = 1;

    public final static int ANIMATION_DURATION = 500;

    public final static String BASE_SEARCH_ENGINE = "";
}

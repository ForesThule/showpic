package com.lesforest.apps.showpic.utils;

public interface Cv {

    String URL = "https://api-fotki.yandex.ru/api/";
    String JPG = "JPG";


    String ORDER = "rpoddate";
    String INIT_PHOTOS_ENDPOINT = Cv.ORDER+";%s/?limit=50";
    String ENDPOINT = "podhistory";
    String URL_PICSUM = "https://picsum.photos/";
    String URL_UNSPLASH = "https://unsplash.com/";





    String app_id = "25472";
    String access_key = "fc48fe874eaadf17d64b6bc07790d128ac35f025ee207a8fb3a28c3e6396ddce";
    String clientSecret = "12621d661095cfa05e80f0eb62fcdb43548c9bd75d4b941edf5ddf1f4754c2ee";
    String redirectUri = "urn:ietf:wg:oauth:2.0:oob";

    String authCode = "8d7640ad509c467f3ca145320effb0dce5dccdfd7446670eb86091300f8b32cf";
    int PERMISSIONS_ITR_REQUEST = 1;
//    String ENDPOINT = "top";
}

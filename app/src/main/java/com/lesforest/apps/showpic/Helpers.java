package com.lesforest.apps.showpic;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class Helpers {

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        boolean b = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        return b;
    }

    public static String getCurrentWiFiInfo(Context ctx) {

        String ssid = "";

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo == null) {
            return null;
        }

        if (networkInfo.isConnected()) {

            final WifiManager wifiManager =
                    (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();

            if (connectionInfo != null) {
                int ipAddress = connectionInfo.getIpAddress();
                ssid = connectionInfo.getSSID();

                return "IP: " + integerToStringIP(ipAddress) + " SSID: " + ssid;
            }
        }
        return ssid;
    }

    public static String integerToStringIP(int ip) {
        return ((ip >> 24) & 0xFF) + "." +

                ((ip >> 16) & 0xFF) + "." +

                ((ip >> 8) & 0xFF) + "." +

                (ip & 0xFF);
    }

    public static MainApi createApi(Context ctx) {

//        String savedServerIpWithPort = PreferenceManager.getDefaultSharedPreferences(ctx)
//                .getString(Cv.PREFS_SERVER_IP_ADDRESS, "");

        String mainUrl =
//                "".equals(savedServerIpWithPort)
//                ? Cv.MAXIMO_MAIN_URL
//                : Cv.HTTP + savedServerIpWithPort +
                        Cv.URL;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        CookieJar jar = new CookieJar() {

            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {

                List<Cookie> cookies = cookieStore.get(url.host());

                return null != cookies ? cookies : new ArrayList<>();
            }
        };


        OkHttpClient.Builder client = new OkHttpClient.Builder();
        ThisApp thisApp = ThisApp.get(ctx);


        client
//                .certificatePinner(new CertificatePinner.Builder()
//                        .add("publicobject.com", Cv.CERTIFICATE)
//                        .build())
                .readTimeout(300, TimeUnit.SECONDS)
//                .addInterceptor(new BasicAuthInterceptor(thisApp.getCurrentUser(), thisApp.getCurrentPwd()))

                .connectTimeout(60, TimeUnit.SECONDS)
                .cookieJar(jar);

        if (BuildConfig.DEBUG) {
            client.addNetworkInterceptor(interceptor);
        }

        return new Retrofit.Builder()
                .baseUrl(mainUrl)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

                .build()
                .create(MainApi.class);
    }

//    public static ApiMaxiDroid createMaxiDroidApi(Context ctx) {
//
//        String savedServerIp = PreferenceManager.getDefaultSharedPreferences(ctx)
//                .getString(Cv.PREFS_SERVER_IP_ADDRESS, "")
//                .split(":")[0];
//
//        String mainUrl = "".equals(savedServerIp)
//                ? Cv.MAXI_DROID_URL
//                : Cv.HTTP + savedServerIp + Cv.MAXI_DROID_PORT;
//
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient.Builder client = new OkHttpClient.Builder()
//                .readTimeout(Cv.READ_TIME_OUT_SEC, TimeUnit.SECONDS)
//                .writeTimeout(Cv.WRITE_TIME_OUT_SEC, TimeUnit.SECONDS);
//
//        if (BuildConfig.DEBUG) {
//            client.addNetworkInterceptor(interceptor);
//        }
//
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        return new Retrofit.Builder()
//                // TODO: 09.06.17 set mainUrl
////                .baseUrl("http://172.22.116.164:8065/")
////                .baseUrl("http://192.166.1.22:8065/")
//                .baseUrl(mainUrl)
//                .client(client.build())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build()
//                .create(ApiMaxiDroid.class);
//    }

//    public static File createAttachmentsDirectory(String dirName) {
//
//        File directory = new File(Environment.getExternalStorageDirectory() +
//                File.separator + Cv.APP_DEVICE_DIR + File.separator +
//                dirName);
//
//        directory.mkdirs();
//
//        return directory;
//    }

//    public static String chlnumMaker(String chlwonum) {
//
//        if (chlwonum.length() < 4) {
//            return chlwonum;
//        }
//        String subFirst = chlwonum.substring(0, chlwonum.length() - 3);
//        String subLast = chlwonum.substring(chlwonum.length() - 3);
//
//        StringBuilder builder = new StringBuilder();
//        builder.append(subFirst);
//        builder.append(" ");
//        builder.append(subLast);
//
//        return builder.toString();
//    }

    public static void vibrate(Context ctx) {

        ((Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE))
                .vibrate(100);
    }

    public static String getNiceTimeFormat(String datestring) {
        if (null == datestring) {
            return "";
        }

        try {
            final String ISO8601DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat(ISO8601DATEFORMAT, Locale.getDefault());

            try {
                Date date = dateformat.parse(datestring.substring(0, 19));
                date.setHours(date.getHours());
//                date.setHours(date.getHours() - 1);
                calendar.setTime(date);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");

            return df.format(calendar.getTime());

        } catch (Exception ex) {
            // something went wrong then.....

            ex.printStackTrace();
            return "";
        }
    }

    public static String getCurrentDateTimeInNiceFormat() {


//        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
//                .format(Calendar.getInstance().getTime());
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
                .format(Calendar.getInstance().getTime());
    }

    public static String getFileExtension(String imgName) {

        String extension = Cv.JPG;

        try {
            String[] split = imgName.split("\\.");   // that's how split by dot

            extension = split[split.length - 1].toLowerCase();
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return extension;
    }

    public static void openPlayMarketByPackageName(Context ctx, String packageName) {

        try {
            ctx.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + packageName)));

        } catch (android.content.ActivityNotFoundException anfe) {

            ctx.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        } catch (Exception ex) {
            ex.printStackTrace();
            // crap.....
        }
    }

    public static String resolveMimeTypeByFileExtension(String extension) {

        String result = "image/*";

        Map<String, String> map = new HashMap<>();

        map.put("pdf", "application/pdf");
        map.put("xls", "application/vnd.ms-excel");
        map.put("xlsx", "application/vnd.ms-excel");
        map.put("ppt", "application/vnd.ms-powerpoint");
        map.put("pptx", "application/vnd.ms-powerpoint");

        String mimeType = map.get(extension);

        if (null != mimeType) {
            result = mimeType;
        }
        return result;
    }



//    public static Uri getImageUriPath(Context ctx, String folderName, String imageName) {
//        //        return Uri.fromFile(new File(createAttachmentsDirectory(folderName), imageName));
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            //            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            return FileProvider.getUriForFile(ctx, BuildConfig.APPLICATION_ID + ".fileprovider", new File(createAttachmentsDirectory(folderName), imageName));
//            //            intent.setDataAndType(contentUri, type);
//        } else {
//            return Uri.fromFile(new File(createAttachmentsDirectory(folderName), imageName));
//        }
//    }


//    public static void showImageOrPdf(Context ctx, String dirName, String imgName) {
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//
//        String fileExtension = Helpers.getFileExtension(imgName);
//
//        intent.setDataAndType(Helpers.getImageUriPath(ctx,
//            dirName, imgName),
//                resolveMimeTypeByFileExtension(fileExtension));
//
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//
//        List<ResolveInfo> resolveInfos = ctx.getPackageManager()
//                .queryIntentActivities(intent, PackageManager.MATCH_ALL);
//
//        if (null == resolveInfos || resolveInfos.size() == 0) {
//
//            showInstallSoftDialog(ctx, fileExtension);
//
//        } else {
//
//            ctx.startActivity(intent);
//        }
//    }

//    public static void showInstallSoftDialog(Context ctx, String fileExtension) {
//
//        new AlertDialog.Builder(ctx)
//                .setMessage(String.format(ctx.getString(R.string.msg_install_app), fileExtension))
//                .setPositiveButton(R.string.btn_ok, (__, ___) -> {
//
//                    if (Cv.PDF.equals(fileExtension)) {
//
//                        openPlayMarketByPackageName(ctx, Cv.APP_PDF_VIEWER);
//                    } else if (Cv.XLS.equals(fileExtension) || Cv.XLSX.equals(fileExtension)
//                            || Cv.PPT.equals(fileExtension) || Cv.PPTX.equals(fileExtension)) {
//
//                        openPlayMarketByPackageName(ctx, Cv.APP_OFFICE);
//                    }
//                })
//                .setNegativeButton(R.string.btn_cancel, null)
//                .show();
//    }

//    public static void addAttachmentsToLayout(Context ctx, LinearLayout frame, Route route) {
//
//        frame.removeAllViews();
//
//        for (String s : route.attachments) {
//
//            try {
//                TextView view = new TextView(ctx);
//
//                view.setCompoundDrawablesWithIntrinsicBounds(
//                        null, ctx.getResources().getDrawable(R.drawable.ic_attachment_black_24dp), null, null);
//
//                String[] split = s.split("/");
//                String fileName = split[split.length - 1];
//                view.setTag(fileName);
//                view.setText(String.format(" %s ", Helpers.getFileExtension(fileName)));
//                view.setTextColor(Color.BLACK);
//                view.setOnClickListener(v -> {
//
//                    String tag = (String) view.getTag();
//                    Helpers.vibrate(ctx);
//                    Helpers.showImageOrPdf(ctx, route.wonum, tag);
//                });
//
//                frame.addView(view);
//            } catch (Exception ex) {
//                // I think it can be ignored here
//                ex.printStackTrace();
//            }
//        }
//    }

//    public static void addAttachmentsToLayout(Context ctx, LinearLayout frame, Claim claim) {
//
//        frame.removeAllViews();
//
//        for (String s : claim.attachments) {
//
//            try {
//                TextView view = new TextView(ctx);
//
//                view.setCompoundDrawablesWithIntrinsicBounds(
//                        null, ctx.getResources().getDrawable(R.drawable.ic_attachment_black_24dp), null, null);
//
//                String[] split = s.split("/");
//                String fileName = split[split.length - 1];
//                view.setTag(fileName);
//                view.setText(String.format(" %s ", Helpers.getFileExtension(fileName)));
//                view.setTextColor(Color.BLACK);
//                view.setOnClickListener(v -> {
//
//                    String tag = (String) view.getTag();
//                    Helpers.vibrate(ctx);
//                    Helpers.showImageOrPdf(ctx, claim.wonum, tag);
//                });
//
//                frame.addView(view);
//            } catch (Exception ex) {
//                // I think it can be ignored here
//                ex.printStackTrace();
//            }
//        }
//    }
//    public static void addAttachmentsToLayout(Context ctx, LinearLayout frame, WorkTaskPPR ppr) {
//
//        frame.removeAllViews();
//
//        for (String s : ppr.attachments) {
//
//            try {
//                TextView view = new TextView(ctx);
//
//                view.setCompoundDrawablesWithIntrinsicBounds(
//                        null, ctx.getResources().getDrawable(R.drawable.ic_attachment_black_24dp), null, null);
//
//                String[] split = s.split("/");
//                String fileName = split[split.length - 1];
//                view.setTag(fileName);
//                view.setText(String.format(" %s ", Helpers.getFileExtension(fileName)));
//                view.setTextColor(Color.BLACK);
//                view.setOnClickListener(v -> {
//
//                    String tag = (String) view.getTag();
//                    Helpers.vibrate(ctx);
//                    Helpers.showImageOrPdf(ctx, ppr.wonum, tag);
//                });
//
//                frame.addView(view);
//            } catch (Exception ex) {
//                // I think it can be ignored here
//                ex.printStackTrace();
//            }
//        }
//    }

//    public static ApiTpsMobileMarket createMarketApi(Context ctx) {
//
//        String savedServerIp = PreferenceManager.getDefaultSharedPreferences(ctx)
//                .getString(Cv.PREFS_SERVER_IP_ADDRESS, "")
//                .split(":")[0];
//
//        String mainUrl = "".equals(savedServerIp)
//                ? Cv.MARKET_URL
//                : Cv.HTTP + savedServerIp + Cv.MARKET_PORT;
//
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient.Builder client = new OkHttpClient.Builder()
//                .readTimeout(Cv.READ_TIME_OUT_SEC, TimeUnit.SECONDS)
//                .writeTimeout(Cv.WRITE_TIME_OUT_SEC, TimeUnit.SECONDS);
//
//        if (BuildConfig.DEBUG) {
//            client.addNetworkInterceptor(interceptor);
//        }
//
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        return new Retrofit.Builder()
////                .baseUrl("http://172.22.116.164:8065/")
//                .baseUrl(mainUrl)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(client.build())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build()
//                .create(ApiTpsMobileMarket.class);
//    }

//    public static Bitmap decodeSampledBitmapFromResource(Context context,
//                                                         Uri uri, int reqWidth, int reqHeight) {
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        InputStream is = null;
//        try {
//            is = context.getContentResolver().openInputStream(uri);
//        } catch (FileNotFoundException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
//
//        if (bitmap != null) {
//
//            Timber.i("RAW BITMAP %d", bitmap.getByteCount());
//        }
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth,
//                reqHeight);
//
//        // Decode editBitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        InputStream inputs = null;
//        try {
//            inputs = context.getContentResolver().openInputStream(uri);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return BitmapFactory.decodeStream(inputs, null, options);
//    }
//
//    public static int calculateInSampleSize(BitmapFactory.Options options,
//                                            int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            // Calculate ratios of height and width to requested height and
//            // width
//            final int heightRatio = Math.round((float) height
//                    / (float) reqHeight);
//            final int widthRatio = Math.round((float) width / (float) reqWidth);
//
//            // Choose the smallest ratio as inSampleSize value, this will
//            // guarantee
//            // a final image with both dimensions larger than or equal to the
//            // requested height and width.
//            inSampleSize = Math.min(heightRatio, widthRatio);
//            // inSampleSize = heightRatio < widthRatio ? heightRatio :
//            // widthRatio;
//        }
//
//        return inSampleSize;
//    }


//    public static ApiMaximo createMaximoApiForAdminMaxidroid(Context ctx) {
//
//        String savedServerIpWithPort = PreferenceManager.getDefaultSharedPreferences(ctx)
//                .getString(Cv.PREFS_SERVER_IP_ADDRESS, "");
//
//        String mainUrl = "".equals(savedServerIpWithPort)
//                ? Cv.MAXIMO_MAIN_URL
//                : Cv.HTTP + savedServerIpWithPort + Cv.MAXIMO_OSLC;
//
//        Timber.i("Main url: %s", mainUrl);
//
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        CookieJar jar = new CookieJar() {
//
//            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
//
//            @Override
//            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//
//                cookieStore.put(url.host(), cookies);
//            }
//
//            @Override
//            public List<Cookie> loadForRequest(HttpUrl url) {
//
//                List<Cookie> cookies = cookieStore.get(url.host());
//
//                return null != cookies ? cookies : new ArrayList<>();
//            }
//        };
//
//        OkHttpClient.Builder client = new OkHttpClient.Builder();
//
//        client
//                .readTimeout(300, TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .addInterceptor( // 24.07.17 Andrey: This is basic auth
//                        new BasicAuthInterceptor(
//                                Cv.NFC_USER,
//                                Cv.NFC_PWD))
//                .cookieJar(jar);
//
//        if (BuildConfig.DEBUG) {
//            client.addNetworkInterceptor(interceptor);
//        }
//
//        return new Retrofit.Builder()
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(mainUrl)
//                .client(client.build())
//                .build()
//                .create(ApiMaximo.class);
//    }

//    public static String byteArrayToHexString(byte[] bytes) {
//
//        int i, j, in;
//
//        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
//                "A", "B", "C", "D", "E", "F"};
//
//        String out = "";
//
//        for (j = 0; j < bytes.length; j++) {
//
//            in = (int) bytes[j] & 0xff;
//            i = (in >> 4) & 0x0f;
//            out += hex[i];
//            i = in & 0x0f;
//            out += hex[i];
//        }
//        return out;
//    }

//    public static String getDateStringFromMaximoTimeFormat(String datestring) {
//
//        try {
//            final String ISO8601DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";
//
//            Calendar calendar = Calendar.getInstance();
//            SimpleDateFormat dateformat = new SimpleDateFormat(ISO8601DATEFORMAT, Locale.getDefault());
//
//            try {
//                Date date = dateformat.parse(datestring.substring(0, 19));
//                date.setHours(date.getHours() - 1);
//                calendar.setTime(date);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
//
//            return df.format(calendar.getTime());
//
//        } catch (Exception ex) {
//            // something went wrong then.....
//
//            ex.printStackTrace();
//            return "";
//        }
//    }


    /**
     *
     * @param ctx
     * @param responseBody
     * @param url
     * @param wonum
     * @return URI.toString of file that we downloaded
     */
//    public static String handleDownloadedFile(Context ctx, ResponseBody responseBody, String url, String wonum) {
//
//        String result = "";
//        String[] separated = url.split("/");
//        String fileName = separated[separated.length - 1];
//        String[] split = fileName.split(".");
//
//        InputStream is = null;
//        FileOutputStream fos = null;
//        try {
//            is = responseBody.byteStream();
//
//            File file = new File(Helpers.createAttachmentsDirectory(wonum), fileName);
//
//            fos = new FileOutputStream(
//                    file
//            );
//            int read;
//            byte[] buffer = new byte[32768];
//
//            while ((read = is.read(buffer)) > 0) {
//                fos.write(buffer, 0, read);
//            }
//
//            Uri uri;
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//
//                uri = FileProvider.getUriForFile(ctx, BuildConfig.APPLICATION_ID + ".fileprovider", file);
//
//            } else {
//
//                uri = Uri.fromFile(file);
//            }
//
//
//            result = uri.getPath();
//
//            Timber.i("File download Successful %s",result);
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        } finally {
//
//            if (null != fos) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (null != is) {
//                try {
//                    is.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return result;
//    }

//    public static boolean checkFileSize(RsDocAttachment rsDocAttachment) {
//
//        Timber.i("checkFileSize: %s",rsDocAttachment);
//
//        long filesize = Long.parseLong(rsDocAttachment.urlparam1);
//
//        boolean b = filesize < 1000000L && filesize >0;
//
//        Timber.i("checkFileSize result: %s",b);
//
//        return b;
//    }

//    public static Claim prepareClaim(Claim claim) {
//
//        long time = System.currentTimeMillis();
//
//        String imgPrefix = String.format(Locale.getDefault(), "%s_%d", claim.wonum, time);
//
//        int imgIndex = 0;
//
//        for (String path : claim.getPhotoLinks()) {
//
//            Timber.i("Path before uploading image %s", path);
//
//            String imgFileName = String.format(Locale.getDefault(), "%s_%d.jpg", imgPrefix, ++imgIndex);
//
//            claim.addPhotoName(imgFileName);
//
//            String description = null==claim.photoDescriptions.get(path)?"":claim.photoDescriptions.get(path);
//            claim.photoDescriptions.put(imgFileName, description);
//            claim.photoDescriptions.remove(path);
//            claim.photoData.put(path,imgFileName);
//
//        }
//
//        return claim;
//    }

//    public static WorkTaskPPR preparePpr(WorkTaskPPR ppr) {
//
//        long time = System.currentTimeMillis();
//
//        String imgPrefix = String.format(Locale.getDefault(), "%s_%d", ppr.wonum, time);
//
//        int imgIndex = 0;
//
//        for (String path : ppr.getPhotoLinks()) {
//
//            String imgFileName = String.format(Locale.getDefault(), "%s_%d.jpg", imgPrefix, ++imgIndex);
//
//            ppr.addPhotoName(imgFileName);
//
//            String description = null==ppr.photoDescriptions.get(path)?"":ppr.photoDescriptions.get(path);
//            ppr.photoDescriptions.put(imgFileName, description);
//            ppr.photoDescriptions.remove(path);
//            ppr.photoData.put(path,imgFileName);
//        }
//
//        return ppr;
//    }


//    public static WorkTaskPPR prepareSubPpr(WorkTaskPPR subPpr) {
//
//        Timber.i(">>>>>>>>>>>>>>>>SUB PPR: %s",subPpr);
//
//
//        long time = System.currentTimeMillis();
//
//        String imgPrefix = String.format(Locale.getDefault(), "%s_%d", subPpr.wonum, time);
//
//        int imgIndex = 0;
//
//        List<String> photoLinksAfter = subPpr.photoLinksAfter;
//        List<String> photoLinksBefore = subPpr.photoLinksBefore;
//
//
//        for (String linkBefore : photoLinksBefore) {
//
//            String imgFileName = String.format(Locale.getDefault(), "%s_%d.jpg", imgPrefix, ++imgIndex);
//            subPpr.photoFileNamesBefore.add(imgFileName);
//            subPpr.photoDescriptions.put(imgFileName, "before");
//            subPpr.photoData.put(linkBefore,imgFileName);
//        }
//
//        for (String linkAfter : photoLinksAfter) {
//
//            String imgFileName = String.format(Locale.getDefault(), "%s_%d.jpg", imgPrefix, ++imgIndex);
//            subPpr.photoFileNamesAfter.add(imgFileName);
//            subPpr.photoDescriptions.put(imgFileName, "after");
//            subPpr.photoData.put(linkAfter,imgFileName);
//        }
//
//        Timber.i("<<<<<<<<<<<<<<<<<<<<<<<<<<SUB PPR: %s",subPpr);
//        return subPpr;
//    }


//    public static MultipartBody.Part prepareBody(String path) {
//
//        File img = new File(path.split(":")[1]);
//
//        Timber.e(img.getAbsolutePath());
//
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse(Cv.MEDIA_TYPE_FORM_MULTIPART), img);
//
//        MultipartBody.Part body = MultipartBody.Part.createFormData(Cv.PARAM_NAME_FORM_DATA,
//                img.getName(), requestFile);
//
//        return body;
//    }

//    public static List<String> getLocationsFromCommaSeparatedString(String input) {
//
//        List<String> result = new ArrayList<>();
//
//        try {
//            String[] split = input.split(",");
//
//            for (String s : split) {
//
//                result.add(s.trim());
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return result;
//    }
}

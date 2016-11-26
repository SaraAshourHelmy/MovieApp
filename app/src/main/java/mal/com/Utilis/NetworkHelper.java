package mal.com.Utilis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sara on 11/19/2016.
 */
public class NetworkHelper {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static String FetchMovieData(String url_txt) {
        Log.e("url", url_txt);
        String result = "";
        try {
            URL url = new URL(url_txt);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream stream = connection.getInputStream();
            if (stream == null) {
                Log.e("stream", "null");
                result = null;
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = reader.readLine()) != null) {
                    //Log.e("line", reader.readLine());
                    stringBuffer.append(line + "\n");
                }

                result = stringBuffer.toString();
                Log.e("inside_res", stringBuffer + "");

                reader.close();

            }
            connection.disconnect();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("result", result);
        return result;
    }
}

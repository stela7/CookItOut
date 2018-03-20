package the_fangovvers.cookitout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by marek on 11/8/2017.
 */

public class Helper {

    public static final ArrayList<Integer> ICONS = new ArrayList<>(Arrays.asList(R.drawable.beef, R.drawable.chicken, R.drawable.desserts, R.drawable.drink, R.drawable.fish, R.drawable.glutenfree));


    /*private Helper instance;

    private Helper(){
    }

    public Helper getInstance(){
        if (instance == null) instance = new Helper();
        return instance;
    }*/

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public static boolean validateEmail(String string) {
        return string.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    }

    public static boolean validateLength(String string, int length) {
        if (length < 1) length = length * (-1);
        return string.length() >= length;

    }

    public static String getRequest(URL urlAddress) {
        String output = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) urlAddress.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            output = readFromIputStream(conn.getInputStream());
            conn.disconnect();

        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return output;
    }

    private static String readFromIputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String output;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                (inputStream)));

        while ((output = bufferedReader.readLine()) != null) {
            stringBuilder.append(output);
        }
        inputStream.close();
        return stringBuilder.toString();
    }
}

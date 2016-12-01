package com.example.stoycho.norfa.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.stoycho.norfa.utils.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by stoycho on 10/13/16.
 */

public class Task extends AsyncTask<Void,Void,String> {

    private Context context;
    private String url;
    private byte[] imageBytes;
    private String mComand;

    public Task(Context context, String url, byte[] imageBytes)
    {
        this.context = context;
        this.url = url;
        this.imageBytes = imageBytes;
    }

    public void setmComand(String comand)
    {
        this.mComand = comand;
    }

    @Override
    protected String doInBackground(Void... params) {

        String dataGetChanges = "";
        URL urlGetData = null;
        try {
            urlGetData = new URL(url);
            HttpURLConnection urlConnectionGetChanges = (HttpURLConnection) urlGetData.openConnection();
            if (imageBytes != null) {
                urlConnectionGetChanges.setRequestMethod("POST");
                urlConnectionGetChanges.setDoOutput(true);
                String boundary = "------WebKitFormBoundaryIC0sNEAW5yabLlEU\r\n";
                String lastBoundary = "------WebKitFormBoundaryIC0sNEAW5yabLlEU--\r\n";
                urlConnectionGetChanges.setRequestProperty("Content-Type","multipart/form-data; boundary=----WebKitFormBoundaryIC0sNEAW5yabLlEU");
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                String content = "Content-Disposition: form-data; name=\"pic_file\"; filename=\"sdsdsdsdaaaa.jpg\"\r\n";
                String secondContent = "Content-Disposition: form-data; name=\"cmd\"\r\n\r\n";

                outputStream.write(boundary.getBytes());
                outputStream.write(content.getBytes());
                outputStream.write("Content-Type: image/jpeg".getBytes());
                outputStream.write("\r\n\r\n".getBytes());
                outputStream.write(imageBytes);
                outputStream.write("\r\n".getBytes());
                outputStream.write(boundary.getBytes());
                outputStream.write(secondContent.getBytes());
                outputStream.write(mComand.getBytes());
                outputStream.write("\r\n".getBytes());
                if(mComand.equals("pic_edit") || mComand.equals("friend_add")) {
                    outputStream.write(boundary.getBytes());
                    outputStream.write("Content-Disposition: form-data; name=\"apikey\"".getBytes());
                    outputStream.write("\r\n\r\n".getBytes());
                    outputStream.write(SharedPref.getAppKey(context).getBytes());
                    outputStream.write("\r\n".getBytes());
                }
                outputStream.write(lastBoundary.getBytes());
                byte[] multiPartWithImage = outputStream.toByteArray();

                urlConnectionGetChanges.getOutputStream().write(multiPartWithImage);
            }
            BufferedReader bufferedReaderGetChanges = new BufferedReader(new InputStreamReader(urlConnectionGetChanges.getInputStream()));
            String nextGetChanges = bufferedReaderGetChanges.readLine();
            while (nextGetChanges != null) {
                dataGetChanges += nextGetChanges;
                nextGetChanges = bufferedReaderGetChanges.readLine();
            }
            urlConnectionGetChanges.disconnect();

           return  dataGetChanges;

        } catch (IOException e) {
           e.printStackTrace();
        }

        return null;
    }
}

package com.skyskew.komparify.smsbackup2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class FileUpload extends Activity {
    Bitmap bm;
    ///GzipCompressingEntity bm

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);
        try {
            // bm = BitmapFactory.decodeResource(getResources(),
            // R.drawable.forest);
            bm = BitmapFactory.decodeFile("filename.json");

            //executeMultipartPost();
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage());
        }
    }

    public void executeMultipartPost() throws Exception {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
           // bm.compress(, 75, bos);
            byte[] data = bos.toByteArray();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://10.0.2.2/cfc/iphoneWebservice.cfc?returnformat=json&amp;method=testUpload");
            ByteArrayBody bab = new ByteArrayBody(data, "filename.json.gz");
            // File file= new File("/mnt/sdcard/forest.png");
            // FileBody bin = new FileBody(file);
            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("uploaded", bab);
            reqEntity.addPart("boundary", new StringBody("---------"));
            postRequest.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();

            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            System.out.println("Response: " + s);
        } catch (Exception e) {
            // handle exception here
            Log.e(e.getClass().getName(), e.getMessage());
        }
    }
}
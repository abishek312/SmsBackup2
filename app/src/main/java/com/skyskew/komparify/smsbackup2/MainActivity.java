package com.skyskew.komparify.smsbackup2;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.provider.Telephony;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;


//import org.apache.http.HttpEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;




public class MainActivity extends ActionBarActivity {

    private String android_id;
    private String jsonSms;
    Calendar c;
    private File FileObj;
    private  String OUTPUT_GZIP_FILE=null;
    private  String SOURCE_FILE=null;
    File file;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////////////////Declarations and initializations
        OUTPUT_GZIP_FILE ="filename1.json.gz";
        SOURCE_FILE="filename.json";
                c = Calendar.getInstance();
        Date time = c.getTime();
        URL url;
        FileOutputStream fOut = null;
        FileOutputStream fOut2 = null;
        FileInputStream fIn;
        FileInputStream fIn2=null;
        InputStreamReader isr=null;
        List<Sms> resultSms=getAllSms(this);


        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        jsonSms="{ \n\"android_id\": "+"\""+android_id+"\""+
                "\n\"upload_time\": "+"\""+time+"\""+
                "\n\"messages\": [";

        try {
            String filename;
            Math j = null;
            Double ran = j.random();
            filename = android_id + Double.toString(ran);
           // fOut2=openFileOutput("filename1.json.gz", MODE_MULTI_PROCESS);
           BufferedWriter bufout= new BufferedWriter(new FileWriter(getFilesDir() +"filename1.json.gz"));
            fIn2=openFileInput("filename1.json.gz");
            fOut = openFileOutput("filename.json", MODE_MULTI_PROCESS);
            fIn = openFileInput("filename.json");
            isr = new InputStreamReader(fIn);
            file= new File(this.getFilesDir(), "filename1.json.gz");
        // url=new URL("filename.json.gz");


            for (int i = 0; i < resultSms.size(); i++) {
                Sms resultObj = new Sms();
                resultObj = resultSms.get(i);
               // SmsMessage message=messages[i];
                //String toAddress=message.getOriginatingAddress();
                //Log.i("toAddress",toAddress);
                //jsonSms+=toAddress;
                jsonSms = jsonSms + "\n\t\t{\"id\":" + "\"" + resultObj.getId() + "\"," +
                        "\n\t\t \"Address\":" + "\"" + resultObj.getAddress() + "\"," +
                        "\n\t\t \"Time\":" + "\"" + resultObj.getTime() + "\"," +
                        "\n\t\t \"Folder\":" + "\"" + resultObj.getFolderName() + "\"," +
                        "\n\t\t \"Readstate\":" + "\"" + resultObj.getReadState() + "\"," +
                        "\n\t\t \"Message\":" + "\"" + resultObj.getMsg() + "\"" + "\n}";
                if (i != resultSms.size() - 1) {
                    jsonSms = jsonSms + ",";

                }


            }
            jsonSms = jsonSms + "\n]\n}";
            //Log.i("message",jsonSms);
//            int con;

            fOut.write(jsonSms.getBytes());
            int charRead;
            char[] inputBuffer = new char[jsonSms.length()];
            //  isr.read(inputBuffer);
            String readString="";
            // Log.i("printedSms",readString);*/
            while (( charRead=isr.read(inputBuffer))>0) {
                // char to string conversion
                readString=String.copyValueOf(inputBuffer,0,charRead);
                readString+=readString;
            }
            Log.i("MYSUCCESS", readString);

        }
        catch(IOException e)
        {
            e.printStackTrace();
        } finally {
            if(fOut!=null)
            {
                try {
                    fOut.flush();
                    fOut.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(isr!=null)
            {
                try {

                    isr.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        gzipIt();

        /*for(SmsMessage message : Telephony.Sms.Intents.getMessagesFromIntent (getIntent())) {
            if (message == null) {
                Log.e("tonull", "message is null");
                break;
            }
            String smsOriginatingAddress = message.getDisplayOriginatingAddress();
            String smsDisplayMessage = message.getDisplayMessageBody();
            Log.i("toAddress",smsOriginatingAddress);
            Log.i("messgBody",smsDisplayMessage);
        }*/



      /*  zip("filename.txt", "/mnt/sdcard/MyZipFolder.zip");
        String urlStr="http://localhost/sms/process.json";
*/
     /* StringBuffer responseBody=new StringBuffer();

        Log.i("response", "Ready to upload file...");
        HttpClient client=new DefaultHttpClient();
        client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

        Log.i("response", "Set remote URL...");
        HttpPost post=new HttpPost("http://localhost/sms/process.json");
        MultipartEntity entity=new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        Log.i("response", "Adding file(s)...");
        entity.addPart("sms_file[sms]", new InputStreamBody(fIn2, "application/x-gzip"));
       // entity.addPart("uploadedfile2", new FileBody((FileObj), "application/zip"));

        Log.i("", "Set entity...");
        post.setEntity(entity);

        BufferedReader bs=null;
        try
        {
            Log.i("response", "Upload...");
            HttpEntity hEntity=client.execute(post).getEntity();
            bs=new BufferedReader(new InputStreamReader(hEntity.getContent()));
            Log.i("response", "Response length - "+hEntity.getContentLength());
            String s="";
            while(s!=null)
            {
                responseBody.append(s);
                s=bs.readLine();
                Log.i("response", "Response body - "+s);
            }
            bs.close();
        }
        catch(IOException ioe)
        {
            Log.i("response-error", "Error on getting response from Server, "+ioe.toString());
            ioe.printStackTrace();
            responseBody.append("...");
        }
*/



    }

    public void gzipIt(){

        byte[] buffer = new byte[1024];

        try{
            GZIPOutputStream gzos =
                    new GZIPOutputStream(new FileOutputStream(file));

            FileInputStream in =
                    new FileInputStream(SOURCE_FILE);

            int len;

            while ((len = in.read(buffer)) > 0) {
                gzos.write(buffer, 0, len);
                Log.i("gzipIt", String.valueOf(buffer));
            }

            in.close();

            gzos.finish();
            gzos.close();

            System.out.println("Done");

        }catch(IOException ex){
            ex.printStackTrace();
        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<Sms> getAllSms(Context mActivity) {
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = mActivity.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        // mActivity.startMana
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < 10; i++) {

                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                lstSms.add(objSms);
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();

        return lstSms;
    }


    public class Sms{
        private String _id;
        private String _address;
        private String _msg;
        private String _readState; //"0" for have not read sms and "1" for have read sms
        private String _time;
        private String _folderName;

        public String getId(){
            return _id;
        }
        public String getAddress(){
            return _address;
        }
        public String getMsg(){
            return _msg;
        }
        public String getReadState(){
            return _readState;
        }
        public String getTime(){
            return _time;
        }
        public String getFolderName(){
            return _folderName;
        }


        public void setId(String id){
            _id = id;
        }
        public void setAddress(String address){
            _address = address;
        }
        public void setMsg(String msg){
            _msg = msg;
        }
        public void setReadState(String readState){
            _readState = readState;
        }
        public void setTime(String time){
            _time = time;
        }
        public void setFolderName(String folderName){
            _folderName = folderName;
        }

    }
}

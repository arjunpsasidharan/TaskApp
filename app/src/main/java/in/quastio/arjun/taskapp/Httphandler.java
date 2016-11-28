package in.quastio.arjun.taskapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by arjun on 28-11-2016.
 */

public class Httphandler {
    String TAG=Httphandler.class.getSimpleName();
    public Httphandler(){

    }

    public String GetSteam(String requrl){
        String response=null;
        try{
            URL url=new URL(requrl);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream=new BufferedInputStream(connection.getInputStream());
            response=convertstreamtoString(inputStream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){

            e.printStackTrace();
        }


        return  response;
    }

    private String convertstreamtoString(InputStream inputStream) {

        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb= new StringBuilder();
        String line;
        try {
            while((line=reader.readLine())!=null){

                sb.append(line).append('\n');


            }

        }
        catch (IOException e){


            e.printStackTrace();


        }
        return String.valueOf(sb);
    }


}

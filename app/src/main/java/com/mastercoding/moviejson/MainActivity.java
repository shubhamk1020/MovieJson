package com.mastercoding.moviejson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private static String JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key=79faaaa4592ef5d4279c97d15316ccb0";

    List<MovieModelClass> movieList;
    RecyclerView recyclerView;
    ImageView header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        header = findViewById(R.id.imageView2);

        Glide.with(this)
                .load(R.drawable.header)
                .into(header);

        GetData getData = new GetData();
        getData.execute();

    }
    public class GetData extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            try {
                URL url;
                HttpsURLConnection urlConnection = null;

                try {
                      url = new URL(JSON_URL);

                      urlConnection = (HttpsURLConnection) url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();
                    while(data != -1){
                        current += (char) data;
                        data = isr.read();

                    }
                    return current;
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally{
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject  = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for(int i = 0; i<jsonArray.length();i++){

                    JSONObject jsonObject1  = jsonArray.getJSONObject(i);

                    MovieModelClass model = new MovieModelClass();

                    model.setId(jsonObject1.getString("vote_average"));
                    model.setName(jsonObject1.getString("title"));
                    model.setImg(jsonObject1.getString("poster_path"));

                    movieList.add(model);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        PutDataIntoRecyclerview(movieList);
        }


    }
    private void PutDataIntoRecyclerview(List<MovieModelClass> movieList){

        Adaptery adaptery = new Adaptery(this,movieList);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        recyclerView.setAdapter(adaptery);
        recyclerView.setHasFixedSize(true);

    }


}
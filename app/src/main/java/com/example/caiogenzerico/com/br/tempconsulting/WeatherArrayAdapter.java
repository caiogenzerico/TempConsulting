package com.example.caiogenzerico.com.br.tempconsulting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherArrayAdapter
        extends ArrayAdapter<Weather> {

    private Map<String, Bitmap> figuras =
            new HashMap<>();

    public WeatherArrayAdapter (Context context, List<Weather> previsoes){
        super (context, -1, previsoes);
    }
    private class ViewHolder{
        public ImageView conditionImageView;
        public TextView dayTextView;
        public TextView lowTextView;
        public TextView highTextView;
        public TextView humidityTextView;

    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null) {
            LayoutInflater inflater =
                    LayoutInflater.from(getContext());
            convertView = inflater.
                    inflate(R.layout.list_item, parent, false);
            vh = new ViewHolder();
            vh.dayTextView=
                    convertView.findViewById(R.id.dayTextView);
            vh.lowTextView=
                    convertView.findViewById(R.id.lowTextView);
            vh.highTextView=
                    convertView.findViewById(R.id.highTextView);
            vh.humidityTextView =
                    convertView.findViewById(R.id.humidityTextView);
            vh.conditionImageView
                    = convertView.findViewById(R.id.conditionImageView);
            convertView.setTag(vh);
        }
        else {
            //downcasting
            vh = (ViewHolder) convertView.getTag();
        }
        Weather caraDaVez = getItem(position);

        if (figuras.containsKey(caraDaVez.iconURL)){
            vh.conditionImageView.setImageBitmap(
                    figuras.get(caraDaVez.iconURL));
        }
        else{
            new LoadImageTask(vh.conditionImageView).
                    execute(caraDaVez.iconURL);
        }
        vh.dayTextView.setText(getContext().
                getString(R.string.day_description,
                        caraDaVez.dayOfWeek,
                        caraDaVez.description));
        vh.lowTextView.setText(getContext().
                getString(R.string.low_temp,
                        caraDaVez.minTemp));
        vh.highTextView.setText(getContext().
                getString(
                        R.string.high_temp,
                        caraDaVez.maxTemp));
        vh.humidityTextView.setText(getContext().
                getString(
                        R.string.humidity,
                        caraDaVez.humidity
                )
        );
        return convertView;
    }

    private class LoadImageTask extends
            AsyncTask<String, Void, Bitmap> {

        private ImageView view;
        public LoadImageTask (ImageView view){
            this.view = view;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String endereco = urls[0];
            try {
                URL url = new URL(endereco);
                HttpURLConnection connection =
                        (HttpURLConnection)url.openConnection();
                Bitmap figura = BitmapFactory.
                        decodeStream(connection.getInputStream());
                connection.disconnect();
                return figura;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            view.setImageBitmap(bitmap);
        }
    }
}
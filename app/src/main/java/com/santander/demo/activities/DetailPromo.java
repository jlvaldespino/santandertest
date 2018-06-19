package com.santander.demo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import com.santander.demo.R;

public class DetailPromo extends Activity {

    TextView tvTitle,tvDescription;
    ImageView ivImagenPromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        tvTitle= (TextView) findViewById(R.id.promotitle);
        tvDescription=(TextView) findViewById(R.id.promoDescription);
        ivImagenPromo=(ImageView) findViewById(R.id.promoImage);
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("url")!= null)
        {
            Picasso.with(this).load(bundle.getString("url")).into(ivImagenPromo);
        }

        if(bundle.getString("title")!= null)
        {
            tvTitle.setText(bundle.getString("title").toString());
        }
        if(bundle.getString("description")!= null)
        {
            tvDescription.setText(bundle.getString("description").toString());
        }
    }

}

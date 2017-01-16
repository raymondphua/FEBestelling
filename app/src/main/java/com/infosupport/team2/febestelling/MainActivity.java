package com.infosupport.team2.febestelling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.infosupport.team2.febestelling.resource.TestData;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_view);

        for (final String i: TestData.getStatus()) {

            Button button = new Button(this);
            button.setText(i);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), OrderListActivity.class);
                    intent.putExtra("status", i);
                    startActivity(intent);
                }
            });

            linearLayout.addView(button);

        }

    }
}

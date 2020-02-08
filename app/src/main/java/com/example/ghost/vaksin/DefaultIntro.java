package com.example.ghost.vaksin;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by ghost on 29/10/16.
 */

public final class DefaultIntro extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //layout slider
        addSlide(SimpleSlide.newInstance(R.layout.intro));
        addSlide(SimpleSlide.newInstance(R.layout.intro2));
        addSlide(SimpleSlide.newInstance(R.layout.intro3));
        addSlide(SimpleSlide.newInstance(R.layout.intro4));

        showStatusBar(true);
        setDepthAnimation();
    }

    @Override
    public void onDonePressed(Fragment currentFragment){
        super.onDonePressed(currentFragment);
        loadMainActivity();
    }

    private void loadMainActivity() {
        Intent intent =new Intent(this, Login.class);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment){
        super.onSkipPressed(currentFragment);
        loadMainActivity();
        Toast.makeText(getApplicationContext(), getString(R.string.skip), Toast.LENGTH_SHORT).show();
    }

    public void getStarted(View v){
        loadMainActivity();
    }

}

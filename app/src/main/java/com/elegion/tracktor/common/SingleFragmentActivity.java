package com.elegion.tracktor.common;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.elegion.tracktor.R;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_single_container);
        if (savedInstanceState == null) {
            changeFragment(getFragment());
        }
    }

    protected abstract Fragment getFragment();

    private void changeFragment(@NonNull Fragment fragment){
        boolean shouldAddToBackStack = getSupportFragmentManager().findFragmentById(R.id.container)!=null;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment);
        if (shouldAddToBackStack)
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

}

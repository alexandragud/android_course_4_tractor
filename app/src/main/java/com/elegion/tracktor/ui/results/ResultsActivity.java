package com.elegion.tracktor.ui.results;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.elegion.tracktor.common.SingleFragmentActivity;

public class ResultsActivity extends SingleFragmentActivity{
    public static final String RESULT_KEY = "RESULT_KEY";
    public static final long LIST_ID = -1L;

    public static void start(@NonNull Context context, long resultId) {
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(RESULT_KEY, resultId);
        context.startActivity(intent);
    }

    @Override
    protected Fragment getFragment() {
        long resultId = getIntent().getLongExtra(RESULT_KEY, LIST_ID);
        if (resultId!=LIST_ID)
            return ResultsDetailsFragment.newInstance(resultId);
        else
            return ResultsFragment.newInstance();
    }
}

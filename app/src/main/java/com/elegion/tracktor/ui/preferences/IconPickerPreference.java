package com.elegion.tracktor.ui.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import com.elegion.tracktor.R;

import java.util.ArrayList;
import java.util.List;

public class IconPickerPreference extends Preference implements IconPickerPreferenceAdapter.OnIconClickListener {

    private Context mContext;
    private final List<IconItem> items = new ArrayList<>();
    private int selectedItemSource;

    public IconPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.preference_icon_picker);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IconPickerPreference);
        int valuesResId = ta.getResourceId(R.styleable.IconPickerPreference_icon_entryValues, 0);
        if (valuesResId != 0) {
            String[] mEntryValues = context.getResources().getStringArray(valuesResId);
            for (String val : mEntryValues) {
                int resId = context.getResources().getIdentifier(val, "drawable", context.getPackageName());
                IconItem item = new IconItem(resId);
                items.add(item);
            }
        }
        ta.recycle();
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getResourceId(index, 0);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        if (isPersistent()) {
            selectedItemSource = getPersistedInt(0);
        } else {
            selectedItemSource = (Integer) defaultValue;
            persistInt(selectedItemSource);
        }
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        updateCheckedItems();
        RecyclerView iconsView = (RecyclerView) holder.findViewById(R.id.iconsView);
        LayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        iconsView.setLayoutManager(layoutManager);
        IconPickerPreferenceAdapter adapter = new IconPickerPreferenceAdapter(this);
        iconsView.setAdapter(adapter);
        adapter.submitList(items);
    }

    public void saveValue(int iconId) {
        selectedItemSource = iconId;
        persistInt(selectedItemSource);
        notifyChanged();
        callChangeListener(selectedItemSource);
    }

    private void updateCheckedItems() {
        for (IconItem item : items) {
            item.isChecked = item.resourceId == selectedItemSource;
        }
    }

    @Override
    public void onClick(int iconId) {
        saveValue(iconId);
    }

    static class IconItem {

        private Boolean isChecked;
        private int resourceId;

        public IconItem(int resourceId) {
            this.resourceId = resourceId;
        }

        public int getResourceId() {
            return resourceId;
        }

        public Boolean getChecked() {
            return isChecked;
        }
    }
}

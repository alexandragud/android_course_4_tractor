package com.elegion.tracktor.ui.preferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.elegion.tracktor.R;
import com.elegion.tracktor.ui.preferences.IconPickerPreference.IconItem;

import static com.elegion.tracktor.ui.preferences.IconPickerPreferenceAdapter.ViewHolder;

public class IconPickerPreferenceAdapter extends ListAdapter<IconItem, ViewHolder> {

    private OnIconClickListener mListener;

    private static final DiffUtil.ItemCallback<IconItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<IconItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull IconItem oldItem, @NonNull IconItem newItem) {
            return oldItem.getResourceId() == newItem.getResourceId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull IconItem oldItem, @NonNull IconItem newItem) {
            return oldItem.getChecked().equals(newItem.getChecked()) && oldItem.getResourceId() == newItem.getResourceId();
        }
    };

    public IconPickerPreferenceAdapter(OnIconClickListener listener) {
        super(DIFF_CALLBACK);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_item_picker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), mListener);
    }

    interface OnIconClickListener {
        void onClick(int iconId);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView iconImage;
        protected View iconPicker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImage);
            iconPicker = itemView.findViewById(R.id.iconPicker);

        }

        public void bind(IconItem item, OnIconClickListener listener) {
            setIconPickerSelected(item.getChecked());
            setIconImage(item.getResourceId());
            iconPicker.setOnClickListener(v -> listener.onClick(item.getResourceId()));
        }

        private void setIconPickerSelected(Boolean isChecked) {
            if (isChecked) {
                iconPicker.setBackgroundResource(R.drawable.ic_selected);
            } else {
                iconPicker.setBackgroundResource(R.drawable.ic_not_selected);
            }
        }

        private void setIconImage(int resourceId) {
            iconImage.setImageResource(resourceId);
        }
    }
}



package com.mprog.phone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A adapter for efficiently populating Recyclerviews with call information
 */
public class CallLogEntryAdapter extends RecyclerView.Adapter<CallLogEntryAdapter.ViewHolder> {
    private CallLogManager mCallLogEntries;
    private Context context;
    private AppCompatActivity activity;

    /**
     * Creates a new CallLogEntryAdapter instance
     */
    public CallLogEntryAdapter(CallLogManager manager, AppCompatActivity act) {
        mCallLogEntries = manager;
        activity = act;
    }

    /**
     * Defines how an item in the view is created
     * @param parent
     * @param viewType
     * @return
     */
    public CallLogEntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_call, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    /**
     * Populates an item in the list.
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(CallLogEntryAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        CallLogEntry entry = mCallLogEntries.getCallLogEntry(position);

        // Set item views based on data model of callrecords.
        TextView numberView = viewHolder.nameTextView;
        numberView.setText(entry.getNumber());
        TextView dateView = viewHolder.dateView;

        dateView.setText(entry.getTime());
        Button button = viewHolder.callButton;
        button.setText("Call");
    }

    /**
     * Returns the total count of items in the underlying data.
     */
    @Override
    public int getItemCount() {
        return mCallLogEntries.getCount();
    }

    /**
     * Defines a item in the recycle view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public Button callButton;
        public TextView dateView;

        /**
         * Creates a new ViewHolder instance.
         * @param itemView
         */
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = itemView.findViewById(R.id.caller_name);
            callButton = itemView.findViewById(R.id.call_button);
            dateView = itemView.findViewById(R.id.caller_time);

            callButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Call the clicked number.
                 * @param v
                 */
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MainActivity.REQUEST_CALL_PERMISSION);
                        return;
                    }

                    int position = getAdapterPosition(); // gets item position

                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        CallLogEntry call = mCallLogEntries.getCallLogEntry(position);

                        Intent intent = new Intent(Intent.ACTION_CALL);

                        intent.setData(Uri.parse("tel:" + call.getNumber()));
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}

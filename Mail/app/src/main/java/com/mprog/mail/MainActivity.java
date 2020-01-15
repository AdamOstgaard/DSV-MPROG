package com.mprog.mail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PICKFILE_RESULT_CODE = 1;
    private Uri attachment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Handle filepicker results. sets attachment if success.
     * @param requestCode Unique identifier for the request.
     * @param resultCode success code for the request.
     * @param data the data returned by the intent.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null || resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                attachment = data.getData();
        }
    }

    /**
     * Open file pickewr for attachments.
     * @param view
     */
    public void attachFIle (View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");

        try {
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        } catch (ActivityNotFoundException e) {
            Log.e("tag", "No activity can handle picking a file.");
        }
    }

    /**
     * Starts an email intent with thew entered information.
     * @param view sending button
     */
    public void sendMail(View view){
        Context context = getApplicationContext();
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setType("message/rfc822");

        // Get inputs
        TextView receiverView = findViewById(R.id.reciever);
        TextView subjectView = findViewById(R.id.subject);
        TextView messageView = findViewById(R.id.messageView);

        // Set intent extras
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {receiverView.getText().toString()}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subjectView.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, messageView.getText().toString());
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        // Attach file if one was selected
        if(attachment != null){
            try {
                Uri attachmentUri = createTempAttachmentUri(attachment);
                // emailIntent.setData(attachmentUri);
                emailIntent.putExtra(Intent.EXTRA_STREAM, attachmentUri);

                // Grant permission to open temp file
                List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    context.grantUriPermission(packageName, attachmentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }

        startActivity(emailIntent);
    }

    /**
     * Because file picker doesn't allow permission to "reshare" Uris we have to stoore the attachment temporary and create a new Uri to it.
     * @param source File to share.
     * @return A new Uri that can be shared.
     * @throws IOException thrown if the temp file can't be created.
     */
    public Uri createTempAttachmentUri(Uri source) throws IOException {
        File tempPath = new File(getFilesDir(), "temp");

        String name = source.getPath();
        int cut = name.lastIndexOf('/');

        File tempFile = new File(tempPath, name.substring(cut + 1));

        copy(source, tempFile);

        return FileProvider.getUriForFile(this, "com.mprog.fileprovider", tempFile);
    }

    /**
     * Copy a file
     * @param src source file
     * @param dst destination file
     * @throws IOException Thrown if the new file can't be created or the dource file can't be read.
     */
    public void copy(Uri src, File dst) throws IOException {
        try (InputStream in = getContentResolver().openInputStream(src)) {
            dst.getParentFile().mkdirs();
            dst.createNewFile();
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }
}

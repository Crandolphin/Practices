package com.crandall.best.practices.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crandall.best.practices.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Downloads a file from the supplied URL and stores the file in the app's designated file system.
 */
public class DownloadFileActivity extends ActionBarActivity implements View.OnClickListener {

    /**
     * The editText containing the URL of the file to be downloaded.
     */
    private EditText mEditTextUrl;

    /**
     * The progressDialog displayed while a file is downloading.
     */
    private ProgressDialog mProgressDialog;

    /**
     * The constant for the testUrl.
     */
    private final String TEST_URL = "http://math.hws.edu/eck/cs124/downloads/javanotes6-linked.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_file);

        mEditTextUrl = (EditText) findViewById(R.id.DownloadFile_editTextEnterUrl);
        Button buttonUseTestUrl = (Button) findViewById(R.id.DownloadFile_buttonUseTestUrl);
        Button buttonDownloadFile = (Button) findViewById(R.id.DownloadFile_buttonDownloadFile);
        Button buttonContinueToProducts = (Button) findViewById(R.id.DownloadFile_buttonContineToProducts);

        buttonUseTestUrl.setOnClickListener(this);
        buttonDownloadFile.setOnClickListener(this);
        buttonContinueToProducts.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.DownloadFileActivity_downloadingFile));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.DownloadFile_buttonUseTestUrl:
                mEditTextUrl.setText(TEST_URL);
                break;

            case R.id.DownloadFile_buttonDownloadFile:
                startFileDownload(mEditTextUrl.getText().toString());
                break;

            case R.id.DownloadFile_buttonContineToProducts:
                startActivity(new Intent(this, ProductsActivity.class));
                break;
        }
    }

    /**
     * Starts an async task with the provided URL to download a file.
     *
     * @param Url The URL of the file to be downloaded.
     */
    private void startFileDownload(String Url) {
        if (!Url.isEmpty()) {
            final FileDownloader fileDownloader = new FileDownloader(this);
            fileDownloader.execute(Url);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    fileDownloader.cancel(true);
                }
            });
        } else {
            Toast.makeText(this, R.string.DownloadFileActivity_enterAUrl, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Downloads a file from a given URL and stores the file in the app specific storage.
     */
    private class FileDownloader extends AsyncTask<String, Integer, String> {

        private Context context;

        public FileDownloader(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected String doInBackground(String... params) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return getResources().getString(R.string.DownloadFileActivity_serverError) + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                inputStream = connection.getInputStream();
                outputStream = new FileOutputStream(getFilesDir() + "/testFile");

                byte data[] = new byte[4096];
                long total = 0;
                int count;

                boolean isFileLengthKnown = fileLength > 0;

                if (isFileLengthKnown) {
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setMax(100);

                    while ((count = inputStream.read(data)) != -1) {
                        if (isCancelled()) {
                            closeIOConnections(inputStream, outputStream, connection);
                            return null;
                        }
                        total += count;

                        publishProgress((int) (total * 100 / fileLength));
                        outputStream.write(data, 0, count);
                    }

                } else {
                    while ((count = inputStream.read(data)) != -1) {
                        if (isCancelled()) {
                            closeIOConnections(inputStream, outputStream, connection);
                            return null;
                        }
                        total += count;

                        outputStream.write(data, 0, count);
                    }
                }
            } catch (MalformedURLException malformedUrl) {
                malformedUrl.printStackTrace();
            } catch (IOException io) {
                io.printStackTrace();
            }

            closeIOConnections(inputStream, outputStream, connection);
            return null;
        }

        /**
         * Closes each stream and disconnects the HttpURLConnection.
         */
        private void closeIOConnections(InputStream inputStream, OutputStream outputStream, HttpURLConnection connection) {
            try {
                if (connection != null) {
                    connection.disconnect();
                }
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException io) {
                io.printStackTrace();
            }
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            Toast.makeText(context, R.string.DownloadFileActivity_downloadCanceled, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (result != null) {
                Toast.makeText(context, getResources().getString(R.string.DownloadFileActivity_downloadError) + result, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, R.string.DownloadFileActivity_fileDownloaded, Toast.LENGTH_SHORT).show();
                mEditTextUrl.setText("");
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteFile("testFile");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}

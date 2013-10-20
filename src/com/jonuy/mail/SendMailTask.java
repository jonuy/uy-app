package com.jonuy.mail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.jonuy.uy_app.R;

import java.net.UnknownHostException;

/**
 * Task to send mail on a non-UI thread.
 */
public class SendMailTask extends AsyncTask<Void, Void, SendMailTask.MailResults> {
    private Context mContext;
    private ProgressDialog mProgressDialog;

    private String mSenderEmail;
    private String mSenderPass;

    private String mTo;
    private String mFrom;
    private String mSubject;
    private String mBody;

    protected enum MailResults {
        SUCCESS,
        FAILURE,
        NO_INTERNET
    }

    public SendMailTask(Context context, String senderEmail, String senderPass, String to, String from, String subject, String body) {
        mContext = context;
        mProgressDialog = null;

        mSenderEmail = senderEmail;
        mSenderPass = senderPass;

        mTo = to;
        mFrom = from;
        mSubject = subject;
        mBody = body;
    }

    protected MailResults doInBackground(Void... params) {
        try {
            UYMail uyMail = new UYMail(mSenderEmail, mSenderPass);
            if (uyMail.send(mTo, mFrom, mSubject, mBody)) {
                return MailResults.SUCCESS;
            }
        }
        catch (Exception e) {
            e.printStackTrace();

            if (e.getCause() != null && e.getCause() instanceof UnknownHostException) {
                return MailResults.NO_INTERNET;
            }
        }

        return MailResults.FAILURE;
    }

    protected void onPreExecute() {
        if (mContext != null) {
            mProgressDialog = ProgressDialog.show(
                    mContext,
                    mContext.getString(R.string.mail_progress_dialog_title),
                    mContext.getString(R.string.mail_progress_dialog_body),
                    true,
                    false);
        }
    }

    protected void onPostExecute(MailResults result) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        switch (result) {
            case SUCCESS:
                Toast.makeText(mContext, mContext.getString(R.string.mail_send_success), Toast.LENGTH_LONG).show();
                break;
            case NO_INTERNET:
                Toast.makeText(mContext, mContext.getString(R.string.mail_send_no_internet), Toast.LENGTH_LONG).show();
                break;
            case FAILURE:
                Toast.makeText(mContext, mContext.getString(R.string.mail_send_failure), Toast.LENGTH_LONG).show();
                break;
        }
    }
}

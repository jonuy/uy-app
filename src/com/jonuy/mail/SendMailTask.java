package com.jonuy.mail;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Task to send mail on a non-UI thread.
 */
public class SendMailTask extends AsyncTask<Void, Void, Boolean> {
    private Context mContext;

    private String mSenderEmail;
    private String mSenderPass;

    private String mTo;
    private String mFrom;
    private String mSubject;
    private String mBody;

    public SendMailTask(Context context, String senderEmail, String senderPass, String to, String from, String subject, String body) {
        mContext = context;

        mSenderEmail = senderEmail;
        mSenderPass = senderPass;

        mTo = to;
        mFrom = from;
        mSubject = subject;
        mBody = body;
    }

    protected Boolean doInBackground(Void... params) {
        try {
            UYMail uyMail = new UYMail(mSenderEmail, mSenderPass);
            if (uyMail.send(mTo, mFrom, mSubject, mBody)) {
                return Boolean.TRUE;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return Boolean.FALSE;
    }

    protected void onPostExecute(Boolean result) {
        if (result.booleanValue() == true) {
            Toast.makeText(mContext, "Mail sent successfully", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(mContext, "Mail send failed", Toast.LENGTH_LONG).show();
        }
    }
}

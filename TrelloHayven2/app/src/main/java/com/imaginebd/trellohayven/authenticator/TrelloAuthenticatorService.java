package com.imaginebd.trellohayven.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TrelloAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {

        TrelloAuthenticator authenticator = new TrelloAuthenticator(this);
        return authenticator.getIBinder();
    }
}

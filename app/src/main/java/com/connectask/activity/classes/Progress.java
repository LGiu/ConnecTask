package com.connectask.activity.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

public class Progress{

    public Context context;
    private ProgressDialog pDialog;

    private boolean home;

    public Progress(Context context, boolean home){
        this.context = context;
        this.home = home;
    }

    public void threard(final int tempoDeEspera) {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Por favor, aguarde...");
        pDialog.setCancelable(false);
        pDialog.show();

        final Handler handler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    SystemClock.sleep(tempoDeEspera);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                pDialog.dismiss();
                                if(home){
                                    Introducao introducao = new Introducao(context);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }).start();
    }

}

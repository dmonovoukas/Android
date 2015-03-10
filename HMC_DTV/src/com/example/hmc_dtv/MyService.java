package com.example.hmc_dtv;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyService extends Service
{
    protected AudioManager mAudioManager; 
    protected SpeechRecognizer mSpeechRecognizer;
    protected Intent mSpeechRecognizerIntent;
    protected final Messenger mServerMessenger = new Messenger( new IncomingHandler(this));
    
    private Messenger clientMessenger = null;
    
    protected boolean mIsListening;
    protected volatile boolean mIsCountDownOn;
    private boolean mIsStreamSolo;

    static final int MSG_RECOGNIZER_START_LISTENING = 1;
    static final int MSG_RECOGNIZER_CANCEL = 2;
    static final int MSG_REGISTER = 3;
    static final int MSG_RESPONSE = 4;
    static final int MSG_PHRASE = 5;
    
	protected static final String TAG = "yolo";

	private CountDownTimer mTimer = null;
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		//Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        Message msg = new Message();
        msg.what = MSG_RECOGNIZER_START_LISTENING;
        try {
			mServerMessenger.send(msg);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return startId;
       
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
        //mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        //mSpeechRecognizer.setRecognitionListener((RecognitionListener) new SpeechRecognitionListener());
        //mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        setupSpeechRecognizer();
        //Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();
        
    }
    
    public void setupSpeechRecognizer() {
    	mSpeechRecognizer = null;
    	mSpeechRecognizerIntent = null;
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener((RecognitionListener) new SpeechRecognitionListener());
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
    }
    
    protected class IncomingHandler extends Handler
    {
        //private final String TAG = "yolo";
		private WeakReference<MyService> mtarget;

        IncomingHandler(MyService target)
        {
            mtarget = new WeakReference<MyService>(target);
        }


        @Override
        public void handleMessage(Message msg)
        {
            final MyService target = mtarget.get();

            switch (msg.what)
            {
                case MSG_RECOGNIZER_START_LISTENING:

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    {
                        // turn off beep sound  
                        if (!mIsStreamSolo)
                        {
                            mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, true);
                            mIsStreamSolo = true;
                        }
                    }
                     if (!target.mIsListening)
                     {
                    	 target.setupSpeechRecognizer();
                         target.mSpeechRecognizer.startListening(target.mSpeechRecognizerIntent);
                         target.mIsListening = true;
                        Log.d(TAG, "message start listening"); //$NON-NLS-1$
                     }
                     break;

                 case MSG_RECOGNIZER_CANCEL:
                    if (mIsStreamSolo)
                   {
                        mAudioManager.setStreamSolo(AudioManager.STREAM_VOICE_CALL, false);
                        mIsStreamSolo = false;
                   }
                      target.mSpeechRecognizer.cancel();
                      target.mIsListening = false;
                      Log.d(TAG, "message canceled recognizer"); //$NON-NLS-1$
                      break;
                 case MSG_REGISTER:
     				/*
     				 * Do whatever we want with the client messenger: Messenger
     				 * clientMessenger = msg.replyTo
     				 */
                	 clientMessenger = msg.replyTo;
                	 Message msgResponse = Message.obtain(null, MSG_RESPONSE);
					try {
						clientMessenger.send(msgResponse);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
     				Toast.makeText(getApplicationContext(),
     						"Service : received client Messenger!",
     						Toast.LENGTH_SHORT).show();
     				break;
                 case MSG_PHRASE:
                	 //Toast.makeText(getApplicationContext(), "Word is: " + (String)msg.obj, Toast.LENGTH_SHORT).show();
                	 Message msgPhrase = Message.obtain(null, MSG_PHRASE, 0 , 0, (String)msg.obj);
					try {
						clientMessenger.send(msgPhrase);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	 break;
             }
       } 
    } 

    // Count down timer for Jelly Bean work around
    protected CountDownTimer mNoSpeechCountDown = new CountDownTimer(5000, 5000)
    {

        @Override
        public void onTick(long millisUntilFinished)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void onFinish()
        {
        	Log.d(TAG, "Countdown over");
            mIsCountDownOn = false;
            Message message = Message.obtain(null, MSG_RECOGNIZER_CANCEL);
            try
            {
                mServerMessenger.send(message);
                message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
                mServerMessenger.send(message);
            }
            catch (RemoteException e)
            {

            }
        }
    };

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (mIsCountDownOn)
        {
            mNoSpeechCountDown.cancel();
        }
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.destroy();
        }
        if (mTimer != null)
        	mTimer.cancel();
        
    }

    protected class SpeechRecognitionListener implements RecognitionListener
    {

        //private final String TAG = "yolo";

		@Override
        public void onBeginningOfSpeech()
        {
            // speech input will be processed, so there is no need for count down anymore
            if (mIsCountDownOn)
            {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }               
            //Log.d(TAG, "onBeginingOfSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {

        }

        @Override
        public void onEndOfSpeech()
        {
            //Log.d(TAG, "onEndOfSpeech"); //$NON-NLS-1$
         }

        @Override
        public void onError(int error)
        {
            if (mIsCountDownOn)
            {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }
             mIsListening = false;
             Message message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
             try
             {
                    mServerMessenger.send(message);
             }
             catch (RemoteException e)
             {

             }
            Log.d(TAG, "error = " + error); //$NON-NLS-1$
        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {

        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {

        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {
        	if(mTimer != null) {
                mTimer.cancel();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                mIsCountDownOn = true;
                mNoSpeechCountDown.start();

            }
				Toast.makeText(getApplicationContext(), "Service : Ready to listen", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onReadyForSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onResults(Bundle results)
        {
        	if(mTimer != null){
                mTimer.cancel();
            }
            String str = new String();
            //Log.d(TAG, "onResults " + results);
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            /*for (int i = 0; i < data.size(); i++)
            {
                      //Log.d(TAG, "result " + data.get(i));
                      str += data.get(i);
            }*/
            Log.d(TAG, "onResults: " + data.get(0)); //$NON-NLS-1$
            Message phrase = Message.obtain(null, MSG_PHRASE, 0 ,0,data.get(0) );
            
            try {
				mServerMessenger.send(phrase);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
            Log.d("Speech", "onResults: Start Listening");
            Message message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
            try {
				mServerMessenger.send(message);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            //Start a timer in case OnReadyForSpeech is never called back (Android Bug?)
            Log.d("Speech", "onResults: Start a timer");
            if(mTimer == null) {
                mTimer = new CountDownTimer(1000, 500) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        Log.d("Speech", "Timer.onFinish: Timer Finished, Restart recognizer");
                        Message message = Message.obtain(null, MSG_RECOGNIZER_CANCEL);
                        try {
							mServerMessenger.send(message);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
                        try {
							mServerMessenger.send(message);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                };
            }
            mTimer.start();
            
            /*Message message = Message.obtain(null, MSG_RECOGNIZER_CANCEL);
            try
            {
            	if ( mIsListening)
            		mServerMessenger.send(message);
                message = Message.obtain(null, MSG_RECOGNIZER_START_LISTENING);
                mServerMessenger.send(message);
            }
            catch (RemoteException e)
            {

            }*/
        }

        @Override
        public void onRmsChanged(float rmsdB)
        {

        }

    }

    @Override
	public IBinder onBind(Intent intent) {
		//Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
		return mServerMessenger.getBinder();
	}
}


package com.queueup.baidumap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.iflytek.speech.ErrorCode;
import com.iflytek.speech.ISpeechModule;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.RecognizerListener;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.SpeechRecognizer;
import com.queueup.ui.R;

/**
 * 这是将语音转换为文字的工具类
 * */
@SuppressLint("ShowToast")
public class TalkToolActivity extends Activity {
	public static TalkToolActivity activity;
	public static String TAG = "IatDemo";
	public static String talktext = "HELLO WORLD!";
	// 语音识别结果
	private RecognizerResult recognizerResult;
	// 语音识别对象。
	private SpeechRecognizer mIat;
	private Toast mToast;
	private static ImageView volume;
	private static final String ACTION_INPUT = "com.iflytek.speech.action.voiceinput";

	/** 外部设置的弹出框完成按钮文字 */
	public static final String TITLE_DONE = "title_done";
	/** 外部设置的弹出框取消按钮文字 */
	public static final String TITLE_CANCEL = "title_cancel";

	private SharedPreferences mSharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.voice_rcd_hint_window);
		volume = (ImageView) this.findViewById(R.id.volume);
		activity = this;

		/**
		 * 初期化监听器。
		 */
		InitListener mInitListener = new InitListener() {

			@Override
			public void onInit(ISpeechModule module, int code) {

				if (code == ErrorCode.SUCCESS) {

				}

				// 开始说话：
				int i = mIat.startListening(mRecognizerListener);
				System.out.println("i=" + i);
			}
		};

		mIat = new SpeechRecognizer(getApplicationContext(), mInitListener);
		init();
	}

	public void init() {

		mSharedPreferences = getSharedPreferences("com.iflytek.setting",
				Activity.MODE_PRIVATE);
		setParam();
	}

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	public void setParam() {

		mIat.setParameter(SpeechConstant.PARAMS, "10000");
		mIat.setParameter(SpeechConstant.LANGUAGE, mSharedPreferences
				.getString("iat_language_preference", "zh_cn"));
		mIat.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences.getString("iat_vadbos_preference", "4000"));
		// mIat.setParameter(SpeechConstant.ACCENT,
		// mSharedPreferences.getString("accent_preference", "mandarin"));
		// mIat.setParameter(SpeechConstant.DOMAIN,
		// mSharedPreferences.getString("domain_perference", "iat"));
		mIat.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences.getString("iat_vadeos_preference", "1000"));

		String param = null;
		param = "asr_ptt="
				+ mSharedPreferences.getString("iat_punc_preference", "1");
		mIat.setParameter(SpeechConstant.PARAMS, param
				+ ",asr_audio_path=/sdcard/iflytek/wavaudio.pcm");
	}

	/**
	 * 识别回调。
	 */
	RecognizerListener mRecognizerListener = new RecognizerListener.Stub() {

		@Override
		public void onVolumeChanged(final int v) throws RemoteException {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					updateDisplay(v);

				}
			});

		}

		@Override
		public void onResult(final RecognizerResult result, boolean isLast)
				throws RemoteException {

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (null != result) {

						// 将返回的json结果解析出来
						TextConstant.TALK_TEXT = JsonParser
								.parseIatResult(result.getResultString());
						System.out
								.println("-----TalkToolActivity:myTalk():解析出来的语音文本 ："
										+ TextConstant.TALK_TEXT);

						String string = LocationOverlayDemo.mMapEditText
								.getText().toString() + TextConstant.TALK_TEXT;
						
						//去掉字符串中的标点符号：
						String str="[\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff1f\u300a\u300b]";
						//中文标点符号的正则表达式
						String string2 = string.replaceAll(str, "");
						
						LocationOverlayDemo.mMapEditText.setText(string2);
						System.out.println("11111111111111111解析完毕");

					} else {

					}
				}
			});

		}

		@Override
		public void onError(int errorCode) throws RemoteException {

		}

		@Override
		public void onEndOfSpeech() throws RemoteException {

		}

		@Override
		public void onBeginOfSpeech() throws RemoteException {

		}
	};

	// 音量变化的监听：
	private void updateDisplay(int v) {

		switch (v) {
		case 0:
		case 1:
			System.out.println(""
					+ "volume.setBackgroundResource(R.drawable.amp1)");
			volume.setBackgroundResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			System.out.println(""
					+ "volume.setBackgroundResource(R.drawable.amp1)");
			volume.setBackgroundResource(R.drawable.amp2);

			break;
		case 4:
		case 5:
			System.out.println(""
					+ "volume.setBackgroundResource(R.drawable.amp2)");
			volume.setBackgroundResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			System.out.println(""
					+ "volume.setBackgroundResource(R.drawable.amp3)");
			volume.setBackgroundResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			System.out.println(""
					+ "volume.setBackgroundResource(R.drawable.amp4)");
			volume.setBackgroundResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			volume.setBackgroundResource(R.drawable.amp6);
			break;
		default:
			volume.setBackgroundResource(R.drawable.amp7);
			break;
		}
	}

	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		//停止录音：
		mIat.stopListening(mRecognizerListener);
		super.onStop();
	}
}

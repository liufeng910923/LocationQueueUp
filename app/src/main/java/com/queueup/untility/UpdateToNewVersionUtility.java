package com.queueup.untility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.queueup.conf.Domain;
import com.queueup.ui.WelcomeView;

public class UpdateToNewVersionUtility extends
		AsyncTask<String, String, Integer> {
	private String verCode;
	public ProgressDialog pBar;

	public UpdateToNewVersionUtility(String verCode) {
		this.verCode = verCode;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Integer doInBackground(String... params) {
		Integer ret = 1;
		try {

			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(Domain.get_download);
			get.addHeader("method", get.getMethod());
			HttpResponse response;
			try {
				response = client.execute(get);
				HttpEntity entity = response.getEntity();
				long length = entity.getContentLength();
				InputStream is = entity.getContent();
				FileOutputStream fileOutputStream = null;
				if (is != null) {
					ret = 2;
					File file = new File(
							Environment.getExternalStorageDirectory(),
							"/QueueClient.apk");
					if (file.exists()) {
						file.delete();
					}
					if (!file.exists()) {
						try {
							file.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					fileOutputStream = new FileOutputStream(file);

					byte[] buf = new byte[1024];
					int ch = -1;
					int count = 0;
					while ((ch = is.read(buf)) != -1) {
						fileOutputStream.write(buf, 0, ch);
						count += ch;
						if (length > 0) {
						}
					}
				}
				fileOutputStream.flush();
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
				if (is != null) {
					is.close();
				}

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(
						Uri.fromFile(new File(Environment
								.getExternalStorageDirectory(),
								"/QueueClient.apk")),
						"application/vnd.android.package-archive");
				WelcomeView.instance.startActivity(intent);

			}catch (IOException e) {
				e.printStackTrace();
				System.out.println("errormsg==>"+e.getMessage());
			}

		} catch (Exception ex) {
			System.out.println("errormsg==>"+ex.getMessage());
			ret = -1;
		}
		return ret;
	}

	protected void onPostExecute(Integer result) {
	}

}
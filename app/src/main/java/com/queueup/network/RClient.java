package com.queueup.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.queueup.ui.R;

public class RClient {

	private static final String CLASSTAG = RClient.class.getSimpleName();

	Exception mEx = null;
	final int mTimeoutConnection = 10000; // Connection TimeOut 10sec
	private ArrayList<NameValuePair> params;
	private ArrayList<NameValuePair> headers;
	private String url;
	private Context context;
	private int responseCode;
	private String message;
	private String response;

	public String getResponse() {
		return response;
	}

	public String getErrorMessage() {
		return message;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public RConstants getResult() {
		RConstants result = RConstants.ERROR_NONE;
		if (responseCode == 200 && !response.startsWith("[]")) {
			result = RConstants.OK;
		} else if (response.startsWith("[]")) {
			result = RConstants.NO_DATA;
		} else if (responseCode >= 400 && responseCode < 500) {
			if (responseCode == 401) {
				result = RConstants.Unauthorized;
			} else {
				result = RConstants.ERROR_CLIENT;
			}
		} else if (responseCode >= 500) {
			result = RConstants.ERROR_SERVER;
		} else {
			result = RConstants.ERROR_UNKOWN;
		}

		return result;
	}

	public RClient(Context context, String url) {
		this.url = url;
		this.context = context;
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
	}

	public void AddParam(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
	}

	public void AddHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}

	public void Execute(RMethod method) throws Exception {
		// Network State Checking //
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		Log.i("info", "ni-->>" + ni);
		boolean isAvail = ni.isAvailable();
		Log.i("info", "isAvail-->>" + isAvail);
		boolean isWifiConn = ni.isConnected();
		ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		ni.isAvailable();
		boolean isMobileConn = ni.isConnected();

		if (!isWifiConn && !isMobileConn) {
			// Wifi & 3G unavailable
			throw new Exception(context.getResources().getString(
					R.string.msg_no_network));
		}

		switch (method) {
		case GET: {
			// add parameters
			String combinedParams = "";
			if (!params.isEmpty()) {
				combinedParams += "?";
				for (NameValuePair p : params) {
					String paramString = p.getName() + "="
							+ URLEncoder.encode(p.getValue(), "UTF-8");
					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
			}

			HttpGet request = new HttpGet(url + combinedParams);

			// add headers
			for (NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}

			Log.i("CLASSTAG", CLASSTAG + url + "\nMethod:get, Param : "
					+ combinedParams);

			executeRequest(request, url);
			break;
		}
		case POST: {
			HttpPost request = new HttpPost(url);

			// add headers
			for (NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}

			if (!params.isEmpty()) {
				StringEntity entity = new StringEntity(
						params.get(0).getValue(), HTTP.UTF_8);
				entity.setContentType("application/json;charset=UTF-8");
				entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json;charset=UTF-8"));
				request.setEntity(entity);
			}

			Log.i("CLASSTAG", CLASSTAG + url + "\nMethod:post, Param : "
					+ params);

			executeRequest(request, url);
			break;
		}
		case DELETE: {
			// add parameters
			String combinedParams = "";
			if (!params.isEmpty()) {
				combinedParams += "?";
				for (NameValuePair p : params) {
					String paramString = p.getName() + "="
							+ URLEncoder.encode(p.getValue(), "UTF-8");
					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
			}

			HttpDelete request = new HttpDelete(url + combinedParams);

			// add headers
			for (NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}

			executeRequest(request, url);
			break;
		}
		}
	}

	private void executeRequest(HttpUriRequest request, String url)
			throws Exception {
		// request.addHeader("user-id", User.ID(context));
		request.addHeader("url", url);
		request.addHeader("method", request.getMethod());
		// request.addHeader("pda-no", AndroidUtils.getDeviceId(context));

		// HttpParam
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				mTimeoutConnection);

		// HttpClient
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);

		HttpResponse httpResponse;

		try {
			httpResponse = client.execute(request);

			// for (Cookie cookie : client.getCookieStore().getCookies()) {
			//
			// if(!cookie.getValue().equals("")){
			// //Log.i(Constants.LOGTAG, " " + CLASSTAG +
			// " Initial set of cookies: ");
			// SessionMng.setSession(cookie, context);
			// }
			// }

			responseCode = httpResponse.getStatusLine().getStatusCode();
			message = "[" + responseCode + "]"
					+ httpResponse.getStatusLine().getReasonPhrase();

			Log.i("CLASSTAG", "===== Http Request =====");
			Log.i("CLASSTAG", "URL : " + request.getURI());
			Log.i("CLASSTAG", "Method : " + request.getMethod());
			Log.i("CLASSTAG", "Result: " + message);

			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {

				InputStream instream = entity.getContent();
				response = convertStreamToString(instream);

				// Closing the input stream will trigger connection release
				instream.close();
			} else {
				Log.i(CLASSTAG, "XXXX Http Response XXXX");
			}

		} catch (ConnectTimeoutException e) {
			client.getConnectionManager().shutdown();

			Log.e(CLASSTAG,
					" ConnectTimeoutException : " + e.getLocalizedMessage());

			throw new Exception(context.getResources().getString(
					R.string.msg_no_network));

		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();

			Log.e(CLASSTAG,
					" ClientProtocolException : " + e.getLocalizedMessage());

			throw new Exception(e.getMessage());

		} catch (IOException e) {
			client.getConnectionManager().shutdown();

			Log.e(CLASSTAG, " IOException : " + e.getLocalizedMessage());

			throw new Exception(e.getMessage());
		}
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}

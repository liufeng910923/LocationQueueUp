/*     */ package com.queueup.network;
/*     */ 
/*     */ import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Proxy;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
/*     */ 
/*     */public class AndroidUtils
/*     */ {
/*     */   public static int dipToPixel(Context ctx, float dip)
/*     */   {
/*  56 */     float scale = ctx.getResources().getDisplayMetrics().density;
/*     */ 
/*  58 */     int mGestureThreshold = (int)(dip * scale + 0.5F);
/*  59 */     return mGestureThreshold;
/*     */   }
/*     */ 
/*     */   public static String getPhoneNumber(Context ctx)
/*     */   {
/*  64 */     TelephonyManager telephony = (TelephonyManager)ctx.getSystemService("phone");
/*  65 */     return telephony != null ? telephony.getLine1Number() : "01000000000";
/*     */   }
/*     */ 
/*     */   public static String getDeviceId(Context ctx)
/*     */   {
/*  70 */     TelephonyManager telephony = (TelephonyManager)ctx.getSystemService("phone");
/*  71 */     return telephony != null ? telephony.getDeviceId() : "";
/*     */   }
/*     */ 
/*     */   public static void setSharedPreferences(Context ctx, String groupkey, String key, String value)
/*     */   {
/*  76 */     SharedPreferences pref = ctx.getSharedPreferences(groupkey, 0);
/*  77 */     SharedPreferences.Editor editor = pref.edit();
/*  78 */     editor.putString(key, value);
/*  79 */     editor.commit();
/*     */   }
/*     */ 
/*     */   public static void setLoginInfo(Context ctx, String groupkey, HashMap<String, Object> info)
/*     */   {
/*  84 */     SharedPreferences pref = ctx.getSharedPreferences(groupkey, 0);
/*  85 */     SharedPreferences.Editor editor = pref.edit();
/*     */ 
/*  87 */     for (int j = 0; j < info.size(); j++)
/*     */     {
/*  89 */       editor.putString((String)info.keySet().toArray()[j], (String)info.get(info.keySet().toArray()[j]));
/*     */     }
/*  91 */     editor.commit();
/*     */   }
/*     */ 
/*     */   public static String getSharedPreferences(Context ctx, String groupkey, String key)
/*     */   {
/*  96 */     SharedPreferences pref = ctx.getSharedPreferences(groupkey, 0);
/*  97 */     return pref.getString(key, "");
/*     */   }
/*     */ 
/*     */   public static String getSharedPreferences(Context ctx, String groupkey, String key, String defaultValue)
/*     */   {
/* 102 */     SharedPreferences pref = ctx.getSharedPreferences(groupkey, 0);
/* 103 */     return pref.getString(key, defaultValue);
/*     */   }
/*     */ 
/*     */   public static void setKeyboardVisible(Context ctx, EditText et, boolean visible)
/*     */   {
/* 108 */     InputMethodManager inputManager = (InputMethodManager)ctx.getSystemService("input_method");
/* 109 */     if (visible)
/*     */     {
/* 111 */       inputManager.showSoftInput(et, 2);
/*     */     }
/*     */     else
/*     */     {
/* 115 */       inputManager.hideSoftInputFromWindow(et.getWindowToken(), 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void updateProgram(Context ctx, String apkUrl, String savePath)
/*     */   {
/* 121 */     File f = null;
/*     */     try
/*     */     {
/* 124 */       HttpParams httpParams = new BasicHttpParams();
/* 125 */       HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
/* 126 */       HttpConnectionParams.setSoTimeout(httpParams, 10000);
/* 127 */       HttpGet httpRequest = null;
/* 128 */       httpRequest = new HttpGet(new URI(apkUrl));
/* 129 */       HttpClient httpclient = new DefaultHttpClient(httpParams);
/*     */ 
/* 131 */       HttpResponse response = httpclient.execute(httpRequest);
/*     */ 
/* 133 */       HttpEntity entity = response.getEntity();
/* 134 */       InputStream instream = entity.getContent();
/* 135 */       f = new File(savePath);
/* 136 */       f.createNewFile();
/*     */ 
/* 138 */       OutputStream out = new FileOutputStream(f);
/* 139 */       int c = 0;
/* 140 */       while ((c = instream.read()) != -1)
/*     */       {
/* 142 */         out.write(c);
/*     */       }
/* 144 */       out.flush();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/* 149 */     Intent intent = new Intent("android.intent.action.VIEW");
/* 150 */     String mimetype = "application/vnd.android.package-archive";
/* 151 */     intent.setDataAndType(Uri.fromFile(f), mimetype);
/* 152 */     ctx.startActivity(intent);
/*     */   }
/*     */ 
/*     */   public static void updateProgram(Context ctx, String apkUrl, String savePath, ProgressBar proBar, ProgressDialog proDlg)
/*     */   {
/* 157 */     File f = null;
/*     */     try
/*     */     {
/* 160 */       HttpParams httpParams = new BasicHttpParams();
/* 161 */       HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
/* 162 */       HttpConnectionParams.setSoTimeout(httpParams, 10000);
/* 163 */       HttpGet httpRequest = null;
/* 164 */       httpRequest = new HttpGet(new URI(apkUrl));
/* 165 */       HttpClient httpclient = new DefaultHttpClient(httpParams);
/*     */ 
/* 167 */       HttpResponse response = httpclient.execute(httpRequest);
/*     */ 
/* 169 */       HttpEntity entity = response.getEntity();
/* 170 */       InputStream instream = entity.getContent();
/* 171 */       f = new File(savePath);
/* 172 */       f.createNewFile();
/*     */ 
/* 174 */       URL url = new URL(apkUrl);
/* 175 */       URLConnection con = url.openConnection();
/*     */ 
/* 177 */       int orgFileSize = con.getContentLength();
/*     */ 
/* 179 */       OutputStream out = new FileOutputStream(f);
/* 180 */       int c = 0;
/* 181 */       while ((c = instream.read()) != -1)
/*     */       {
/* 183 */         int fileSize = (int)f.length();
/* 184 */         int cnt = fileSize * 80 / orgFileSize;
/* 185 */         if ((cnt > 15) && (fileSize % 1000 == 0))
/*     */         {
/* 187 */           proBar.setProgress(cnt);
/*     */         }
/* 189 */         out.write(c);
/*     */       }
/* 191 */       out.flush();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/* 196 */     proBar.setProgress(100);
/* 197 */     Intent intent = new Intent("android.intent.action.VIEW");
/* 198 */     String mimetype = "application/vnd.android.package-archive";
/* 199 */     intent.setDataAndType(Uri.fromFile(f), mimetype);
/* 200 */     ctx.startActivity(intent);
/* 201 */     ((Activity)ctx).finish();
/*     */   }
/*     */ 
/*     */   public static void install(Context context, String url, String fileName) throws ClientProtocolException, IOException, FileNotFoundException
/*     */   {
/* 206 */     HttpParams httpParams = new BasicHttpParams();
/* 207 */     HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
/* 208 */     HttpConnectionParams.setSoTimeout(httpParams, 10000);
/*     */ 
/* 210 */     DefaultHttpClient client = new DefaultHttpClient();
/* 211 */     client.setParams(httpParams);
/*     */ 
/* 213 */     FileOutputStream fos = context.openFileOutput(fileName, 1);
/*     */ 
/* 215 */     client.execute(new HttpGet(url + fileName)).getEntity().writeTo(fos);
/*     */ 
/* 217 */     File file = context.getFileStreamPath(fileName);
/*     */ 
/* 219 */     if (file.length() < 10000L)
/*     */     {
/* 221 */       throw new FileNotFoundException("Check Your File");
/*     */     }
/*     */ 
/* 224 */     Intent intent = new Intent("android.intent.action.VIEW");
/* 225 */     String mimetype = "application/vnd.android.package-archive";
/* 226 */     intent.setDataAndType(Uri.fromFile(file), mimetype);
/*     */ 
/* 228 */     context.startActivity(intent);
/* 229 */     ((Activity)context).finish();
/*     */   }
/*     */ 
/*     */   public static String getVersionInfo(Context ctx)
/*     */   {
/*     */     try
/*     */     {
/* 236 */       ComponentName name = new ComponentName(ctx, ctx.getClass());
/* 237 */       PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(name.getPackageName(), 0);
/* 238 */       return pInfo.versionName;
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/* 243 */     return "0";
/*     */   }
/*     */ 
/*     */   public static void makeCall(Context ctx, String telNum)
/*     */   {
/* 248 */     Intent intent = new Intent("android.intent.action.CALL", Uri.fromParts("tel", telNum, null));
/* 249 */     ctx.startActivity(intent);
/*     */   }
/*     */ 
/*     */   public static String getFileInstallDate(String path)
/*     */   {
/* 254 */     String result = "";
/*     */     try
/*     */     {
/* 257 */       File f = new File(path);
/* 258 */       Date d = new Date(f.lastModified());
/* 259 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
/* 260 */       result = sdf.format(d);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 264 */       e.printStackTrace();
/*     */     }
/* 266 */     return result;
/*     */   }
/*     */ 
/*     */   public static String connectionCheck(Context ct)
/*     */   {
/* 271 */     ConnectivityManager cm = (ConnectivityManager)ct.getSystemService("connectivity");
/*     */ 
/* 273 */     if (cm.getNetworkInfo(1).isConnected())
/*     */     {
/* 275 */       return "1";
/*     */     }
/*     */ 
/* 278 */     if (cm.getNetworkInfo(0).isConnected())
/*     */     {
/* 280 */       return "2";
/*     */     }
/* 282 */     return "3";
/*     */   }
/*     */ 
/*     */   public static void gotoURL(Context ctx, String url)
/*     */   {
/* 287 */     Uri uri = Uri.parse(url);
/* 288 */     Intent intent = new Intent("android.intent.action.VIEW", uri);
/* 289 */     ctx.startActivity(intent);
/*     */   }
/*     */ 
/*     */   /*public static int metersToRadius(float meters, MapView map, double latitude)
        {
 294      return (int)(map.getProjection().metersToEquatorPixels(meters) * (1.0D / Math.cos(Math.toRadians(latitude))));
        }*/
/*     */ 
/*     */   public static Bitmap getBitmap(Context context, String url)
/*     */   {
/* 299 */     Bitmap bitmap = null;
/*     */     try
/*     */     {
/* 302 */       HttpParams httpParams = new BasicHttpParams();
/* 303 */       HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
/* 304 */       HttpConnectionParams.setSoTimeout(httpParams, 10000);
/*     */ 
/* 306 */       HttpClient httpClient = new DefaultHttpClient(httpParams);
/* 307 */       if (Proxy.getHost(context) != null)
/*     */       {
/* 309 */         HttpHost httpHost = new HttpHost(Proxy.getHost(context), Proxy.getPort(context), "http");
/* 310 */         httpClient.getParams().setParameter("http.route.default-proxy", httpHost);
/*     */       }
/*     */ 
/* 313 */       StringBuffer sb = new StringBuffer();
/* 314 */       sb.append(url);
/*     */ 
/* 316 */       URI u = new URI(url.toString());
/* 317 */       HttpGet httpRequest = new HttpGet(u);
/* 318 */       httpRequest.addHeader("Connection", "Keep-Alive");
/* 319 */       HttpResponse httpResponse = httpClient.execute(httpRequest);
/* 320 */       int _status = httpResponse.getStatusLine().getStatusCode();
/* 321 */       if (200 == _status)
/*     */       {
/* 323 */         HttpEntity httpEntity = httpResponse.getEntity();
/* 324 */         InputStream inputStream = httpEntity.getContent();
/*     */ 
/* 326 */         FlushedInputStream flushedInputStream = new FlushedInputStream(inputStream);
/* 327 */         bitmap = BitmapFactory.decodeStream(flushedInputStream);
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/*     */     }
/* 333 */     return bitmap;
/*     */   }
/*     */ 
/*     */   public static void viewToastMessage(Context ctc, String message)
/*     */   {
/* 338 */     Toast.makeText(ctc, message, 0).show();
/*     */   }
/*     */ 
/*     */   public static void initSpinner(Context ctx, ArrayList arr, Spinner spn)
/*     */   {
/* 343 */     if (arr.size() == 0)
/*     */     {
/* 345 */       return;
/*     */     }
/* 347 */     spn.setPrompt("선택");
/* 348 */     ArrayAdapter adapter = new ArrayAdapter(ctx, 17367048);
/* 349 */     adapter.setDropDownViewResource(17367049);
/* 350 */     for (int i = 0; i < arr.size(); i++)
/*     */     {
/* 352 */       CodeDto info = (CodeDto)arr.get(i);
/* 353 */       adapter.add(info);
/*     */     }
/* 355 */     spn.setAdapter(adapter);
/*     */   }
/*     */ 
/*     */   public static void initSpinner_arrays(Context ctx, int textArr, Spinner spn)
/*     */   {
/* 360 */     spn.setPrompt("선택");
/* 361 */     ArrayAdapter adapter = ArrayAdapter.createFromResource(ctx, textArr, 17367048);
/* 362 */     adapter.setDropDownViewResource(17367049);
/* 363 */     spn.setAdapter(adapter);
/*     */   }
/*     */ 
/*     */   public static void initSpinner_empty(Context ctx, ArrayList arr, Spinner spn)
/*     */   {
/* 368 */     if (arr.size() == 0)
/*     */     {
/* 370 */       return;
/*     */     }
/* 372 */     CodeDto tmp = new CodeDto();
/* 373 */     tmp.setName("");
/* 374 */     tmp.setValue("");
/*     */ 
/* 376 */     spn.setPrompt("선택");
/* 377 */     ArrayAdapter adapter = new ArrayAdapter(ctx, 17367048);
/* 378 */     adapter.setDropDownViewResource(17367049);
/* 379 */     adapter.add(tmp);
/*     */ 
/* 381 */     for (int i = 0; i < arr.size(); i++)
/*     */     {
/* 383 */       CodeDto info = (CodeDto)arr.get(i);
/* 384 */       adapter.add(info);
/*     */     }
/* 386 */     spn.setAdapter(adapter);
/*     */   }
/*     */ 
/*     */   public static String getSpinnerEtc(Spinner spn)
/*     */   {
/* 391 */     String result = "";
/*     */     try
/*     */     {
/* 394 */       CodeDto cd = (CodeDto)spn.getSelectedItem();
/* 395 */       result = cd.getETC();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/* 400 */     return result;
/*     */   }
/*     */ 
/*     */   public static String getSpinnerValue(Spinner spn)
/*     */   {
/* 405 */     String result = "";
/*     */     try
/*     */     {
/* 408 */       CodeDto cd = (CodeDto)spn.getSelectedItem();
/* 409 */       result = cd.getValue();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/* 414 */     return result;
/*     */   }
/*     */ 
/*     */   public static String getSpinnerName(Spinner spn)
/*     */   {
/* 419 */     String result = "";
/*     */     try
/*     */     {
/* 422 */       result = spn.getSelectedItem().toString();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/* 427 */     return result;
/*     */   }
/*     */ 
/*     */   public static void setSpinnerValue(Spinner spn, String value)
/*     */   {
/* 432 */     if (value.length() == 0)
/*     */     {
/* 434 */       return;
/*     */     }
/*     */ 
/* 437 */     for (int i = 0; i < spn.getCount(); i++)
/*     */     {
/* 439 */       CodeDto info = (CodeDto)spn.getItemAtPosition(i);
/* 440 */       if (value.equals(info.getValue()))
/*     */       {
/* 442 */         spn.setSelection(i);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void setSpinnerName(Spinner spn, String name)
/*     */   {
/* 449 */     if (name.length() == 0)
/*     */     {
/* 451 */       return;
/*     */     }
/*     */ 
/* 454 */     for (int i = 0; i < spn.getCount(); i++)
/*     */     {
/* 456 */       CodeDto info = (CodeDto)spn.getItemAtPosition(i);
/* 457 */       if (name.equals(info.getName()))
/*     */       {
/* 459 */         spn.setSelection(i);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void setSpinnerValue_arrays(Spinner spn, String value)
/*     */   {
/* 466 */     if (value.length() == 0)
/*     */     {
/* 468 */       return;
/*     */     }
/* 470 */     for (int i = 0; i < spn.getCount(); i++)
/*     */     {
/* 472 */       if (value.equals(spn.getItemAtPosition(i).toString()))
/*     */       {
/* 474 */         spn.setSelection(i);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void setSpinnerValue_arrays(Context ctx, Spinner spn, int res, String value)
/*     */   {
/* 481 */     String[] arr = ctx.getResources().getStringArray(res);
/*     */ 
/* 483 */     if (value.length() == 0)
/*     */     {
/* 485 */       return;
/*     */     }
/* 487 */     int i = 0;
/* 488 */     for (int icnt = arr.length; i < icnt; i++)
/*     */     {
/* 490 */       if (value.equals(arr[i]))
/*     */       {
/* 492 */         spn.setSelection(i);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\new\temp\equalcomponent-Release1.0.jar
 * Qualified Name:     com.equal.logicomponent.AndroidUtils
 * JD-Core Version:    0.6.2
 */
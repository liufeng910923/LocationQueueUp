package com.queueup.untility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

/*将assets文件夹下的数据库写入SD卡中
 * @author Dave */
public class AssetsWriteToSD {
	private static String filePath = android.os.Environment.getExternalStorageDirectory() + "/.paidui";

	public static String write(Context context) {

		if (isExist()) {
			return "";
		} else {

			InputStream inputStream;
			try {
				inputStream = context.getResources().getAssets().open("map.jpg");
				File file = new File(filePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/map.jpg");
				byte[] buffer = new byte[512];
				int count = 0;
				while ((count = inputStream.read(buffer)) > 0) {
					fileOutputStream.write(buffer, 0, count);
				}
				fileOutputStream.flush();
				fileOutputStream.close();
				inputStream.close();
				System.out.println("成功：" + filePath + "/map.jpg");

				return filePath + "/map.jpg";

			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("失败：" + filePath + "/map.jpg");
			return "";

		}

	}

	public static boolean isExist() {
		File file = new File(filePath + "/map.jpg");
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}
}

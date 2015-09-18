package com.example.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 归属地查询工具
 * 
 * @author yu_longji
 * 
 */
public class AddressDao {

	// 注意,该路径必须是data/data目录的文件,否则数据库访问不到
	private static final String PATH = "data/data/com.example.mobilesafe/files/address.db";

	public static String getAddress(String number) {

		String address = "未知号码";
		// 获取数据库对象
		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null,
				SQLiteDatabase.OPEN_READONLY);
		// 手机号码特点: 1 + (3,4,5,6,7,8) + (9位数字)
		// 正则表达式
		// ^1[3-8]\d{9}$
		// 匹配手机号码
		if (number.matches("^1[3-8]\\d{9}$")) {
			// 先从data1表中查找id 之后再在data2表中查找 location
			String sqlString = "select location from data2 where id=(select outkey from data1 where id=?)";
			Cursor cursor = database.rawQuery(sqlString,
					new String[] { number.substring(0, 7) });

			if (cursor.moveToNext()) {
				address = cursor.getString(0);
			}
			// 将cursor关闭
			cursor.close();

		} // 匹配数字
		else if (number.matches("^\\d+$")) {
			switch (number.length()) {
			case 3:
				address = "报警电话";
				break;
			case 4:
				address = "模拟器";
				break;
			case 5:
				address = "客服电话";
				break;
			case 7:
			case 8:
				address = "本地电话";
				break;
			default:
				// 01088881234
				// 048388888888
				// 有可能是长途电话
				if (number.startsWith("0") && number.length() > 10) {
					// 有些区号是4位,有些区号是3位(包括0)

					// 先查询4位区号
					String sqlString = "select location from data2 where area =?";
					Cursor cursor = database.rawQuery(sqlString,
							new String[] { number.substring(1, 4) });
					if (cursor.moveToNext()) {
						address = cursor.getString(0);
					} else {

						cursor.close();

						// 查询3位区号
						String sql = "select location from data2 where area = ?";
						cursor = database.rawQuery(sql,
								new String[] { number.substring(1, 3) });
						if (cursor.moveToNext()) {
							address = cursor.getString(0);
						}
						cursor.close();
					}
				}
				break;
			}
		}
		// 关闭数据库
		database.close();

		return address;

	}
}

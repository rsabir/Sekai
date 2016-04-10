package utils;

import constants.Urls;
import database.controller.DBManager;

public class TestDBManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

        DBManager dbManager=new DBManager("server");
        dbManager.getClientData("BD:E5:F7:43:21:11");
        //dbManager.addData("AA:AA:AA:AA:22:99", 44.710f, -0.5700f);
     // System.out.println(dbManager.getClientData("AA:99:AA:AA:22:99"));
        //System.out.println(dbManager.getAllData());
     //   dbManager.getClientDataToday("AA:AA:AA:AA:22:99");

	}

}

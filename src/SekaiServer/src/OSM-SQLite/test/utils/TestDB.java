package utils;

import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestDB {
	
	@Test
 public void testVerifyMacAddr(){
	 ArrayList<String> listMAC= new ArrayList<String>();
	 listMAC.add("  3 Julien  26:4E:96:55:DD:AB"); //no
	 listMAC.add("26:4E:96:55:DD:AB"); //yes
	 listMAC.add("26-4E-96-55-DD-AB"); //no
	 listMAC.add("12:-GH"); //no
	 listMAC.add("26:4e:96:55:ee:ab"); //yes
	 listMAC.add(" 26:4E:96:55:DD:AB \n"); //no
	 listMAC.add("26:4E:96:55:DD:AB:DD"); //no
	 assert(DB.verifyMacAddr(listMAC.get(0))==false);
	 assert(DB.verifyMacAddr(listMAC.get(1))==true);
	 assert(DB.verifyMacAddr(listMAC.get(2))==false);
	 assert(DB.verifyMacAddr(listMAC.get(3))==false);
	 assert(DB.verifyMacAddr(listMAC.get(4))==true);
	 assert(DB.verifyMacAddr(listMAC.get(5))==false);
	 assert(DB.verifyMacAddr(listMAC.get(6))==false);


 }
}

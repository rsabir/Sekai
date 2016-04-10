package utils;
import java.util.regex.*;
public class DB {
	private static Pattern pattern;
    private static Matcher matcher;
 public static boolean verifyMacAddr(String MAC){
	String regexpr= "^([0-9A-Fa-f]{2}[:]){5}([0-9A-Fa-f]{2})$";
	pattern = Pattern.compile(regexpr);
	matcher = pattern.matcher(MAC);
	return (matcher.find());
 }
}

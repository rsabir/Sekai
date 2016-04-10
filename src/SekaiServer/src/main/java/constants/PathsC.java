package constants;

public class PathsC {
	
	public static final String PATHCONFIG     = "/opt/sekai";
	public static final String DIRECTORYLOG   = "log/";
	public static  String PATHCONFIGJSON      = PATHCONFIG+"/"+FileNames.CONFIGJSON;
	public static  String PATHSETTING         = PATHCONFIG+"/"+FileNames.SETTINGSJSON;
	public static String PATHUSERS            = PATHCONFIG+"/"+FileNames.USERSPROPERTIES;
	public static String PATHLOGSETTINGS      = PATHCONFIG+"/"+FileNames.LOGPROPERTIES;
	
	// Will be loaded at startup
	
	public static  String PATHGENERALLOG = PATHCONFIG+DIRECTORYLOG+"general";
	public static  String PATHHTTPLOG	 = PATHCONFIG+DIRECTORYLOG+"http";
	public static  String PATHERRORLOG 	 = PATHCONFIG+DIRECTORYLOG+"error";
	
}

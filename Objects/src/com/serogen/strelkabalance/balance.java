package com.serogen.strelkabalance;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class balance extends  android.app.Service{
	public static class balance_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
			android.content.Intent in = new android.content.Intent(context, balance.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
			context.startService(in);
		}

	}
    static balance mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return balance.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "com.serogen.strelkabalance", "com.serogen.strelkabalance.balance");
            if (BA.isShellModeRuntimeCheck(processBA)) {
                processBA.raiseEvent2(null, true, "SHELL", false);
		    }
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processBA.loadHtSubs(this.getClass());
            ServiceHelper.init();
        }
        _service = new ServiceHelper(this);
        processBA.service = this;
        
        if (BA.isShellModeRuntimeCheck(processBA)) {
			processBA.raiseEvent2(null, true, "CREATE", true, "com.serogen.strelkabalance.balance", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!false && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("** Service (balance) Create **");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (false) {
			if (ServiceHelper.StarterHelper.waitForLayout != null)
				BA.handler.post(ServiceHelper.StarterHelper.waitForLayout);
		}
    }
		@Override
	public void onStart(android.content.Intent intent, int startId) {
		onStartCommand(intent, 0, 0);
    }
    @Override
    public int onStartCommand(final android.content.Intent intent, int flags, int startId) {
    	if (ServiceHelper.StarterHelper.onStartCommand(processBA))
			handleStart(intent);
		else {
			ServiceHelper.StarterHelper.waitForLayout = new Runnable() {
				public void run() {
                    processBA.setActivityPaused(false);
                    BA.LogInfo("** Service (balance) Create **");
                    processBA.raiseEvent(null, "service_create");
					handleStart(intent);
				}
			};
		}
        processBA.runHook("onstartcommand", this, new Object[] {intent, flags, startId});
		return android.app.Service.START_NOT_STICKY;
    }
    public void onTaskRemoved(android.content.Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (false)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (balance) Start **");
    	java.lang.reflect.Method startEvent = processBA.htSubs.get("service_start");
    	if (startEvent != null) {
    		if (startEvent.getParameterTypes().length > 0) {
    			anywheresoftware.b4a.objects.IntentWrapper iw = new anywheresoftware.b4a.objects.IntentWrapper();
    			if (intent != null) {
    				if (intent.hasExtra("b4a_internal_intent"))
    					iw.setObject((android.content.Intent) intent.getParcelableExtra("b4a_internal_intent"));
    				else
    					iw.setObject(intent);
    			}
    			processBA.raiseEvent(null, "service_start", iw);
    		}
    		else {
    			processBA.raiseEvent(null, "service_start");
    		}
    	}
    }
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        BA.LogInfo("** Service (balance) Destroy **");
		processBA.raiseEvent(null, "service_destroy");
        processBA.service = null;
		mostCurrent = null;
		processBA.setActivityPaused(true);
        processBA.runHook("ondestroy", this, null);
	}

@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.RemoteViewsWrapper _rv = null;
public static String _cardnumber = "";
public anywheresoftware.b4a.samples.httputils2.httputils2service _httputils2service = null;
public com.serogen.strelkabalance.main _main = null;
public com.serogen.strelkabalance.starter _starter = null;
public static String  _changecolor(int _balance) throws Exception{
int _value = 0;
 //BA.debugLineNum = 37;BA.debugLine="Sub ChangeColor(Balance As Int)";
 //BA.debugLineNum = 38;BA.debugLine="Dim Value As Int = Balance/480";
_value = (int) (_balance/(double)480);
 //BA.debugLineNum = 39;BA.debugLine="If Value < 1 Then";
if (_value<1) { 
 //BA.debugLineNum = 40;BA.debugLine="Value = 1";
_value = (int) (1);
 }else {
 //BA.debugLineNum = 42;BA.debugLine="If Value > 90 Then Value = 90";
if (_value>90) { 
_value = (int) (90);};
 };
 //BA.debugLineNum = 44;BA.debugLine="rv.SetTextColor(\"Label1\",HSLtoRGB(Value,255,128,2";
_rv.SetTextColor(processBA,"Label1",_hsltorgb(_value,(int) (255),(int) (128),(int) (255)));
 //BA.debugLineNum = 45;BA.debugLine="End Sub";
return "";
}
public static int  _hsltorgb(int _hue,int _saturation,int _luminance,int _alpha) throws Exception{
double[] _temp3 = null;
int _red = 0;
int _green = 0;
int _blue = 0;
double _temp1 = 0;
double _temp2 = 0;
int _n = 0;
double _phue = 0;
double _psat = 0;
double _plum = 0;
double _pred = 0;
double _pgreen = 0;
double _pblue = 0;
 //BA.debugLineNum = 46;BA.debugLine="Sub HSLtoRGB(Hue As Int, Saturation As Int, Lumina";
 //BA.debugLineNum = 47;BA.debugLine="Dim temp3(3) As Double , Red As Int, Green As Int";
_temp3 = new double[(int) (3)];
;
_red = 0;
_green = 0;
_blue = 0;
_temp1 = 0;
_temp2 = 0;
_n = 0;
 //BA.debugLineNum = 48;BA.debugLine="Dim pHue As Double, pSat As Double, pLum As Doubl";
_phue = 0;
_psat = 0;
_plum = 0;
_pred = 0;
_pgreen = 0;
_pblue = 0;
 //BA.debugLineNum = 50;BA.debugLine="pHue = Min(239, Hue) / 239";
_phue = anywheresoftware.b4a.keywords.Common.Min(239,_hue)/(double)239;
 //BA.debugLineNum = 51;BA.debugLine="pSat = Min(239, Saturation) / 239";
_psat = anywheresoftware.b4a.keywords.Common.Min(239,_saturation)/(double)239;
 //BA.debugLineNum = 52;BA.debugLine="pLum = Min(239, Luminance) / 239";
_plum = anywheresoftware.b4a.keywords.Common.Min(239,_luminance)/(double)239;
 //BA.debugLineNum = 54;BA.debugLine="If pSat = 0 Then";
if (_psat==0) { 
 //BA.debugLineNum = 55;BA.debugLine="pRed = pLum";
_pred = _plum;
 //BA.debugLineNum = 56;BA.debugLine="pGreen = pLum";
_pgreen = _plum;
 //BA.debugLineNum = 57;BA.debugLine="pBlue = pLum";
_pblue = _plum;
 }else {
 //BA.debugLineNum = 59;BA.debugLine="If pLum < 0.5 Then";
if (_plum<0.5) { 
 //BA.debugLineNum = 60;BA.debugLine="temp2 = pLum * (1 + pSat)";
_temp2 = _plum*(1+_psat);
 }else {
 //BA.debugLineNum = 62;BA.debugLine="temp2 = pLum + pSat - pLum * pSat";
_temp2 = _plum+_psat-_plum*_psat;
 };
 //BA.debugLineNum = 64;BA.debugLine="temp1 = 2 * pLum - temp2";
_temp1 = 2*_plum-_temp2;
 //BA.debugLineNum = 66;BA.debugLine="temp3(0) = pHue + 1 / 3";
_temp3[(int) (0)] = _phue+1/(double)3;
 //BA.debugLineNum = 67;BA.debugLine="temp3(1) = pHue";
_temp3[(int) (1)] = _phue;
 //BA.debugLineNum = 68;BA.debugLine="temp3(2) = pHue - 1 / 3";
_temp3[(int) (2)] = _phue-1/(double)3;
 //BA.debugLineNum = 70;BA.debugLine="For n = 0 To 2";
{
final int step20 = 1;
final int limit20 = (int) (2);
for (_n = (int) (0) ; (step20 > 0 && _n <= limit20) || (step20 < 0 && _n >= limit20); _n = ((int)(0 + _n + step20)) ) {
 //BA.debugLineNum = 71;BA.debugLine="If temp3(n) < 0 Then temp3(n) = temp3(n) + 1";
if (_temp3[_n]<0) { 
_temp3[_n] = _temp3[_n]+1;};
 //BA.debugLineNum = 72;BA.debugLine="If temp3(n) > 1 Then temp3(n) = temp3(n) - 1";
if (_temp3[_n]>1) { 
_temp3[_n] = _temp3[_n]-1;};
 //BA.debugLineNum = 74;BA.debugLine="If 6 * temp3(n) < 1 Then";
if (6*_temp3[_n]<1) { 
 //BA.debugLineNum = 75;BA.debugLine="temp3(n) = temp1 + (temp2 - temp1) * 6 * temp3";
_temp3[_n] = _temp1+(_temp2-_temp1)*6*_temp3[_n];
 }else {
 //BA.debugLineNum = 77;BA.debugLine="If 2 * temp3(n) < 1 Then";
if (2*_temp3[_n]<1) { 
 //BA.debugLineNum = 78;BA.debugLine="temp3(n) = temp2";
_temp3[_n] = _temp2;
 }else {
 //BA.debugLineNum = 80;BA.debugLine="If 3 * temp3(n) < 2 Then";
if (3*_temp3[_n]<2) { 
 //BA.debugLineNum = 81;BA.debugLine="temp3(n) = temp1 + (temp2 - temp1) * ((2 / 3";
_temp3[_n] = _temp1+(_temp2-_temp1)*((2/(double)3)-_temp3[_n])*6;
 }else {
 //BA.debugLineNum = 83;BA.debugLine="temp3(n) = temp1";
_temp3[_n] = _temp1;
 };
 };
 };
 }
};
 //BA.debugLineNum = 89;BA.debugLine="pRed = temp3(0)";
_pred = _temp3[(int) (0)];
 //BA.debugLineNum = 90;BA.debugLine="pGreen = temp3(1)";
_pgreen = _temp3[(int) (1)];
 //BA.debugLineNum = 91;BA.debugLine="pBlue = temp3(2)";
_pblue = _temp3[(int) (2)];
 };
 //BA.debugLineNum = 94;BA.debugLine="Red = pRed * 255";
_red = (int) (_pred*255);
 //BA.debugLineNum = 95;BA.debugLine="Green = pGreen * 255";
_green = (int) (_pgreen*255);
 //BA.debugLineNum = 96;BA.debugLine="Blue = pBlue * 255";
_blue = (int) (_pblue*255);
 //BA.debugLineNum = 98;BA.debugLine="Return Colors.ARGB(Alpha, Red,Green,Blue)";
if (true) return anywheresoftware.b4a.keywords.Common.Colors.ARGB(_alpha,_red,_green,_blue);
 //BA.debugLineNum = 99;BA.debugLine="End Sub";
return 0;
}
public static String  _imageview1_click() throws Exception{
 //BA.debugLineNum = 149;BA.debugLine="Sub ImageView1_Click";
 //BA.debugLineNum = 150;BA.debugLine="Update";
_update();
 //BA.debugLineNum = 151;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(anywheresoftware.b4a.samples.httputils2.httpjob _job) throws Exception{
 //BA.debugLineNum = 114;BA.debugLine="Sub JobDone (Job As HttpJob)";
 //BA.debugLineNum = 115;BA.debugLine="Log(\"JobName = \" & Job.JobName & \", Success = \" &";
anywheresoftware.b4a.keywords.Common.Log("JobName = "+_job._jobname+", Success = "+BA.ObjectToString(_job._success));
 //BA.debugLineNum = 116;BA.debugLine="If Job.Success = True Then";
if (_job._success==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 117;BA.debugLine="rv.SetText(\"Label1\",Parse_Json(Job.GetString) )";
_rv.SetText(processBA,"Label1",_parse_json(_job._getstring()));
 }else {
 //BA.debugLineNum = 119;BA.debugLine="rv.SetTextColor(\"Label1\",Colors.Red)";
_rv.SetTextColor(processBA,"Label1",anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 120;BA.debugLine="rv.SetText(\"Label1\",\"Ошибка\")";
_rv.SetText(processBA,"Label1","Ошибка");
 //BA.debugLineNum = 121;BA.debugLine="ToastMessageShow(\"Проверьте правильность введённ";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Проверьте правильность введённого номера карты или наличие подключения к Интернету",anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 123;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 //BA.debugLineNum = 124;BA.debugLine="Job.Release";
_job._release();
 //BA.debugLineNum = 125;BA.debugLine="End Sub";
return "";
}
public static String  _makerequest() throws Exception{
anywheresoftware.b4a.samples.httputils2.httpjob _job3 = null;
 //BA.debugLineNum = 18;BA.debugLine="Sub MakeRequest";
 //BA.debugLineNum = 19;BA.debugLine="Dim job3 As HttpJob";
_job3 = new anywheresoftware.b4a.samples.httputils2.httpjob();
 //BA.debugLineNum = 20;BA.debugLine="job3.Initialize(\"Job3\", Me)";
_job3._initialize(processBA,"Job3",balance.getObject());
 //BA.debugLineNum = 21;BA.debugLine="job3.Download(\"http://strelkacard.ru/api/cards/st";
_job3._download("http://strelkacard.ru/api/cards/status/?cardnum="+_cardnumber+"&cardtypeid=3ae427a1-0f17-4524-acb1-a3f50090a8f3");
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
public static String  _parse_json(String _result) throws Exception{
anywheresoftware.b4a.objects.collections.JSONParser _json = null;
anywheresoftware.b4a.objects.collections.Map _map1 = null;
String _balance = "";
 //BA.debugLineNum = 100;BA.debugLine="Sub Parse_Json(Result As String) As String";
 //BA.debugLineNum = 101;BA.debugLine="Dim JSON As JSONParser";
_json = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 102;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 103;BA.debugLine="Dim Balance As String";
_balance = "";
 //BA.debugLineNum = 104;BA.debugLine="JSON.Initialize(Result)";
_json.Initialize(_result);
 //BA.debugLineNum = 105;BA.debugLine="Map1 = JSON.NextObject";
_map1 = _json.NextObject();
 //BA.debugLineNum = 106;BA.debugLine="Balance = Map1.Get(\"balance\")";
_balance = BA.ObjectToString(_map1.Get((Object)("balance")));
 //BA.debugLineNum = 107;BA.debugLine="If Balance <> \"\" Then";
if ((_balance).equals("") == false) { 
 //BA.debugLineNum = 108;BA.debugLine="ChangeColor(Balance)";
_changecolor((int)(Double.parseDouble(_balance)));
 //BA.debugLineNum = 109;BA.debugLine="Return Balance.SubString2(0,Balance.Length-2) &";
if (true) return _balance.substring((int) (0),(int) (_balance.length()-2))+","+_balance.substring((int) (_balance.length()-2),_balance.length())+" Р";
 }else {
 //BA.debugLineNum = 111;BA.debugLine="Return \"Ошибка\"";
if (true) return "Ошибка";
 };
 //BA.debugLineNum = 113;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim rv As RemoteViews";
_rv = new anywheresoftware.b4a.objects.RemoteViewsWrapper();
 //BA.debugLineNum = 8;BA.debugLine="Dim CardNumber As String";
_cardnumber = "";
 //BA.debugLineNum = 9;BA.debugLine="End Sub";
return "";
}
public static String  _readmap() throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 11;BA.debugLine="Sub ReadMap As String";
 //BA.debugLineNum = 12;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 13;BA.debugLine="Map1 = File.ReadMap(File.DirInternal, \"key.dat\")";
_map1 = anywheresoftware.b4a.keywords.Common.File.ReadMap(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"key.dat");
 //BA.debugLineNum = 14;BA.debugLine="Map1.GetKeyAt(0)";
_map1.GetKeyAt((int) (0));
 //BA.debugLineNum = 15;BA.debugLine="Return Map1.GetValueAt(0)";
if (true) return BA.ObjectToString(_map1.GetValueAt((int) (0)));
 //BA.debugLineNum = 16;BA.debugLine="End Sub";
return "";
}
public static String  _rv_disabled() throws Exception{
 //BA.debugLineNum = 153;BA.debugLine="Sub rv_Disabled";
 //BA.debugLineNum = 154;BA.debugLine="StopService(\"\")";
anywheresoftware.b4a.keywords.Common.StopService(processBA,(Object)(""));
 //BA.debugLineNum = 155;BA.debugLine="End Sub";
return "";
}
public static String  _rv_requestupdate() throws Exception{
 //BA.debugLineNum = 145;BA.debugLine="Sub rv_RequestUpdate";
 //BA.debugLineNum = 146;BA.debugLine="Update";
_update();
 //BA.debugLineNum = 147;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 24;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 25;BA.debugLine="rv = ConfigureHomeWidget(\"BalanceLayout\", \"rv\", 6";
_rv = anywheresoftware.b4a.objects.RemoteViewsWrapper.createRemoteViews(processBA, R.layout.balance_layout, "BalanceLayout","rv");
 //BA.debugLineNum = 26;BA.debugLine="rv.SetImage(\"ImageView1\", LoadBitmap(File.DirAsse";
_rv.SetImage(processBA,"ImageView1",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"refresh_button.png").getObject()));
 //BA.debugLineNum = 27;BA.debugLine="If File.Exists(File.DirInternal, \"key.dat\") Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"key.dat")) { 
 //BA.debugLineNum = 28;BA.debugLine="CardNumber=ReadMap";
_cardnumber = _readmap();
 //BA.debugLineNum = 29;BA.debugLine="rv.SetText(\"Label3\",\"№\" & CardNumber)";
_rv.SetText(processBA,"Label3","№"+_cardnumber);
 }else {
 //BA.debugLineNum = 31;BA.debugLine="StartActivity(Main)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 32;BA.debugLine="StopService(Me)";
anywheresoftware.b4a.keywords.Common.StopService(processBA,balance.getObject());
 };
 //BA.debugLineNum = 34;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 //BA.debugLineNum = 35;BA.debugLine="MakeRequest";
_makerequest();
 //BA.debugLineNum = 36;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 157;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 159;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 127;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 128;BA.debugLine="If rv.HandleWidgetEvents(StartingIntent) Then Ret";
if (_rv.HandleWidgetEvents(processBA,(android.content.Intent)(_startingintent.getObject()))) { 
if (true) return "";};
 //BA.debugLineNum = 129;BA.debugLine="End Sub";
return "";
}
public static String  _update() throws Exception{
 //BA.debugLineNum = 131;BA.debugLine="Sub Update";
 //BA.debugLineNum = 132;BA.debugLine="rv.SetText(\"Label1\",\"Загрузка...\")";
_rv.SetText(processBA,"Label1","Загрузка...");
 //BA.debugLineNum = 133;BA.debugLine="rv.SetTextColor(\"Label1\", Colors.White)";
_rv.SetTextColor(processBA,"Label1",anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 134;BA.debugLine="rv.UpdateWidget";
_rv.UpdateWidget(processBA);
 //BA.debugLineNum = 135;BA.debugLine="If File.Exists(File.DirInternal, \"key.dat\") Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"key.dat")) { 
 //BA.debugLineNum = 136;BA.debugLine="CardNumber=ReadMap";
_cardnumber = _readmap();
 //BA.debugLineNum = 137;BA.debugLine="rv.SetText(\"Label3\",\"№\" & CardNumber)";
_rv.SetText(processBA,"Label3","№"+_cardnumber);
 }else {
 //BA.debugLineNum = 139;BA.debugLine="StartActivity(Main)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._main.getObject()));
 //BA.debugLineNum = 140;BA.debugLine="StopService(Me)";
anywheresoftware.b4a.keywords.Common.StopService(processBA,balance.getObject());
 };
 //BA.debugLineNum = 142;BA.debugLine="MakeRequest";
_makerequest();
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
return "";
}
}

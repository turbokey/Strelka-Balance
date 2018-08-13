package com.serogen.strelkabalance;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.serogen.strelkabalance", "com.serogen.strelkabalance.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "com.serogen.strelkabalance", "com.serogen.strelkabalance.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.serogen.strelkabalance.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        Object[] o;
        if (permissions.length > 0)
            o = new Object[] {permissions[0], grantResults[0] == 0};
        else
            o = new Object[] {"", false};
        processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button1 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imageview1 = null;
public anywheresoftware.b4a.samples.httputils2.httputils2service _httputils2service = null;
public com.serogen.strelkabalance.balance _balance = null;
public com.serogen.strelkabalance.starter _starter = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 98;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 99;BA.debugLine="Activity.LoadLayout(\"BaseLayout\")";
mostCurrent._activity.LoadLayout("BaseLayout",mostCurrent.activityBA);
 //BA.debugLineNum = 100;BA.debugLine="ConfigureViews";
_configureviews();
 //BA.debugLineNum = 101;BA.debugLine="If File.Exists(File.DirInternal, \"key.dat\") Then";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"key.dat")) { 
mostCurrent._edittext1.setText((Object)(_readmap()));};
 //BA.debugLineNum = 102;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 108;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 110;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 104;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 106;BA.debugLine="End Sub";
return "";
}
public static String  _button1_click() throws Exception{
 //BA.debugLineNum = 113;BA.debugLine="Sub Button1_Click";
 //BA.debugLineNum = 114;BA.debugLine="If EditText1.Text <> \"\" Then";
if ((mostCurrent._edittext1.getText()).equals("") == false) { 
 //BA.debugLineNum = 115;BA.debugLine="WriteMap";
_writemap();
 //BA.debugLineNum = 116;BA.debugLine="ToastMessageShow(\"Номер карты сохранён. Размести";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Номер карты сохранён. Разместите виджет на домашнем экране",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 117;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else {
 //BA.debugLineNum = 119;BA.debugLine="ToastMessageShow(\"Введите номер карты\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Введите номер карты",anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 121;BA.debugLine="End Sub";
return "";
}
public static String  _configureviews() throws Exception{
 //BA.debugLineNum = 64;BA.debugLine="Sub ConfigureViews()";
 //BA.debugLineNum = 65;BA.debugLine="ImageView1.Left=0";
mostCurrent._imageview1.setLeft((int) (0));
 //BA.debugLineNum = 66;BA.debugLine="ImageView1.Top=0";
mostCurrent._imageview1.setTop((int) (0));
 //BA.debugLineNum = 67;BA.debugLine="ImageView1.Width=100%x";
mostCurrent._imageview1.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 68;BA.debugLine="ImageView1.Height=25%y";
mostCurrent._imageview1.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA));
 //BA.debugLineNum = 69;BA.debugLine="Label1.Left=0";
mostCurrent._label1.setLeft((int) (0));
 //BA.debugLineNum = 70;BA.debugLine="Label1.Top = 30%y";
mostCurrent._label1.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 71;BA.debugLine="Label1.Width =100%x";
mostCurrent._label1.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 72;BA.debugLine="Label1.Height=20%y";
mostCurrent._label1.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 73;BA.debugLine="SetLabelTextSize(Label1, Label1.Text, 20 ,1)";
_setlabeltextsize(mostCurrent._label1,mostCurrent._label1.getText(),(float) (20),(float) (1));
 //BA.debugLineNum = 74;BA.debugLine="EditText1.Left=0";
mostCurrent._edittext1.setLeft((int) (0));
 //BA.debugLineNum = 75;BA.debugLine="EditText1.Top = 50%y";
mostCurrent._edittext1.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 76;BA.debugLine="EditText1.Width =100%x";
mostCurrent._edittext1.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 77;BA.debugLine="EditText1.Height=20%y";
mostCurrent._edittext1.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 78;BA.debugLine="SetEditTextSize(EditText1, 00000000000, 50 ,1)";
_setedittextsize(mostCurrent._edittext1,BA.NumberToString(00000000000),(float) (50),(float) (1));
 //BA.debugLineNum = 79;BA.debugLine="Button1.Left=5%x";
mostCurrent._button1.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 80;BA.debugLine="Button1.Top = 75%y";
mostCurrent._button1.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (75),mostCurrent.activityBA));
 //BA.debugLineNum = 81;BA.debugLine="Button1.Width = 90%x";
mostCurrent._button1.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA));
 //BA.debugLineNum = 82;BA.debugLine="Button1.Height = 10%y";
mostCurrent._button1.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 83;BA.debugLine="SetButtonTextSize(Button1, Button1.Text, 20 ,1)";
_setbuttontextsize(mostCurrent._button1,mostCurrent._button1.getText(),(float) (20),(float) (1));
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 19;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 20;BA.debugLine="Dim Button1 As Button";
mostCurrent._button1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Dim EditText1 As EditText";
mostCurrent._edittext1 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Dim Label1 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim ImageView1 As ImageView";
mostCurrent._imageview1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        anywheresoftware.b4a.samples.httputils2.httputils2service._process_globals();
main._process_globals();
balance._process_globals();
starter._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 17;BA.debugLine="End Sub";
return "";
}
public static String  _readmap() throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 92;BA.debugLine="Sub ReadMap As String";
 //BA.debugLineNum = 93;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 94;BA.debugLine="Map1 = File.ReadMap(File.DirInternal, \"key.dat\")";
_map1 = anywheresoftware.b4a.keywords.Common.File.ReadMap(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"key.dat");
 //BA.debugLineNum = 95;BA.debugLine="Map1.GetKeyAt(0)";
_map1.GetKeyAt((int) (0));
 //BA.debugLineNum = 96;BA.debugLine="Return Map1.GetValueAt(0)";
if (true) return BA.ObjectToString(_map1.GetValueAt((int) (0)));
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return "";
}
public static String  _setbuttontextsize(anywheresoftware.b4a.objects.ButtonWrapper _lbl2,String _txt2,float _maxfontsize,float _minfontsize) throws Exception{
float _fontsize = 0f;
int _height = 0;
anywheresoftware.b4a.objects.StringUtils _stu = null;
 //BA.debugLineNum = 25;BA.debugLine="Sub SetButtonTextSize(lbl2 As Button, txt2 As Stri";
 //BA.debugLineNum = 26;BA.debugLine="Dim FontSize = MaxFontSize As Float";
_fontsize = _maxfontsize;
 //BA.debugLineNum = 27;BA.debugLine="Dim Height As Int";
_height = 0;
 //BA.debugLineNum = 28;BA.debugLine="Dim stu As StringUtils";
_stu = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 30;BA.debugLine="lbl2.TextSize = FontSize";
_lbl2.setTextSize(_fontsize);
 //BA.debugLineNum = 31;BA.debugLine="Height = stu.MeasureMultilineTextHeight(lbl2, txt";
_height = _stu.MeasureMultilineTextHeight((android.widget.TextView)(_lbl2.getObject()),_txt2);
 //BA.debugLineNum = 32;BA.debugLine="Do While Height > lbl2.Height And FontSize > MinF";
while (_height>_lbl2.getHeight() && _fontsize>_minfontsize) {
 //BA.debugLineNum = 33;BA.debugLine="FontSize = FontSize - 1";
_fontsize = (float) (_fontsize-1);
 //BA.debugLineNum = 34;BA.debugLine="lbl2.TextSize = FontSize";
_lbl2.setTextSize(_fontsize);
 //BA.debugLineNum = 35;BA.debugLine="Height = stu.MeasureMultilineTextHeight(lbl2, tx";
_height = _stu.MeasureMultilineTextHeight((android.widget.TextView)(_lbl2.getObject()),_txt2);
 }
;
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
public static String  _setedittextsize(anywheresoftware.b4a.objects.EditTextWrapper _lbl2,String _txt2,float _maxfontsize,float _minfontsize) throws Exception{
float _fontsize = 0f;
int _height = 0;
anywheresoftware.b4a.objects.StringUtils _stu = null;
 //BA.debugLineNum = 38;BA.debugLine="Sub SetEditTextSize(lbl2 As EditText, txt2 As Stri";
 //BA.debugLineNum = 39;BA.debugLine="Dim FontSize = MaxFontSize As Float";
_fontsize = _maxfontsize;
 //BA.debugLineNum = 40;BA.debugLine="Dim Height As Int";
_height = 0;
 //BA.debugLineNum = 41;BA.debugLine="Dim stu As StringUtils";
_stu = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 43;BA.debugLine="lbl2.TextSize = FontSize";
_lbl2.setTextSize(_fontsize);
 //BA.debugLineNum = 44;BA.debugLine="Height = stu.MeasureMultilineTextHeight(lbl2, txt";
_height = _stu.MeasureMultilineTextHeight((android.widget.TextView)(_lbl2.getObject()),_txt2);
 //BA.debugLineNum = 45;BA.debugLine="Do While Height > lbl2.Height And FontSize > MinF";
while (_height>_lbl2.getHeight() && _fontsize>_minfontsize) {
 //BA.debugLineNum = 46;BA.debugLine="FontSize = FontSize - 1";
_fontsize = (float) (_fontsize-1);
 //BA.debugLineNum = 47;BA.debugLine="lbl2.TextSize = FontSize";
_lbl2.setTextSize(_fontsize);
 //BA.debugLineNum = 48;BA.debugLine="Height = stu.MeasureMultilineTextHeight(lbl2, tx";
_height = _stu.MeasureMultilineTextHeight((android.widget.TextView)(_lbl2.getObject()),_txt2);
 }
;
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return "";
}
public static String  _setlabeltextsize(anywheresoftware.b4a.objects.LabelWrapper _lbl2,String _txt2,float _maxfontsize,float _minfontsize) throws Exception{
float _fontsize = 0f;
int _height = 0;
anywheresoftware.b4a.objects.StringUtils _stu = null;
 //BA.debugLineNum = 51;BA.debugLine="Sub SetLabelTextSize(lbl2 As Label, txt2 As String";
 //BA.debugLineNum = 52;BA.debugLine="Dim FontSize = MaxFontSize As Float";
_fontsize = _maxfontsize;
 //BA.debugLineNum = 53;BA.debugLine="Dim Height As Int";
_height = 0;
 //BA.debugLineNum = 54;BA.debugLine="Dim stu As StringUtils";
_stu = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 56;BA.debugLine="lbl2.TextSize = FontSize";
_lbl2.setTextSize(_fontsize);
 //BA.debugLineNum = 57;BA.debugLine="Height = stu.MeasureMultilineTextHeight(lbl2, txt";
_height = _stu.MeasureMultilineTextHeight((android.widget.TextView)(_lbl2.getObject()),_txt2);
 //BA.debugLineNum = 58;BA.debugLine="Do While Height > lbl2.Height And FontSize > MinF";
while (_height>_lbl2.getHeight() && _fontsize>_minfontsize) {
 //BA.debugLineNum = 59;BA.debugLine="FontSize = FontSize - 1";
_fontsize = (float) (_fontsize-1);
 //BA.debugLineNum = 60;BA.debugLine="lbl2.TextSize = FontSize";
_lbl2.setTextSize(_fontsize);
 //BA.debugLineNum = 61;BA.debugLine="Height = stu.MeasureMultilineTextHeight(lbl2, tx";
_height = _stu.MeasureMultilineTextHeight((android.widget.TextView)(_lbl2.getObject()),_txt2);
 }
;
 //BA.debugLineNum = 63;BA.debugLine="End Sub";
return "";
}
public static String  _writemap() throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 85;BA.debugLine="Sub WriteMap";
 //BA.debugLineNum = 86;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 87;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 88;BA.debugLine="Map1.Put(\"CardNumber\" , EditText1.Text)";
_map1.Put((Object)("CardNumber"),(Object)(mostCurrent._edittext1.getText()));
 //BA.debugLineNum = 89;BA.debugLine="File.WriteMap(File.DirInternal, \"key.dat\", Map1)";
anywheresoftware.b4a.keywords.Common.File.WriteMap(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"key.dat",_map1);
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return "";
}
}

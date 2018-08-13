Type=Service
Version=6.5
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: true
	
#End Region

Sub Process_Globals
	Dim rv As RemoteViews
	Dim CardNumber As String
End Sub

Sub ReadMap As String
	Dim Map1 As Map
	Map1 = File.ReadMap(File.DirInternal, "key.dat")
	Map1.GetKeyAt(0)
	Return Map1.GetValueAt(0)
End Sub

Sub MakeRequest
	Dim job3 As HttpJob
	job3.Initialize("Job3", Me)
	job3.Download("http://strelkacard.ru/api/cards/status/?cardnum="&CardNumber&"&cardtypeid=3ae427a1-0f17-4524-acb1-a3f50090a8f3")
End Sub

Sub Service_Create
	rv = ConfigureHomeWidget("BalanceLayout", "rv", 60, "Strelka card balance", True)
	rv.SetImage("ImageView1", LoadBitmap(File.DirAssets, "refresh_button.png"))
	If File.Exists(File.DirInternal, "key.dat") Then 
		CardNumber=ReadMap
		rv.SetText("Label3","№" & CardNumber)
	Else 
		StartActivity(Main)
		StopService(Me)
	End If
	rv.UpdateWidget
	MakeRequest
End Sub
Sub ChangeColor(Balance As Int)
	Dim Value As Int = Balance/480
	If Value < 1 Then 
		Value = 1 
	Else
		If Value > 90 Then Value = 90
	End If
	rv.SetTextColor("Label1",HSLtoRGB(Value,255,128,255))
End Sub
Sub HSLtoRGB(Hue As Int, Saturation As Int, Luminance As Int, Alpha As Int ) As Int
	Dim temp3(3) As Double , Red As Int, Green As Int, Blue As Int ,temp1 As Double, temp2 As Double ,n As Int
	Dim pHue As Double, pSat As Double, pLum As Double , pRed As Double, pGreen As Double, pBlue As Double
   
	pHue = Min(239, Hue) / 239
	pSat = Min(239, Saturation) / 239
	pLum = Min(239, Luminance) / 239

	If pSat = 0 Then
		pRed = pLum
		pGreen = pLum
		pBlue = pLum
	Else
		If pLum < 0.5 Then
			temp2 = pLum * (1 + pSat)
		Else
			temp2 = pLum + pSat - pLum * pSat
		End If
		temp1 = 2 * pLum - temp2
   
		temp3(0) = pHue + 1 / 3
		temp3(1) = pHue
		temp3(2) = pHue - 1 / 3
      
		For n = 0 To 2
			If temp3(n) < 0 Then temp3(n) = temp3(n) + 1
			If temp3(n) > 1 Then temp3(n) = temp3(n) - 1
      
			If 6 * temp3(n) < 1 Then
				temp3(n) = temp1 + (temp2 - temp1) * 6 * temp3(n)
			Else
				If 2 * temp3(n) < 1 Then
					temp3(n) = temp2
				Else
					If 3 * temp3(n) < 2 Then
						temp3(n) = temp1 + (temp2 - temp1) * ((2 / 3) - temp3(n)) * 6
					Else
						temp3(n) = temp1
					End If
				End If
			End If
		Next

		pRed = temp3(0)
		pGreen = temp3(1)
		pBlue = temp3(2)
	End If

	Red = pRed * 255
	Green = pGreen * 255
	Blue = pBlue * 255

	Return Colors.ARGB(Alpha, Red,Green,Blue)
End Sub
Sub Parse_Json(Result As String) As String
	Dim JSON As JSONParser
	Dim Map1 As Map
	Dim Balance As String
	JSON.Initialize(Result)
	Map1 = JSON.NextObject
	Balance = Map1.Get("balance")
	If Balance <> "" Then
		ChangeColor(Balance)
		Return Balance.SubString2(0,Balance.Length-2) & "," & Balance.SubString2(Balance.Length-2,Balance.Length)& " Р"
	Else
		Return "Ошибка"
	End If
End Sub
Sub JobDone (Job As HttpJob)
	Log("JobName = " & Job.JobName & ", Success = " & Job.Success)
	If Job.Success = True Then
		rv.SetText("Label1",Parse_Json(Job.GetString) )
	Else
		rv.SetTextColor("Label1",Colors.Red)
		rv.SetText("Label1","Ошибка")
		ToastMessageShow("Проверьте правильность введённого номера карты или наличие подключения к Интернету",True)
	End If
	rv.UpdateWidget
	Job.Release
End Sub

Sub Service_Start (StartingIntent As Intent)
	If rv.HandleWidgetEvents(StartingIntent) Then Return
End Sub

Sub Update
	rv.SetText("Label1","Загрузка...")
	rv.SetTextColor("Label1", Colors.White)
	rv.UpdateWidget
	If File.Exists(File.DirInternal, "key.dat") Then
		CardNumber=ReadMap
		rv.SetText("Label3","№" & CardNumber)
	Else
		StartActivity(Main)
		StopService(Me)
	End If
	MakeRequest
End Sub

Sub rv_RequestUpdate
	Update
End Sub

Sub ImageView1_Click
	Update
End Sub

Sub rv_Disabled
	StopService("")
End Sub

Sub Service_Destroy

End Sub

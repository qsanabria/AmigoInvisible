package com.example.amigo_invisible.clases_ayuda;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;

public class Evento_agenda
{
	private Activity activity;
	
	public Evento_agenda(Activity activity) 
	{
		this.activity = activity;
	}
	
	public void anadir_evento_calendario(String evento, String lugar, String fecha, String hora)
	{
		String dia = "";
		String mes = "";
		String anio = "";
		String horas = "";
		String minutos = "";
		int dia_final, mes_final, anio_final, horas_final, minutos_final;
		
		for (int i=0; i<fecha.length(); i++)
		{
			if (i==0)
			{
				dia = dia+fecha.charAt(i);
			}
			if (i==1)
			{
				dia = dia+fecha.charAt(i);
			}
			if (i==3)
			{
				mes = mes+fecha.charAt(i);
			}
			if (i==4)
			{
				mes = mes+fecha.charAt(i);
			}
			if (i==6)
			{
				anio = anio+fecha.charAt(i);
			}
			if (i==7)
			{
				anio = anio+fecha.charAt(i);
			}
			if (i==8)
			{
				anio = anio+fecha.charAt(i);
			}
			if (i==9)
			{
				anio = anio+fecha.charAt(i);
			}
		}
		
		dia_final = Integer.parseInt(dia);
		mes_final = Integer.parseInt(mes);
		anio_final = Integer.parseInt(anio);
		
		for (int i=0; i<hora.length(); i++)
		{
			if (i==0)
			{
				horas = horas+hora.charAt(i);
			}
			if (i==1)
			{
				horas = horas+hora.charAt(i);
			}
			if (i==3)
			{
				minutos = minutos+hora.charAt(i);
			}
			if (i==4)
			{
				minutos = minutos+hora.charAt(i);
			}
		}
		
		horas_final = Integer.parseInt(horas);
		minutos_final = Integer.parseInt(minutos);
		
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.DAY_OF_MONTH, dia_final);
		cal.set(Calendar.MONTH, mes_final-1);
		cal.set(Calendar.YEAR, anio_final);
		  
		cal.set(Calendar.HOUR_OF_DAY, horas_final);
		cal.set(Calendar.MINUTE, minutos_final);
		  
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");

		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis()+60*60*1000);

		intent.putExtra(Events.ALL_DAY, false);
		intent.putExtra(Events.RRULE , "FREQ=DAILY;COUNT=1");
		intent.putExtra(Events.TITLE, "Amigo Invisible");
		intent.putExtra(Events.DESCRIPTION, evento);
		intent.putExtra(Events.EVENT_LOCATION, lugar);

		activity.startActivity(intent);
	}
}

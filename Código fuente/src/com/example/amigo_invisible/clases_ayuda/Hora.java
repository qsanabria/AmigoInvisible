package com.example.amigo_invisible.clases_ayuda;

import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

//Clase que muestra un TimePickerDialog al pulsar un EditText
public class Hora 
{
	int hora, minuto;
	Calendar calendario = Calendar.getInstance();
	Activity activity;
	EditText et;
	
	public Hora(Activity activity, EditText ethora)//Constructor para pasar la activity donde se ejecutará
	{
		this.activity = activity;
		this.et = ethora;
	}
	
	public void hora()//Método del TimePickerDialog
	{
		hora = calendario.get(Calendar.HOUR_OF_DAY);
		minuto = calendario.get(Calendar.MINUTE);
		
		//Método de rellamada para el constructor del TimePickerDialog
		TimePickerDialog.OnTimeSetListener metodo_hora = new TimePickerDialog.OnTimeSetListener() 
		{
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
			{
				hora = hourOfDay;
				minuto = minute;
				ponerHora();
			}
		};
		
		//Creación de una instancia de TimePickerDialog(constructor)
		final TimePickerDialog tpd = new TimePickerDialog(activity, metodo_hora, hora, minuto, true);
		
		et.setOnTouchListener(new View.OnTouchListener()//Método para que se muestre el TimePickerDialog al pulsar el et
		{
			
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				tpd.show();
				return true;
			}
		});
	}
	
	public void ponerHora()//Muestra la hora seleccionada en el EditText
	{
		et.setText(new StringBuilder().append(digitos(hora)).append(":").append(digitos(minuto)));
	}
	
	public String digitos(int c)//Método para añadir un cero a la izquierda si el número seleccionado en la 
	{							//hora o en la hora es menor que 10
		if (c >= 10)
		{
			return String.valueOf(c);
		}
		else
		{
			return "0"+String.valueOf(c);
		}
	}
}

package com.example.amigo_invisible.clases_ayuda;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

//Clase que muestra un DatePickerDialog al pulsar un EditText
public class Fecha 
{
	private Activity activity;
	private Calendar calendario = Calendar.getInstance();
	private int dia, mes, anio;
	private EditText et;
	
	public Fecha(Activity activity, EditText etfecha)//constructor en el que le pasamos la activity y el et
	{
		this.activity = activity;
		this.et = etfecha;
	}
	
	public void fecha()//Método del DatePickerDialog
	{
		anio = calendario.get(Calendar.YEAR);
		mes = calendario.get(Calendar.MONTH);
		dia = calendario.get(Calendar.DAY_OF_MONTH);
		
		//Método de rellamada que tenemos que pasarle al constructor del DatePickerDialog
		DatePickerDialog.OnDateSetListener metodo_fecha = new DatePickerDialog.OnDateSetListener() 
		{
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
			{
				anio = year;
				mes = monthOfYear;
				dia = dayOfMonth;
				ponerFecha();
			}
		};
		
		final DatePickerDialog dpd = new DatePickerDialog(activity, metodo_fecha, anio, mes, dia);//Creación del DatePickerDialog
		//con función de rellamada y valores iniciales(anio, mes, dia)
		
		et.setOnTouchListener(new View.OnTouchListener()//Método para que se muestre el datePickerDialog al pulsar el et
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				dpd.show();
				return true;
			}
		});
	}
	
	public void ponerFecha()//Muestra la fecha seleccionada en el EditText
	{
		et.setText(new StringBuilder().append(digitos(dia)).append("/").append(digitos(mes+1)).append("/").append(anio));
	}
	
	public String digitos(int c)//Método para añadir un cero a la izquierda si el número seleccionado en la 
	{							  //fecha o en la hora es menor que 10
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

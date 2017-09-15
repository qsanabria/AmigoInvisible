package com.example.amigo_invisible;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amigo_invisible.clases_ayuda.Comprueba_EditText;
import com.example.amigo_invisible.clases_ayuda.Fecha;
import com.example.amigo_invisible.clases_ayuda.Hora;

/**
 * Clase que muestra los elementos para la introducción del evento
 * @author Quintin Sanabria Sánchez
 */

public class Nuevo_evento extends Activity 
{
	private EditText et_fecha, et_hora, nombre_evento, lugar_evento, tematica, p_min, p_max;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//Quita el título de la aplicación
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//Quita la barra de notificaciones
		
		setContentView(R.layout.nuevo_evento);//Asigna a .java la intefaz del xml de nuevo evento
		
		//Obtención de los manejadores de los EditText de nuevo_evento.xml
		nombre_evento = (EditText)findViewById(R.id.et_nombre_evento);
		lugar_evento = (EditText)findViewById(R.id.et_lugar_evento);
		et_fecha = (EditText)findViewById(R.id.et_fecha);
		et_hora = (EditText)findViewById(R.id.et_hora);
		tematica = (EditText)findViewById(R.id.et_tematica_regalo);
		p_min = (EditText)findViewById(R.id.et_precio_min);
		p_max = (EditText)findViewById(R.id.et_precio_max);
		
		SharedPreferences evento = getSharedPreferences("evento", Context.MODE_PRIVATE);//Obtenemos el archivo SharedPreferences
		String nombre = evento.getString("nombre_evento", "");//Obtenemos el nombre del evento del archivo para comprobaciones
		if (nombre.equals(""))//Si no hay nombre de evento es que está vacío el archivo
		{
			//Nada, solo se muestra todo vacío
		}
		else//Si hay elemento en archivo de preferencias
		{
			//Cuadro de diálogo para elegir si recuperar datos del último grupo no acabado
			AlertDialog.Builder dialogo = new AlertDialog.Builder(this);//Crea un cuadro de dialogo
			dialogo.setTitle("¿Recuperar datos \ndel último grupo?");//Título del AlertDialog
			dialogo.setIcon(android.R.drawable.ic_input_get);//Icono del AlertDialog
			
			dialogo.setPositiveButton("Recuperar", new DialogInterface.OnClickListener()//Funcionalidad para botón si
			{
				@Override
				public void onClick(DialogInterface dialogo, int id) 
				{
					//Recuperamos el evento del SharedPreferences
					recuperar();
				}
			});
			dialogo.setNegativeButton("No recuperar", new DialogInterface.OnClickListener()//Funcionalidad para botón no
			{
				@Override
				public void onClick(DialogInterface dialogo, int id) 
				{
					//No hace nada, sólo quedará en blanco para un nuevo evento
				}
			});
			dialogo.show();
		}
		
		//Instancia de DatePickerDialog
		Fecha nuevafecha = new Fecha(this, et_fecha);
		nuevafecha.fecha();
		
		//Instancia de TimePickerDialog
		Hora nuevahora = new Hora(this, et_hora);
		nuevahora.hora();
	}
	/**
	 * Se encarga de cargar en pantalla el evento que se encuentra almacenado
	 */
	public void recuperar()
	{
		//Recuperación de los campos del archivo SharedPreferences(si no hay elementos pone los valores por defecto)
		SharedPreferences evento = getSharedPreferences("evento", Context.MODE_PRIVATE);
		nombre_evento.setText(evento.getString("nombre_evento", ""));
		lugar_evento.setText(evento.getString("lugar_evento", ""));
		et_fecha.setText(evento.getString("fecha_evento", ""));
		et_hora.setText(evento.getString("hora_evento", ""));
		tematica.setText(evento.getString("tematica", ""));
		p_min.setText(evento.getString("p_min", ""));
		p_max.setText(evento.getString("p_max", ""));
	}
	
	/**
	 * Se encarga de volver a la pantalla principal
	 * @param view
	 */
	public void atras(View view)//Ir a la pantalla de inicio
	{
		finish();
	}
	
	/**
	 * Se encarga de verificar y guardar el evento
	 * @param view
	 */
	//Botón siguiente en nuevo evento
	public void creacion_evento(View view)
	{
		Comprueba_EditText comprobar = new Comprueba_EditText();//Instancia de la clase de comprobación
		//Comprueba si ciertos EditText están vacíos y no deja avanzar si lo están
		if(comprobar.comprobar(nombre_evento) && comprobar.comprobar(lugar_evento) && comprobar.comprobar(et_fecha) && comprobar.comprobar(et_hora))
		{
			try
			{
				//Obtenemos el archivo SharedPreferences o lo crea si no existe
				SharedPreferences evento = getSharedPreferences("evento", Context.MODE_PRIVATE);
				Editor editor = evento.edit();//Crea el editor para escribir en el archivo SharedPreferences
				//Añadimos en el archivo elementos (String, int, float,....)
				editor.putString("nombre_evento", nombre_evento.getText().toString());
				editor.putString("lugar_evento", lugar_evento.getText().toString());
				editor.putString("fecha_evento", et_fecha.getText().toString());
				editor.putString("hora_evento", et_hora.getText().toString());
				editor.putString("tematica", tematica.getText().toString());
				editor.putString("p_min", p_min.getText().toString());
				editor.putString("p_max", p_max.getText().toString());
				editor.commit();//Confirmamos la escritura en el archivo
			}
			catch(Exception e)//Error en el almacenamiento
			{
				Toast.makeText(getApplicationContext(), "Imposible almacenar datos del evento", Toast.LENGTH_SHORT).show();
			}
			
			//Pasamos a la siguiente activity(Numero de grupos)
			Intent numero_grupos = new Intent(this, Numero_grupos.class);
			startActivity(numero_grupos);
		}
	}
}

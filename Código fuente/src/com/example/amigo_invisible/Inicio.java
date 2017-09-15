package com.example.amigo_invisible;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * Clase que reprensenta por pantalla el menú principal de la aplicación
 * @author Quintin Sanabria Sánchez
 */

public class Inicio extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//Quita el título de la aplicación
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//Quita la barra de notificaciones
		
		setContentView(R.layout.inicio);//Asigna a .java la intefaz del xml inicio
	}
	
	/**
	 * Se encarga de presentar un nuevo evento para su creación
	 * @param view
	 */
	public void nuevo_evento(View view)//Llamada a intent para activity de creación del evento
	{
		Intent nuevo_evento = new Intent(this, Nuevo_evento.class);
		startActivity(nuevo_evento);
	}
	
	/**
	 * Se encarga de presentar la pantalla de ayuda
	 * @param view
	 */
	public void ayuda(View view)//Nos lleva a la pantalla de ayuda(activity)
	{
		Intent ayuda = new Intent(this, Ayuda.class);//Crea un intent para llevarnos a otra activity
		startActivity(ayuda);//Comienza la activity ayuda
	}
	
	/**
	 * Se encarga de salir de la aplicación
	 */
	public void onBackPressed()//Funcionalidad para cuando presionemos el botón atrás(igual que darle a salir)
	{
		View l = new View(this);//Creamos una vista para pasársela al método salir que la necesitará
		salir(l);//Llamamos a salir para que haga lo mismo que en el botón salir
	}
	
	/**
	 * Se encarga de salir de la aplicación
	 * @param view
	 */
	public void salir(View view)//Funcionalidad del botón salir
	{
		AlertDialog.Builder dialogo = new AlertDialog.Builder(this);//Crea un cuadro de dialogo
		dialogo.setTitle("¿Desea salir \nde la aplicación?");//Título del AlertDialog
		dialogo.setIcon(android.R.drawable.ic_dialog_info);//Icono del AlertDialog
		
		dialogo.setPositiveButton("Salir", new DialogInterface.OnClickListener()//Funcionalidad para botón si
		{
			@Override
			public void onClick(DialogInterface dialogo, int id) 
			{
				finish();//Sale definitivamente del programa
			}
		});
		dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()//Funcionalidad para botón no
		{
			@Override
			public void onClick(DialogInterface dialogo, int id) 
			{
				//No hace nada, sólo vuelve
			}
		});
		dialogo.show();
	}
}

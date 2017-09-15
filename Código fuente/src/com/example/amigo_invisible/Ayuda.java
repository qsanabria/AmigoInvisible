package com.example.amigo_invisible;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * Clase que reprensenta por pantalla una breve descripci�n de la aplicaci�n
 * @author Quintin Sanabria S�nchez
 */

public class Ayuda extends Activity
{		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//Quita el t�tulo de la aplicaci�n
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//Quita la barra de notificaciones
		
		setContentView(R.layout.ayuda);//Asigna a .java la intefaz del xml inicio
	}
	
	/**
	 * Se encarga de volver a la pantalla principal
	 * @param view
	 */
	public void atras(View view)//Volvemos a Inicio en la ayuda
	{
		finish();
	}
}

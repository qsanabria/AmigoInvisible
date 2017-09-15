package com.example.amigo_invisible.clases_ayuda;

import android.widget.EditText;

public class Comprueba_EditText 
{
	public boolean comprobar(EditText et) 
	{
		int len = et.length();
		
		if (len<=0)
		{
			et.setError("¡Este campo no puede estar vacío!");
			return false;
		}
		else
		{
			et.setError(null);
			return true;
		}
	}
}

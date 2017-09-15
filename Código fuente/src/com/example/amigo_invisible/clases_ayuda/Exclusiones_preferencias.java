package com.example.amigo_invisible.clases_ayuda;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

public class Exclusiones_preferencias 
{
	private Context context;
	private Editor editor;
	private SharedPreferences exclusiones;
	
	public Exclusiones_preferencias(Context context) 
	{
		this.context = context;
		exclusiones = context.getSharedPreferences("exclusiones", Context.MODE_PRIVATE);
	}
	
	public int obtener_num_exclusiones()
	{
		int num_exclusiones = exclusiones.getInt("num_exclusiones", 0);
		return num_exclusiones;
	}
	
	public String[] obtener_origen()
	{
		String []origen;
		int num = obtener_num_exclusiones();
		origen = new String[num];
		
		for (int i=0; i<num; i++)
		{
			origen[i] = exclusiones.getString("origen"+(i+1), "");
		}
		
		return origen;
	}
	
	public String[] obtener_destino()
	{
		String []destino;
		int num = obtener_num_exclusiones();
		destino = new String[num];
		
		for (int i=0; i<num; i++)
		{
			destino[i] = exclusiones.getString("destino"+(i+1), "");
		}
		
		return destino;
	}
	
	public void anadir_exclusion(String origen, String destino)
	{
		int num = obtener_num_exclusiones();
		editor = exclusiones.edit();
		
		try
		{
			editor.putInt("num_exclusiones", num+1);
			editor.putString("origen"+(num+1), origen);
			editor.putString("destino"+(num+1), destino);
			editor.commit();
		}
		catch (Exception e)
		{
			Toast.makeText(context, "No se ha podido añadir la exclusión", Toast.LENGTH_SHORT).show();
		}
	}
	
	public boolean comprobar_si_existe(String selec1, String selec2)
	{
		int num = obtener_num_exclusiones();
    	String []origen = new String[num];
    	String []destino = new String[num];
    	origen = obtener_origen();
    	destino = obtener_destino();
    	boolean bandera = true;
    	
    	//Comprobamos si ya existe dicha exclusión para no repetirla
    	for (int i=0; i<num; i++)
    	{
    		if (selec1.equals(origen[i]))
    		{
    			if (selec2.equals(destino[i]))
    			{
    				bandera = false;//Ya existe esa exclusión
    				return bandera;
    			}
    			return bandera;
    		}
    	}
    	return bandera;
	}
	
	public String[] mostrar_exclusion()
	{
		//Obtenemos en dos listas los elementos origen y destino (quien no regala y a quien)
		String []lista1 = obtener_origen();
		String []lista2 = obtener_destino();
		
		//Unimos las dos listas para presentarlas en ver con unos aspectos visuales que lo muestren
		int num = obtener_num_exclusiones();
		String []lista = new String[num];
		
		for(int i=0; i<num; i++)
		{
			//Concatenación
			lista[i] = lista1[i] +" --(no)--> "+ lista2[i];
		}
		return lista;
	}
	
	public void eliminar_exclusion(int posicion)
	{
		editor = exclusiones.edit();
		int num = obtener_num_exclusiones();
		String []lista_origen = obtener_origen();
		String []lista_destino = obtener_destino();
		
		String []lista_origen_final = new String[num-1];
		String []lista_destino_final = new String[num-1];
		
		if (num == 1)
		{
			editor.clear();
			editor.putInt("num_exclusiones", 0);
			editor.commit();
		}
		else
		{		
			editor.clear();
			editor.putInt("num_exclusiones", 0);
			editor.commit();
			//Hacemos unas nuevas listas para mostrar borrando el elemento
			for (int i=0, j=0; i<(num-1); i++, j++)
			{
				if (i == (posicion-1))
				{
					j++;
				}
				lista_origen_final[i] = lista_origen[j];
				lista_destino_final[i] = lista_destino[j];
			}
			
			for (int i=0; i<(num-1); i++)
			{
				anadir_exclusion(lista_origen_final[i], lista_destino_final[i]);
			}
		}
	}
}

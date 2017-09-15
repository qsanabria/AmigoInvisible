package com.example.amigo_invisible.clases_ayuda;

import java.util.ArrayList;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Lista_desplegable_participantes 
{
	private ArrayList<String> alista;
	private ArrayAdapter<String> adapter;
	private Activity activity;
	
	public Lista_desplegable_participantes(Activity activity, ListView lista)//Constructor, pasamos contexto actual y la lista a la cual hacer los cambios
	{
		this.activity = activity;
		alista = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, alista);
		lista.setAdapter(adapter);
	}
	
	//Añade una cadena a la listView en la siguiente posicion
	public void anadir(String cadena)
	{
		alista.add(cadena);
		adapter.notifyDataSetChanged();
	}
	
	//Añade una cadena en la posición indicada
	public void anadir_posicion(int lugar, String cadena)
	{
		alista.add(lugar-1, cadena);
		adapter.notifyDataSetChanged();
	}
	
	//Elimina una posición específica
	public void eliminar_posicion(int lugar)
	{
		alista.remove(lugar-1);
		adapter.notifyDataSetChanged();
	}
	
	//Elimina una lista
	public void eliminar_lista()
	{
		adapter.clear();
	}
	
	//Devuelve el elemento de la pos en la lista
	public String obtener_elemento(int pos)
	{
		return (alista.get(pos));
	}
}

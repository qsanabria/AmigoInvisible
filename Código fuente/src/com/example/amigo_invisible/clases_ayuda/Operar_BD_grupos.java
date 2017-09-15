package com.example.amigo_invisible.clases_ayuda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.widget.Toast;

public class Operar_BD_grupos 
{
	private Context context;
	private String nombre_bd;
	private CursorFactory factory;
	private int version;
	SQLiteDatabase bd;
	private Crear_BD_grupos helper;
	
	//Pasamos los parámetros que llegan a parámetros locales
	public Operar_BD_grupos(Context context, String nombre_bd, CursorFactory factory, int version) 
	{
		this.context = context;
		this.nombre_bd = nombre_bd;
		this.factory = factory;
		this.version = version;
	}
	
	//Método para abrir la base de datos en modo lectura/escritura
	public Operar_BD_grupos abrirBD() throws SQLException
	{
		helper = new Crear_BD_grupos(context, nombre_bd, factory, version);
		bd = helper.getWritableDatabase();
		return this;
	}
		
	//Método para cerrar la base de datos y el helper
	public void cerrarBD() throws SQLException
	{		
		helper.close();
		bd.close();
	}
	
	//Inserta los datos de un grupo en la base de datos
	public boolean insertar(String nombre_grupo)
	{
		ContentValues registro = new ContentValues();
		Cursor cursor = bd.rawQuery("SELECT COUNT(*) FROM "+nombre_bd, null);
		int id = 1;
		
		if (comprobar_nombre(nombre_grupo))
		{
			if ((cursor.moveToFirst())&&(cursor.getInt(0)>0))//Movemos el cursor al principio y vemos si hay elementos
			{
				id = cursor.getInt(0)+1;
			}
		
			try
			{
				registro.put("_id", id);
				registro.put("nombre_grupo", nombre_grupo);
				bd.insert(nombre_bd, null, registro);
				Toast.makeText(context, "Grupo añadido", Toast.LENGTH_SHORT).show();
				return true;
			}
			catch (Exception e)
			{
				Toast.makeText(context, "Error añadiendo grupo", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		else
		{
			Toast.makeText(context, "El grupo ya existe", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	public String[] recuperar()
	{
		Cursor cursor = bd.rawQuery("SELECT COUNT(*) FROM "+nombre_bd, null);//Busca el número de elementos de la base de datos
		
		if ((cursor.moveToFirst())&&(cursor.getInt(0)>0))//Movemos el cursor al principio y vemos si hay elementos
		{
			int numero = cursor.getInt(0);//Obtenemos el número de elementos que hay
			String[] lista = new String[numero];//Creamos un array String de ese tamaño para los nombres
			int pos = 0;
			
			for(int i=1; i<=numero; i++)
    		{
        		//Rellenamos la lista con las ecuaciones obtenidas.
        		cursor = bd.rawQuery("SELECT nombre_grupo FROM "+nombre_bd+" WHERE _id="+i, null);
        		if(cursor.moveToFirst())
        		{
        			lista[pos] = cursor.getString(0);//Sacamos el nombre
        			pos++;
        		}
        	}
			cursor.close();
			return lista;
		}
		else
		{
			return null;
		}
	}
	
	//Elimina un registro de la base de datos
	public void eliminarBD(int pos)
	{
		Cursor cursor = bd.rawQuery("SELECT COUNT(*) FROM "+nombre_bd, null);
		int total;
		
		if((cursor.moveToFirst())&&(cursor.getInt(0)>0))
		{
			total = cursor.getInt(0);
			
			bd.execSQL("UPDATE "+nombre_bd+" SET _id = '999' where _id = '"+pos+"'");
			
			for (int i=pos+1; i<=total; i++)
			{
				bd.execSQL("UPDATE "+nombre_bd+" SET _id = '"+(i-1)+"' where _id = '"+i+"'");
			}
			bd.execSQL("DELETE FROM "+nombre_bd+" where _id = '999'");
		}
		cursor.close();
	}
	
	public boolean comprobar_nombre(String nombre)
	{
		Cursor cursor = bd.rawQuery("SELECT COUNT(*) FROM "+nombre_bd, null);
		int num;
		
		if (cursor.moveToFirst())
		{
			num = cursor.getInt(0);
			if (num > 0)
			{
				for(int i=1; i<=num; i++)
				{
					cursor = bd.rawQuery("SELECT nombre_grupo FROM "+nombre_bd+" WHERE _id="+i, null);
					cursor.moveToFirst();
					String cadena = cursor.getString(0);
					if (cadena.equals(nombre))
					{
						return false;
					}
				}
			}
			else
			{
				return true;
			}
		}
		else
		{
			Toast.makeText(context, "Error con el cursor", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}

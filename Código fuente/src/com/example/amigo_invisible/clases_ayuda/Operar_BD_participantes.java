package com.example.amigo_invisible.clases_ayuda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.widget.Toast;

public class Operar_BD_participantes 
{
	private Crear_BD_participantes helper;
	private Context context;
	private String nombre_bd;
	SQLiteDatabase bd;
	private CursorFactory factory;
	private int version;
	
	//Pasamos los parámetros que llegan a parámetros locales
	public Operar_BD_participantes(Context context, String nombre_bd, CursorFactory factory, int version) 
	{
		this.context = context;
		this.nombre_bd = nombre_bd;
		this.factory = factory;
		this.version = version;
	}
	
	//Método para abrir la base de datos en modo lectura/escritura
	public Operar_BD_participantes abrirBD() throws SQLException
	{
		helper = new Crear_BD_participantes(context, nombre_bd, factory, version);
		bd = helper.getWritableDatabase();
		return this;
	}
	
	//Método para cerrar la base de datos y el helper
	public void cerrarBD() throws SQLException
	{		
		helper.close();
		bd.close();
	}
	
	//Inserta los datos de un participante en la base de datos
	public boolean insertar(String nombre, String email, String numero)
	{
		ContentValues registro = new ContentValues();
		Cursor cursor = bd.rawQuery("SELECT COUNT(*) FROM "+nombre_bd, null);
		int id = 1;
		
		if (comprobar_nombre(nombre))
		{
			if ((cursor.moveToFirst())&&(cursor.getInt(0)>0))//Movemos el cursor al principio y vemos si hay elementos
			{
				id = cursor.getInt(0)+1;
			}
		
			try
			{
				registro.put("_id", id);
				registro.put("nombre", nombre);
				registro.put("email", email);
				registro.put("numero", numero);
				bd.insert(nombre_bd, null, registro);
				Toast.makeText(context, "Participante añadido", Toast.LENGTH_SHORT).show();
				return true;
			}
			catch (Exception e)
			{
				Toast.makeText(context, "Error añadiendo participante", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		else
		{
			Toast.makeText(context, "El nombre del participante ya existe", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	//Muestra a un participante de la BD para modificarlo
	public String mostrar(int pos_id, String elemento)
	{
		String retorno = null;

		Cursor cursor = bd.rawQuery("SELECT "+elemento+" FROM "+nombre_bd+" where _id = '"+pos_id+"'", null);
		if (cursor.moveToFirst())
		{
			retorno = cursor.getString(0);
		}
		cursor.close();
		return retorno;
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
        		cursor = bd.rawQuery("SELECT nombre FROM "+nombre_bd+" WHERE _id="+i, null);
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

	//Obtiene el email de un nombre pasado al método
	public String recuperar_email(String nombre)
	{
		String email = null;

		Cursor cursor = bd.rawQuery("SELECT email FROM "+nombre_bd+" where nombre = '"+nombre+"'", null);
		if (cursor.moveToFirst())
		{
			email = cursor.getString(0);
		}
		cursor.close();
		return email;
	}
	
	//Obtiene el telefono de un nombre pasado
	public String recuperar_telefono(String nombre)
	{
		String numero = null;

		Cursor cursor = bd.rawQuery("SELECT numero FROM "+nombre_bd+" where nombre = '"+nombre+"'", null);
		if (cursor.moveToFirst())
		{
			numero = cursor.getString(0);
		}
		cursor.close();
		return numero;
	}
	
	//Modifica los datos de un participante
	public boolean actualizar(String nombre, String email, String numero, int pos_id) 
	{
		try
		{
			bd.execSQL("UPDATE "+nombre_bd+" SET nombre = '"+nombre+"', email = '"+email+"', numero = '"+numero+"' where _id ="+pos_id);
			return true;
		}
		catch (Exception e)
		{
			Toast.makeText(context, "No se pudo actualizar el participante", Toast.LENGTH_SHORT).show();
			return false;
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
	
	//Método para ver si la base de datos contiene 3 elementos o mas
	public int num_elementos()
	{
		Cursor cursor = bd.rawQuery("SELECT COUNT(*) FROM "+nombre_bd, null);
		int num;
		
		if((cursor.moveToFirst())&&(cursor.getInt(0)>0))
		{
			num = cursor.getInt(0);
			cursor.close();
			return num;
		}
		else
		{
			cursor.close();
			return 0;
		}
	}
	
	public void eliminar_filas_BD()
	{
		bd.delete(nombre_bd, null, null);
	}
	
	public void elimiarBD_completa()
	{	
		context.deleteDatabase(nombre_bd);
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
					cursor = bd.rawQuery("SELECT nombre FROM "+nombre_bd+" WHERE _id="+i, null);
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

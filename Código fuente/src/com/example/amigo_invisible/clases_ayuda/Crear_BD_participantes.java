package com.example.amigo_invisible.clases_ayuda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Crear_BD_participantes extends SQLiteOpenHelper 
{
	private String nombre;

	public Crear_BD_participantes(Context context, String nombre, CursorFactory factory, int version) 
	{
		//Llama al constructor y pasa a la llamada de la superclase los parámetros
		super(context, nombre, factory, version);
		this.nombre = nombre;
	}

	//Crea la base de datos si no existe con los campos especificados y sus restricciones
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("CREATE TABLE "+nombre+" (_id INTEGER PRIMARY KEY, nombre TEXT NOT NULL, email TEXT NOT NULL, numero TEXT)");
	}

	//En el caso de actualizaciones de la base de datos borra y la vuelve a crear
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS "+nombre);
		onCreate(db);
	}

}

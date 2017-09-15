package com.example.amigo_invisible.clases_ayuda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Crear_BD_grupos extends SQLiteOpenHelper 
{

	public Crear_BD_grupos(Context context, String nombre, CursorFactory factory, int version) 
	{
		//Llama al constructor y pasa a la llamada de la superclase los parámetros
		super(context, nombre, factory, version);
	} 

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		// TODO Apéndice de método generado automáticamente
		db.execSQL("CREATE TABLE grupos (_id INTEGER PRIMARY KEY, nombre_grupo TEXT NOT NULL)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		// TODO Apéndice de método generado automáticamente
		db.execSQL("DROP TABLE IF EXISTS grupos");
		onCreate(db);
	}

}

package com.example.amigo_invisible.clases_ayuda;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.widget.Toast;

public class Acceder_Agenda 
{
	private Activity activity;
	private int codigo = 999;
	
	public Acceder_Agenda(Activity activity)//Constructor al cúal pasamos el activity actual
	{
		this.activity = activity;
	}
	
	//Abre la agenda y llama a un startActivityForResult para recoger datos de la acción al pulsar el contacto al volver
	public void seleccionarContacto()
	{
		Intent contacto = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
		this.activity.startActivityForResult(contacto, codigo);
	}
	
	//Método del onActivityForResult donde según la selección de la agenda extrae los datos del contacto
	//este es un metodo para llamarlo que tiene los datos del contacto
	public String onActivityResult(int requestCode, int resultCode, Intent data, String tipo)
	{
		Uri datos;
		String id;
		Cursor cursor = null;
		int tlfIndex, emailIndex;
		String retorno = "";
			
		if (requestCode == codigo)//Debe coincidir el requestCode con el codigo pasado arriba
		{
			if(resultCode == Activity.RESULT_OK)//El activityForResult ha tenido éxito
			{
				datos = data.getData();//Obtiene del intent un Uri para sacar los datos
				id = datos.getLastPathSegment();//Obtiene un id de la selección en la agenda
					
				try
				{
					if (tipo.equals("telefono"))//Obtenemos el teléfono
					{
						cursor = this.activity.getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID+"=?", new String[]{id}, null);//Realiza una consulta
						tlfIndex = cursor.getColumnIndex(Phone.DATA);//Obtiene el indice de donde está el número de teléfono	
						if (cursor.moveToFirst())
						{
							retorno = cursor.getString(tlfIndex);//Copia el número de teléfono
						}
					}
					if (tipo.equals("email"))//Obtenemos el email
					{
						cursor = this.activity.getContentResolver().query(Email.CONTENT_URI, null, Email.CONTACT_ID+"=?", new String[]{id}, null);
						emailIndex = cursor.getColumnIndex(Email.DATA);//Obtiene el indice de donde está el email	
						if (cursor.moveToFirst())
						{
							retorno = cursor.getString(emailIndex);//Copia el email
						}
					}
					if (tipo.equals("nombre"))//Obtenemos el nombre
					{
						cursor = this.activity.getContentResolver().query(datos, null, null, null, null);//acceso con query
						int nombreIndex = cursor.getColumnIndex(Contacts.DISPLAY_NAME);//Obtencion del índice de la columna del nombre
						if (cursor.moveToFirst())
						{
							retorno = cursor.getString(nombreIndex);//Obtención del nombre a partir del índice
						}
					}
				}
				catch(Exception e)
				{
					Toast.makeText(activity, "Error accediendo al contacto de la agenda", Toast.LENGTH_SHORT).show();
				}
				finally//Parte final del try-catch que cierra el cursor si está abierto
				{
					if (cursor != null)
					{
						cursor.close();
					}
				}
			}
		}
		return retorno;//Devuelve el dato obtenido(nombre del contacto, email, número)
	}
}

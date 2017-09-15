package com.example.amigo_invisible;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amigo_invisible.clases_ayuda.Acceder_Agenda;
import com.example.amigo_invisible.clases_ayuda.Comprueba_EditText;
import com.example.amigo_invisible.clases_ayuda.Operar_BD_participantes;

/**
 * Clase que recoge los datos de un participante que quiere ser añadido a el grupo
 * @author Quintin Sanabria Sánchez
 */

public class Nuevo_participante extends Activity 
{
	private int pos_id = 0;
	private String nombre_grupo;
	private Operar_BD_participantes bd;
	private Acceder_Agenda acceso = new Acceder_Agenda(this);
	private EditText et_nombre_participante, et_email_participante, et_telefono_participante;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//Quita el título de la aplicación
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//Quita la barra de notificaciones
		
		setContentView(R.layout.nuevo_participante);//Asigna a .java la intefaz del xml de numero_grupos
		
		et_nombre_participante = (EditText)findViewById(R.id.et_nombre_participante);
		et_email_participante = (EditText)findViewById(R.id.et_email_participante);
		et_telefono_participante = (EditText)findViewById(R.id.et_numero_participante);
		
		Bundle bolsa = getIntent().getExtras();
		pos_id = bolsa.getInt("pos_id");
		nombre_grupo = bolsa.getString("nombre_grupo").toString();
		
		if (pos_id > 0)
		{
			bd = new Operar_BD_participantes(this, nombre_grupo, null, 1);
			
			try
			{
				bd.abrirBD();
				String nombre = bd.mostrar(pos_id, "nombre");
				String email = bd.mostrar(pos_id, "email");
				String tlf =bd.mostrar(pos_id, "numero");
				bd.cerrarBD();
				
				et_nombre_participante.setText(nombre);
				et_email_participante.setText(email);
				et_telefono_participante.setText(tlf);
			}
			catch (Exception e)
			{
				Toast.makeText(this, "Error mostrando contacto para modificar", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * Se encarga de volver a la pantalla del grupo
	 */
	public void atras(View view)
	{
		finish();
	}
	
	/**
	 * Se encarga de abrir la agenda del teléfono y seleccionar un contacto para añadir sus datos
	 */
	//Añadir participante con los datos de la agenda del teléfono
	public void desde_agenda(View view)
	{
		acceso.seleccionarContacto();//Llamada a la clase que realiza la apertura de agenda
	}

	/**
	 * Se encarga de cargar los datos del contacto de la agenda en los recuadros de introducción
	 */
	//Método onActivityResult que es lanzado al volver de la activity desde agenda
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
	       super.onActivityResult(requestCode, resultCode, data);
	       //Llamadas al onActivityResult de la agenda para obtener datos
	       String tlf = acceso.onActivityResult(requestCode, resultCode, data, "telefono");
	       String email = acceso.onActivityResult(requestCode, resultCode, data, "email");
	       String nombre = acceso.onActivityResult(requestCode, resultCode, data, "nombre");
	       
	       //Ponemos en los EditText los datos obtenidos
	       et_telefono_participante.setText(tlf);
	       et_email_participante.setText(email);
	       et_nombre_participante.setText(nombre);
	}
	
	/**
	 * Se encarga de verificar los campos de los datos y añadir el participante a la BD
	 */
	//Añadir el participante a la listView del grupo
	public void agregar(View view)
	{
		Comprueba_EditText comp = new Comprueba_EditText();
		
		if (comp.comprobar(et_nombre_participante) && comp.comprobar(et_email_participante))
		{
			//Pasamos a la actividad anterior a través de un Intent
			Intent i = new Intent(this, Nuevo_participante.class);
			i.putExtra("nombre_participante", et_nombre_participante.getText().toString());
			i.putExtra("email_participante", et_email_participante.getText().toString());
			i.putExtra("telefono_participante", et_telefono_participante.getText().toString());
			String posi = Integer.toString(pos_id);
			i.putExtra("posi", posi);
			setResult(Activity.RESULT_OK, i);
			Nuevo_participante.this.finish();//Finalizamos la actividad
		}
	}
}

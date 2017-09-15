package com.example.amigo_invisible;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.example.amigo_invisible.clases_ayuda.Evento_agenda;
import com.example.amigo_invisible.clases_ayuda.Generar_sorteo;
import com.example.amigo_invisible.clases_ayuda.Lista_desplegable_participantes;
import com.example.amigo_invisible.clases_ayuda.Operar_BD_participantes;

/**
 * Clase que recoge los datos de un participante que quiere ser a�adido a el grupo
 * @author Quintin Sanabria S�nchez
 */

public class Datos_finales extends Activity
{
	private TextView text_evento, text_lugar, text_fecha, text_hora, text_tematica, text_precios, text_participantes;
	private String evento, lugar, fecha, hora, tematica, p_min, p_max, total, nombre_grupo;
	private ListView lista;
	private Lista_desplegable_participantes desplegable;
	private String []lista_nombres;
	private String []pasar_datos = {"","","","","",""};
	private Operar_BD_participantes bd;
	private int i;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//Quita el t�tulo de la aplicaci�n
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//Quita la barra de notificaciones
		
		setContentView(R.layout.datos_finales);//Asigna a .java la intefaz del xml datos_finales
		
		//Obtenemos las referencias
		text_evento = (TextView)findViewById(R.id.tv_evento);
		text_lugar = (TextView)findViewById(R.id.tv_lugar);
		text_fecha = (TextView)findViewById(R.id.tv_ver_fecha);
		text_hora = (TextView)findViewById(R.id.tv_ver_hora);
		text_tematica = (TextView)findViewById(R.id.tv_ver_tematica);
		text_precios = (TextView)findViewById(R.id.tv_precios);
		lista = (ListView)findViewById(R.id.lv_ver_participantes);
		text_participantes = (TextView)findViewById(R.id.tv_ver_participantes);
		
		Bundle bolsa = getIntent().getExtras();//Obtenemos la bolsa a la que hemos pasado el nombre del grupo
		nombre_grupo = bolsa.getString("nombre_grupo").toString();//Cogemos el nombre del grupo de la bolsa
		
		//Nueva instancia de la clase ListaDesplegable para crear un lisview y poder a�adir los elementos para verlos(participantes)
		desplegable = new Lista_desplegable_participantes(this, lista);
		
		bd = new Operar_BD_participantes(this, nombre_grupo, null, 1);//Abrimos la base de datos del grupo
		
		//Comprobamos que existen o no elementos en la base de datos
		bd.abrirBD();//Abrimos la BD
		lista_nombres = bd.recuperar();//Almacenamos en una variable String los nombres que hay en la BD
		if (lista_nombres != null)//Si hay elementos almacenados...
		{
			for (i=0; i<lista_nombres.length; i++)
			{
				String cadena = lista_nombres[i];
				desplegable.anadir(cadena);//A�adimos a la listView
			}
			i = lista_nombres.length;
		}
		else
		{
			i = 0;
		}
		bd.cerrarBD();//Cerramos la BD
		text_participantes.setText("Participantes("+i+"):");//Actualizamos el n�mero de participantes que hay
		
		//Obtenemos los datos del SharedPreferences del evento
		SharedPreferences pref_evento = getSharedPreferences("evento",Context.MODE_PRIVATE);
		evento = pref_evento.getString("nombre_evento", "");
		pasar_datos[0] += evento;
		lugar = pref_evento.getString("lugar_evento", "");
		pasar_datos[1] += lugar;
		fecha = pref_evento.getString("fecha_evento", "");
		pasar_datos[2] += fecha;
		hora = pref_evento.getString("hora_evento", "");
		pasar_datos[3] += hora;
		tematica = pref_evento.getString("tematica", "");
		pasar_datos[4] += tematica;
		p_min = pref_evento.getString("p_min", "");
		p_max = pref_evento.getString("p_max", "");
		total = "";
		
		if (tematica.equals(""))//Para poner el texto si no hay tema
		{
			tematica+="A elecci�n de los participantes";
			pasar_datos[4] += tematica;
		}
		
		//Comprobaci�n de precios para ponerlos en pantalla
		if (!(p_min.equals("")) && !(p_max.equals("")))
		{
			total+="De "+p_min+"� a "+p_max+"�";
			pasar_datos[5] += total;
		}
		else if (!(p_min.equals("")) && (p_max.equals("")))
		{
			total+="M�s de "+p_min+"�";
			pasar_datos[5] += total;
		}
		else if (p_min.equals("") && !(p_max.equals("")))
		{
			total+="Menos de "+p_max+"�";
			pasar_datos[5] += total;
		}
		else
		{
			total+="Sin definir";
			pasar_datos[5] += total;
		}
		
		//Ponemos los datos en los TextView
		text_evento.setText(evento);
		text_lugar.setText(lugar);
		text_fecha.setText(fecha);
		text_hora.setText(hora);
		text_tematica.setText(tematica);
		text_precios.setText(total);
		
		final Evento_agenda ana_agen = new Evento_agenda(this);
		
		AlertDialog.Builder even = new AlertDialog.Builder(this);
		even.setIcon(android.R.drawable.ic_dialog_info);
		even.setTitle("A�adir evento al calendario");
		even.setMessage("�Desea a�adir el evento al calendario del tel�fono?");
		even.setPositiveButton("S�", new OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface arg0, int arg1) 
			{
				ana_agen.anadir_evento_calendario(evento, lugar, fecha, hora);
			}
		});
		even.setNegativeButton("No", null);
		even.show();
	}
	
	/**
	 * Se encarga de volver a la pantalla exclusiones
	 */
	public void atras(View v)
	{
		Intent i = new Intent(this, Exclusiones.class);
		i.putExtra("primera_vez", false);//Flag pasado para no inicializar el archivo de preferencias de exclusiones
		Datos_finales.this.finish();//Finalizamos la actividad
	}
	
	/**
	 * Se encarga de volver a la pantalla exclusiones
	 */
	public void onBackPressed()//Para el pulsamiento de atr�s
	{
		View v = new View(this);
		atras(v);
	}
	
	/**
	 * Se encarga de mostrar un cuadro de di�logo para preguntar si se desea mandar la informaci�n
	 * a trav�s de SMS
	 */
	public void generar_sorteo(View v)
	{
		AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
		dialogo.setIcon(android.R.drawable.ic_dialog_info);
		dialogo.setTitle("Realizando Sorteo...");
		dialogo.setMessage(R.string.sorteo_movil);
		dialogo.setPositiveButton("Si", new OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) 
			{
				boolean movil = true;//Si mandamos por m�vil
				sorteo(movil);
			}
		});
		dialogo.setNegativeButton("No", new OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				boolean movil = false;//No mandamos por m�vil
				sorteo(movil);
			}
		});
		dialogo.show();
	}
	
	/**
	 * Se encarga de realizar la asignaci�n entre los participantes, enviar los datos y borrar el evento
	 * finalizado
	 */
	public void sorteo(boolean movil)
	{
		boolean valor;
		Generar_sorteo resultado = new Generar_sorteo(this, nombre_grupo);
		valor = resultado.sortear(pasar_datos, movil);
		
		if (valor == true)
		{	
			SharedPreferences evento = getSharedPreferences("evento", Context.MODE_PRIVATE);
			Editor editor = evento.edit();
			editor.clear();
			editor.commit();
			
			
			AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
			dialogo.setIcon(android.R.drawable.ic_dialog_info);
			dialogo.setTitle("�Evento Realizado!");
			dialogo.setMessage("Si desea realizar otro evento vuelva a la pantalla para introducir " +
					"otro, sino salga de la aplicaci�n");
			dialogo.setNeutralButton("Aceptar", new OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface arg0, int arg1) 
				{
					System.runFinalization();
					System.exit(0);
				}
			});
			dialogo.show();
		}
	}
}

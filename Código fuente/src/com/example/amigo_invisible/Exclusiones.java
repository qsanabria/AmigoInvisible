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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.amigo_invisible.clases_ayuda.Exclusiones_preferencias;
import com.example.amigo_invisible.clases_ayuda.Generar_sorteo;
import com.example.amigo_invisible.clases_ayuda.Operar_BD_participantes;

/**
 * Clase que recoge los datos de un participante que quiere ser a�adido a el grupo
 * @author Quintin Sanabria S�nchez
 */

public class Exclusiones extends Activity implements android.widget.AdapterView.OnClickListener
{
	private Button boton_info, a�adir, ver;
	private boolean primera_vez;
	private CheckBox no_exclusiones, si_exclusiones, ambos_sentidos;
	private String nombre_grupo;
	private Operar_BD_participantes bd;
	private String []lista_nombres;
	private String []lista;
	private Spinner spinner1, spinner2;
	private ArrayAdapter<String> adapter1, adapter2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//Quita el t�tulo de la aplicaci�n
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//Quita la barra de notificaciones
		
		setContentView(R.layout.exclusiones);//Asigna a .java la intefaz del xml exclusiones
		
		boton_info = (Button)findViewById(R.id.b_info_exclusiones);
		a�adir = (Button)findViewById(R.id.b_anadir_exclusion);
		ver = (Button)findViewById(R.id.b_ver_exclusiones);
		boton_info.setBackgroundResource(android.R.drawable.ic_dialog_info);//Bot�n para informaci�n de que hacer en exclusiones
		boton_info.setOnClickListener(this);
		
		no_exclusiones = (CheckBox)findViewById(R.id.cb_no_exclusiones);
		si_exclusiones = (CheckBox)findViewById(R.id.cb_si_exclusiones);
		ambos_sentidos = (CheckBox)findViewById(R.id.cb_ambos_sentidos);
		
		Bundle bolsa = getIntent().getExtras();//Obtenemos la bolsa a la que hemos pasado el nombre del grupo
		nombre_grupo = bolsa.getString("nombre_grupo").toString();//Cogemos el nombre del grupo de la bolsa
		primera_vez = bolsa.getBoolean("primera_vez");
		
		if (primera_vez)
		{
			//Limpia el archivo SharedPreferences cada vez que se carga la activity (por si acaso)
			SharedPreferences exclusiones=getSharedPreferences("exclusiones",Context.MODE_PRIVATE);
			Editor editor = exclusiones.edit();
			editor.clear().commit();
			editor.putInt("num_exclusiones", 0);
			editor.commit();
		}
		
		bd = new Operar_BD_participantes(this, nombre_grupo, null, 1);
		bd.abrirBD();
		lista_nombres = bd.recuperar();//Obtenemos los nombres de los participantes del grupo
		bd.cerrarBD();
		
		//Copiamos la lista para a�adir en otra cadena una cadena vac�a al principio
		lista = new String[lista_nombres.length + 1];
		lista[0] = "";
		for(int i=0; i<lista_nombres.length; i++)
		{
			lista[i + 1] = "";
			lista[i + 1] = "" + lista_nombres[i];
		}
		
		//Spinners y sus adapter con los nombres de los participantes del grupo
		spinner1 = (Spinner)findViewById(R.id.spinner1);
		adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, lista);
        spinner1.setAdapter(adapter1);
        spinner1.setEnabled(false);
        
        spinner2 = (Spinner)findViewById(R.id.spinner2);
		adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, lista);
        spinner2.setAdapter(adapter2);
        spinner2.setEnabled(false);        
        
        //Inicializaci�n de elementos no activados
        ambos_sentidos.setEnabled(false);
        a�adir.setEnabled(false);
        ver.setEnabled(false);
	}
	
	/**
	 * Se encarga de mostrar una peque�a ayuda
	 */
	@Override
	public void onClick(View v)//M�todo para la pulsaci�n del bot�n info
	{
		AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
		dialogo.setTitle("Exclusiones");
		dialogo.setIcon(android.R.drawable.ic_dialog_info);
		dialogo.setMessage(R.string.info_exclusiones);//A�adimos el texto del cuadro de di�logo
		dialogo.setNegativeButton("Aceptar", null);
		dialogo.show();
	}
	
	/**
	 * Se encarga de volver a selecci�n de grupos
	 */
	public void onBackPressed()//Atr�s del tel�fono
	{
		View v = new View(this);
		atras(v);
	}
	
	/**
	 * Se encarga de volver a selecci�n de grupos
	 */
	public void atras(View v)
	{
		AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
		dialogo.setTitle("�Salir de la pantalla \nexclusiones?");
		dialogo.setIcon(android.R.drawable.ic_dialog_info);
		dialogo.setMessage("Si vuelve atr�s los datos aplicados de las exclusiones ser�n borrados");//A�adimos el texto del cuadro de di�logo
		dialogo.setPositiveButton("Atr�s", new OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				//Vuelve atr�s y borra el contenido de SharedPreferences
				SharedPreferences exclusiones=getSharedPreferences("exclusiones",Context.MODE_PRIVATE);
				Editor editor = exclusiones.edit();
				editor.clear().commit();
				finish();
			}
		});
		dialogo.setNegativeButton("Cancelar", new OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				//No hace nada
			}
		});
		dialogo.show();
	}
	
	/**
	 * Se encarga de realizar acciones al seleccionar "No realizar exclusiones"
	 */
	public void click_no(View v)//M�todo para el click en el checkbox no realizar exclusiones
	{
		//Elementos activos o no
		no_exclusiones.setChecked(true);
		si_exclusiones.setChecked(false);
		spinner1.setEnabled(false);
		spinner2.setEnabled(false);
		ambos_sentidos.setEnabled(false);
		a�adir.setEnabled(false);
		ambos_sentidos.setChecked(false);
		ver.setEnabled(false);
		spinner1.setAdapter(adapter1);
		spinner2.setAdapter(adapter2);
	}
	
	/**
	 * Se encarga de realizar acciones al seleccionar "Realizar exclusiones"
	 */
	public void click_si(View v)//M�todo para el click en el checkbox si realizar exclusiones
	{
		//Elementos activos o no
		si_exclusiones.setChecked(true);
		no_exclusiones.setChecked(false);
		spinner1.setEnabled(true);
		spinner2.setEnabled(true);
		ambos_sentidos.setEnabled(true);
		a�adir.setEnabled(true);
		ver.setEnabled(true);
	}
	
	/**
	 * Se encarga de mostrar las exclusiones realizadas
	 */
	public void ver_exclusiones(View v)
	{
		Exclusiones_preferencias ex = new Exclusiones_preferencias(this);
		
		//Llamada al m�todo de concatenaci�n para mostrar las exclusiones
		String []lista_ver = ex.mostrar_exclusion();
		
		//Dialogo para mostrar las exclusiones ya hechas del sharedpreferences
		final AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
		dialogo.setTitle("Exclusiones");
		dialogo.setItems(lista_ver, new OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int posicion)
			{
				elemento(posicion+1);
			}
		});//Mostramos
		
		dialogo.setNegativeButton("Atr�s", null);
		
		dialogo.setPositiveButton("Borrar", new OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) 
			{
				//Borra todas las exclusiones (lista y SharedPreferences)
				dialogo.setItems(null, null);
				SharedPreferences exclusiones=getSharedPreferences("exclusiones",Context.MODE_PRIVATE);
				Editor editor = exclusiones.edit();
				editor.clear().commit();
				editor.putInt("num_exclusiones", 0);
				editor.commit();
			}
		});
		
		dialogo.show();
	}
	
	/**
	 * Se encarga de preguntar si se desea borrar una exclusi�n al pinchar sobre ella
	 */
	//M�todo lanzado al pulsar un elemento de la lista de exclusiones
	public void elemento(int posicion)
	{
		final Exclusiones_preferencias ex = new Exclusiones_preferencias(this);
		final int pos = posicion;
		final AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
		dialogo.setTitle("�Desea eliminar\nla exclusi�n?");
		dialogo.setIcon(android.R.drawable.ic_dialog_info);
		dialogo.setNegativeButton("Cancelar", null);
		dialogo.setPositiveButton("Eliminar", new OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				//Pulsamos eliminar
				ex.eliminar_exclusion(pos);
				Toast.makeText(getBaseContext(), "Exclusi�n eliminada", Toast.LENGTH_SHORT).show();
			}
		});
		dialogo.show();
	}
	
	/**
	 * A�ade una exclusi�n a la lista
	 */
	public void anadir_exclusion(View v)
	{
		Exclusiones_preferencias ex = new Exclusiones_preferencias(this);
		//M�ltiples selecciones para las opciones v�lidas o no de las exclusiones
		Generar_sorteo gene = new Generar_sorteo(this, nombre_grupo);
        String selec1=spinner1.getSelectedItem().toString();
        int posicion1 = (spinner1.getSelectedItemPosition())-1;
        String selec2=spinner2.getSelectedItem().toString();
        int posicion2 = (spinner2.getSelectedItemPosition())-1;
        boolean bandera, derecha = false, izquierda = false;
        
        if (selec1.equals("") || selec2.equals(""))
        {
        	Toast.makeText(getBaseContext(), "Debe elegir nombres v�lidos", Toast.LENGTH_SHORT).show();
        }
        else if(selec1.equals(selec2))
        {
        	Toast.makeText(getBaseContext(), "Exclusiones de uno mismo \nrealizadas por defecto", Toast.LENGTH_SHORT).show();
        }
        else
        {        	
        	if (ambos_sentidos.isChecked())
        	{
        		//Comprobamos si ya existe la exclusi�n hacia uno de los dos lados o hacia los dos
        		if (ex.comprobar_si_existe(selec1, selec2)==false)
        		{
        			//Existe hacia la derecha
        			derecha = true;
        		}
        		if (ex.comprobar_si_existe(selec2, selec1)==false)
        		{
        			//Existe hacia la izquierda
        			izquierda = true;
        		}
        		
        		//Realizamos la inclusi�n en la lista de los elementos que no haya
        		if ((derecha==true) && (izquierda==true))
        		{
        			//Existe para los dos sentidos
        			Toast.makeText(getBaseContext(), "Ya existe la exclusi�n hacia los dos sentidos", Toast.LENGTH_SHORT).show();
        		}
        		
        		if ((derecha==true) && (izquierda==false))
        		{
        			//Si existe a derecha a�adir hacia izquierda
        			String [][]como_regalar = new String[2][lista_nombres.length];
        			Generar_sorteo gen = new Generar_sorteo(this, nombre_grupo);
        			
        			ex.anadir_exclusion(selec2, selec1);
        			
        			como_regalar = gen.comp_exclusiones();
        			if (como_regalar == null)
        			{
        				int num = ex.obtener_num_exclusiones();
        				ex.eliminar_exclusion(num);
        			}
        			else
        			{
        				Toast.makeText(getBaseContext(), "Exclusi�n realizada (dos sentidos)", Toast.LENGTH_SHORT).show();
        			}
        		}
        		if ((derecha==false) && (izquierda==true))
        		{
        			//Si existe a izquierda a�adir hacia derecha
        			String [][]como_regalar = new String[2][lista_nombres.length];
        			Generar_sorteo gen = new Generar_sorteo(this, nombre_grupo);
        			
        			ex.anadir_exclusion(selec1, selec2);
        			
        			como_regalar = gen.comp_exclusiones();
        			if (como_regalar == null)
        			{
        				int num = ex.obtener_num_exclusiones();
        				ex.eliminar_exclusion(num);
        			}
        			else
        			{
        				Toast.makeText(getBaseContext(), "Exclusi�n realizada (dos sentidos)", Toast.LENGTH_SHORT).show();
        			}
        		}
        		if ((derecha==false) && (izquierda==false))
        		{
        			//No existe dicha exclusi�n hacia ningun lado
        			String [][]como_regalar = new String[2][lista_nombres.length];
        			Generar_sorteo gen = new Generar_sorteo(this, nombre_grupo);
        			
        			ex.anadir_exclusion(selec1, selec2);
        			ex.anadir_exclusion(selec2, selec1);
        			
        			como_regalar = gen.comp_exclusiones();
        			if (como_regalar == null)
        			{
        				int num = ex.obtener_num_exclusiones();
        				ex.eliminar_exclusion(num);
        				int num1 = ex.obtener_num_exclusiones();
        				ex.eliminar_exclusion(num1);
        			}
        			else
        			{
        				Toast.makeText(getBaseContext(), "Exclusi�n realizada (dos sentidos)", Toast.LENGTH_SHORT).show();
        			}	
        		}
        	}
        	else
        	{
        		bandera = ex.comprobar_si_existe(selec1, selec2);
        		
            	if (bandera)//Si no existe la exclusi�n
            	{
            		Exclusiones_preferencias exc = new Exclusiones_preferencias(this);
            		if (exc.obtener_num_exclusiones() == 0)
            		{
            			ex.anadir_exclusion(selec1, selec2);
                		Toast.makeText(getBaseContext(), "Exclusi�n realizada", Toast.LENGTH_SHORT).show();
            		}
            		else
            		{
            			String [][]como_regalar = new String[2][lista_nombres.length];
            			Generar_sorteo gen = new Generar_sorteo(this, nombre_grupo);
            			
            			ex.anadir_exclusion(selec1, selec2);
            			
            			como_regalar = gen.comp_exclusiones();
            			if (como_regalar == null)
            			{
            				int num = ex.obtener_num_exclusiones();
            				ex.eliminar_exclusion(num);
            			}
            			else
            			{
            				Toast.makeText(getBaseContext(), "Exclusi�n realizada", Toast.LENGTH_SHORT).show();
            			}
            		}
            	}
            	else
            	{
            		Toast.makeText(getBaseContext(), "Ya existe esa exclusi�n (ver exclusiones)", Toast.LENGTH_SHORT).show();
            	}
        	}
        }
	}
	
	/**
	 * Se encarga de comprobar en el momento de realizar la exclusi�n si hay posibilidades de realizar el sorteo
	 */
	public int[] comp_posibilidad(int []regalado, String []origen, String []destino, int parti)
	{
		regalado[parti]=2;//A si mismo no se puede
		
		for (int i=0; i<origen.length; i++)//Bucle para pasar por todos los nombres de origen
		{
			if (lista_nombres[parti].equals(origen[i]))//Si coincide el participante con un nombre origen tiene exclusi�n
			{
				String nombre = destino[i];//Guardamos a quien no puede regalar
				for(int j=0; j<lista_nombres.length; j++)//Bucle para buscar el participante en la lista
				{
					if (lista_nombres[j].equals(nombre))//Cuando encontremos su posici�n
					{
						if (regalado[j] == 0)//En regalado, si puede ser regalado ponemos 2
						{
							regalado[j] = 2;
						}
						else
						{
							
						}
					}
				}
			}
		}
		
		//Comprobaci�n de la validez de las exclusiones
		boolean posible = false;
		for (int j=0; j<regalado.length; j++)
		{
			if (regalado[j] == 0)//Si existe alg�n elemento a 0 se puede asociar a �l, con lo cual puede hacerse el sorteo de este elemento
			{
				posible = true;
			}
		}
				
		if (posible)//Si se pueden hacer exclusiones devolver� regalado para el sorteo
		{
			return regalado;
		}
		else//Si no hay participantes a ser elegidos, devolver� null, con lo cual no podremos realizar el sorteo
		{
			return null;
		}
	}
	
	/**
	 * Se encarga de avanzar hasta la pantalla de generaci�n de sorteo
	 */
	public void siguiente_exclusiones(View v)
	{
		if (no_exclusiones.isChecked())
		{
			SharedPreferences exclusiones=getSharedPreferences("exclusiones",Context.MODE_PRIVATE);
			Editor editor = exclusiones.edit();
			editor.clear().commit();
			editor.putInt("num_exclusiones", 0);
			editor.commit();
		}
		Intent datos_finales = new Intent(this, Datos_finales.class);
		datos_finales.putExtra("nombre_grupo", nombre_grupo);
		startActivity(datos_finales);
	}
}

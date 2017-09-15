package com.example.amigo_invisible;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amigo_invisible.clases_ayuda.Comprueba_EditText;
import com.example.amigo_invisible.clases_ayuda.Lista_desplegable_participantes;
import com.example.amigo_invisible.clases_ayuda.Operar_BD_grupos;
import com.example.amigo_invisible.clases_ayuda.Operar_BD_participantes;

/**
 * Clase que muestra los grupos introducidos y permite la creación de nuevos
 * @author Quintin Sanabria Sánchez
 */

public class Numero_grupos extends Activity implements android.widget.AdapterView.OnItemClickListener
{
	private EditText et_grupo_seleccionado;
	private int codigo1 = 998, codigo2 = 997;
	Lista_desplegable_participantes desplegable_grupos;
	private ListView lista_grupos;
	private Operar_BD_grupos bd;
	private Operar_BD_participantes bd_parti;
	private TextView grupos;
	private int i, j;
	private String []lista_nombres;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//Quita el título de la aplicación
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//Quita la barra de notificaciones
		
		setContentView(R.layout.numero_grupos);//Asigna a .java la intefaz del xml de numero_grupos
		
		et_grupo_seleccionado = (EditText)findViewById(R.id.et_grupo_seleccionado);
		grupos = (TextView)findViewById(R.id.tv_numero_grupos);
		lista_grupos = (ListView)findViewById(R.id.lv_grupos);
		
		bd = new Operar_BD_grupos(this, "grupos", null, 1);//Abrimos la base de datos de los grupos si existe o la crea
		
		//Nueva instancia de la clase ListaDesplegable para crear un lisview y poder añadir elementos
		desplegable_grupos = new Lista_desplegable_participantes(this, lista_grupos);
		
		//Comprobamos que existen o no elementos en la base de datos
		bd.abrirBD();//Abrimos la BD
		lista_nombres = bd.recuperar();//Almacenamos en una variable String los nombres que hay en la BD
		if (lista_nombres != null)//Si hay elementos almacenados...
		{
			for (i=0; i<lista_nombres.length; i++)
			{
				String cadena = lista_nombres[i];
				desplegable_grupos.anadir(cadena);//Añadimos a la listView
			}
			i = lista_nombres.length;
		}
		else
		{
			i = 0;
		}
		bd.cerrarBD();//Cerramos la BD
		grupos.setText("Grupos("+i+"):");//Actualizamos el número de participantes que hay
		
		lista_grupos.setOnItemClickListener(this);//Funcionalidad para el pulsamiento del listView
	}
	
	/**
	 * Se encarga de pedir un nombre de grupo para su creación
	 * @param view
	 */
	public void crear_grupo(View view)
	{	
		final Intent crear_grupo = new Intent(this, Nuevo_grupo.class);
		
		AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
		dialogo.setTitle("Nombre del Grupo:");
		dialogo.setIcon(android.R.drawable.ic_input_get);
		final EditText grupo = new EditText(this);//creamos un EditText
		dialogo.setView(grupo);
		
		dialogo.setPositiveButton("Continuar", new  DialogInterface.OnClickListener() 
		{ // si le das al si
		    public void onClick(DialogInterface dialog, int whichButton) 
		    {
		    	//Lineas para ocultar el teclado virtual (Hide keyboard)
		    	InputMethodManager ocultar_teclado = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    	ocultar_teclado.hideSoftInputFromWindow(grupo.getWindowToken(), 0);
		    	
		    	//aqui haces lo que necesitas
		    	Comprueba_EditText comp = new Comprueba_EditText();
		    	if (comp.comprobar(grupo))
		    	{
		    		String n_grupo = grupo.getText().toString();
		    		boolean bool = false;//Inicializamos a false, si hay alguno con igual nombre se pone a true
		    		
		    		if (lista_nombres != null)
		    		{
		    			for (j=0; j<lista_nombres.length; j++)
			    		{
			    			if (n_grupo.equals(lista_nombres[j]))
			    			{
			    				bool = true;
			    			}
			    		}
			    		
			    		if (!bool)//Si es false, es decir ningún grupo tiene ese nombre
			    		{
			    			//Vamos a la creación del grupo
				    		crear_grupo.putExtra("nombre_grupo", grupo.getText().toString());
				    		crear_grupo.putExtra("eliminacion", 1);//Enviamos esta bandera para la salida del la actividad crear_grupo
				    		startActivityForResult(crear_grupo, codigo1);
			    		}
			    		else
			    		{
			    			Toast.makeText(getBaseContext(), "Nombre de grupo ya usado, utilice otro", Toast.LENGTH_SHORT).show();
			    		}
		    		}
		    		else
		    		{
		    			//Vamos a la creación del grupo
			    		crear_grupo.putExtra("nombre_grupo", grupo.getText().toString());
			    		crear_grupo.putExtra("eliminacion", 1);//Enviamos esta bandera para la salida del la actividad crear_grupo
			    		startActivityForResult(crear_grupo, codigo1);
		    		}
		    		
		    	}
		    	else
		    	{
		    		Toast.makeText(getBaseContext(), "Debe introducir un nombre de grupo válido", Toast.LENGTH_SHORT).show();
		    	}
		    }
		});
		dialogo.setNegativeButton("Cancelar", new  DialogInterface.OnClickListener() 
		{ // si le das al si
		    public void onClick(DialogInterface dialog, int whichButton) 
		    {
		    	//Lineas para ocultar el teclado virtual (Hide keyboard)
		    	InputMethodManager ocultar_teclado = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		    	ocultar_teclado.hideSoftInputFromWindow(grupo.getWindowToken(), 0);
		    }
		});
		dialogo.show();
	}
	
	/**
	 * Se encarga de volver a la pantalla de introducción de evento
	 * @param view
	 */
	//Botón atrás numero grupos
	public void atras(View view)
	{
		finish();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == codigo1)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				String nombre_del_grupo = data.getExtras().getString("nombre_del_grupo").toString();
				
				//Abrimos, insertamos, cerramos la BD y si eso se hace correctamente se hace el if 
				bd.abrirBD();
				boolean bandera = bd.insertar(nombre_del_grupo);
				bd.cerrarBD();
				
				if (bandera)
				{
					//Hacemos esto porque sino da fallo de inserción de lista y en la BD
					//Eliminamos el listView y lo volvemos a sobreescribir
					desplegable_grupos.eliminar_lista();
					//Comprobamos que existen o no elementos en la base de datos
					bd.abrirBD();//Abrimos la BD
					lista_nombres = bd.recuperar();//Almacenamos en una variable String los nombres que hay en la BD
					if (lista_nombres != null)//Si hay elementos almacenados...
					{
						for (i=0; i<lista_nombres.length; i++)
						{
							String cadena = lista_nombres[i];
							desplegable_grupos.anadir(cadena);//Añadimos a la listView
						}
						i = lista_nombres.length;
					}
					else
					{
						i = 0;
					}
					bd.cerrarBD();//Cerramos la BD
					grupos.setText("Grupos("+i+"):");//Actualizamos el número de participantes que hay
				}
			}
		}
		
		if (requestCode == codigo2)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				Toast.makeText(getBaseContext(), "Grupo Modificado", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * Se encarga de mostrar las opciones al seleccionar un grupo
	 */
	//Método implementado que se realiza con el toque de un elemento del listView
	@Override
	public void onItemClick(AdapterView<?> parent, final View v, int posicion, long id) 
	{
		final Intent crear_grupo = new Intent(this, Nuevo_grupo.class);
		
		final String []elementos = {"Seleccionar","Modificar","Eliminar","Cancelar"};
		AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
		final int pos = posicion;
		
		dialogo.setTitle("¿Qué desea hacer con el\ngrupo?");
		dialogo.setItems(elementos, new DialogInterface.OnClickListener() 
		{
	        public void onClick(DialogInterface dialog, int item) 
	        {
	        	switch (item)
	    		{
	    			case 0://Elección seleccionar
	    				String seleccion = lista_nombres[pos];
	    				et_grupo_seleccionado.setText(seleccion);
	    				break;
	    			case 1://Elección ver
	    				String ver = lista_nombres[pos];
	    				crear_grupo.putExtra("nombre_grupo", ver);
	    				crear_grupo.putExtra("eliminacion", 0);//Bandera para no eliminar el grupo dando atrás
			    		startActivityForResult(crear_grupo, codigo2);
	    				break;
	    			case 2://Elección eliminar
	    				String eliminar = lista_nombres[pos];
	    				eliminar(eliminar, pos+1);
	    				break;
	    			case 3://Elección cancelar(no se hace nada)
	    				break;
	    		}
	        }
	    });
		dialogo.show();
	}
	
	/**
	 * Se encarga de eliminar el grupo y sus participantes asociados
	 */
	public void eliminar(String eliminar, int posicion)
	{
		bd_parti = new Operar_BD_participantes(this, eliminar, null, 1);
		
		try
		{
			bd_parti.elimiarBD_completa();//Elimina la BD del grupo con el nombre pasado
			bd.abrirBD();
			bd.eliminarBD(posicion);//Elimina la posicion del grupo en la BD de grupos
			
			//Eliminamos el listView y lo volvemos a sobreescribir
			desplegable_grupos.eliminar_lista();
			//Comprobamos que existen o no elementos en la base de datos
			lista_nombres = bd.recuperar();//Almacenamos en una variable String los nombres que hay en la BD
			if (lista_nombres != null)//Si hay elementos almacenados...
			{
				for (i=0; i<lista_nombres.length; i++)
				{
					String cadena = lista_nombres[i];
					desplegable_grupos.anadir(cadena);//Añadimos a la listView
				}
				i = lista_nombres.length;
			}
			else
			{
				i = 0;
			}
			bd.cerrarBD();//Cerramos la BD
			grupos.setText("Grupos("+i+"):");//Actualizamos el número de participantes que hay
		}
		catch(Exception e)
		{
			Toast.makeText(getBaseContext(), "No se pudo borrar el grupo", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Avanza después de la selección de grupo hacia las exclusiones
	 * @param view
	 */
	public void hacia_exclusiones(View view)
	{
		Comprueba_EditText comp = new Comprueba_EditText();
		
		if (comp.comprobar(et_grupo_seleccionado))//Pasa si hay algún grupo seleccionado
		{
			Intent exclusiones = new Intent(this, Exclusiones.class);
			exclusiones.putExtra("nombre_grupo", et_grupo_seleccionado.getText().toString());
			exclusiones.putExtra("primera_vez", true);
			startActivity(exclusiones);
		}
	}
}

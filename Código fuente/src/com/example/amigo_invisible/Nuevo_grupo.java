package com.example.amigo_invisible;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amigo_invisible.clases_ayuda.Lista_desplegable_participantes;
import com.example.amigo_invisible.clases_ayuda.Operar_BD_grupos;
import com.example.amigo_invisible.clases_ayuda.Operar_BD_participantes;

/**
 * Clase que muestra los participantes introducidos y permite el añadir más, así como opciones para
 * los ya introducidos
 * @author Quintin Sanabria Sánchez
 */

public class Nuevo_grupo extends Activity implements android.widget.AdapterView.OnItemClickListener
{
	private int codigo_nuevo = 999, i, cod = 998, eliminacion;
	private EditText et_nombre_grupo;
	private Operar_BD_participantes bd;
	private Operar_BD_grupos bd_grupos;
	Lista_desplegable_participantes desplegable;
	private TextView participantes;
	private ListView lista;
	private String []lista_nombres;
	private String nombre_grupo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//Quita el título de la aplicación
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//Quita la barra de notificaciones
		
		setContentView(R.layout.nuevo_grupo);//Asigna a .java la intefaz del xml de numero_grupos
		
		et_nombre_grupo = (EditText)findViewById(R.id.et_nombre_grupo);
		participantes = (TextView)findViewById(R.id.tv_participantes);
		lista = (ListView)findViewById(R.id.lv_participantes);
		
		Bundle bolsa = getIntent().getExtras();//Obtenemos la bolsa a la que hemos pasado el nombre del grupo
		nombre_grupo = bolsa.getString("nombre_grupo").toString();//Cogemos el nombre del grupo de la bolsa
		eliminacion = bolsa.getInt("eliminacion");//Cogemos la bandera para el toque de atrás
		
		et_nombre_grupo.setEnabled(false);//Ponemos el EditText para que no se pueda escribir
		et_nombre_grupo.setText(nombre_grupo);//Y le ponemos el nombre del grupo al EditText
		
		bd = new Operar_BD_participantes(this, nombre_grupo, null, 1);//Abrimos la base de datos del grupo si existe o la crea
		
		//Nueva instancia de la clase ListaDesplegable para crear un lisview y poder añadir elementos
		desplegable = new Lista_desplegable_participantes(this, lista);
		
		//Comprobamos que existen o no elementos en la base de datos
		bd.abrirBD();//Abrimos la BD
		lista_nombres = bd.recuperar();//Almacenamos en una variable String los nombres que hay en la BD
		if (lista_nombres != null)//Si hay elementos almacenados...
		{
			for (i=0; i<lista_nombres.length; i++)
			{
				String cadena = lista_nombres[i];
				desplegable.anadir(cadena);//Añadimos a la listView
			}
			i = lista_nombres.length;
		}
		else
		{
			i = 0;
		}
		bd.cerrarBD();//Cerramos la BD
		participantes.setText("Participantes("+i+"):");//Actualizamos el número de participantes que hay
		
		lista.setOnItemClickListener(this);//Funcionalidad para el pulsamiento del listView
	}
	
	/**
	 * Se encarga de volver a la pantalla selección de grupos
	 */
	//Método atrás pero aplicado al botón atrás del teléfono
	public void onBackPressed()
	{
		if (eliminacion == 1)
		{
			View l = new View(this);
			atras(l);
		}
		else
		{
			//Si estamos en modificación de grupo solo permite añadir
			Toast.makeText(getBaseContext(), "Para terminar pulse Añadir", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Se encarga de volver a la pantalla selección de grupos
	 */
	public void atras(View view)
	{
		if (eliminacion == 1)
		{
			//Cuadro de diálogo para elegir si recuperar datos del último grupo no acabado
			AlertDialog.Builder dialogo = new AlertDialog.Builder(this);//Crea un cuadro de dialogo
			dialogo.setTitle("¿Salir de la \ncreación del grupo?");//Título del AlertDialog
			dialogo.setIcon(android.R.drawable.ic_dialog_alert);//Icono del AlertDialog
		
			dialogo.setPositiveButton("Salir", new DialogInterface.OnClickListener()//Funcionalidad para botón si
			{
				@Override
				public void onClick(DialogInterface dialogo, int id) 
				{
					if (eliminacion == 1)//Eliminamos grupo
					{
						bd.elimiarBD_completa();
					}
					//Salimos de creación de grupos
					finish();
				}
			});
			dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()//Funcionalidad para botón no
			{
				@Override
				public void onClick(DialogInterface dialogo, int id) 
				{
					//No hace nada
				}
			});
			dialogo.show();
		}
		else
		{
			//Para el caso modificar que solo valga añadir
			Toast.makeText(getBaseContext(), "Para terminar pulse Añadir", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Se encarga de ir a la pantalla para introducir los datos de un participante
	 */
	//Pasamos a la activity para añadir un participante
	public void nuevo_participante(View view)
	{
		Intent nuevo_participante = new Intent(this, Nuevo_participante.class);
		nuevo_participante.putExtra("pos_id", 0);
		nuevo_participante.putExtra("nombre_grupo", nombre_grupo);
		this.startActivityForResult(nuevo_participante, codigo_nuevo);
	}
	
	//onActivityResult para cuando le demos a agregar participante o modificar
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		//Caso para agregar nuevo participante
		if (requestCode == codigo_nuevo)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				//Obtención de los datos del nuevo participante para añadirlos a la BD
				String nombre = data.getExtras().getString("nombre_participante").toString(); 
				String email = data.getExtras().getString("email_participante").toString();
				String numero = data.getExtras().getString("telefono_participante").toString();
				
				try
				{
					//Abrimos, insertamos, cerramos la BD y si eso se hace correctamente se hace el if 
					bd.abrirBD();
					boolean bandera = bd.insertar(nombre, email, numero);
					bd.cerrarBD();
					
					if (bandera)
					{
						desplegable.anadir(nombre);//Añadir nombre a la listView
						i++;//Aumentar contador numero participantes
						participantes.setText("Participantes("+i+"):");
					}
				}
				catch (Exception e)
				{
			        Toast.makeText(getBaseContext(), "Error al abrir la base de datos", Toast.LENGTH_SHORT).show();
				}
			}
		}
		//Caso para modificar el participante
		if (requestCode == cod)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				//Obtención de los datos modificados del participante para añadirlos a la BD
				String nombre = data.getExtras().getString("nombre_participante").toString();
				String email = data.getExtras().getString("email_participante").toString();
				String numero = data.getExtras().getString("telefono_participante").toString();
				//Aquí obtenemos tmb la posición de este participante para poder modificar la posición en la BD y en la listView
				String posi = data.getExtras().getString("posi").toString();
				
				int pos = Integer.parseInt(posi);//Pasamos la posición a int
				
				try
				{
					//Abrimos, insertamos, cerramos la BD y si eso se hace correctamente se hace el if
					bd.abrirBD();
					boolean bandera = bd.actualizar(nombre, email, numero, pos);
					bd.cerrarBD();
					
					if (bandera)
					{
						desplegable.eliminar_posicion(pos);//Elimina el participante en el listView y añade el modificado en esa posición
						desplegable.anadir_posicion(pos, nombre);//Añadir nombre a la listView
						Toast.makeText(getBaseContext(), "Participante modificado", Toast.LENGTH_SHORT).show();
					}
				}
				catch (Exception e)
				{
			        Toast.makeText(this, "Error al abrir la base de datos", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	/**
	 * Se encarga de mostrar las opciones al hacer clic en un participante añadido
	 */
	//Método implementado que se realiza con el toque de un elemento del listView
	@Override
	public void onItemClick(AdapterView<?> parent, final View v, int posicion, long id) 
	{
		AlertDialog.Builder dialogo = new AlertDialog.Builder(this);//Crea un cuadro de dialogo
		final int pos = posicion;
		dialogo.setTitle("¿Qué desea hacer con el \nparticipante?");
		
		dialogo.setPositiveButton("Modificar", new DialogInterface.OnClickListener()//Funcionalidad para botón si
		{
			@Override
			public void onClick(DialogInterface dialogo, int id) 
			{
				modificar(v, pos+1);//Modifica el participante(abre con un intent nuevo_participante con datos del participante a modificar)
			}
		});
		dialogo.setNeutralButton("Eliminar", new DialogInterface.OnClickListener()//Funcionalidad para botón si
		{
			@Override
			public void onClick(DialogInterface dialogo, int id) 
			{
				//Elimina el participante
				eliminar(pos+1);
			}
		});
		dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()//Funcionalidad para botón no
		{
			@Override
			public void onClick(DialogInterface dialogo, int id) 
			{
				//No hace nada, sólo vuelve
			}
		});
		dialogo.show();
	}
	
	/**
	 * Se encarga de mostrar la pantalla de introducir datos de un participante con los datos del
	 * participante seleccionado
	 */
	//Método al pulsar modificar(intent a activity nuevo_participante con datos a modificar)
	public void modificar(View view, int pos_id) 
	{
		Intent i = new Intent(this, Nuevo_participante.class);
		i.putExtra("pos_id", pos_id);//Pasamos la posición del participante a modificar para obtener datos
		i.putExtra("nombre_grupo", nombre_grupo);//Nombre del grupo para abrir BD
		this.startActivityForResult(i, cod);
	}
	
	/**
	 * Se encarga de eliminar el participante seleccionado
	 */
	//Método para elimiar un participante de la lista participantes
	public void eliminar(int posicion)
	{
		AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
		final int lugar = posicion;
		dialogo.setTitle("¿Está seguro?");
		dialogo.setPositiveButton("No", new DialogInterface.OnClickListener()//Funcionalidad para botón no de eliminar
		{
			@Override
			public void onClick(DialogInterface dialogo, int id) 
			{
				//No hacemos nada
			}
		});
		dialogo.setNegativeButton("Si", new DialogInterface.OnClickListener()//Funcionalidad para botón si de eliminar
		{
			@Override
			public void onClick(DialogInterface dialogo, int id) 
			{
				//Eliminamos de la BD su posición y la lista desplegable completa y sobreescribimos el listView
				bd.abrirBD();
				bd.eliminarBD(lugar);
				desplegable.eliminar_lista();
				
				String []lista_nombres1 = bd.recuperar();//Almacenamos en una variable String los nombres que hay en la BD
				if (lista_nombres1 != null)//Si hay elementos almacenados...
				{
					for (i=0; i<lista_nombres1.length; i++)
					{
						String cadena = lista_nombres1[i];
						desplegable.anadir(cadena);//Añadimos a la listView
					}
					i = lista_nombres1.length;
				}
				else
				{
					i = 0;
				}
				bd.cerrarBD();//Cerramos la BD*/
				participantes.setText("Participantes("+i+"):");//Actualizamos el número de participantes que hay
			}
		});
		dialogo.show();
	}
	
	/**
	 * Se encarga de eliminar todos los participantes del grupo
	 */
	//Método que elimina la listView completa y a todos los participantes de la BD
	public void limpiar(View v)
	{
		AlertDialog.Builder dialogo = new AlertDialog.Builder(this);//Crea un cuadro de dialogo
		
		dialogo.setTitle("¿Desea eliminar la \nlista completa?");
		dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener()//Funcionalidad para botón si
		{
			@Override
			public void onClick(DialogInterface dialogo, int id) 
			{
				bd.abrirBD();
				bd.eliminar_filas_BD();
				bd.cerrarBD();
				desplegable.eliminar_lista();
				i=0;
				participantes.setText("Participantes("+i+"):");
				
			}
		});
		dialogo.setNegativeButton("No", new DialogInterface.OnClickListener()//Funcionalidad para botón no
		{
			@Override
			public void onClick(DialogInterface dialogo, int id) 
			{
				//No hace nada, sólo vuelve
			}
		});
		dialogo.show();
	}
	
	/**
	 * Se encarga de verificar el numero de participantes introducidos y de añadir el grupo a la BD
	 */
	//Método que crea el grupo y vuelve a numero de grupos
	public void crear_grupo(View v)
	{
		bd.abrirBD();
		int bandera = bd.num_elementos();
		bd.cerrarBD();
		if (bandera>=3)
		{
			Intent i = new Intent(this, Numero_grupos.class);
			i.putExtra("nombre_del_grupo", et_nombre_grupo.getText().toString());
			setResult(Activity.RESULT_OK, i);
			Nuevo_grupo.this.finish();//Finalizamos la actividad
		}
		else
		{
			Toast.makeText(this, "Debe haber al menos 3 participantes", Toast.LENGTH_SHORT).show();
		}
	}
}

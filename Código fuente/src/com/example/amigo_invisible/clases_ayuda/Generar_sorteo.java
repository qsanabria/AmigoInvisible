package com.example.amigo_invisible.clases_ayuda;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Generar_sorteo
{
	private Context context;
	private String nombre_grupo;
	private Operar_BD_participantes bd;
	private String []lista_nombres;
	private String []origen;
	private String []destino;
	private Exclusiones_preferencias exclusiones;
	
	public Generar_sorteo(Context context, String nombre_grupo) 
	{
		this.context = context;
		this.nombre_grupo = nombre_grupo;
		lista_nombres = null;
		
		//Obtenemos todos los participantes
		bd = new Operar_BD_participantes(context, nombre_grupo, null, 1);
		bd.abrirBD();
		lista_nombres = bd.recuperar();
		bd.cerrarBD();
		
		//Obtenemos los nombres de las exclusiones
		exclusiones = new Exclusiones_preferencias(context);
	}
	
	public boolean sortear(String []pasar_datos, boolean movil)
	{
		String [][]como_regalar = new String[2][lista_nombres.length];
		String []email = new String[como_regalar[0].length];
		boolean valor = false;
		
		if (exclusiones.obtener_num_exclusiones() == 0)
		{
			//No hay exclusiones, evitamos la parte de ellas
			como_regalar = sin_exclusiones();
		}
		else
		{
			//Si hay exclusiones
			origen = exclusiones.obtener_origen();
			destino = exclusiones.obtener_destino();
			como_regalar = con_exclusiones();
		}
		if (como_regalar != null)
		{
			//Aqui llamamos al m�todo de env�o de e-mails
			valor = envio_datos(como_regalar, pasar_datos, movil);
		}
		if (valor == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String[][] comp_exclusiones()
	{
		String [][]como_regalar = new String[2][lista_nombres.length];
		//Si hay exclusiones
		origen = exclusiones.obtener_origen();
		destino = exclusiones.obtener_destino();
		como_regalar = con_exclusiones();
		
		if (como_regalar != null)
		{
			return como_regalar;
		}
		else
		{
			return null;
		}
	}
	
	public String[][] sin_exclusiones()
	{
		//M�todo que realiza el sorteo sin ninguna exclusi�n
		String [][]como_regalar = new String[2][lista_nombres.length];
		int []regalado = new int[lista_nombres.length];
		boolean bandera = true;
		
		for (int i=0; i<lista_nombres.length; i++)//Cada elemento de la lista
		{	
			bandera = true;
			for (int j=0; bandera == true; j++)
			{
				int x = (int)(Math.random()*(lista_nombres.length));
				
				if (lista_nombres[i].equals(lista_nombres[x]))//Si se elige el mismo
				{
					//Toast.makeText(context, "Salio el mismo", Toast.LENGTH_SHORT).show();
				}
				else if (regalado[x] == 1)//Si ya est� elegido
				{
					//Toast.makeText(context, "Ya tiene regalador", Toast.LENGTH_SHORT).show();
				}
				else//Si elige un elemento con 0, es decir sin asignar aun
				{
					regalado[x] = 1;
					como_regalar[0][i] = "" + lista_nombres[i];
					como_regalar[1][i] = "" + lista_nombres[x];
					bandera = false;
				}
			}
		}
		return como_regalar;
	}
	
	public String[][] con_exclusiones()
	{
		String []lista = new String[lista_nombres.length];
		String [][]como_regalar = new String[2][lista_nombres.length];
		int []regalado = new int[lista_nombres.length];
		boolean bandera = true;

		lista = ordenar();
		
		for (int i=0; i<lista.length; i++)//Participantes que regalar�n
		{
			if (regalado[i] == 0)//Si no est� cogido ponemos un dos porque no puede elegirse el mismo
			{
				regalado[i] = 2;
			}
			//Ahora debemos a�adir las exclusiones(poner a 2)
			regalado = excluir(regalado, i, lista);
			
			if (regalado != null)//Sorteo porque podremos hacerlo
			{
				bandera = true;
				for (int j=0; bandera == true; j++)
				{
					int x = (int)(Math.random()*(lista_nombres.length));
					
					if (lista[i].equals(lista[x]))//Si se elige el mismo
					{
						//Toast.makeText(context, "Salio el mismo", Toast.LENGTH_SHORT).show();
					}
					else if (regalado[x] == 1)//Si ya est� elegido
					{
						//Toast.makeText(context, "Ya tiene regalador", Toast.LENGTH_SHORT).show();
					}
					else if (regalado[x] == 2)//Si es parte de una excusi�n
					{
						//Toast.makeText(context, "Est� excluido", Toast.LENGTH_SHORT).show();
					}
					else//Si puede hacerse el sorteo
					{
						regalado[x] = 1;
						como_regalar[0][i] = "" + lista[i];
						como_regalar[1][i] = "" + lista[x];
						bandera = false;
					}
				}
				//Desquitamos las exclusiones
				for (int j=0; j<regalado.length; j++)
				{
					if (regalado[j] == 2)
					{
						regalado[j] = 0;
					}
				}
			}
			else
			{
				//total = false;
				Toast.makeText(context, "No se puede hacer el sorteo debido a las exclusiones. Rev�selas y vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
				return null;
			}
		}
		return como_regalar;
	}
	
	public int[] excluir(int []regalado, int i, String []lista)//Pasamos el vector para poner 2 y el indice que marcar� el participante
	{
		for(int t=0; t<origen.length; t++)//Recorremos origen para ver si el primer participante tiene alguna exclusi�n
		{
			if (lista[i].equals(origen[t]))//Si esta su nombre en origen tiene una exclusion al menos
			{
				String nombre = destino[t];//Copiamos el destino asociado que ser� a quien no podr� regalar
				//Toast.makeText(context, "No a "+nombre,Toast.LENGTH_SHORT).show();
				for(int j=0; j<lista.length; j++)//Comprobamos en la posici�n que est� a quien no puede regalar
				{
					if (lista[j].equals(nombre))//Cuando coincidan, como la posici�n es la misma que en regalado:
					{
						//Toast.makeText(context, "Est� en la pos "+(j+1),Toast.LENGTH_SHORT).show();
						if (regalado[j] == 1)
						{
							//Dejamos en 1 porque no hace falta, ya que de todas maneras no podr� regalar
						}
						else if (regalado[j] == 0)
						{
							//Si es cero a�n puede alguien regalarle con lo cual ponemos un 2 para que no
							//pueda ser regalado por origen[i] y a su vez luego podamos quitar la exclusi�n
							regalado[j] = 2;
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
	
	public String[] ordenar()
	{
		//Si ordenamos la lista y ponemos a los participantes con mas exclusiones delante, le daremos
		//prioridad para que elijan a quien regalar, lo que nos evitar� casos de posibles errores por
		//falta de participantes v�lidos en un determinado punto de el sorteo
		String []lista = new String[lista_nombres.length];
		int []numero = new int[lista_nombres.length];
		
		
		for (int i=0; i<lista_nombres.length; i++)
		{
			for (int j=0; j<origen.length; j++)
			{
				if (lista_nombres[i].equals(origen[j]))//Vemos si esta en origen(tiene exclusi�n) y contamos cuantas tiene
				{
					numero[i]++;//Contamos el n�mero de exclusiones de cada elemento
				}
			}
			lista[i] = "" + lista_nombres[i];//Copiamos lista_nombres en lista
		}
		
		for (int i=0; i<(numero.length-1); i++)//Ordenamos la lista de los nombres
		{
			int max = i;//Cogemos como mayor el numero de la lista que corresponda
			for (int j = i+1 ; j<numero.length ; j++) //Buscamos si existe otro mayor hacia adelante
			{
                if (numero[j] > numero[max]) 
                {
                    max = j;//Si hay uno mayor, cogemos su �ndice para permutarlo en la posici�n de i
                }
            }
			
			if (i != max) 
			{
                //Permutamos los valores para la lista de el numero de exclusiones y de los nombres
                int aux = numero[i];
                numero[i] = numero[max];
                numero[max] = aux;
                
                String aux1 = lista[i];
                lista[i] = lista[max];
                lista[max] = aux1;
            }
		}	
		return lista;
	}
	
	public boolean envio_datos(String [][]como_regalar, String []pasar_datos, boolean movil)
	{
		String []email = new String[como_regalar[0].length];
		String []numeros = new String[como_regalar[0].length];
		
		bd.abrirBD();
		for (int i=0; i<como_regalar[0].length; i++)
		{
			Mail m = new Mail("amigoinvisibleesp@gmail.com", "amigoinvisible", this.context);
			email[i] = bd.recuperar_email(como_regalar[0][i]);
			numeros[i] = bd.recuperar_telefono(como_regalar[0][i]);
			
			String[] toArr = {email[i]};
			m.setTo(toArr);
			m.setFrom("amigoinvisibleesp@gmail.com"); 
            m.setSubject("Amigo Invisible"); 
            m.setBody("�Hola "+como_regalar[0][i]+"! Te toca regalar a: "+como_regalar[1][i]+"\n"+
            "El evento llamado "+pasar_datos[0]+" se celebrar� en "+pasar_datos[1]+" en la fecha "+
            pasar_datos[2]+" a las "+ pasar_datos[3]+"\nLa tem�tica ser�: "+pasar_datos[4]+
            " y el rango de precios de los regalos: "+pasar_datos[5]);
            try 
            {
            	
				if(m.send()) 
				{ 
					
				} 
				else 
				{ 
				    Toast.makeText(context, "El Email no fue enviado", Toast.LENGTH_SHORT).show(); 
				    return false;
				}
			} catch (Exception e) 
			{
				// TODO Bloque catch generado autom�ticamente
				Toast.makeText(context, "Error en los datos", Toast.LENGTH_SHORT).show(); 
				return false;
			}
            
            if (movil == true)
    		{
            	if (numeros[i] != "")
            	{
            		try
            		{
            			SmsManager sms = SmsManager.getDefault();
            			sms.sendTextMessage("+34"+numeros[i], null, "�Hola "+como_regalar[0][i]+"! Te toca regalar a: "+como_regalar[1][i], null, null);
            			Toast.makeText(context, "SMS enviado a "+como_regalar[0][i], Toast.LENGTH_SHORT).show();
            		}
            		catch (Exception e)
            		{
            			Toast.makeText(context, "Error en el env�o de SMS", Toast.LENGTH_SHORT).show();
            			return false;
            		}
            	}
    		}
			//Toast.makeText(context, numeros[i], Toast.LENGTH_SHORT).show();
		}
		bd.cerrarBD();
		return true;
	}
}

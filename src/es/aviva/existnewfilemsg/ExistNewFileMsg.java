/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.aviva.existnewfilemsg;

import es.aviva.utiles.Fechas;
import es.aviva.utiles.Mail;
import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author m0072
 */
public class ExistNewFileMsg {
    
    static final String FICHERO_PROPERTIES="existnewfilemsg.properties";
    static Logger logger = Logger.getLogger(ExistNewFileMsg.class);
    static ArrayList<String> destinos = new ArrayList<String>();
    static ArrayList<String> ficheros = new ArrayList<String>();
    static String activo="N";
    static String mailHost;
    static String mailPort;
    static String mailFrom;


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
                
        String fichero[] = new String[2];
        String fecha;
        String hora;
        String clave;
        String valor;
        
        PropertyConfigurator.configure("log4j.properties");
        logger.info("------Iniciando la aplicación...");
        
        // Leemos la configuración del fichero .properties
        logger.info("Leyendo properties...");
        if(!leeProperties()){
            logger.error("Error leyendo properties.");
            System.exit(0);
        }
        
        logger.info("Comprobando si está activo el proceso...");
        if(!activo.equals("S")){
            logger.info("No está activo el proceso.");
            System.exit(0);
        }
        
        // Buscamos los ficheros uno a uno
        logger.info("Buscamos los ficheros uno a uno...");
        for(int indice=0; indice<ficheros.size(); indice++ ){
            
            // Vaciamos la matriz "indice"
            for(int i=0; i<fichero.length;i++)
                fichero[i]="";
            
            logger.info("Validando patrón de ficheros...");
            if(esValidoPattern(ficheros.get(indice),fichero)){
                logger.error("Datos de fichero no válidos" + ficheros.get(indice));
            }
            
            // Comparamos las fechas para ver si hay que enviar e-mail
            logger.info("Comprobamos si hay que enviar el e-mail...");
            if(hayQueEnviarEmail(fichero)){
                logger.info("Sí hay que enviar el e-mail: " + ficheros.get(indice));
                enviarMail(fichero[0]);
                clave="fichero.file" + Integer.toString(indice+1);
                escribirProperties(clave, getValorFichero(fichero[0]));
            }
            else
                logger.info("No hay que enviar el e-mail: " + ficheros.get(indice));
        }
        
        logger.info("------Fin de la ejecución.");
        
    }
    
    private static boolean leeProperties(){
        
        int indice=0;
        String destino;
        String usuario;
        String fichero;
        String pattern;
        
        try {

            // Creamos un Objeto de tipo Properties
            Properties propiedades = new Properties();

            // Cargamos el archivo desde la ruta especificada
            propiedades.load(new FileInputStream(FICHERO_PROPERTIES));

            activo = propiedades.getProperty("activo");
            
            // Obtenemos los parametros definidos en el archivo
            mailHost = propiedades.getProperty("mail.smtp.host");
            mailPort = propiedades.getProperty("mail.smtp.port");
            mailFrom = propiedades.getProperty("mail.from.dir");
            
            // Recogemos los e-mail destinos
            while(true){
                indice++;
                usuario= "user" + Integer.toString(indice);
                destino = propiedades.getProperty("mail.to."+usuario);
                if(destino==null){
                    break;
                }
                destinos.add(destino);
            }

            // Recogemos los datos de los ficheros
            indice=0;
            while(true){
                indice++;
                fichero= "file" + Integer.toString(indice);
                pattern = propiedades.getProperty("fichero."+fichero);
                if(pattern==null){
                    break;
                }
                ficheros.add(pattern);
            }
            return true;

        } catch (FileNotFoundException e) {
            System.out.println("Error, El archivo no exite");
            return false;
        } catch (IOException e) {
            System.out.println("Error, No se puede leer el archivo");
            return false;
        }

    }
    
    private static boolean esValidoPattern(String cadena, String fichero[]){
        
        int indice=0;
        StringTokenizer st = new StringTokenizer(cadena, ";");
 
        while (st.hasMoreElements()) {
            fichero[indice]=(String)st.nextElement();
            indice++;
            //System.out.println(st.nextElement());
	}
        
        return false;
        
    }
    
    private static boolean hayQueEnviarEmail(String fichero[]){
        
        File f = new File(fichero[0]);
        
        // Comprobamos si existe el fichero
        if(!f.exists())
            return false;
        
        // Comprobamos la fecha
        String ffichero = Fechas.fechaModificacionFichero(f);
        if(Fechas.compararFechas(ffichero, fichero[1])==1)
                return true;
        return false;
    }
    
    private static void enviarMail(String fichero){
        
        String asunto="Chequeo de existencia de ficheros en red Aviva";
        String cuerpo="Se ha recibido un nuevo fichero: " + fichero;
        
        // Enviamos el e-mail a los destinatarios
        for(int indice=0; indice<destinos.size(); indice++ ){
            Mail.EnviaMail(mailHost,mailPort,mailFrom, destinos.get(indice), asunto, cuerpo);
        }

    }
    
    private static void escribirProperties(String clave, String valor){
        
        Properties prop = new Properties();
        
        try{
            prop.load(new FileInputStream(FICHERO_PROPERTIES));
            prop.setProperty(clave, valor);
            prop.store(new FileOutputStream(FICHERO_PROPERTIES),"sistema");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
    
    private static String getValorFichero(String fichero){
        
        File f = new File(fichero);
        
        // Comprobamos la fecha
        String ffichero = Fechas.fechaModificacionFichero(f);
        
        return fichero + ";" + ffichero;
    }
}

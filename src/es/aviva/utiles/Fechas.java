/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.aviva.utiles;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 *
 * @author m0072
 */
public class Fechas {
    
    public static int compararFechas(String fecha1, String fechaActual) { 
    
        System.out.println("Parametro String Fecha 1 = "+fecha1+"\n" + "Parametro String fechaActual = "+fechaActual+"\n"); 
        
        try {
            /**Obtenemos las fechas enviadas en el formato a comparar*/
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaDate1 = formateador.parse(fecha1);
            Date fechaDate2 = formateador.parse(fechaActual);

            System.out.println("Parametro Date Fecha 1 = "+fechaDate1+"\n" + "Parametro Date fechaActual = "+fechaDate2+"\n");

            if (fechaDate1.before(fechaDate2)){
                return 2;
            }else{
                if (fechaDate2.before(fechaDate1)){
                    return 1;
                }else{
                    return 0;
                }
            }
        } catch (ParseException e) {
            System.out.println("Se Produjo un Error!!!  "+e.getMessage());
            return -1;
        } 

    }
    
    public static long fechaEnMs(String fecha, String hora){

        int dia=1;
        int mes=1;
        int anno=1960;
        int horas=0;
        int minutos=0;
        int segundos=0;
        String dato;
        int indice;
        
        try {
            // Descomponemos la fecha
            StringTokenizer st = new StringTokenizer(fecha, "/");

            indice=1;
            while (st.hasMoreElements()) {
                dato=(String)st.nextElement();
                if(indice==1){
                    dia=Integer.parseInt(dato);
                } else if(indice==2){
                    mes=Integer.parseInt(dato);
                } else {
                    anno=Integer.parseInt(dato);
                }
                indice++;
            }

            // Descomponemos la hora
            st = new StringTokenizer(hora, ":");

            indice=1;
            while (st.hasMoreElements()) {
                dato=(String)st.nextElement();
                if(indice==1){
                    horas=Integer.parseInt(dato);
                } else if(indice==2){
                    minutos=Integer.parseInt(dato);
                } else {
                    segundos=Integer.parseInt(dato);
                }
                indice++;
            }

            Calendar ahoraCal = Calendar.getInstance();
            ahoraCal.set(anno,mes,dia,horas,minutos,segundos);

            return ahoraCal.getTimeInMillis();
        } catch (Exception e) {
            return -1;
        }
        
    }
    
    public static String fechaModificacionFichero (File file)
    {
        String fechaFormato;
        long miliseg;
        SimpleDateFormat fecha = new SimpleDateFormat();
        fecha.applyPattern("dd/MM/yyyy HH:mm");

        //Se obtienen el numero total de milisegundos transcurridos
        //desde el 1 de enero de 1970 a las 00:00:00h hasta la última
        //modificación del fichero
        miliseg = file.lastModified();

        //Se crea un objeto de la clase Date, pasando como paramétro el numero
        //de milisegundos transcurridos desde el 1 de enero de 1970
        Date fechaInicio = new Date(miliseg);

        //Dada un objeto de la clase Date, se le da formato y se convierte en
        //una cadena de caracteres
        fechaFormato = fecha.format(fechaInicio);

        return fechaFormato;
    }
   
}

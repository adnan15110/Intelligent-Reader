/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author akhan
 */

/*
 * Static class for utility function
 * Use by just calling them using Utilities.function
 */
public class Utilities {

    public Utilities() {
    }

    public static Dimension getWindowsize() {
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        //System.out.println("--" + screenSize.height + "---" + screenSize.width);
        return screensize;
    }

    /*
     * Customizable file chooser dialog
     * Returns: File object 
     * 
     */
    public static File fileChooser() {
        File file = null;
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setMultiSelectionEnabled(false);
        FileFilter ff = new FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".pdf") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "*.pdf";
            }
        };
        fc.setFileFilter(ff);
        int returnVal = fc.showOpenDialog(fc);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        }
        return file;
    }

    // done by Mahdi
    public static String getTitle(String articletext) {
        int j = 0, spltstr_length, title_str = 0;
        String[] splitString = (articletext.split("[.]|[,]"));
        //System.out.println(splitString[8]);
        Boolean flag = true;
        for (int i = 0; i < splitString.length; i++) {
            spltstr_length = splitString[i].length();
            if (spltstr_length > title_str) {
                title_str = spltstr_length;
                if (title_str > 25 && flag) {
                    j = i;
                    flag = false;
                }
            }
        }
        //System.out.println("title of the paper: " + splitString[j]);
        String title = splitString[j];
        return title;

    }
    
    public static Point setWindowPosition(Toolkit env){
        Point p = new Point();
        Dimension envDim = env.getScreenSize();
        p.y = (envDim.height /4);
        p.x = (envDim.width /4);
        return p;

    
    }
    
    public static String getyear(String aricletext){
    String[] splitString = (aricletext.split("\\s+"));
    String Pub_year = null;
			for (String val : splitString) {
				//System.out.println(val);
			    boolean year;
                            if (year=val.matches("(\\d{4})(,?)(.?)(\\w?.?)")){
                                Pub_year = val.substring(0,4);
                                //System.out.println(val);
                            }
				
                            else if(year=val.matches("(\\(?)(\\d{4})(,?)(.?)(\\w?.?)")){
                                //else if(val.charAt(0)== '(' && val.charAt(val.length()-2) == ')'){
                                Pub_year = val.substring(1, 5);
                            }

                            else if(year=val.matches("[(]\\d{4}[)],?.?")){
				//else if(val.charAt(0)== '(' && val.charAt(val.length()-2) == ')'){
					Pub_year = val.substring(1, 5);
			    }
				
			   	//else if(year=val.matches("[']\\d{2},?.? | [�]\\d{2},?.?")){    
				else if((val.charAt(0)== '\'' | val.charAt(0)== '�') && val.charAt(val.length()-1) == ','){
					Pub_year =  "20" + val.substring(1, 3);
			    }
			   	
			   	if(year) break;
			
    
    }
        return Pub_year;
}
}
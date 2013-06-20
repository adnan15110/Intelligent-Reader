/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import DataModel.Article;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author akhan
 */
public class LogCreator {

    Article currentarticle;
    File logfile;
    String source;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date date = new Date();

    public LogCreator(Article currentarticle, File file, String title) {
        this.currentarticle = currentarticle;
        this.logfile = file;
        this.source = title;
    }

    public void createReferencelink() {
        Writer output;
        try {
            output = new BufferedWriter(new FileWriter(logfile, true));
            List<String> ref = currentarticle.getReferredarticles();
            for (int i = 0; i < ref.size(); i++) {
                String line = date.toString() + "###" + source + "###" + ref.get(i) + "\n";
                output.append(line);
            }
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(LogCreator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void createCitationlink() {
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter(logfile, true));
            List<String> cref = currentarticle.getCitiedarticle();
            for (int i = 0; i < cref.size(); i++) {
                String line = date.toString() + "###" + cref.get(i) + "###" + source + "\n";
                output.append(line);
            }
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(LogCreator.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
// log creation


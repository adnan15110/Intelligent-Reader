/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 *
 * @author akhan
 */
public class PdfTextParser {

    File f;
    PDDocument doc;

    public PdfTextParser(File file) {
        try {
            f = file; //uise herefile location
            doc = PDDocument.load(f);
        } catch (IOException ex) {
            Logger.getLogger(PdfTextParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getArticleTitle() throws IOException {
        PDFTextStripper st = new PDFTextStripper();
        st.setStartPage(1);
        st.setEndPage(1);
        String data = st.getText(doc);
        //System.out.println("pdf text :" + data);
        //intuition used onlt first two line are mainly paper's title split whole text based on '//n' can be improved

        String s[] = data.split("\\n", 3);
        //System.out.println("--" + s[0] + s[1]);
        String title = s[0] + s[1];
        // remove all newline sequences from string
        return title.replaceAll("[\n\r]", "");

    }

    public void closingPdfStream() {
        try {
            doc.close();
        } catch (IOException ex) {
            Logger.getLogger(PdfTextParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String args[]) throws IOException {
        PdfTextParser pdfparser = new PdfTextParser(new File("3.pdf"));
        String data = pdfparser.getArticleTitle();
        System.out.println(data);
        
    }
}

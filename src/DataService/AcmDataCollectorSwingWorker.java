/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService;

import javax.swing.SwingWorker;
import DataModel.Article;
import GUI.ButtonColumn;
import GUI.Entrymain;
import static GUI.Entrymain.abstractArea;
import static GUI.Entrymain.articleList;
import static GUI.Entrymain.citedbyTable;
import static GUI.Entrymain.referenceTable;
import static GUI.Entrymain.selectCombo;
import java.io.IOException;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import utilities.LogCreator;
import utilities.Utilities;

/**
 *
 * @author akhan
 */
public class AcmDataCollectorSwingWorker extends SwingWorker<Article, Integer> {

    String searchquery;
    String primarytitle;
    String fulltitle;
    Article article;

    public AcmDataCollectorSwingWorker(String query, String primarytitle) {
        this.searchquery = query;
        this.primarytitle = primarytitle;
        this.fulltitle = primarytitle;

    }


    @Override
    protected Article doInBackground() {
        Article article = new Article();
        System.err.println("Do background" + primarytitle);
        Entrymain.ProgressBar.setIndeterminate(true);
        Entrymain.ProgressBar.setStringPainted(true);
        Entrymain.ProgressBar.setString("Extracting....");
        try {
            WebClient webClient = new WebClient(BrowserVersion.CHROME_16);
            article.setTitle(primarytitle);
            // Configuring the webClient
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.waitForBackgroundJavaScriptStartingBefore(10000);
            HtmlPage page = webClient.getPage("https://dl.acm.org/");
            System.out.println("Title :" + page.getTitleText());

            HtmlForm form = page.getFormByName("qiksearch");
            HtmlTextInput textInput = form.getInputByName("query");
            textInput.setValueAttribute(searchquery);
            HtmlImageInput imageInput = form.getInputByName("Go");
            page = (HtmlPage) imageInput.click();
            System.out.println("Title :" + page.getTitleText());

            HtmlAnchor link = (HtmlAnchor) page
                    .getByXPath(
                    "/html/body/div/table/tbody/tr[3]/td/table/tbody/tr[3]/td[2]/table/tbody/tr[2]/td[2]/table/tbody/tr[1]/td/a")
                    .get(0);
            System.out.println("Link:" + link.getHrefAttribute());
            String prefix = "https://dl.acm.org/";
            String suffix = "&preflayout=flat";
            String middle = link.getHrefAttribute().split("&", 2)[0];
            String Acm_location = prefix + middle + suffix;
            System.out.println(Acm_location);
            webClient.setJavaScriptEnabled(false);
            page = webClient.getPage(Acm_location);
            webClient.waitForBackgroundJavaScriptStartingBefore(10000);

            // abstract
            try {
                HtmlParagraph abstractdiv = (HtmlParagraph) page.getByXPath("//*[@id=\"fback\"]/div[@class='flatbody']/div/p").get(0);
                article.setArticleabstract(abstractdiv.asText());
            } catch (NullPointerException ex) {
                article.setArticleabstract("No Abstract Available");
            }

            // Reference and citation building

            article.setVideoavailable(false);

            //references
            try {
                HtmlTable reftable = (HtmlTable) page.getByXPath(
                        "//div[@id='fback']/div[@class='flatbody']/div[@class='flatbody'][2]/table").get(0);
                for (HtmlTableRow row : reftable.getRows()) {

                    article.addArticlesToReferereceList(row.getCell(2).asText());

                }
            } catch (IndexOutOfBoundsException ex) {
                System.out.println("No reference found");
            }
            //cited
            try {
                HtmlTable citetable = (HtmlTable) page.getByXPath(
                        "//div[@id='fback']/div[@class='flatbody']/div[@class='flatbody'][3]/table").get(0);
                for (HtmlTableRow row : citetable.getRows()) {
                    //System.out.println("Cited article: " + row.getCell(1).asText());
                    article.addArticlesToCitedList(row.getCell(1).asText());
                }
            } catch (IndexOutOfBoundsException ex) {
                System.out.println("No citation found");
            }
            webClient.closeAllWindows();

            File logfile = new File("log.txt");
            if (!logfile.exists()) {
                logfile.createNewFile();
            }

            LogCreator logcreator = new LogCreator(article, logfile, fulltitle);
            logcreator.createReferencelink();
            logcreator.createCitationlink();
            
            File viewedfile = new File("viewed.txt");
            if (!viewedfile.exists()) {
                viewedfile.createNewFile();
            }
            
             Writer output = new BufferedWriter(new FileWriter(viewedfile, true));
             
             output.append(fulltitle+"\n");
             output.close();
            
        } catch (IOException ex) {
            System.err.println("Exception in Fetching: " + searchquery);
            Entrymain.ProgressBar.setIndeterminate(false);
            Entrymain.ProgressBar.setString("Not Found");
        }

        return article;
    }

    @Override
    protected void done() {
        try {
            Article article = get();
            Entrymain.articleList.add(article);
            Entrymain.ProgressBar.setIndeterminate(false);
            Entrymain.ProgressBar.setString("");
            int last = Entrymain.articleList.size() - 1;
            ShowinformationInTable(last);
            abstractArea.setText(article.getArticleabstract());
            selectCombo.addItem(article.getTitle());
            int combosize = selectCombo.getItemCount();
            selectCombo.setSelectedIndex(combosize - 1);
            Entrymain.informationViewtab.validate();
        } catch (InterruptedException ex) {
            Logger.getLogger(Entrymain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(Entrymain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void ShowinformationInTable(int index) {
        // initializing reference table

        DefaultTableModel referencemodel = new DefaultTableModel();
        Entrymain.referenceTable.setModel(referencemodel);
        referencemodel.addColumn("Index");
        referencemodel.addColumn("Article title");
        referencemodel.addColumn("year");
        referencemodel.addColumn("Click ");
        Entrymain.referenceTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


        List<String> referencelist = articleList.get(index).getReferredarticles();
        for (int i = 0; i < referencelist.size(); i++) {
            String year = utilities.Utilities.getyear(referencelist.get(i));
            referencemodel.addRow(new Object[]{i + 1, referencelist.get(i), year, "More"});
        }

        Entrymain.referenceTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        Entrymain.referenceTable.getColumnModel().getColumn(1).setPreferredWidth(1100);
        Entrymain.referenceTable.getColumnModel().getColumn(2).setPreferredWidth(50);
        Entrymain.referenceTable.getColumnModel().getColumn(3).setPreferredWidth(50);



        Action show = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Entrymain.referenceTable = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                System.out.println("Article name :" + ((DefaultTableModel) Entrymain.referenceTable.getModel()).getValueAt(modelRow, 1));
                String title_ref = Utilities.getTitle(((DefaultTableModel) Entrymain.referenceTable.getModel()).getValueAt(modelRow, 1).toString());
                String searchquery_ref = title_ref.replaceAll(" ", "+");
                AcmDataCollectorSwingWorker ad_reference = new AcmDataCollectorSwingWorker(searchquery_ref, title_ref);
                ad_reference.execute();
            }
        };
        ButtonColumn bc = new ButtonColumn(referenceTable, show, 3);
        bc.setMnemonic(KeyEvent.VK_D);


        // initializing cited by table
        DefaultTableModel citedmodel = new DefaultTableModel();
        Entrymain.citedbyTable.setModel(citedmodel);
        citedmodel.addColumn("Index");
        citedmodel.addColumn("Article title");
        citedmodel.addColumn("Year");
        citedmodel.addColumn("Click ");
        citedbyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        List<String> citedlist = articleList.get(index).getCitiedarticle();
        for (int i = 0; i < citedlist.size(); i++) {
            String year = utilities.Utilities.getyear(citedlist.get(i));
            citedmodel.addRow(new Object[]{i + 1, citedlist.get(i), year, "More"});
        }

        Entrymain.citedbyTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        Entrymain.citedbyTable.getColumnModel().getColumn(1).setPreferredWidth(1100);
        Entrymain.citedbyTable.getColumnModel().getColumn(2).setPreferredWidth(50);
        Entrymain.citedbyTable.getColumnModel().getColumn(3).setPreferredWidth(50);

        Action citedshow = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                referenceTable = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                //String articlefullname = ((DefaultTableModel) Entrymain.citedbyTable.getModel()).getValueAt(modelRow, 1).toString();
                String title_citation = Utilities.getTitle(((DefaultTableModel) Entrymain.citedbyTable.getModel()).getValueAt(modelRow, 1).toString());
                String search_citation = title_citation.replaceAll(" ", "+");
                AcmDataCollectorSwingWorker ad_citation = new AcmDataCollectorSwingWorker(search_citation, title_citation);
                ad_citation.execute();

            }
        };
        ButtonColumn cite_bc = new ButtonColumn(Entrymain.citedbyTable, citedshow, 3);
        bc.setMnemonic(KeyEvent.VK_D);
        if (articleList.get(index).getVideoavailable()) {
            Entrymain.sourceCheckBox.setSelected(true);
        } else {
            Entrymain.sourceCheckBox.setSelected(false);
        }

        Entrymain.abstractArea.setText(articleList.get(index).getArticleabstract());

    }
}

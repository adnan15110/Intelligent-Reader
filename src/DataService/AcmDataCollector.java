package DataService;

import DataModel.Article;
import java.io.IOException;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcmDataCollector implements Runnable {

    public Article article;
    private String searchquery;
    WebClient webClient;

    public AcmDataCollector(String query) {
        // TODO Auto-generated constructor stub
        searchquery = query;
    }

    @Override
    public void run() {
        try {
            article = new Article();
            webClient = new WebClient(BrowserVersion.CHROME_16);

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

            List div = page.getByXPath("//div[@id='fback']/div[@class='flatbody']");


            // abstract
            HtmlDivision abstractdiv = (HtmlDivision) div.get(0);
            System.out.println("Abstract : " + abstractdiv.asText());
            article.setArticleabstract(abstractdiv.asText());




            // Reference and citation building

            if (div.size() == 8) {
                article.setVideoavailable(false);

                //references
                HtmlTable reftable = (HtmlTable) page.getByXPath(
                        "//*[@id=\"fback\"]/div[5]/table").get(0);
                for (HtmlTableRow row : reftable.getRows()) {

                    System.out.println("Reference text: " + row.getCell(2).asText());
                    article.addArticlesToReferereceList(row.getCell(2).asText());

                }
                //cited
                try {
                    HtmlTable citetable = (HtmlTable) page.getByXPath(
                            "//*[@id=\"fback\"]/div[6]/table").get(0);

                    for (HtmlTableRow row : citetable.getRows()) {
                        System.out.println("Cited article: " + row.getCell(1).asText());
                        article.addArticlesToCitedList(row.getCell(1).asText());
                    }
                } catch (IndexOutOfBoundsException ex) {
                    System.out.println("No citation found");
                }

            } else {   //article with resources available
                HtmlDivision resourcediv = (HtmlDivision) div.get(1);
                article.setVideoavailable(true);

                //references
                HtmlTable reftable = (HtmlTable) page.getByXPath(
                        "//*[@id=\"fback\"]/div[6]/table").get(0);
                for (HtmlTableRow row : reftable.getRows()) {

                    System.out.println("Reference text: " + row.getCell(2).asText());
                    article.addArticlesToReferereceList(row.getCell(2).asText());

                }
                //cited
                try {
                    HtmlTable citetable = (HtmlTable) page.getByXPath(
                            "//*[@id=\"fback\"]/div[7]/table").get(0);
                    if (citetable.asText().contains("Citings are not available")) {
                        System.out.println("Citation not available");
                    } else {
                        for (HtmlTableRow row : citetable.getRows()) {
                            System.out.println("Cited article: " + row.getCell(1).asText());
                            article.addArticlesToCitedList(row.getCell(1).asText());
                        }
                    }
                }catch (IndexOutOfBoundsException ex) {
                    System.out.println("No citation found");
                }

            }
            webClient.closeAllWindows();
        } catch (IOException ex) {
            Logger.getLogger(AcmDataCollector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Article getArticleInformation() {
        List<String> reference = article.getReferredarticles();
        //for (String ref : reference) {
          //  System.out.println(ref);
       // }
        
        List<String> citation = article.getCitiedarticle();
        //for (Iterator<String> it = citation.iterator(); it.hasNext();) {
          //  String ref = it.next();
           // System.out.println(ref);
        //}
        return article;
    }

}

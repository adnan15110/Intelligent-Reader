/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author akhan
 */
public class Article {
    private String title;
    private String articleabstract;
    private boolean videoavailable;
    private List<String> referredarticles;
    private List<String> citedarticle;
    
    public Article() {
        referredarticles = new ArrayList<>();
        citedarticle = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    /**
     * @return the articleabstract
     */
    public String getArticleabstract() {
        return articleabstract;
    }

    /**
     * @param articleabstract the articleabstract to set
     */
    public void setArticleabstract(String articleabstract) {
        this.articleabstract = articleabstract;
    }

    /**
     * @return the vedioavailable
     */
    public boolean getVideoavailable() {
        return videoavailable;
    }

    /**
     * @param videoavailable the videoavailable to set
     */
    public void setVideoavailable(boolean videoavailable) {
        this.videoavailable = videoavailable;
    }

    /**
     * @return the referredarticles
     */
    public List<String> getReferredarticles() {
        return referredarticles;
    }

    /**
     * @return the citiedarticle
     */
    public List<String> getCitiedarticle() {
        return citedarticle;
    }
    
    public void addArticlesToReferereceList(String article){
        referredarticles.add(article);
    }
    public void addArticlesToCitedList(String article){
          citedarticle.add(article);
    }
}


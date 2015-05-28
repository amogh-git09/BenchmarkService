


/**
 *
 * @author amogh_000
 */
public class Page {
    private int id;
    private String title;
    private Float pageRank;
    
    public Page(int id, String title, Float pageRank){
        this.id = id;
        this.title = title;
        this.pageRank = pageRank;
    }
    
    public int getId(){
        return id;
    }
    
    public String getTitle(){
        return title;
    }
    
    public Float getPageRank(){
        return pageRank;
    }
}

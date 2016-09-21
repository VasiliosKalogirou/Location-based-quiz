package uk.ac.ucl.cege.cegeg077.ucesvka.londonquiz;

/**
 * Created by Vasilis on 5/7/2016.
 */
public class GeoPoint
{
    private Double latitude;
    private Double longitude;
    private String name;
    private String question;
    private String possible_answer_1;
    private String possible_answer_2;
    private String possible_answer_3;
    private String possible_answer_4;
    private String correct_answer;
    private Boolean reached;

    //this method is the constructor of the class
    public GeoPoint(Double lat, Double lng, String n, String q, String pa1,String pa2, String pa3, String pa4, String ca )
    {
        latitude = lat;
        longitude = lng;
        name = n;
        question = q;
        possible_answer_1 = pa1;
        possible_answer_2 = pa2;
        possible_answer_3 = pa3;
        possible_answer_4 = pa4;
        reached = Boolean.FALSE;
        correct_answer = ca;
    }

    //the following methods are the getters of each variable (property)
    public Double getLongitude(){return longitude;}
    public Double getLatitude() {return latitude;}
    public String getName()
    {
        return name;
    }
    public String getQuestion()
    {
        return question;
    }
    public String getPossibleAnswer1()
    {
        return possible_answer_1;
    }
    public String getPossibleAnswer2()
    {
        return possible_answer_2;
    }
    public String getPossibleAnswer3()
    {
        return possible_answer_3;
    }
    public String getPossibleAnswer4()
    {
        return possible_answer_4;
    }
    public String getCorrectAnswer() {return correct_answer;}
    public Boolean getReached()
    {
        return reached;
    }
    //the following method is the setter of the reached variable (property)
    public void setReached(boolean rNew) {reached = rNew;}
}

package com.example.a.customproject.model;

public class Pet {

    private String ImageUrl;

    private String Name;
    private String Feed;
    private String Shower;
    private String Walk;
    private String UserId;


    public Pet(){}

    public Pet(String fName,String fUrl,String fFeed, String fShower,String fWalk,String fuserId){
       // Caption = fCaption;
        ImageUrl =fUrl;
        Name =fName;
        Feed = fFeed;
        Shower =fShower;
        Walk = fWalk;
        UserId=fuserId;

    }

    public String getName(){return Name;}
    public void setName(String fName){Name=fName;}

    public String getImageUrl(){return ImageUrl;}
    public void setImageUrl(String fImageUrl){ImageUrl=fImageUrl;}

    public String getFeed(){return Feed;}
    public void setFeed(String fFeed){Feed=fFeed;}

    public String getShower(){return Shower;}
    public void setShower(String fShower){Shower=fShower;}

    public String getWalk(){return Walk;}
    public void setWalk(String fWalk){Walk=fWalk;}

    public String getUserId(){return UserId;}
    public void setUserId(String fUserId){UserId=fUserId;}





}

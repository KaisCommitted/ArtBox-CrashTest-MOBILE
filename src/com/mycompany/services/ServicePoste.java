/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.services;


import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.events.ActionListener;
import com.mycompany.entities.Categorie;
import com.mycompany.entities.Post;
import com.mycompany.entities.User;
import com.mycompany.utils.Statics;
import java.io.IOException;
import com.mycompany.entities.Categorie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;




/**
 *
 * @author Adam Khalfaoui
 */
public class ServicePoste {
    
    
    public ArrayList<Post> postes;

    public static ServicePoste instance = null;
    public boolean resultOK;
    private ConnectionRequest req;
    
    
    private ServicePoste() {
        req = new ConnectionRequest();
    }

    public static ServicePoste getInstance() {
        if (instance == null) {
            instance = new ServicePoste();
        }
        return instance;
    }
    
    
     public ArrayList<Categorie> displayCat(String url) {
        ArrayList<Categorie> Cats = new ArrayList<>();

        req.setUrl(url);
        req.addResponseListener((NetworkEvent evt) -> {
            JSONParser jsonp;
            jsonp = new JSONParser();

            try {
                Map<String, Object> mapEvents = jsonp.parseJSON(new CharArrayReader(new String(req.getResponseData()).toCharArray()));
                List<Map<String, Object>> listOfMaps = (List<Map<String, Object>>) mapEvents.get("root");
                for (Map<String, Object> obj : listOfMaps) {

                    Categorie Cat = new Categorie();

                    Cat.setCategorie_name(obj.get("categorieName").toString());
                    Cat.setCategorie_image(obj.get("categorieImage").toString());
                    Cats.add(Cat);
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        });
        //  return null;
        NetworkManager.getInstance().addToQueueAndWait(req);
        return Cats;
    }
    

    public ArrayList<Categorie> displayCats() {

        String url = Statics.BASE_URL + "/categorie/json/displayCat";
        return displayCat(url);
    }
    
    
     public ArrayList<Post> displayNew() {

        String url = Statics.BASE_URL + "/postes/json/displayNew";
        return displayPostes(url);
    }
    
    public ArrayList<Post> displaylike() {

        String url = Statics.BASE_URL + "/postes/json/displaylike";
        return displayPostes(url);
    }
    public ArrayList<Post> displayPostes() {

        String url = Statics.BASE_URL + "/postes/json/displayPostes";
        return displayPostes(url);
    }
    

   
    
     public boolean addPoste(Post P) {

        String url = Statics.BASE_URL + "/postes/json/addPoste?NomPost=" + P.getNom_post()
                + "&description=" + P.getDescription()          
                + "&categorie=" + P.getCategorie().getCategorie_name()
                + "&idUser=" + P.getid_user().getId_user()
                + "&posteDate=" + P.getPost_date()
                + "&file=" + P.getFile(); //cr??ation de l'URL
        req.setUrl(url);// Insertion de l'URL de notre demande de connexion
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200;
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }
    
    
    
    public ArrayList<Post>displayPostes(String url) {
        ArrayList<Post> postes = new ArrayList<>();
       
        req.setUrl(url);
        req.addResponseListener((NetworkEvent evt) -> {
            JSONParser jsonp;
            jsonp = new JSONParser();

            try {
                Map<String, Object> mapPostes = jsonp.parseJSON(new CharArrayReader(new String(req.getResponseData()).toCharArray()));
                List<Map<String, Object>> listOfMaps = (List<Map<String, Object>>) mapPostes.get("root");
                for (Map<String, Object> obj : listOfMaps) {

                    Post P = new Post();

                    float id = Float.parseFloat(obj.get("idPost").toString());
//                

                   // String type = obj.get("typeEvent").toString();
                   // type = type.substring(10, type.length() - 1);

                    //9a3ed yarjaali bhchichou brichou donc kaadi ncoupi menou ken feli hachti bih eli houwa esm l categorie
                    String categorie = obj.get("categorie").toString();
                    categorie = categorie.substring(15, categorie.length() - 1);

                    Categorie C = new Categorie();
                    C.setCategorie_name(categorie);

                    String username = obj.get("idUser").toString();
                    username = username.substring(10, username.length() - 1);

                    User U = new User();
                    U.setUsername(username);

                    P.setId_post((int) id);
                    P.setPost_date(obj.get("postDate").toString());
                   // E.setType_event(type);
                    P.setDescription(obj.get("description").toString());
                    P.setFile(obj.get("file").toString());
                    P.setId_user(U);
                    P.setCategorie(C);
                    P.setNom_post(obj.get("nomPost").toString());
                   // System.out.println(P.toString());
                    postes.add(P);
                }
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        });
        //  return null;
        NetworkManager.getInstance().addToQueueAndWait(req);
        return postes;
    }
    
    
    
     public boolean deletePost(int id){
        String url =Statics.BASE_URL+"/postes/json/deletePostesj/?idPost="+id;
        //http://127.0.0.1:8000/postes/json/deletePostesj/?idPost=15
        
        req.setUrl(url);
        
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
        
                req.removeResponseCodeListener(this);
                
            }
        });
        
        NetworkManager.getInstance().addToQueueAndWait(req);
        
        return resultOK;
        
    }
    
    
    
    
}
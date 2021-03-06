/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gui;




import com.codename1.components.InfiniteProgress;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.BOTTOM;
import static com.codename1.ui.Component.CENTER;
import static com.codename1.ui.Component.LEFT;
import static com.codename1.ui.Component.RIGHT;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.Toolbar;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.mycompany.entities.Post;
import com.mycompany.services.ServicePoste;
import static com.mycompany.utils.Statics.CurrentUser;
import java.io.IOException;
import java.util.ArrayList;


/**
 *
 * @author Adam Khalfaoui
 */
public class Posteslike  extends BaseForm {
    
    public Posteslike(Resources res) throws IOException {
       super("Postes", BoxLayout.y());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("News Feed");
        getContentPane().setScrollVisible(false);
        
        super.addSideMenu(res);
        tb.addSearchCommand(e -> {});
        
        Tabs swipe = new Tabs();

        Label spacer1 = new Label();
        Label spacer2 = new Label();
        addTab(swipe, res.getImage("welcome.jpg"), spacer1, "", "", "Postes ");
        
                
        swipe.setUIID("Container");
        swipe.getContentPane().setUIID("Container");
        swipe.hideTabs();
        
        ButtonGroup bg = new ButtonGroup();
        int size = Display.getInstance().convertToPixels(1);
        Image unselectedWalkthru = Image.createImage(size, size, 0);
        Graphics g = unselectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAlpha(100);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        Image selectedWalkthru = Image.createImage(size, size, 0);
        g = selectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        RadioButton[] rbs = new RadioButton[swipe.getTabCount()];
        FlowLayout flow = new FlowLayout(CENTER);
        flow.setValign(BOTTOM);
        Container radioContainer = new Container(flow);
        for(int iter = 0 ; iter < rbs.length ; iter++) {
            rbs[iter] = RadioButton.createToggle(unselectedWalkthru, bg);
            rbs[iter].setPressedIcon(selectedWalkthru);
            rbs[iter].setUIID("Label");
            radioContainer.add(rbs[iter]);
        }
                
        rbs[0].setSelected(true);
        swipe.addSelectionListener((i, ii) -> {
            if(!rbs[ii].isSelected()) {
                rbs[ii].setSelected(true);
            }
        });
        
        Component.setSameSize(radioContainer, spacer1, spacer2);
        add(LayeredLayout.encloseIn(swipe, radioContainer));
        
        ButtonGroup barGroup = new ButtonGroup();
        RadioButton all = RadioButton.createToggle("All", barGroup);
        all.setUIID("SelectBar");
        RadioButton neww = RadioButton.createToggle("New", barGroup);
        neww.setUIID("SelectBar");
        RadioButton popular = RadioButton.createToggle("Popular", barGroup);
        popular.setUIID("SelectBar");
        RadioButton Host = RadioButton.createToggle("Add Post", barGroup);
        
         Host.addActionListener((e) -> {
            try {
                InfiniteProgress ip = new InfiniteProgress();
                final Dialog ipDlg = ip.showInifiniteBlocking();
                new PosteAdd(res).show();
                refreshTheme();
            } catch (IOException ex) {
               
            }
        });
         popular.addActionListener((e) -> {
            try {
                InfiniteProgress ip = new InfiniteProgress();
                final Dialog ipDlg = ip.showInifiniteBlocking();
                new Posteslike(res).show();
                refreshTheme();
            } catch (IOException ex) {
               
            }
        });
         
         
         neww.addActionListener((e) -> {
            try {
                InfiniteProgress ip = new InfiniteProgress();
                final Dialog ipDlg = ip.showInifiniteBlocking();
                new displayNew(res).show();
                refreshTheme();
            } catch (IOException ex) {
               
            }
        });
         
         
         
         
        Host.setUIID("SelectBar");
        Label arrow = new Label(res.getImage("news-tab-down-arrow.png"), "Container");
        
        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(4, all, neww, popular, Host),
                FlowLayout.encloseBottom(arrow)
        ));
        
        all.setSelected(true);
      
        Host.setSelected(false);
        arrow.setVisible(false);
        addShowListener(e -> {
            arrow.setVisible(true);
            updateArrowPosition(popular, arrow);
        });
        bindButtonSelection(all, arrow);
        bindButtonSelection(neww, arrow);
        bindButtonSelection(popular, arrow);
        bindButtonSelection(Host, arrow);
        
        // special case for rotation
        addOrientationListener(e -> {
            updateArrowPosition(barGroup.getRadioButton(barGroup.getSelectedIndex()), arrow);
        });
     
          ArrayList<Post> list = ServicePoste.getInstance().displaylike();

        for (Post P : list) {
            String urlImage = "44ebdde27745ae31cd2cbc45fa3e9985).jpg";
            Image placeHolder = Image.createImage(12, 90);
            EncodedImage enc = EncodedImage.createFromImage(placeHolder, false);
            URLImage urlimg = URLImage.createToStorage(enc, urlImage, urlImage, URLImage.RESIZE_SCALE);
            // addButton(null,E.getCodeP(),E.getReduction(),E.getDated(),E.getDatef());
            // addButton(null,E.getCodeP(),E.getReduction(),E.getDated(),E.getDatef());
            //addButton(placeHolder, E.getNiveau());
            Image logo=null;
            String ppt="";
                   ppt="file://C:/xampp/php/www/ArtBox-CrashTest-WEB/public/Postes/" + P.getFile();
                   System.out.println(ppt);
            
                logo = Image.createImage(ppt).scaledHeight(500);
           
            
            addButton(logo, P.getNom_post(), false, P.getLikes(), 32);
            
            

            ScaleImageLabel image = new ScaleImageLabel(urlimg);
            Container containerImg = new Container();
            image.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);

        }
       
       
    }
    
    private void updateArrowPosition(Button b, Label arrow) {
        arrow.getUnselectedStyle().setMargin(LEFT, b.getX() + b.getWidth() / 2 - arrow.getWidth() / 2);
        arrow.getParent().repaint();
        
        
    }
    
    private void addTab(Tabs swipe, Image img, Label spacer, String likesStr, String commentsStr, String text) {
        int size = Math.min(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
        if(img.getHeight() < size) {
            img = img.scaledHeight(size);
        }
        Label likes = new Label(likesStr);
        Style heartStyle = new Style(likes.getUnselectedStyle());
        heartStyle.setFgColor(0xff2d55);
        FontImage heartImage = FontImage.createMaterial(FontImage.MATERIAL_FAVORITE, heartStyle);
        likes.setIcon(heartImage);
        likes.setTextPosition(RIGHT);

        Label comments = new Label(commentsStr);
        FontImage.setMaterialIcon(comments, FontImage.MATERIAL_CHAT);
        if(img.getHeight() > Display.getInstance().getDisplayHeight() / 2) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 2);
        }
        ScaleImageLabel image = new ScaleImageLabel(img);
        image.setUIID("Container");
        image.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        Label overlay = new Label(" ", "ImageOverlay");
        
        Container page1 = 
            LayeredLayout.encloseIn(
                image,
                overlay,
                BorderLayout.south(
                    BoxLayout.encloseY(
                            new SpanLabel(text, "LargeWhiteText"),
                            FlowLayout.encloseIn(likes, comments),
                            spacer
                        )
                )
            );

        swipe.addTab("", page1);
    }
    
   
    private void addButton(Image img, String title, boolean liked, int likeCount, int commentCount) {
       int height = Display.getInstance().convertToPixels(11.5f);
       int width = Display.getInstance().convertToPixels(14f);
       Button image = new Button(img.fill(width, height));
       image.setUIID("Label");
       Container cnt = BorderLayout.west(image);
       cnt.setLeadComponent(image);
       TextArea ta = new TextArea(title);
       ta.setUIID("NewsTopLine");
       ta.setEditable(false);

       Label likes = new Label(likeCount + " Likes  ", "NewsBottomLine");
       likes.setTextPosition(RIGHT);
       if(!liked) {
           FontImage.setMaterialIcon(likes, FontImage.MATERIAL_FAVORITE);
       } else {
           Style s = new Style(likes.getUnselectedStyle());
           s.setFgColor(0xff2d55);
           FontImage heartImage = FontImage.createMaterial(FontImage.MATERIAL_FAVORITE, s);
           likes.setIcon(heartImage);
       }
       Label comments = new Label(commentCount + " Comments", "NewsBottomLine");
       FontImage.setMaterialIcon(likes, FontImage.MATERIAL_CHAT);
       
       
       cnt.add(BorderLayout.CENTER, 
               BoxLayout.encloseY(
                       ta,
                       BoxLayout.encloseX(likes, comments)
               ));
       add(cnt);
       image.addActionListener(e -> ToastBar.showMessage(title, FontImage.MATERIAL_INFO));
   }
    
    private void bindButtonSelection(Button b, Label arrow) {
        b.addActionListener(e -> {
            if(b.isSelected()) {
                updateArrowPosition(b, arrow);
            }
        });
    }
    
    
    
    
    
}
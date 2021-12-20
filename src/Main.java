import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.Walking;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.script.RandomEvent;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;
import org.osbot.rs07.utility.ConditionalSleep2;

import java.awt.*;
import java.util.Random;

@ScriptManifest(name = "CookingScript", author = "Bulletmagnet", logo = "", version = 0.01, info = "CookingScript by bulletmagnet")
public class Main extends Script {

    Area CookingRangeArea = new Area(3212, 3217, 3209, 3212).setPlane(0);
    Area BankArea = new Area(3207, 3218, 3210, 3222).setPlane(2);



    public boolean Start;
    @Override
    public void onStart() throws InterruptedException {
        ItemToCook();
        if (getInventory().contains(item)){
            Cook();
        } else {
            bank();
        }
    }
    String item = "Raw shrimps";

    public void bank() throws InterruptedException {
        if (getInventory().isEmpty() || getInventory().contains("Shrimps")){
            walking.webWalk(Banks.LUMBRIDGE_UPPER);
            sleep(random(1000, 2500));
            getBank().open();
            sleep(random(1000, 2500));
            if (getInventory().contains("Shrimps")){
                getBank().depositAll();
            }
            sleep(random(1000, 2500));
            getBank().withdraw(item, 28);
            sleep(random(1000, 2500));
            getBank().close();
            } else {
                Cook();
            }

    }


    public void Cook() throws InterruptedException {

        RS2Object Range = objects.closest("Cooking range");
        if (!CookingRangeArea.contains(myPlayer()) && getInventory().contains(item)){
            log("WALKING TO COOKING RANGE");
            walking.webWalk(CookingRangeArea);
            sleep(random(1200, 2400));
        } else if (CookingRangeArea.contains(myPlayer()) && inventory.contains(item)){
            getInventory().interact("Use", "Raw shrimps");
            sleep(random(1200, 2100));
            Range.interact("Use");
            sleep(random(1200, 2100));
            log("INTERACTING WITH DIALOUGE BOX");
            RS2Widget cookMenu = widgets.get(270,14);
            if(cookMenu != null && cookMenu.isVisible())
                cookMenu.interact("Cook");
            sleep(random(1200, 2100));
            while(myPlayer().isAnimating()){
                sleep(random(1000, 2700));
                getMouse().moveOutsideScreen();
            }
            sleep(random(1200, 2100));
        } else {
            log("BANKING ON COOK METHOD");
            bank();
        }

    }

    public String ItemToCook(){
        item = "Raw shrimps";
        return item;
    }


    @Override
    public int onLoop() throws InterruptedException {
        if (getInventory().contains(item)){
            Cook();
        } else {
            bank();
        }
        return 603;
    }

    @Override
    public void onExit() throws InterruptedException {
        super.onExit();
    }
    @Override
    public void onPaint(Graphics2D g){
        g.setColor(Color.RED);
        Point mP = getMouse().getPosition();

        // Draw a line from top of screen (0), to bottom (500), with mouse x coordinate
        g.drawLine(mP.x, 0, mP.x, 500);

        // Draw a line from left of screen (0), to right (800), with mouse y coordinate
        g.drawLine(0, mP.y, 800, mP.y);

        g.drawLine(mP.x - 5, mP.y + 5, mP.x + 5, mP.y - 5);
        g.drawLine(mP.x + 5, mP.y + 5, mP.x - 5, mP.y - 5);
    }


}

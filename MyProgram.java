/**
 * Game: Ascension
 * Author: Gurangad Batth
 * 
 * Context:
 *      You wake up in a cold abandoned tunnel in your old wooden bed.
 *      You remember except the light. The goal of this game is to defeat
 *      an old man looking character in the game called "Zeus". You must
 *      cross a portal, talk to this zeus and fight him to the death.
 * 
 * Instructions:
 *      WASD or arrow-keys and space bar for mobility; left, right and jump.
 *      You must interact with the zeus character verbally (use mouse click).
 *      You must move and jump to dodge attacks (lightning bolts) conjured by
 *      zeus/old-man. After some time, your special attack cooldown will finish
 *      and you will be able to eliminate zeus using your special attack.
 *  
 * What Works
 * - Movement and game physics
 * - Cutscenes and Buttons
 * - Fade animations
 * - Game is progressed part by part using "gameState"
 * - Game resets all variables when "play again" action is initiated.
 * 
 * 
 * What doesn't Work:
 * - I did not have enough time to code in other characters for users to choose 
 *   so gameState 2 (choose your character) is practically useless because there
 *   is only one character. To progress from gameState 2; Press ENTER.
 * - There are not any major bugs in my game except the main boss fight.
 * 
 * Major Bug:
 *      When fighting the boss, sometimes you may randomly die in the vicinity of the
 *      attack. Or you may not die while attack is clearly effective. I have the collision
 *      or hit detector on LINE 881. There seems to be no major issue 
 *      in it. I decided to use CharacterX - 30 or 65 because the actual character sprite 
 *      dimentions are not accurate to character hitboxes. I tried implementing hitboxes 
 *      before but it was hard to integrate that hitbox method I had made into my main attack
 *      method.
 * 
 * Other information:
 * - Fade speeds and character speeds are controllable using method parameters.
 *
 * 
 * Additional Note:
 *  `   I feel like the code is slightly inefficient, but this is my first big project
 *      and I am glad it is this way because it will teach me how to be more efficient in
 *      the next project.
 * 
 * Also don't worry about the uploads and files organization, codeHS is too hard to upload files.
 * It would take too long to re organize them.
 * 
 */



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;




public class MyProgram extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
  // Timer for the game loop
  Timer gameTimer;
  // used to keep track of what keys are pressed and released
  boolean[] keyPressed = new boolean[256];
  // used to keep track of mouse buttons pressed and released
  boolean[] mousePressed = new boolean[10];
  // location of the mouse - this is updated everytime the mouse moves
  int mouseX = 0;
  int mouseY = 0;
  // How fast do you want your game to run? Frames Per Second  
  final int FPS = 60;
  // What size of screen do you want?
  final int WIDTH = 800;
  final int HEIGHT = 500;



  // This is the current game state which controls how the game progresses and
  // determines which logic branch should run.
  GameState gameState = GameState.HOME;




  // Below this point are are the variables I have created for the game.
  
  

  
  
  // These are variables part of the Homescreen where the Playbutton is.
  BufferedImage playButton = loadImage("PlayButton.png");
  BufferedImage playButtonClicked = loadImage("PlayButtonClicked.png");
  int playButtonX = 275;
  int playButtonWidth = 250;
  int playButtonY = 125;
  int playButtonHeight = 250;
  boolean onPlayButton = false;
  // The following 5 variables are to control the moving background.
  int frameCount = 0;
  int skySpeed = 4;
  BufferedImage nightSky = loadImage("HomescreenBackdrop.png");
  int nightSkyX = 0;
  boolean skyGoingRight = true;
  BufferedImage zeusHomescreen = loadImage("ZeusHomescreen.png");
  // The game logo.
  BufferedImage logo = loadImage("AscensionLogo.png");
  
  
  // These are variables part of "Choose your character" screen.
  BufferedImage chooseCharacter = loadImage("Choose.png");
  BufferedImage left = loadImage("left.png");
  BufferedImage right = loadImage("right.png");
  BufferedImage wasd = loadImage("WASD.png");
  BufferedImage toMove = loadImage("ToMove.png");
  
  
  
  // These are variables for keyboard relation sprites and control menu.
  BufferedImage leftKey = loadImage("Keyboard/leftKey.png");
  BufferedImage rightKey = loadImage("Keyboard/rightKey.png");
  BufferedImage changeCharacter = loadImage("changeCharacter.png");
  BufferedImage enterKey = loadImage("enterKey.png");
  BufferedImage startGame = loadImage("startGame.png");
  BufferedImage controlTips = loadImage("controlTips.png");
  BufferedImage showControls = loadImage("ShowControls.png");
  BufferedImage keyP = loadImage("PKey.png");
  BufferedImage hideControls = loadImage("HideControls.png");
  BufferedImage controlMenu = loadImage("ControlMenu.png");
  BufferedImage blur = loadImage("Blur.png");
  boolean controlsVisible = false;
  
  
  // These are variables for the tunnel stage of game.
  BufferedImage bed = loadImage("bed.png");
  BufferedImage cabin = loadImage("metalTunnelBackdrop.png"); // Ignore the name cabin; too lazy to change.
  BufferedImage character = loadImage("Character.png");
  // Tunnel movement related variables.
  boolean movingTunnel = false;
  boolean reverseMovingTunnel = false;
  boolean c1Walking = false;
  boolean c1WalkingReverse = false;
  
  
  // The following are animations for the character to be able to move.
  BufferedImage[] c1Walk = new BufferedImage[8];
  BufferedImage[] c1Fall = new BufferedImage[8];
  BufferedImage character1 = loadImage("Character.png");
  double c1WalkSpeed = 0;
  int tunnelX = 0;
  int tunnelX2 = tunnelX + 750;
  int tunnelX3 = tunnelX2 + 750;
  boolean deleteWasdToMove = false;
  
  
  
  boolean stage2Adjustments = true;
  
  // The following are variables for the portal animation and cutscene.
  BufferedImage[] portal = new BufferedImage[7];
  double portalSpeed = 0;
  BufferedImage portalBack = loadImage("portalBack.png");
  boolean nearPortal = false;
  BufferedImage portalBubble = loadImage("PortalBubble.png");
  boolean cutsceneWalking = false;
  BufferedImage[] c1Fade = new BufferedImage[8];
  boolean characterFade = false;
  boolean fadeComplete = false;
  
  // Character dimentions used for movement and game physics.
  int characterX = 178;
  int characterY = 230;
  int characterHeight = 150;
  int characterWidth = 150;
  int lastMovement = 0; // 0-right, 1-left
  
  // I used these variables to make a custom timer in the game counting in seconds.
  boolean startTimer = false;
  int timerX = 0;
  int timerWidth = 20;
  boolean reverseTimer = false;
  int secondsPassed = 0;
  
  
  // Some variables for game physics to function.
  int jumpY = 0;
  int gravity = 1;
  int maxJumpVelocity = -15;
  boolean onGround = true;
  BufferedImage[] c1Jump = new BufferedImage[8];
  boolean jumping = false;
  
  // Sprite for heart picture.
  BufferedImage heart = loadImage("Heart.png");

 
  // These variables for prefight - gameState 5
  BufferedImage[] prefightBackdrop = new BufferedImage[6];
  double prefightBackdropSpeed = 0;
  BufferedImage fightButton = loadImage("FightButton.png");
  BufferedImage fightButtonClicked = loadImage("FightButtonClicked.png");
  boolean onFightButton = false;
  int fightButtonX = 620;
  int fightButtonY = 360;
  int fightButtonWidth = 150;
  int fightButtonHeight = 150;
  
  
  // These are variables used to fade screens inbetween gameStates
  int screenTransparency = 0;
  boolean reverseFade = false;
  boolean startFading = false;
  boolean fadingDone = false;
  
  // These are variables part of gameState 7 (Interaction with old man).
  BufferedImage heavenBackdrop = loadImage("Heaven.png");
  BufferedImage cloud = loadImage("Cloud.png");
  boolean stage6Adjustments = true;
  int heavenGround = 430;
  BufferedImage heavenFloor = loadImage("HeavenFloor.png");
  BufferedImage[] zeusIdle = new BufferedImage[4];
  BufferedImage[] zeusRun = new BufferedImage[4];
  double zeusSpeed = 0;
  BufferedImage characterHead = loadImage("CharacterHead.png");
  BufferedImage zeusHead = loadImage("ZeusHead.png");
  
  // Narration variables.
  int nar = 1;
  int charTurn = 1;
  
  // Variables for gameState 8.
  BufferedImage fightScreen = loadImage("FightScreen.png");
  boolean stage8Adjustments = true;
  
  // These are variables for the endgame of the game.
  int zeusX = 710;
  boolean reverseZeus = false;
  int attackX = 0;
  int hitCount = 0;
  boolean gameOver = false;
  boolean characterDead = false;
  boolean specialAttack = false;
  BufferedImage kKey = loadImage("Kkey.png");
  BufferedImage pressMe = loadImage("specialAttack.png");
  BufferedImage[] darkBolt = new BufferedImage[12];
  double boltSpeed = 0;
  boolean boltDone = false;
  BufferedImage gameWon = loadImage("GameWon.png");
  BufferedImage gameLost = loadImage("GameLost.png");
  double c1FallSpeed = 0;
  BufferedImage again = loadImage("Again.png");
  BufferedImage againClick = loadImage("AgainClick.png");
  boolean onAgainButton = false;
  int againX = 370;
  int againY = 200;
  int againWidth = 300;
  int againHeight = 200;
  
  

  // Initialize things BEFORE the game starts
  public void setup(){
    // Variables that need to be initialized before game starts
    
    
    // The following for loops will load in all the animations used in the game.
    
    for(int i = 0; i < c1Walk.length; i++)
    {
        c1Fall[i] = loadImage("Animations/Char1/Char1Fall/C1F"+ (i+1) + ".png");
    }
    
    for(int i = 0; i < c1Walk.length; i++)
    {
        c1Walk[i] = loadImage("Animations/Char1/Char1Walking/C1W"+ (i+1) + ".png");
    }
    
    for(int i = 0; i < c1Jump.length; i++)
    {
        c1Jump[i] = loadImage("Animations/Char1/Char1Jump/C1J"+ (i+1) + ".png");
    }
    
    for(int i = 0; i < portal.length; i++)
    {
        portal[i] = loadImage("Animations/Portal/portal"+ (i+1) + ".png");
    }
    
    for(int i = 0; i < c1Fade.length; i++)
    {
        c1Fade[i] = loadImage("Animations/Char1/Char1Fade/C1FADE"+ (i+1) + ".png");
    }
    
    for(int i = 0; i < prefightBackdrop.length; i++)
    {
        prefightBackdrop[i] = loadImage("Animations/TrippyBackdrop/PFBD"+ (i+1) + ".png");
    }
    
    for(int i = 0; i < zeusIdle.length; i++)
    {
        zeusIdle[i] = loadImage("Animations/Zeus/ZeusIdle/ZI"+ (i) + ".png");
    }
    
    for(int i = 0; i < zeusRun.length; i++)
    {
        zeusRun[i] = loadImage("Animations/Zeus/ZeusRun/ZR"+ (i) + ".png");
    }
    
    for(int i = 0; i < darkBolt.length; i++)
    {
        darkBolt[i] = loadImage("Animations/Attack/Dark-Bolt"+ (i+1) + ".png");
    }
    
    // ^^ all animations loaded.
  }

    // Where game components will be painted.
    @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.clearRect(0, 0, WIDTH, HEIGHT);

    // This paint component is where all my drawings and drawing methods will be placed.
    // Drawing methods are possible by using "g" graphics as a parameter in the method.
    
    // This is the game timer, it does not need to be drawn but it helped me make it in the first place.
    g.fillRect(timerX, 250, timerWidth, 20);
    
    
    /**
     *   THESE ARE WHERE THE DRAWINGS ARE:
     * 
     * GAMESTATE -1: This is where all the variables are reset.
     * GAMESTATE 0: Homescreen with button to play game.
     * GAMESTATE 1: Choose your character screen.
     * GAMESTATE 2: Wake up in tunnel.
     * GAMESTATE 3: Talking with portal.
     * GAMESTATE 4: Portal Cutscene animation.
     * GAMESTATE 5: Prefight state where fight button is present.
     * GAMESTATE 6: You are in heaven.
     * GAMESTATE 7: Interaction with zeus state.
     * GAMESTATE 8: Fight cutscene.
     * GAMESTATE 9: Boss fight with zeus.
     * GAMESTATE 10, 11, 12, and 13: This is the end game (option to restart).
     */
    
    
    if(gameState == GameState.HOME)
    {
        // Drawings for background.
        g.drawImage(nightSky, nightSkyX, 0, 1040, 693, null);
        g.drawImage(zeusHomescreen, -10, 180, 350, 350, null);
        g.drawImage(logo, 200, -100, 400, 400, null);
        g.drawImage(blur, 0, 0, 800, 500, null);
        g.drawImage(blur, 0, 0, 800, 500, null);
        
        // Logic based drawing for playbutton. (Should have done half of this in logic).
        if(mouseX >= playButtonX + 12 && mouseX <= playButtonX + playButtonWidth - 12)
        {
            if(mouseY >= playButtonY + 45 && mouseY <= playButtonY + playButtonWidth - 55)
            {
                g.setColor(Color.BLACK);
                g.fillRect(300, 184, 202, 124);
                onPlayButton = true;
                g.drawImage(playButtonClicked, playButtonX, playButtonY, playButtonWidth, playButtonHeight, null);
            }
            else
            {
                onPlayButton = false;
                g.setColor(Color.BLACK);
                g.fillRect(300, 184, 202, 124);
                g.drawImage(playButton, playButtonX, playButtonY, playButtonWidth, playButtonHeight, null);

            }
        }
        else
        {
            onPlayButton = false;
            g.setColor(Color.BLACK);
            g.fillRect(300, 184, 202, 124);
            g.drawImage(playButton, playButtonX, playButtonY, playButtonWidth, playButtonHeight, null);
        }
        
        // Fade method. middle parameter 1 is fade out, 2 is fade in.
        if(startFading)
        {
            fadeSwitchState(g, 1, 1); // second parameter is part and third is speed.
        }
    }
    else if(gameState == GameState.CHOOSE_CHARACTER)
    {
        // Backgrond
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 500);
        g.drawImage(chooseCharacter, -80, -170, 960, 540, null);
        
        // Switching UI (does not work)
        g.drawImage(left, 220, 160, 200, 200, null);
        g.drawImage(right, 380, 160, 200, 200, null);
        g.fillRect(300, 202, 200, 105);
        g.drawImage(character1, 300, 150, 200, 200, null);
        
        // Explain controls for character switch.
        g.drawImage(leftKey, 10, 445, 50, 50, null);
        g.drawImage(rightKey, 70, 445, 50, 50, null);
        g.drawImage(changeCharacter, 13, 425, 1366/10, 130/10, null);
        
        // Explain controls for start game.
        g.drawImage(enterKey, 190, 445, 100, 50, null);
        g.drawImage(startGame, 194, 425, 854/10, 130/10, null);
        
        // Fade in animation.
        if(startFading && !fadingDone)
        {
            if(fadeSwitchState(g, 2, 2) == true)
            {
                startFading = false;
                fadingDone = true;
            }
        }
        
        // Fade out animation.
        if(startFading && fadingDone)
        {
            fadeSwitchState(g, 1, 2);
        }
    }
    else if(gameState == GameState.TUNNEL)
    {
        // Re-adjust character for game start.
        if(stage2Adjustments)
        {
            rearrangeCharacter(GameState.TUNNEL);
        }
        
        // Moving tunnel drawings.
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 500);
        g.drawImage(cabin, tunnelX, 0, 750, 500, null);
        g.drawImage(cabin, tunnelX2, 0, 750, 500, null);
        g.drawImage(cabin, tunnelX3, 0, 750, 500, null);
        g.drawImage(bed, tunnelX + 5, 254, (300*10)/13, (200*10)/13, null);
        int frameP = (int)portalSpeed;
        // Portal drawing.
        g.drawImage(portalBack, tunnelX3 + 400, 200, 200, 200, null);
        g.drawImage(portal[frameP], tunnelX3 + 400, 200, 200, 200, null);
        
        // These are methods to draw basic UI (health, character, menu).
        drawHealth(g);
        drawCharacter(g);
        controlMenu(g);
        
        
        // Fade in animation.
        if(startFading && !fadingDone)
        {
            if(fadeSwitchState(g, 2, 2) == true)
            {
                startFading = false;
                fadingDone = true;
            }
        }
    }
    else if(gameState == GameState.PORTAL_DIALOGUE)
    {
        // Reset character Y if jumped in previous state
        characterY = 230;
        
        // Draw tunnels again.
        g.drawImage(cabin, tunnelX2, 0, 750, 500, null);
        g.drawImage(cabin, tunnelX3, 0, 750, 500, null);
        int frameP = (int)portalSpeed;
        g.drawImage(portalBack, tunnelX3 + 400, 200, 200, 200, null);
        g.drawImage(portal[frameP], tunnelX3 + 400, 200, 200, 200, null);
        
        //Draw portal and characters and text bubble.
        g.drawImage(character1, characterX, characterY, characterWidth, characterHeight, null);
        g.drawImage(portalBubble, 380, 130, 639/2, 270/2, null);
        g.drawImage(enterKey, 490, 173, 100, 50, null);
        
    }
    else if(gameState == GameState.PORTAL_CUTSCENE)
    {
        // Reset character Y if jumped in previous state
        characterY = 230;
        
        // Draw tunnels again
        g.drawImage(cabin, tunnelX3-610, 0-402+60, 750*2, 500*2, null);
        int frameP = (int)portalSpeed;
        g.drawImage(portalBack, tunnelX3 + 400-200, 200-175+60, 200*2, 200*2, null);
        g.drawImage(portal[frameP], tunnelX3 + 400-200, 200-175+60, 200*2, 200*2, null);
        
        // Animation for character walking cutscene animation.
        int frameC = (int)c1WalkSpeed;
        if(characterFade)
        {
            g.drawImage(c1Fade[frameC], characterX-400, characterY-175+60, characterWidth*2, characterHeight*2, null);
            if(frameC == 7)
            {
                fadeComplete = true;
            }
        }
        
        // Character fades into the portal.
        if(!characterFade && !fadeComplete)
        {
            if(c1Walking)
            {
                g.drawImage(c1Walk[frameC], characterX-400, characterY-175+60, characterWidth*2, characterHeight*2, null);
            }
            else if(lastMovement == 0)
            {
                g.drawImage(character1, characterX-400, characterY-175+60, characterWidth*2, characterHeight*2, null);
            }
            
        }
        
        // Fade out animation.
        if(startFading && fadingDone)
        {
            fadeSwitchState(g, 1, 2);
        }
    }
    else if(gameState == GameState.PREFIGHT)
    {
        // Draw the trippy moving background.
        int frameB = (int)prefightBackdropSpeed;
        g.drawImage(prefightBackdrop[frameB], 0, -170, 800, 800, null);
        g.drawImage(zeusHomescreen, -10, 70, 500, 500, null);
        
        // Draw the button wheter you are hovering over it or not.
        if(onFightButton)
        {
            g.drawImage(fightButtonClicked, fightButtonX, fightButtonY, fightButtonWidth, fightButtonHeight, null);
        }
        else
        {
            g.drawImage(fightButton, fightButtonX, fightButtonY, fightButtonWidth, fightButtonHeight, null);
        }
        
        
        // Below are fade in AND out animations.
        
        if(startFading && fadingDone)
        {
            fadeSwitchState(g, 1, 3);
        }
        if(startFading && !fadingDone)
        {
            if(fadeSwitchState(g, 2, 2) == true)
            {
                startFading = false;
                fadingDone = true;
            }
        }
    }
    else if(gameState == GameState.HEAVEN)
    {
        // End timer from previous state.
        startTimer = false;
        
        // Draw background
        g.drawImage(heavenBackdrop, 0, 0, 800, 533, null);
        g.drawImage(heavenFloor, 0, heavenGround, 400, 533/2, null);
        g.drawImage(heavenFloor, 400, heavenGround, 400, 533/2, null);
        
        // Adjust characters.
        if(stage6Adjustments)
        {
            rearrangeCharacter(GameState.HEAVEN);
        }
        
        // Basic UI methods.
        drawCharacter(g);
        drawHealth(g);
        
        // Draw Zeus Idling.
        int frameZ = (int)zeusSpeed;
        g.drawImage(zeusIdle[frameZ], 630, 350, -80, 80, null);
        
        // Control menu method.
        controlMenu(g);
        
        
        // Fade animation.
        if(startFading)
        {
            if(fadeSwitchState(g, 2, 2) == true)
            {
                startFading = false;
            }
        }
    }
    else if(gameState == GameState.ZEUS_INTERACTION)
    {
        // Reset player Y if jumped previous state.
        characterY = 300;
        g.drawImage(heavenBackdrop, 0, 0, 800, 533, null);
        g.drawImage(heavenFloor, 0, heavenGround, 400, 533/2, null);
        g.drawImage(heavenFloor, 400, heavenGround, 400, 533/2, null);
        g.drawImage(character1, characterX, characterY, characterWidth, characterHeight, null);
        
        // Zeus animation.
        int frameZ = (int)zeusSpeed;
        g.drawImage(zeusIdle[frameZ], 630, 350, -80, 80, null);
        
        // Narration method.
        narration(g, charTurn, nar);
        
        
        // Fade animation
        if(startFading)
        {
            fadeSwitchState(g, 1, 3);
        }
        
        // This belongs in logic but its too far down there and im too lazy.
        if(secondsPassed == 5)
        {
            startFading = true;
        }
    }
    else if(gameState == GameState.FIGHT_CUTSCENE)
    {
        // Cutscene fight screen.
        g.drawImage(fightScreen, 0, 0, 800, 500, null);
        
        // Fade animations.
        if(startFading && !fadingDone)
        {
            if(fadeSwitchState(g, 2, 2) == true)
            {
                startFading = false;
                fadingDone = true;
            }
        }
        
        if(startFading)
        {
            fadeSwitchState(g, 1, 3);
        }
    }
    else if(gameState == GameState.BOSS_FIGHT)
    {
        // Draw heaven again (I should really make a method for drawing heaven).
        g.drawImage(heavenBackdrop, 0, 0, 800, 533, null);
        g.drawImage(heavenFloor, 0, heavenGround, 400, 533/2, null);
        g.drawImage(heavenFloor, 400, heavenGround, 400, 533/2, null);

        // Adjust characters.
        if(stage8Adjustments)
        {
            rearrangeCharacter(GameState.FIGHT_CUTSCENE);
        }
        
        // Basic UI
        drawHealth(g);
        drawCharacter(g);
        
        // These are drawings and logic methods for the Boss Fight.
        zeusMovement(g);
        zeusAttack(g);
        if(specialAttack)
        {
            showAttack(g);
        }
        
        // Fade Animations
        if(startFading)
        {
            if(fadeSwitchState(g, 2, 2) == true)
            {
                startFading = false;
                fadingDone = true;
            }
        }
    }
    else if(gameState == GameState.DEATH_ANIMATION)
    {
        // If you lose boss fight you end up here and these are the dead animations.
        
        g.drawImage(heavenBackdrop, 0, 0, 800, 533, null);
        g.drawImage(heavenFloor, 0, heavenGround, 400, 533/2, null);
        g.drawImage(heavenFloor, 400, heavenGround, 400, 533/2, null);
        
        // Zeus animation.
        int frameZ = (int)zeusSpeed;
        if(reverseZeus)
        {
            g.drawImage(zeusIdle[frameZ], zeusX, 350, 80, 80, null);
        }
        else
        {
            g.drawImage(zeusIdle[frameZ], zeusX+80, 350, -80, 80, null);
        }
        
        
        // This is the character falling down animation.
        int frameC = (int)c1FallSpeed;
        g.drawImage(c1Fall[frameC], characterX, characterY, characterWidth, characterHeight, null);
        
        
        // Fade animation
        if(startFading)
        {
            fadeSwitchState(g, 1, 3);
        }
        
    }
    else if(gameState == GameState.WIN_ANIMATION)
    {
        // You end up here if you win.
        
        // Heaven
        g.drawImage(heavenBackdrop, 0, 0, 800, 533, null);
        g.drawImage(heavenFloor, 0, heavenGround, 400, 533/2, null);
        g.drawImage(heavenFloor, 400, heavenGround, 400, 533/2, null);
        
        // Zeus disappears if bolt done.
        if(!boltDone)
        {
            int frameZ = (int)zeusSpeed;
            if(reverseZeus)
            {
                g.drawImage(zeusIdle[frameZ], zeusX, 350, 80, 80, null);
            }
            else
            {
                g.drawImage(zeusIdle[frameZ], zeusX+80, 350, -80, 80, null);
            }
        
        }
        
        drawCharacter(g);
        
        // This is drawing of the attack happening (animation).
        int frameA = (int)boltSpeed;
        g.drawImage(darkBolt[frameA], zeusX-90, 100, 64*4, 88*4, null);
        
        
        // Fade.
        if(startFading)
        {
            fadeSwitchState(g, 1, 3);
        }
    }
    else if(gameState == GameState.WIN_SCREEN)
    {
        // This is win/Lose state, play again button is present.
        
        g.drawImage(gameWon, 0, 0, 800, 500, null);
        
        // Play again button
        playAgain(g);
        
        
        // Fade animations
        if(startFading && !fadingDone)
        {
            if(fadeSwitchState(g, 2, 2) == true)
            {
                startFading = false;
                fadingDone = true;
            }
        }
        
    }
    else if(gameState == GameState.LOSE_SCREEN)
    {
        // This is win/Lose state, play again button is present.
       
        g.drawImage(gameLost, 0, 0, 800, 500, null);
        
        // Play again button
        playAgain(g);
        
        // Fade animations
        if(startFading && !fadingDone)
        {
            if(fadeSwitchState(g, 2, 2) == true)
            {
                startFading = false;
                fadingDone = true;
            }
        }
        
    }
    
  }
  
  public void playAgain(Graphics g)
  {
      startFading = false;
      g.setColor(Color.BLACK);
      g.fillRect(407, 235, 225, 143);
      
      // Draw button based on logic below.
      if(onAgainButton)
      {
          g.drawImage(againClick, againX, againY, againWidth, againHeight, null);
      }
      else
      {
          g.drawImage(again, againX, againY, againWidth, againHeight, null);
      }
      
      // Check if hovering.
      if(mouseX >= againX  && mouseX <= againX + againWidth)
      {
          if(mouseY >= againY+47 && mouseY <= againY + againHeight - 41)
          {
              onAgainButton = true;
          }
          else
          {
              onAgainButton = false;
          }
      }
      else
      {
          onAgainButton = false;
      }
  }
  
  public void showAttack(Graphics g)
  {
      // Draw instructions.
      g.drawImage(kKey, 10, 445, 50, 50, null);
      g.drawImage(pressMe, 64, 462, 1040/10, 130/10, null);
      
      // You win.
      if(keyPressed[KeyEvent.VK_K])
      {
          gameState = GameState.WIN_ANIMATION;
      }
  }
  public void drawHealth(Graphics g)
  {
      // Draw a little heart at bottom right of screen.
      if(!gameOver)
      {
          g.drawImage(heart, 800-(454/12) - 10, 500-(400/12)-10, 454/12, 400/12, null);
      }
  }
  public void zeusAttack(Graphics g)
  {
    // A moving attack based on if zeus is going left or right.
    if(!reverseZeus)
    {
      int boxX = zeusX + attackX;
      g.setColor(Color.YELLOW);
      g.fillRect(boxX, 405, 20, 20);
      attackX-=4;
      // This will ensure the attack is reposition every 5 seconds.
      if(secondsPassed % 5 == 0)
      {
          // Reset attack.
          attackX = 0;
      }
    }
    
    // Does the same as above but reversed.
    if(reverseZeus)
    {
      int boxX = zeusX + attackX;
      g.setColor(Color.YELLOW);
      g.fillRect(boxX+80, 405, 20, 20);
      attackX+=4;
      if(secondsPassed % 5 == 0)
      {
          attackX = 0;
      }
    }
    
    // THIS IS WHERE THE BUGS ARE BEING CAUSED BUT I CANNOT FIND ERROR IN THIS?
    if(((zeusX+attackX)+20) > (characterX+30)    &&   (zeusX+attackX) < ((characterX+30)+65))
    {
        if(characterY > 280)
        {
            // Game over
            gameOver = true;
        }
    }
  }
  public void zeusMovement(Graphics g)
  {
      // Draw zeus running animation based on direction.
      int frameZ = (int)zeusSpeed;
      if(reverseZeus)
      {
          g.drawImage(zeusRun[frameZ], zeusX, 350, 80, 80, null);
      }
      else
      {
          g.drawImage(zeusRun[frameZ], zeusX+80, 350, -80, 80, null);
      }
      
      // Logic for drawing based on screen porportions.
      if(!reverseZeus)
      {
          zeusX--;
      }
      else
      {
          zeusX++;
      }
      
      // Left end of screen
      if(zeusX <=0)
      {
          reverseZeus = true;
          attackX = 0;
      }
      // Right end of screen
      if(zeusX >= 750)
      {
          reverseZeus = false;
          attackX = 0;
      }
  }
  
  public boolean fadeSwitchState(Graphics g, int part, int speed) // 1-slow, 2-medium slow, 3-medium, 4-fast
  {
    if(part == 1)
    {
        // Part one increases transparency till 255 then adds a gamestate.
        Color black = new Color(0, 0, 0, screenTransparency);
        g.setColor(black);
        g.fillRect(0, 0, 800, 500);
        if(!reverseFade)
        {
            screenTransparency+=speed;
        }
        if(screenTransparency >= 252)
        {
            reverseFade = true;
            gameState = GameState.values()[gameState.ordinal() + 1];
            fadingDone = false;
        }
    }
    if(part == 2)
    {
        // Part 2 decreases transparency till 0.
        Color black = new Color(0, 0, 0, screenTransparency);
        g.setColor(black);
        g.fillRect(0, 0, 800, 500);
        if(reverseFade)
        {
            screenTransparency-=speed;
        }
        if(screenTransparency <= 0)
        {
            reverseFade = false;
            // If part 2 complete, return true.
            return true;
        }
    }
    // retun false.
    return false;
  }

  public void rearrangeCharacter(GameState state)
  {
      // ADJUST THE VARIABLE BASED FOR STATE.
      if(state == GameState.TUNNEL)
      {
          stage2Adjustments = false;
          characterX = 178;
          characterY = 230;
          characterHeight = 150;
          characterWidth = 150;
          tunnelX = 0;
          tunnelX2 = tunnelX + 750;
          tunnelX3 = tunnelX2 + 750;
          characterFade = false;
          fadeComplete = false;
          
      }
      // ADJUST THE VARIABLE BASED FOR STATE.
      if(state == GameState.HEAVEN)
      {
          stage6Adjustments = false;
          characterX = 100;
          characterY = 299;
          characterWidth = 130;
          characterHeight = 130;
      }
      // ADJUST THE VARIABLE BASED FOR STATE.
      if(state == GameState.FIGHT_CUTSCENE)
      {
          stage8Adjustments = false;
          characterX = 100;
          characterY = 299;
          characterWidth = 130;
          characterHeight = 130;
          zeusX = 710;
      }
  }
  
  public void drawCharacter(Graphics g)
  {
      int frameC = (int)c1WalkSpeed;
      /**
       * These are movement animations of character based on game physics, direction and logic.
       * 
       * Right jump, left jump, right run, left run, right left.
       */
       
      if(c1Walking && !jumping)
      {
          g.drawImage(c1Walk[frameC], characterX, characterY, characterWidth, characterHeight, null);
      }
      else if(jumping && lastMovement == 0)
      {
          g.drawImage(c1Jump[frameC], characterX, characterY, characterWidth, characterHeight, null);
      }
      else if(jumping && lastMovement == 1)
      {
          g.drawImage(c1Jump[frameC], characterX+characterWidth, characterY, -characterWidth, characterHeight, null);
      }
      else if(c1WalkingReverse && !jumping)
      {
          g.drawImage(c1Walk[frameC], characterX+characterWidth, characterY, -characterWidth, characterHeight, null);
      }
      else if(lastMovement == 0)
      {
          g.drawImage(character1, characterX, characterY, characterWidth, characterHeight, null);
      }
      else if(lastMovement == 1)
      {
          g.drawImage(character1, characterX+characterWidth, characterY, -characterWidth, characterHeight, null);
      }
  }
  public void sideMovementMechanism()
  {
      //Simple mechanism allows character to move left and right based on key pressed.
      c1WalkSpeed += 0.2;
      if(c1WalkSpeed >= c1Walk.length)
      {
        c1WalkSpeed = 0;
      }
      // Control animation.
      c1Walking = false;
      c1WalkingReverse = false;
      
      // Movement
      if(keyPressed[KeyEvent.VK_D] || keyPressed[KeyEvent.VK_RIGHT])
      {
          // Start walking and walking animation
          c1Walking = true;
          lastMovement = 0;
          if(characterX+characterWidth <= 830)
          {
              characterX+=4;
          }
          
      }
      else if(keyPressed[KeyEvent.VK_A]|| keyPressed[KeyEvent.VK_LEFT])
      {
          // Start walking and walking animation
          c1WalkingReverse = true;
          lastMovement = 1;
          if(characterX >= -30)
          {
              characterX-=4;
          }
          
      }
  }
  
  public void jumpMechanism(int ground)
  {
      
      // If key is pressed, jumping is true and the integers cancel out and decrease till character reaches ground.
      if(keyPressed[KeyEvent.VK_SPACE] || keyPressed[KeyEvent.VK_W] || keyPressed[KeyEvent.VK_UP])
      {
          if(onGround)
          {
              jumpY = maxJumpVelocity;
              onGround = false;
              jumping = true;
          }
      }
      
      // Move in the up or down direction
      jumpY += gravity;
      characterY += jumpY;
      
      // To know when on ground.
      if(characterY + characterHeight >= ground)
      {
          jumpY = 0;
          onGround = true;
          jumping = false;
          characterY = ground - characterHeight;
      }
  }
  
  public void controlMenu(Graphics g)
  {
      // If control menu has not been pressed.
      if(!controlsVisible)
      {
          g.drawImage(showControls, 64, 462, 1430/10, 130/10, null);
          g.drawImage(controlTips, 10, 445, 50, 50, null);
      }
      else if(controlsVisible)
      {
          g.drawImage(blur, 0, 0, 800, 500, null);
          g.drawImage(blur, 0, 0, 800, 500, null);
          g.drawImage(hideControls, 64, 462, 1374/10, 130/10, null);
          g.drawImage(controlMenu, -45, -115, 2541*10/29, 1881*10/29, null);
          g.drawImage(keyP, 10, 445, 50, 50, null);
      }
      // ^^ If control menu has been pressed.
      
      
      // Key logic to control drawing.
      if(!controlsVisible && keyPressed[KeyEvent.VK_H])
      {
          controlsVisible = true;
      }
      else if(controlsVisible && keyPressed[KeyEvent.VK_P])
      {
          controlsVisible = false;
      }
  }
  public void narration(Graphics g, int character, int nar) //char 1 = player, char 2 = zeus, nar = which text to display
  {
      
      // There are dialouges in gameState 7.
      String[] dialouges = {
          "Who are you?",
          "I am Zeus, God of Thunder.",
          "No you're not... you're just an old man in a bathrobe.",
          "That's very disrespectful. This is a divine robe.",
          "Right... and I supose you smite people with a cane?",
          "Mind your tone odd child... or you shall be punished.",
          "Prove you are zeus... Summon a light bolt or something.",
          "Unfortunately, my powers are... limited in this haven.",
          "Sounds convienient.",
          "You'll believe soon enough child... The storm ALWAYS RETURNS..."
      };
      
      // Font and size and color.
      Color white = new Color(255, 255, 255, 130);
      g.setColor(white);
      g.fillRect(10, 10, 780, 70);
      // Which head to display.
      if(character % 2 != 0)
      {
          g.drawImage(characterHead, -5, -15, 120, 120, null);
      }
      if(character % 2 == 0)
      {
          g.drawImage(zeusHead, 5, 5, 90, 90, null);
      }
      // Set font and color
      g.setFont(new Font("Arial", Font.BOLD, 18));
      g.setColor(Color.BLACK);
      
      g.drawString(dialouges[nar-1], 100, 55);
  }
  
  // GAME LOGIC
  public void loop() {
      
      // Before other states, this is logic for timer.
      
      // 780/13 is exactly 60. and 60FPS game meaning 1 second for timerX to reach 780.
      if(startTimer)
      {
          if(!reverseTimer)
          {
              timerX+=13;
          }
          else
          {
              timerX-=13;
          }
          if(timerX+timerWidth >= 780)
          {
              reverseTimer = true;
              secondsPassed++;
          }
          else if(timerX <= 0)
          { 
              reverseTimer = false;
              secondsPassed++;
          }
      }
      // ^^^ every time timerX hits 780 or 0, seconds passed goes up by one.
     
     
      // RESET ALL VARIABLES IF THE GAME IS PLAYED AGAIN. (I HAD TO DO THIS BECAUSE WITHOUT IT, TOO MANY BUGS).
      if(gameState == GameState.RESET)
      {
          boolean skyGoingRight = true;
          movingTunnel = false;
          reverseMovingTunnel = false;
          c1Walking = false;
          c1WalkingReverse = false;
          c1WalkSpeed = 0;
          tunnelX = 0;
          tunnelX2 = tunnelX + 750;
          tunnelX3 = tunnelX2 + 750;
          controlsVisible = false;
          stage2Adjustments = true;
          portalSpeed = 0;
          nearPortal = false;
          cutsceneWalking = false;
          characterFade = false;
          fadeComplete = false;
          characterX = 178;
          characterY = 230;
          characterHeight = 150;
          characterWidth = 150;
          lastMovement = 0;
          startTimer = false;
          timerX = 0;
          timerWidth = 20;
          reverseTimer = false;
          secondsPassed = 0;
          onGround = true;
          jumping = false;
          prefightBackdropSpeed = 0;
          onFightButton = false;
          screenTransparency = 0;
          reverseFade = false;
          startFading = false;
          fadingDone = false;
          stage6Adjustments = true;
          heavenGround = 430;
          zeusSpeed = 0;
          nar = 1;
          charTurn = 1;
          stage8Adjustments = true;        
          zeusX = 710;
          reverseZeus = false;
          attackX = 0;
          hitCount = 0;
          gameOver = false;
          characterDead = false;
          specialAttack = false;
          boltSpeed = 0;
          boltDone = false;
          c1FallSpeed = 0;
          onAgainButton = false;
          gameState = GameState.values()[gameState.ordinal() + 1];
      }
      if(gameState == GameState.HOME)
      {
          // Moving bricks (ignore word sky).
          frameCount++;
          if(frameCount % skySpeed == 0)
          {
              if(skyGoingRight)
              {
                  nightSkyX--;
              }
              else
              {
                  nightSkyX++;
              }
          }
          // ^^ move sky left and right.
          
          // Borders for sky.
          if(nightSkyX + 1040 <= WIDTH)
          {
              skyGoingRight = false;
          }
          else if(nightSkyX >= 0)
          {
              skyGoingRight = true;
          }
          
      }
      else if(gameState == GameState.CHOOSE_CHARACTER)
      {
          // ALL GAME LOGIC FOR Choose character Screen.
          if(keyPressed[KeyEvent.VK_ENTER])
          {
              startFading = true;
          }
      }
      else if(gameState == GameState.TUNNEL)
      {
          if(characterX >= 450)
          {
              movingTunnel = true;
          }
          else
          {
              movingTunnel = false;
          }
    
          if(characterX <= 75)
          {
              reverseMovingTunnel = true;
          }
          else
          {
              reverseMovingTunnel = false;
          }
          // ^^ moving tunnel logic based on borders and length.
          // Move tunnels left if character is moving and not near frame end.
          if(movingTunnel)
          {
              if(keyPressed[KeyEvent.VK_D] || keyPressed[KeyEvent.VK_RIGHT])
              {
                  c1Walking = true;
                  lastMovement = 0;
                  if(tunnelX <= 0 && tunnelX > -1350)
                  {
                      tunnelX-=4;
                      tunnelX2-=4;
                      tunnelX3-=4;
                      characterX-=4;
                  }
                  
                  
              }
          }
          
          // Is the same as above but for reverse.
          if(reverseMovingTunnel)
          {
              if(keyPressed[KeyEvent.VK_A] || keyPressed[KeyEvent.VK_LEFT])
              {
                  c1WalkingReverse = true;
                  lastMovement = 1;
                  if(tunnelX < 0 && tunnelX >= -1350)
                  {
                      tunnelX+=4;
                      tunnelX2+=4;
                      tunnelX3+=4;
                      characterX+=4;
                  }
                  
              }
          }
          
          // Check if character is near the portal.
          if(characterX >= 458)
          {
              nearPortal = true;
          }
          else
          {
              nearPortal = false;
          }
          
          // If so, add gameState and initiate cutscene.
          if(nearPortal)
          {
              gameState = GameState.values()[gameState.ordinal() + 1];
          }
          
          // Portal Animations.
          portalSpeed += 0.2;
          if(portalSpeed >= portal.length)
          {
            portalSpeed = 0;
          }
          
          // The following controls are for moving left and right (player).
          sideMovementMechanism();
          int ground = 380;
          jumpMechanism(ground);
          // space/up arrow/w to jump. method for jump and gravity logic.
          
          
      }
      else if(gameState == GameState.PORTAL_DIALOGUE)
      {
          
          // Update portal animation.
          portalSpeed += 0.2;
          if(portalSpeed >= portal.length)
          {
              portalSpeed = 0;
          }
          
          // Next gamestate if character finished interacting with portal.
          if(keyPressed[KeyEvent.VK_ENTER])
          {
              gameState = GameState.PORTAL_CUTSCENE;
              cutsceneWalking = true;
          }
          
      }
      else if(gameState == GameState.PORTAL_CUTSCENE)
      {
          
          // Cutscene moving for character.
          c1WalkSpeed += 0.2;
          if(c1WalkSpeed >= c1Walk.length || secondsPassed == 1)
          {
            c1WalkSpeed = 0;
          }
          // Stop moving when reach portal.
          if(characterX >= 800)
          {
              // Start the timer.
              cutsceneWalking = false;
              if(secondsPassed == 0)
              {
                  startTimer = true;
              }
              if(secondsPassed == 1)
              {
                  secondsPassed = 2;
              }
              if(secondsPassed == 2)
              {
                  // Fade the character.
                  if(fadeComplete)
                  {
                      characterFade = false;
                  }
                  else
                  {
                      characterFade = true;
                  }
              }
              // After character faded and 6 seconds passed, next state by fade.
              if(secondsPassed == 6)
              {
                  startTimer = false;
                  secondsPassed = 0;
                  timerX = 0;
                  startFading = true;
              }
          }
          
          // Logic for cutscene walking.
          if(cutsceneWalking)
          {
              c1Walking = true;
              characterX+=3;
          }
          else
          {
              c1Walking = false;
          }
          
          // Update portal animation.
          portalSpeed += 0.2;
          if(portalSpeed >= portal.length)
          {
              portalSpeed = 0;
          }
          
          
      }
      else if(gameState == GameState.PREFIGHT)
      {
          // Reset characterX. I had a bug and it should work without 
          // the line below but the program crashes at gamestate 6.
          characterX = 100;
          // Reset timer.
          secondsPassed = 0;
          
          // Update background animation.
          prefightBackdropSpeed+=0.2;
          if(prefightBackdropSpeed >= prefightBackdrop.length)
          {
              prefightBackdropSpeed = 0;
          }
          
          // Hover animation for fight mouse.
          if(mouseX >= fightButtonX  && mouseX <= fightButtonX + fightButtonWidth)
          {
              if(mouseY >= fightButtonY+47 && mouseY <= fightButtonY + fightButtonHeight - 41)
              {
                  onFightButton = true;
              }
              else
              {
                  onFightButton = false;
              }
          }
          else
          {
              onFightButton = false;
          }
      }
      else if(gameState == GameState.HEAVEN)
      {
          // All jumping and moving mechanisms.
          sideMovementMechanism();
          int ground = heavenGround;
          jumpMechanism(ground);
          
          // Update Zeus.
          zeusSpeed += 0.2;
          if(zeusSpeed >= zeusIdle.length)
          {
              zeusSpeed = 0;
          }
          
          // If character approach zeus, initiate interaction state,
          if(characterX >= 460)
          {
              gameState = GameState.values()[gameState.ordinal() + 1];
          }
      }
      else if(gameState == GameState.ZEUS_INTERACTION)
      {
          // Update zeus.
          zeusSpeed += 0.2;
          if(zeusSpeed >= zeusIdle.length)
          {
              zeusSpeed = 0;
          }
      }
      else if(gameState == GameState.FIGHT_CUTSCENE)
      {
          // Show off fight cutscene for few seconds.
          if(secondsPassed == 9)
          {
              startFading = true;
          }
      }
      else if(gameState == GameState.BOSS_FIGHT)
      {
          // THIS IS THE BOSS FIGHT LOGIC.
          
          // ALL BASIC MOVEMENT MECHANISMS.
          sideMovementMechanism();
          int ground = heavenGround;
          jumpMechanism(ground);
          
          // Update zeus animation.
          zeusSpeed += 0.2;
          if(zeusSpeed >= zeusRun.length)
          {
              zeusSpeed = 0;
          }
          if(gameOver)
          {
              gameState = GameState.DEATH_ANIMATION;
          }
          
          if(secondsPassed == 20)
          {
              specialAttack = true;
          }
      }
      else if(gameState == GameState.DEATH_ANIMATION)
      {
          // Start timer to fall and reset character Y
          if(!characterDead)
          {
              secondsPassed = 0;
              characterY = 300;
          }
          
          // Update zeus.
          zeusSpeed += 0.2;
          if(zeusSpeed >= zeusIdle.length)
          {
              zeusSpeed = 0;
          }
          
          // Make character fall down and die.
          if(!characterDead)
          {
              c1FallSpeed += 0.1;
          }
          if(c1FallSpeed >= c1Fall.length)
          {
              c1FallSpeed = 7;
              characterDead = true;
          }
          
          // 5 seconds after death, start fading into new state.
          if(secondsPassed == 5)
          {
              startFading = true;
          }
      }
      else if(gameState == GameState.WIN_ANIMATION)
      {
          // Does same as the dying logic but except character dead, its bolt done
          // and differnt directory state (win or lose).
          if(!boltDone)
          {
              secondsPassed = 0;
          }
          zeusSpeed += 0.2;
          if(zeusSpeed >= zeusIdle.length)
          {
              zeusSpeed = 0;
          }
          // Update bolt speed.
          if(!boltDone)
          {
              boltSpeed += 0.2;
          }
          // Stop bolt speed.
          if(boltSpeed >= darkBolt.length)
          {
              boltSpeed = 11;
              boltDone = true;
          }
          
          // Win state after 3 seconds.
          if(secondsPassed == 3)
          {
              startFading = true;
          }
          
      }
      else if(gameState == GameState.WIN_SCREEN) // YOU WIN
      {
          
      }
      else if(gameState == GameState.LOSE_SCREEN) // YOU LOSE
      {
          
      }
  }
  


  // YOU SHOULDN'T NEED TO MODIFY ANYTHING AFTER THIS POINT
  // Feel free to have a look to see what is happening but don't touch the code down here!
  // This is what makes the window and all of the keyboard and mouse stuff work

  // creates the game window and sets everything up to run properly
  public MyProgram() {
    // Initialize the game window
    JFrame frame = new JFrame("Ascension");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    frame.setVisible(true);
    frame.add(this);
    frame.pack();
    // Register the KeyListener, MouseListener, and MouseMotionListener
    frame.addKeyListener(this); 
    this.addMouseListener(this);
    this.addMouseMotionListener(this);

    // call the setup method for parts that need initialized before the game starts
    setup();

    // Initialize game timer to run at a constant FPS
    gameTimer = new Timer(1000/FPS, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            loop();
            repaint();
        }
    });
    gameTimer.start();
  }
  
  public BufferedImage loadImage(String filename){
      BufferedImage img = null;
      try{
          img = ImageIO.read(new File(filename));
      }catch(Exception e){
          e.printStackTrace();
      }
      return img;
  }

  
  // KeyListener methods
  public void keyPressed(KeyEvent e) {
    // when the key is pressed, determine the code and set the boolean array to true for that key
    int keyCode = e.getKeyCode();
    keyPressed[keyCode] = true;
    
  }

  public void keyReleased(KeyEvent e) {
    // when the key is pressed, determine the code and set the boolean array to false for that key
    int keyCode = e.getKeyCode();
    keyPressed[keyCode] = false;
  }

  // mouse events
  public void mousePressed(MouseEvent e){
    // when the mouse button is pressed down, set the boolean to true
    mousePressed[e.getButton()] = true;
    
    // if on homescreen and play button is pressed.
    if(gameState == GameState.HOME)
    {
        if(onPlayButton)
        {
            startFading = true;
        }
    }
    if(gameState == GameState.PREFIGHT)
    {
        // if fight button is pressed.
        if(onFightButton)
        {
            startFading = true;
        }
    }
    // incremental narration.
    if(gameState == GameState.ZEUS_INTERACTION)
    {
        if(charTurn != 10)
        {
            nar++;
            charTurn++;
        }
        if(charTurn == 10)
        {
            if(secondsPassed == 0)
            {
                startTimer = true;
            }
            
        }
    }
    // Play again button.
    if(gameState == GameState.LOSE_SCREEN || gameState == GameState.WIN_SCREEN)
    {
        if(onAgainButton)
        {
            gameState = GameState.RESET;
        }
    }
  }

  public void mouseReleased(MouseEvent e){
    // when the mouse button is let go, set the boolean to false
    mousePressed[e.getButton()] = false;
  }

  public void mouseMoved(MouseEvent e){
    // update the mouse coordinates when the mouse moves
    mouseX = e.getX();
    mouseY = e.getY();
  }

  // currently unused listeners for the keyboard and mouse
  public void keyTyped(KeyEvent e){
    
  }

  public void mouseDragged(MouseEvent e){
    // update the mouse coordinates when the mouse moves
    mouseX = e.getX();
    mouseY = e.getY();
  }

  public void mouseEntered(MouseEvent e){
    
  }

  public void mouseExited(MouseEvent e){
    
  }
  
  public void mouseClicked(MouseEvent e){
    
  }

  public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
          public void run() {
              new MyProgram();
          }
      });
  }
}
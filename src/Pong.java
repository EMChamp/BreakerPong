import java.util.Scanner;

import ucigame.Image;
import ucigame.Sound;
import ucigame.Sprite;
import ucigame.Ucigame;
/* <applet code=HomeApplet width=850 height=600></applet> */
public class Pong extends Ucigame
{
	//game objects
	Sprite redBall, blueBall, yellowBall;
	//player-controlled objects
	Sprite redPaddle, bluePaddle;
	//field objects
	Sprite redQuarterLine, blueQuarterLine, middleLine, bottomEdge;
	Sprite invisibleBarrierRed, invisibleBarrierBlue;
	Sprite[] redBricks;
	Sprite[] blueBricks;
	//win counters
	Sprite redCounter, blueCounter;
	Sprite roundOverBlueMsg, roundOverRedMsg;
	Sound themeMusic;
	
	//variables containing gamefield info
	int scoreboardAdjustment = 100;
	int middleOfGamespace = 315;
	boolean[] redBrickDestroyed = new boolean[16];
	boolean[] blueBrickDestroyed = new boolean[16];
	
	//variables containing game info
	int blueWinTotal = 0;
	int redWinTotal = 0;
	boolean redFirst, blueFirst;
	boolean roundOverRedWins, roundOverBlueWins;
	boolean yellowBallMoving; //control for when the yellow ball first starts moving
	boolean firstRun = true;
	int yellowBallPasses;
	int redBallPasses;
	int blueBallPasses;
	
	public void setup()
	{
		window.size(850, 600);
		window.title("Breaker Pong");
		framerate(30);
		
		//give window time to open, faster processors can start game before window opens
		try
		{
			Thread.sleep(1000);//sleep for 1 second
		}
		catch(InterruptedException ie)
		{
		}
	
		
		
		
		if (firstRun == true) //display start menu on start and loop music
		{
		Image bkg = getImage("images/startScreen.png");
		canvas.background(bkg);
		//play theme music
		themeMusic = getSound("Sandstorm.mp3");
		themeMusic.loop();
		}
		else
		{
		Image bkg = getImage("images/BreakerPongField.png");
		canvas.background(bkg);
		}
		
		//create game objects
		redBall = makeSprite(getImage("images/redBall.png", 255, 255, 255));
		blueBall = makeSprite(getImage("images/blueBall.png", 255, 255, 255));
		yellowBall = makeSprite(getImage("images/yellowBall.png", 255, 255, 255));
		redPaddle = makeSprite(getImage("images/redPaddle.png"));
		bluePaddle = makeSprite(getImage("images/bluePaddle.png"));
		redQuarterLine = makeSprite(getImage("images/redQuarterLine.png"));
		blueQuarterLine = makeSprite(getImage("images/blueQuarterLine.png"));
		middleLine = makeSprite(getImage("images/middleLine.png"));
		bottomEdge = makeSprite(getImage("images/bottomEdge.png"));
		invisibleBarrierRed = makeSprite(getImage("images/invisibleBarrier.png"));
		invisibleBarrierBlue = makeSprite(getImage("images/invisibleBarrier.png"));
		redCounter = makeSprite(getImage("images/redCounter.png"));
		blueCounter = makeSprite(getImage("images/blueCounter.png"));
		roundOverRedMsg = makeSprite(getImage("images/roundOverRedWins.png"));
		roundOverBlueMsg = makeSprite(getImage("images/roundOverBlueWins.png"));
		
		//bricks
		redBricks = new Sprite[16]; //only 15 bricks, need one extra space
		blueBricks = new Sprite[16];
		//create 15 red bricks
		for(int i = 1; i <= 15; i++)
		{
			redBricks[i] = makeSprite(getImage("images/redBrick.png"));
			redBrickDestroyed[i] = false;
		}
		//create 15 blue bricks
		for(int i = 1; i <= 15; i++)
		{
			blueBricks[i] = makeSprite(getImage("images/blueBrick.png"));
			blueBrickDestroyed[i] = false;
		}
	
		//position game objects
		//NOTE: red = LEFT side, blue = RIGHT side
		//red and blue ball
		redBall.position(100,
		              canvas.height()/2 + scoreboardAdjustment);
		blueBall.position(750,
		              canvas.height()/2 + scoreboardAdjustment);
		redBall.motion(-3.5, -3.5);
		blueBall.motion(3.5, -3.5);
		
		//yellow ball is not in play at start
		yellowBall.position(425, 265);
		yellowBall.motion(0, 0, SET);
		
		//red and blue paddle
		redPaddle.position(0, middleOfGamespace - redPaddle.width()); 
		bluePaddle.position(canvas.width(), middleOfGamespace - redPaddle.width());
						
		//red, middle and blue lines
		redQuarterLine.position(canvas.width()/4, 0);
		blueQuarterLine.position(3*canvas.width()/4, 0);
		middleLine.position(canvas.width()/2, 0);
		
		//bottom edge
		bottomEdge.position(0, canvas.height() - scoreboardAdjustment + 30); //30 is adjustment for balancing resize
		
		//invisible barrier
		invisibleBarrierRed.position(35,0);
		invisibleBarrierBlue.position(canvas.width() - 35,0);
		
		//counters
		redCounter.position(50, 570);
		redCounter.font("Arial", BOLD, 14);
		blueCounter.position(650, 570);
		blueCounter.font("Arial", BOLD, 14);
		
		//round over messages
		roundOverRedMsg.position(275,100);
		roundOverBlueMsg.position(275,100);
		
		//bricks
		//red bricks
		for(int i = 1; i <= 5; i++)
		{
			redBricks[i].position(385, 50 + (i-1)*(96));
		}
		
		for(int i = 6; i <= 10; i++)
		{
			redBricks[i].position(285, 50 + (i-6)*(96));
		}
		
		redBricks[11].position(200, 103);
		redBricks[12].position(150, 153);
		redBricks[13].position(200, 223);
		redBricks[14].position(150, 293);
		redBricks[15].position(200, 363);
		
		//blue bricks	
		for(int i = 1; i <= 5; i++)
		{
			blueBricks[i].position(458, 50 + (i-1)*(96));
		}
		
		for(int i = 6; i <= 10; i++)
		{
			blueBricks[i].position(558, 50 + (i-6)*(96));
		}
		
		blueBricks[11].position(643, 103);
		blueBricks[12].position(693, 153);
		blueBricks[13].position(643, 223);
		blueBricks[14].position(693, 293);
		blueBricks[15].position(643, 363);
		
		//game information
		redFirst = false;
		blueFirst = false;
		roundOverRedWins = false;
		roundOverBlueWins = false;
		yellowBallMoving = false;
		yellowBallPasses = 0;
		redBallPasses = 0;
		blueBallPasses = 0;
	}
	
	public void draw()
	{
	
		//press enter in console to leave start screen
		if(firstRun == true)
		{
		canvas.clear();
		firstRun = false;
		String userInput;
		Scanner in = new Scanner(System.in);
		userInput = in.nextLine(); //pause game until keystroke
		try
		{
			Thread.sleep(2000);//sleep for 2 seconds
		}
		catch(InterruptedException ie)
		{
		}
		setup();
		}
		
		//---------------------------------------------------------------------
		// ALL BRICKS DOWN
		// detect if all bricks are down
		// destroy both balls
		// spawn yellow ball at center to decide game
		//---------------------------------------------------------------------
		if (AllBlueBricksDown() && AllRedBricksDown() && !roundOverRedWins && !roundOverBlueWins)
		{
		canvas.clear();	
		
		//counters
		redCounter.draw();
		blueCounter.draw();
		redCounter.putText(redWinTotal,20,20);
		blueCounter.putText(blueWinTotal,20,20);
		
		//animate ball and stop red and blue
		blueBall.motion(0,0, SET);
		redBall.motion(0,0, SET);
		blueBall.position(1000, 1000);
		redBall.position(1000, 1000);
		
		// serve the yellow ball to whoever broke the bricks first
		if(yellowBallMoving == false)
		{
			if(redFirst)
			{
				yellowBall.motion(-3.5, 2.5, SET);
				yellowBallMoving = true;
			}
			else
			{
				yellowBall.motion(3.5, 2.5, SET);
				yellowBallMoving = true;
			}
		}
		
		//speedboost every time ball passes middle line
		yellowBall.checkIfCollidesWith(middleLine);
		if(yellowBall.collided())
		{
			double speedBoost = 1.0 + (.0012*yellowBallPasses);
			yellowBallPasses++;
			yellowBall.motion(speedBoost, speedBoost, MULTIPLY);
		}
		
		yellowBall.move();
		
		//Set collisions
		yellowBall.bounceIfCollidesWith(redPaddle);
		yellowBall.bounceIfCollidesWith(TOPEDGE, bottomEdge);
		yellowBall.bounceIfCollidesWith(bluePaddle);
		
		redPaddle.stopIfCollidesWith(TOPEDGE, bottomEdge, LEFTEDGE, RIGHTEDGE, 
		redQuarterLine);
		bluePaddle.stopIfCollidesWith(TOPEDGE, bottomEdge, LEFTEDGE, RIGHTEDGE, 
		blueQuarterLine);
		
		//victory conditions
		//red victory
		yellowBall.checkIfCollidesWith(RIGHTEDGE);
		if(yellowBall.collided())
		{			
			//stop balls
			if(roundOverRedWins == false)
			{
			redWinTotal++;
			}
			roundOverRedWins = true;
			yellowBall.motion(0,0,SET);
		}
		//blue victory
		yellowBall.checkIfCollidesWith(LEFTEDGE);
		if(yellowBall.collided())
		{			
			//stop balls
			if(roundOverBlueWins == false)
			{
			blueWinTotal++;
			}
			roundOverBlueWins = true;
			yellowBall.motion(0,0,SET);
		}
		//
		
		//Draw paddles and ball
		redPaddle.draw();
		bluePaddle.draw();
		yellowBall.draw();
		}
		//end ALL BRICKS DOWN
		//---------------------------------------------------------------------
		
		//---------------------------------------------------------------------
		// RED BRICKS DOWN
		// detect if all red bricks are down
		// let redPaddle pass REDInvisibleBarrier 
		// let redBall pass middleBarrier
		//---------------------------------------------------------------------
		else if (AllRedBricksDown() && !AllBlueBricksDown() && !roundOverRedWins && !roundOverBlueWins)
		{
		canvas.clear();

		//counters
		redCounter.draw();
		blueCounter.draw();
		redCounter.putText(redWinTotal,20,20);
		blueCounter.putText(blueWinTotal,20,20);
		
		//Draw fieldlines
		redQuarterLine.draw();	
		middleLine.draw();
		
		//speedboost every time red ball passes middle line
		redBall.checkIfCollidesWith(middleLine);
		if(redBall.collided())
		{
			double speedBoost = 1.0 + (.001*redBallPasses);
			redBallPasses++;
			redBall.motion(speedBoost, speedBoost, MULTIPLY);
		}
		
		//animate balls
		redBall.move();
		blueBall.move();	
		
		//Set collisions
		redBall.bounceIfCollidesWith(redPaddle);
		redBall.bounceIfCollidesWith(TOPEDGE, bottomEdge, LEFTEDGE);
		redBall.bounceIfCollidesWith(bluePaddle);
		
		blueBall.bounceIfCollidesWith(redPaddle);
		blueBall.bounceIfCollidesWith(TOPEDGE, bottomEdge, LEFTEDGE, RIGHTEDGE);
		blueBall.bounceIfCollidesWith(bluePaddle);
		blueBall.bounceIfCollidesWith(middleLine);
		
		redPaddle.stopIfCollidesWith(TOPEDGE, bottomEdge, LEFTEDGE, RIGHTEDGE, 
		redQuarterLine);
		bluePaddle.stopIfCollidesWith(TOPEDGE, bottomEdge, LEFTEDGE, RIGHTEDGE, 
		blueQuarterLine, invisibleBarrierBlue);
		
		for(int i = 1; i <= 15; i++)
		{
			blueBall.bounceIfCollidesWith(blueBricks[i]);
		}
		
		//draw only blueBricks
		for(int i = 1; i <= 15; i++)
		{
			blueBricks[i].checkIfCollidesWith(blueBall);
			//do not draw and set destroyed flag if collided
			if (blueBricks[i].collided() && (blueBrickDestroyed[i] == false))
			{
				if(blueBrickDestroyed[i] == false)
				{
					blueBrickDestroyed[i] = true;
				}
				blueBricks[i].hide();
				blueBricks[i].position(1500,1500); //moves out of gamespace
			}
			//otherwise draw
			else
			{
				blueBricks[i].draw();
			}
		}
		//red victory condition
		redBall.checkIfCollidesWith(RIGHTEDGE);
		if(redBall.collided())
		{			
			//stop balls
			if(roundOverRedWins == false)
			{
			redWinTotal++;
			}
			roundOverRedWins = true;
			redBall.motion(0,0,SET);
			blueBall.motion(0,0,SET);
		}
		
		//Draw paddles and balls
		redPaddle.draw();
		bluePaddle.draw();
		redBall.draw();
		blueBall.draw();
		}
		//end RED BRICKS DOWN
		//---------------------------------------------------------------------
		
		//---------------------------------------------------------------------
		// BLUE BRICKS DOWN
		// detect if all blue bricks are down
		// let bluePaddle pass blueInvisibleBarrier 
		// let blueBall pass middleBarrier
		//---------------------------------------------------------------------
		else if (AllBlueBricksDown() && !AllRedBricksDown() && !roundOverRedWins && !roundOverBlueWins)
		{
		canvas.clear();
		
		//counters
		redCounter.draw();
		blueCounter.draw();
		redCounter.putText(redWinTotal,20,20);
		blueCounter.putText(blueWinTotal,20,20);
		
		//Draw fieldlines
		blueQuarterLine.draw();	
		middleLine.draw();
		
		//speedboost every time blue ball passes middle line
		blueBall.checkIfCollidesWith(middleLine);
		if(blueBall.collided())
		{
			double speedBoost = 1.0 + (.002*blueBallPasses);
			blueBallPasses++;
			blueBall.motion(speedBoost, speedBoost, MULTIPLY);
		}
		
		//animate balls
		redBall.move();
		blueBall.move();	
		
		//Set collisions
		redBall.bounceIfCollidesWith(redPaddle);
		redBall.bounceIfCollidesWith(TOPEDGE, bottomEdge, LEFTEDGE, RIGHTEDGE);
		redBall.bounceIfCollidesWith(bluePaddle);
		redBall.bounceIfCollidesWith(middleLine);
		
		blueBall.bounceIfCollidesWith(redPaddle);
		blueBall.bounceIfCollidesWith(TOPEDGE, bottomEdge, RIGHTEDGE);
		blueBall.bounceIfCollidesWith(bluePaddle);
		
		redPaddle.stopIfCollidesWith(TOPEDGE, bottomEdge, LEFTEDGE, RIGHTEDGE, 
		redQuarterLine, invisibleBarrierRed);
		bluePaddle.stopIfCollidesWith(TOPEDGE, bottomEdge, LEFTEDGE, RIGHTEDGE, 
		blueQuarterLine);
		
		for(int i = 1; i <= 15; i++)
		{
			redBall.bounceIfCollidesWith(redBricks[i]);
		}
		
		//draw only red bricks
		for(int i = 1; i <= 15; i++)
		{
			redBricks[i].checkIfCollidesWith(redBall);
			//do not draw and set destroyed flag if collided
			if (redBricks[i].collided() && (redBrickDestroyed[i] == false))
			{
				if(redBrickDestroyed[i] == false)
				{
					redBrickDestroyed[i] = true;
				}
				redBricks[i].hide();
				redBricks[i].position(1500,1500); //moves out of gamespace
			}
			//otherwise draw
			else
			{
				redBricks[i].draw();
			}
		}
		
		//blue victory condition
		blueBall.checkIfCollidesWith(LEFTEDGE,PIXELPERFECT);
		if(blueBall.collided())
		{			
			//stop balls
			if(roundOverBlueWins == false)
			{
			blueWinTotal++;
			}
			roundOverRedWins = true;
			redBall.motion(0,0,SET);
			blueBall.motion(0,0,SET);
		}
		
		//Draw paddles and balls
		redPaddle.draw();
		bluePaddle.draw();
		redBall.draw();
		blueBall.draw();
		}
		//end BLUE BRICKS DOWN
		//---------------------------------------------------------------------
		
		//---------------------------------------------------------------------
		// NORMAL CASE
		// This is the phase when no player has all their bricks down
		// paddles cannot pass their invisible lines
		// balls cannot pass middle line
		//---------------------------------------------------------------------
		else if(!roundOverRedWins && !roundOverBlueWins)
		{
		canvas.clear();
	
		//counters
		redCounter.draw();
		blueCounter.draw();
		redCounter.putText(redWinTotal,20,20);
		blueCounter.putText(blueWinTotal,20,20);
		
		//Draw fieldlines
		middleLine.draw();
		
		//animate balls
		redBall.move();
		blueBall.move();	
		
		//Set collisions
		redBall.bounceIfCollidesWith(redPaddle);
		redBall.bounceIfCollidesWith(TOPEDGE, bottomEdge, LEFTEDGE, RIGHTEDGE);
		redBall.bounceIfCollidesWith(bluePaddle);
		redBall.bounceIfCollidesWith(middleLine);
		
		blueBall.bounceIfCollidesWith(redPaddle);
		blueBall.bounceIfCollidesWith(TOPEDGE, bottomEdge, LEFTEDGE, RIGHTEDGE);
		blueBall.bounceIfCollidesWith(bluePaddle);
		blueBall.bounceIfCollidesWith(middleLine);
		
		redPaddle.stopIfCollidesWith(TOPEDGE, bottomEdge, LEFTEDGE, RIGHTEDGE, 
		redQuarterLine, invisibleBarrierRed);
		bluePaddle.stopIfCollidesWith(TOPEDGE, bottomEdge, LEFTEDGE, RIGHTEDGE, 
		blueQuarterLine, invisibleBarrierBlue);
		
		for(int i = 1; i <= 15; i++)
		{
			redBall.bounceIfCollidesWith(redBricks[i]);
			blueBall.bounceIfCollidesWith(blueBricks[i]);
		}
		
		//draw bricks
		//red bricks
		for(int i = 1; i <= 15; i++)
		{
			redBricks[i].checkIfCollidesWith(redBall);
			//do not draw and set destroyed flag if collided
			if (redBricks[i].collided() && (redBrickDestroyed[i] == false))
			{
				if(redBrickDestroyed[i] == false)
				{
					redBrickDestroyed[i] = true;
				}
				redBricks[i].hide();
				redBricks[i].position(1500,1500); //moves out of gamespace
			}
			//otherwise draw
			else
			{
				redBricks[i].draw();
			}
		}
		
		//blue bricks
		for(int i = 1; i <= 15; i++)
		{
			blueBricks[i].checkIfCollidesWith(blueBall);
			//do not draw and set destroyed flag if collided
			if (blueBricks[i].collided() && (blueBrickDestroyed[i] == false))
			{
				if(blueBrickDestroyed[i] == false)
				{
					blueBrickDestroyed[i] = true;
				}
				blueBricks[i].hide();
				blueBricks[i].position(1500,1500); //moves out of gamespace
			}
			//otherwise draw
			else
			{
				blueBricks[i].draw();
			}
		}
		
		//Draw paddles and balls
		redPaddle.draw();
		bluePaddle.draw();
		redBall.draw();
		blueBall.draw();
		//END NORMAL CASE
		//---------------------------------------------------------------------
		}
		
		//---------------------------------------------------------------------
		// ROUND OVER
		// Just draw the field (balls,paddles,bricks)
		//---------------------------------------------------------------------
		else if(roundOverRedWins || roundOverBlueWins)
		{
		canvas.clear();
		
		//messages for winner
		if(roundOverRedWins)
		roundOverRedMsg.draw();
		else if(roundOverBlueWins)
		roundOverBlueMsg.draw();
		
		//counters
		redCounter.draw();
		blueCounter.draw();
		redCounter.putText(redWinTotal,20,20);
		blueCounter.putText(blueWinTotal,20,20);
		
		//draw bricks
		//red bricks
		for(int i = 1; i <= 15; i++)
		{
			redBricks[i].checkIfCollidesWith(redBall);
			//do not draw and set destroyed flag if collided
			if (redBricks[i].collided() && (redBrickDestroyed[i] == false))
			{
				if(redBrickDestroyed[i] == false)
				{
					redBrickDestroyed[i] = true;
				}
				redBricks[i].hide();
				redBricks[i].position(1500,1500); //moves out of gamespace
			}
			//otherwise draw
			else
			{
				redBricks[i].draw();
			}
		}
		
		//blue bricks
		for(int i = 1; i <= 15; i++)
		{
			blueBricks[i].checkIfCollidesWith(blueBall);
			//do not draw and set destroyed flag if collided
			if (blueBricks[i].collided() && (blueBrickDestroyed[i] == false))
			{
				if(blueBrickDestroyed[i] == false)
				{
					blueBrickDestroyed[i] = true;
				}
				blueBricks[i].hide();
				blueBricks[i].position(1500,1500); //moves out of gamespace
			}
			//otherwise draw
			else
			{
				blueBricks[i].draw();
			}
		}
		
		//Draw paddles and balls
		redPaddle.draw();
		bluePaddle.draw();
		yellowBall.draw();
		redBall.draw();
		blueBall.draw();
		}
		
	}

	public void onKeyPress()
	{
		// Arrow keys move the redPaddle	
		if (keyboard.isDown(keyboard.W))	
			redPaddle.nextY(redPaddle.y() - 14);	
		if (keyboard.isDown(keyboard.S))	
			redPaddle.nextY(redPaddle.y() + 14);
		if (keyboard.isDown(keyboard.A))	
			redPaddle.nextX(redPaddle.x() - 14);	
		if (keyboard.isDown(keyboard.D))	
			redPaddle.nextX(redPaddle.x() + 14);
			
		// Arrow keys move the bluePaddle
		if (keyboard.isDown(keyboard.UP))
			bluePaddle.nextY(bluePaddle.y() - 14);
		if (keyboard.isDown(keyboard.DOWN))
			bluePaddle.nextY(bluePaddle.y() + 14);
		if (keyboard.isDown(keyboard.LEFT))
			bluePaddle.nextX(bluePaddle.x() - 14);
		if (keyboard.isDown(keyboard.RIGHT))
			bluePaddle.nextX(bluePaddle.x() + 14);
			
		//'Y' key to reset gamespace and advance to next round
		if (keyboard.isDown(keyboard.Y))
		{	
				setup();
		}
	}
	
	public boolean AllRedBricksDown()
	{
	for(int i = 1; i <= 15; i++)
	{
		if(redBrickDestroyed[i] == false)
			return false; //return false if a single red brick is up
	}
	
	if(blueFirst == false)
	{
		redFirst = true;
	}
	
	return true; //return true if all red bricks are down
	}
	
	public boolean AllBlueBricksDown()
	{
	for(int i = 1; i <= 15; i++)
	{
		if(blueBrickDestroyed[i] == false)
			return false; //return false if a single blue brick is up
	}
	
	if(redFirst == true)
	{
		blueFirst = true;
	}
	
	return true; //return true if all blue bricks are down
	}
	
}


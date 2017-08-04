package com.demo.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;

class Flappy_Birds extends ApplicationAdapter {

    Circle birdcircle ;
	Rectangle pipeshapeup ;
	Rectangle pipeshapedown ;
	Preferences preferences ;

	BitmapFont bitmapFont ;
	int score = 0;
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture pipeup ;
	Texture pipedown ;
	Texture gameover ;
	Texture logo ;

	int flip = 0 ;
	int gamestate = 0 ;
	int decrease = 0;
	int increment = 0;
	int randomno =0 ;
	int travel =0;
	int storedscore =0 ;

	Stage stage;
	Texture start;
	TextureRegion startRegion;
	TextureRegionDrawable startDrawable;
	ImageButton startbutton;

	Texture retry;
	TextureRegion retryRegion;
	TextureRegionDrawable retryDrawable;
	ImageButton retrybutton ;

	public int random()
	{
		Random rand = new Random();
		return rand.nextInt(900) - 200 ;
	}

	@Override
	public void create () {
		preferences = Gdx.app.getPreferences("com.demo.game");
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		pipeup = new Texture("toptube.png");
		pipedown = new Texture("bottomtube.png");
		gameover = new Texture("gameover.png");
		logo = new Texture("flappyBird.png");

        birdcircle = new Circle() ;
		pipeshapeup = new Rectangle() ;
		pipeshapedown = new Rectangle() ;
		bitmapFont = new BitmapFont();
		bitmapFont.setColor(Color.WHITE);
		bitmapFont.getData().setScale(10);


		start = new Texture("button.png");
		startRegion = new TextureRegion(start);
		startDrawable = new TextureRegionDrawable(startRegion);
		startbutton = new ImageButton(startDrawable);

		retry = new Texture("retry.png");
		retryRegion = new TextureRegion(retry);
		retryDrawable = new TextureRegionDrawable(retryRegion);
		retrybutton = new ImageButton(retryDrawable);

		stage = new Stage(new ScreenViewport()); //Set up a stage for the ui
		stage.addActor(startbutton); //Add the button to the stage to perform rendering and take input.
		stage.addActor(retrybutton);
		Gdx.input.setInputProcessor(stage); //Start taking input from the ui

	}

	@Override
	public void render () {

		storedscore = preferences.getInteger("bestscore",0);

		if(storedscore < score)
		{
			preferences.putInteger("bestscore" ,score);
			preferences.flush();
		}

		if(travel ==0)
			randomno = random() ;

		if(flip == 0)
			flip = 1;
		else
			flip = 0;

		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.draw(birds[flip],Gdx.graphics.getWidth()/2 - birds[flip].getWidth()/2,Gdx.graphics.getHeight()/2 - birds[flip].getHeight()/2-decrease);
		batch.draw(pipeup,Gdx.graphics.getWidth() - travel,Gdx.graphics.getHeight()/2 + randomno );
		batch.draw(pipedown,Gdx.graphics.getWidth() - travel,Gdx.graphics.getHeight()/2-pipeup.getHeight()+ randomno - 500);

		birdcircle.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2 -decrease ,birds[0].getWidth()/2);
		pipeshapeup.set(Gdx.graphics.getWidth() - travel,Gdx.graphics.getHeight()/2 + randomno ,pipeup.getWidth(),pipeup.getHeight());
		pipeshapedown.set(Gdx.graphics.getWidth() - travel, Gdx.graphics.getHeight()/2-pipedown.getHeight()+ randomno -500,pipedown.getWidth(),pipedown.getHeight());

		if(Intersector.overlaps(birdcircle,pipeshapeup) || Intersector.overlaps(birdcircle,pipeshapedown)){

			gamestate = 2;

		}

		if(gamestate == 1 ){

			increment = increment + 1;

			bitmapFont.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2 + (Gdx.graphics.getHeight()/2)/2);

			if(travel == (1290/2))
			{
				score++;
			}

			if(travel > Gdx.graphics.getWidth() + pipedown.getWidth())
				travel =0 ;
			else
				travel = travel + 5 ;

			if(Gdx.input.justTouched()){
				increment = 0 ;
				if(decrease > -Gdx.graphics.getHeight()/2)
				decrease = decrease - 150;
			}
			else{
				if(decrease < Gdx.graphics.getHeight()/2)
				decrease = decrease + increment ;
			}
		}
		else if ( gamestate == 0 ){

			startbutton.setPosition(Gdx.graphics.getWidth()/2 - start.getWidth()/2,Gdx.graphics.getHeight()/2 - start.getHeight()/2 - 400);
			startbutton.draw(batch,1f);
			batch.draw(logo,Gdx.graphics.getWidth()/2 - logo.getWidth()/2 ,Gdx.graphics.getHeight()/2 + (Gdx.graphics.getHeight()/2)/2 -logo.getHeight()/2 );
			startbutton.addListener(new EventListener()
			{
				@Override
				public boolean handle(Event event)
				{
					gamestate = 1 ;
					return true ;
				}
			});

		}
		else if ( gamestate == 2 )
		{
			bitmapFont.getData().setScale(6);
			batch.draw(gameover,Gdx.graphics.getWidth()/2 - gameover.getWidth()/2 ,Gdx.graphics.getHeight()/2 - gameover.getHeight()/2 );
			bitmapFont.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()/2 + 230 ,Gdx.graphics.getHeight()/2 - 30);
			bitmapFont.draw(batch,String.valueOf(preferences.getInteger("bestscore")),Gdx.graphics.getWidth()/2 + 230 ,Gdx.graphics.getHeight()/2 - 200);

			retrybutton.setPosition(Gdx.graphics.getWidth()/2 - retry.getWidth()/2,Gdx.graphics.getHeight()/2 - retry.getHeight()/2 - 420);
			retrybutton.draw(batch,1f);
			retrybutton.addListener(new EventListener()
			{
				@Override
				public boolean handle(Event event)
				{
					gamestate = 1 ;
					decrease = 0;
					increment = 0;
					randomno =0 ;
					travel =0;
					score =0 ;

					return  true ;
				}
			});

		}

		batch.end();

	}

}

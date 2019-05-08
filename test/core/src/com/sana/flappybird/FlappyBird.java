package com.sana.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import sun.security.jgss.GSSCaller;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture birds[];
	int flapState=0;
	int birdY=0;
	int velocity=0;
	int gameState=0;
	float gravity=2;
	Texture bottom;
	Texture top;
	float gap=400;
	int maxTubeOffset;
	Random random;
	int tubeVelocity=7;
	int numberOfTubes=4;
	float[] tubeX= new float[numberOfTubes];
    float[] tubeOffSet= new float[numberOfTubes];
	int distanceBetweenTubes;
	//ShapeRenderer shapeRenderer;
	Circle birdCircle;
	Rectangle[] bottomRect;
	Rectangle[] topRect;
	int score=0;
	int scoringTube=0;
	BitmapFont font;
	Texture gameOver;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
	//	shapeRenderer= new ShapeRenderer();
        font= new BitmapFont();
        gameOver = new Texture("gameover.jpg");
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
		birdCircle = new Circle();
		bottomRect = new Rectangle[numberOfTubes];
		topRect = new Rectangle[numberOfTubes];
		birds= new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1]= new Texture("bird2.png");
		birdY=Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
	    bottom = new Texture("bottomtube.png");
	    top= new Texture("toptube.png");
	    maxTubeOffset= (int) (Gdx.graphics.getHeight()/2-gap/2-100);
	    random= new Random();
	    distanceBetweenTubes=Gdx.graphics.getWidth()* 3/4;
        startGame();
	}

	public void startGame()
    {
        birdY=Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
        for(int i=0;i<numberOfTubes;i++)
        {
            tubeOffSet[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-600);
            tubeX[i]=Gdx.graphics.getWidth() / 2 - bottom.getWidth() / 2 + Gdx.graphics.getWidth()+ i*distanceBetweenTubes;
            bottomRect[i]= new Rectangle();
            topRect[i]= new Rectangle();
        }
    }

	@Override
	public void render () {

        batch.begin();
        batch.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if(gameState==1)
        {
            if(tubeX[scoringTube]<Gdx.graphics.getWidth()/2)
            {
                score++;
                Gdx.app.log("Score", String.valueOf(score));
                if(scoringTube<numberOfTubes-1)
                {
                    scoringTube++;
                }
                else
                {
                    scoringTube=0;
                }
            }
            if (Gdx.input.justTouched()) {
                velocity = -20;

            }
            for(int i=0;i<numberOfTubes;i++) {
                if(tubeX[i]<-top.getWidth())
                {
                    tubeX[i]+= numberOfTubes*distanceBetweenTubes;
                    tubeOffSet[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-1200);

                }else {
                    tubeX[i] = tubeX[i] - tubeVelocity;
                }

                //if(Gdx.graphics.getHeight()/2-gap/2-bottom.getHeight()+tubeOffSet<=0 && Gdx.graphics.getHeight()/2+gap/2+tubeOffSet>= Gdx.graphics.getHeight()-top.getHeight() ) {
                batch.draw(bottom, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottom.getHeight() + tubeOffSet[i],bottom.getWidth(), Gdx.graphics.getHeight()/2);
                batch.draw(top, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i], top.getWidth(), Gdx.graphics.getHeight()-Gdx.graphics.getHeight() / 2 - gap / 2 - tubeOffSet[i]);
                //}
            }
            if(birdY>0){
            velocity= (int) (velocity+gravity);
            birdY-=velocity;
            }
            else
            {
                gameState=2;
                batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2, Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
            }
        }
        else if(gameState==2) {
            batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
            if (Gdx.input.justTouched()) {
                gameState = 1;
                startGame();
                scoringTube = 0;
                score = 0;
                velocity = 0;
            }
        }
        else {
            if(Gdx.input.justTouched()){
                Gdx.app.log("Touched", "Yep");
                gameState=1;
            }
        }

	    if(flapState==0)
        {
            flapState=1;
        }
        else {
            flapState=0;
        }

		batch.draw(birds[flapState], Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2, birdY);

        font.draw(batch, String.valueOf(score),100, 200);



		birdCircle.set(Gdx.graphics.getWidth()/2, birdY+birds[flapState].getHeight()/2, birds[flapState].getHeight()/2);
        for(int i=0;i<numberOfTubes;i++)
        {
            bottomRect[i].set(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottom.getHeight() + tubeOffSet[i],bottom.getWidth(),Gdx.graphics.getHeight() / 2);
            topRect[i].set(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i],top.getWidth(),Gdx.graphics.getHeight()-Gdx.graphics.getHeight() / 2 - gap / 2 - tubeOffSet[i]);
        }
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLUE);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
		//if(gameState!=0){
        int i=0;
		for(i=0;i<numberOfTubes;i++) {
            //shapeRenderer.rect(topRect[i].x, topRect[i].y, topRect[i].width, topRect[i].height);
            //shapeRenderer.rect(bottomRect[i].x, bottomRect[i].y, bottomRect[i].width, bottomRect[i].height);
            if (Intersector.overlaps(birdCircle, topRect[i]) || Intersector.overlaps(birdCircle, bottomRect[i])) {
                //Gdx.app.log("Collision", "Yes");
                gameState=2;
                batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2, Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
            }
        }
        batch.end();
        //}
		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		/*batch.dispose();
		img.dispose();*/
	}
}

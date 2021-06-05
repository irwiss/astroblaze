package com.astroblaze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen extends ScreenAdapter {
    private final AstroblazeGame game;
    private final Stage stage;
    private ParallaxBackground parallax;
    private FadePainter fadePainter;

    public GameScreen(AstroblazeGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 0f, true);

        this.stage.act(delta);
        this.game.getScene().act(delta);
        this.stage.draw();
        this.game.getScene().render(game.getBatch());
        if (this.stage.getRoot().getColor().a != 1f) {
            fadePainter.draw(stage.getBatch(), this.stage.getRoot().getColor().a);
        }
    }

    @Override
    public void resize(int width, int height) {
        this.game.getScene().resize(width, height);
    }

    @Override
    public void show() {
        fadePainter = new FadePainter(this.game.getScene().getCamera());
        parallax = new ParallaxBackground(8f);

        this.stage.addActor(parallax);
        this.stage.addActor(new DebugTextDrawer());
        this.stage.addActor(new EnemySpawner(game.getScene(), 2f));
        this.stage.addAction(Actions.sequence(Actions.fadeOut(0f), Actions.fadeIn(1f)));
    }

    public void startGame() {
        final float durations = 0.5f;
        this.stage.act(10f);
        this.stage.addAction(Actions.sequence(
                Actions.fadeOut(durations),
                Actions.delay(durations),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.getScene().reset();
                    }
                }),
                Actions.fadeIn(1f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.getScene().respawnPlayer();
                    }
                })));
    }

    public void stopGame() {
        final float durations = 0.5f;
        this.stage.act(10f);
        this.stage.addAction(Actions.sequence(
                Actions.fadeOut(durations),
                Actions.delay(durations),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.getScene().reset();
                    }
                }),
                Actions.fadeIn(1f),
                Actions.delay(durations),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.resumeGame();
                    }
                })));
    }
}

package com.astroblaze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class DebugTextDrawer extends Actor {
    private final BitmapFont font = new BitmapFont();
    private final float heightOffset;
    private float timeAccumulator = 0f;
    private static String report = "0 fps   ";
    private static String extraReport = "";
    private static float width;
    private static DebugTextDrawer instance;
    private final GlyphLayout layout = new GlyphLayout();

    public DebugTextDrawer() {
        if (instance == null) {
            instance = this;
        }

        heightOffset = 60f * Gdx.graphics.getDensity();
        font.getData().scale(1.5f);
        setExtraReport("");
    }

    public static void setExtraReport(String extra) {
        if (instance == null) {
            return;
        }
        instance.setExtraReportInternal(extra);
    }

    private void setExtraReportInternal(String extra) {
        layout.setText(font, report + " " + extra);
        width = layout.width;// contains the width of the current set text
        // no need for height because text flows top point downwards
        extraReport = extra;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        timeAccumulator += delta;
        float refreshTime = 0.25f;
        if (timeAccumulator >= refreshTime) {
            timeAccumulator = 0f;
            report = Gdx.graphics.getFramesPerSecond() + " fps";
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font.draw(batch, report + " " + extraReport,
                Gdx.graphics.getWidth() - width,
                Gdx.graphics.getHeight() - heightOffset);
    }

    @Override
    public boolean remove() {
        font.dispose();
        return super.remove();
    }
}

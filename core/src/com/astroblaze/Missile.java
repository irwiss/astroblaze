package com.astroblaze;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class Missile extends Renderable {
    private final float moveSpeed = 80f;
    private final float unpoweredDir;
    private final float unpoweredSpeed = 150f;
    private float maxUnpoweredTime = 0.25f;
    private float unpoweredTime = 0.5f;
    private final Vector3 moveVector = new Vector3();
    private final static Random rng = new Random();
    protected ParticleEffect effect;
    private float damage = 35f;

    public Missile(Scene3D scene, Model model) {
        super(scene, model);
        unpoweredDir = (rng.nextFloat() - 0.5f) * unpoweredSpeed;
    }

    public void reset() {
        this.unpoweredTime = maxUnpoweredTime;
        this.setScale(0.75f);
    }

    public void setTargetVector(Vector3 moveVector) {
        this.moveVector.set(moveVector);
    }

    public Vector3 getVelocity() {
        return this.moveVector.cpy().nor().scl(moveSpeed);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (unpoweredTime > 0f) {
            unpoweredTime = Math.max(0f, unpoweredTime - delta);
            final float fakeDeceleration = unpoweredTime / maxUnpoweredTime;
            getPosition().add(
                    unpoweredDir * fakeDeceleration * delta,
                    0f, //-3f * delta,
                    0f);
            addRotation(new Quaternion(Vector3.Z, delta * 720f * fakeDeceleration));
            applyTRS();
        } else if (unpoweredTime == 0f) {
            effect.start();
            unpoweredTime = -1f;
        } else {
            Vector3 currentPos = getPosition();
            Vector3 diff = moveVector.cpy().sub(currentPos);
            float travelDist = moveSpeed * delta;

            if (diff.len() > travelDist) {
                currentPos.mulAdd(diff.nor(), travelDist);
            } else {
                currentPos = moveVector.cpy();
            }

            setPosition(currentPos);
            addRotation(new Quaternion(Vector3.Z, delta * 360f));
            applyTRS();
            effect.setTransform(getTransform().cpy());
        }
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }
}

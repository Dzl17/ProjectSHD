package com.a02.entity;

import com.a02.component.HealthBar;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    private float x;
    private float y;
    private int width;
    private int height;
    public HealthBar hpBar;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Entity() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", hpBar=" + hpBar +
                '}';
    }

    protected boolean overlaps(Vector2 vec, int height, int width) {
        return x < vec.x + width && x + width > vec.x && y < vec.y + height && y + height > vec.y;
    }

    public boolean overlaps(Entity entity) {
        return x < entity.x + entity.width && x + width > entity.x && y < entity.y + entity.height && y + height > entity.y;
    }

    public boolean overlapsPoint(float x, float y) {
        return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
    }

    public boolean overlapsPoint(float x, float y, int offset) {
        return this.x - offset <= x && this.x + offset >= x && this.y - offset <= y && this.y + offset >= y;
    }
}

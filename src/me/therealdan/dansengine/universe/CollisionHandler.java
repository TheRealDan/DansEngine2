package me.therealdan.dansengine.universe;

import me.therealdan.dansengine.main.Error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CollisionHandler {

    private static CollisionHandler collisionHandler;

    private HashMap<Entity, HashSet<Entity>> overlap = new HashMap<>();
    private HashSet<Entity> entities = new HashSet<>();

    private CollisionHandler() {
        Thread thread = new Thread("CollisionHandler") {
            @Override
            public void run() {
                while (true) {
                    try {
                        for (Entity entity : getEntities()) {
                            for (Entity entity1 : getEntities()) {
                                if (entity != entity1) {
                                    boolean overlapping = entity.getHitbox().contains(entity1.getHitbox());
                                    setOverlap(entity, entity1, overlapping);
                                    if (overlapping && entity.isSolid() && entity1.isSolid()) {
                                        separate(entity, entity1);
                                        separate(entity1, entity);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        new Error(e, "CollisionHandler Thread");
                    }
                }
            }
        };
        thread.start();
    }

    private void separate(Entity entity, Entity entity1) {
        if (entity.isImmovable()) return;

        Location location = entity.getLocation();



        entity.teleport(location);
    }

    private void setOverlap(Entity entity, Entity entity1, boolean overlapping) {
        HashSet<Entity> list = this.overlap.getOrDefault(entity, new HashSet<>());
        if (overlapping) {
            list.add(entity1);
        } else {
            list.remove(entity1);
        }
        this.overlap.put(entity, list);
    }

    public void register(Entity entity) {
        entities.add(entity);
    }

    public void unregister(Entity entity) {
        entities.remove(entity);

        overlap.remove(entity);
        for (HashSet<Entity> entities : overlap.values())
            entities.remove(entity); // TODO - Test whether or not the set needs to be set back to the map to take effect
    }

    private List<Entity> getEntities() {
        return new ArrayList<>(entities);
    }

    public static boolean areOverlapping(Entity entity, Entity entity1) {
        return getInstance().overlap.getOrDefault(entity, new HashSet<>()).contains(entity1);
    }

    public static CollisionHandler getInstance() {
        if (collisionHandler == null) collisionHandler = new CollisionHandler();
        return collisionHandler;
    }
}
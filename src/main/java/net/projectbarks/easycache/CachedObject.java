package net.projectbarks.easycache;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Cached object is utilized in the {@link EasyCache}
 * to hold and serialize java objects. Gson is used to serialize the java object
 * to be de-serialize later.
 *
 * Note you should not need to use this class as it is really only used as a an
 * alternative to a hashmap and sorting by value. Furthermore, the class has the
 * compare function enabled to sort cached objects by importance.
 *
 * Created by brandon on 10/4/14.
 */
public class CachedObject implements Comparable<CachedObject> {

    /**
     * Get the lifetime of the cached object and how long it will live.
     *
     * @return the time the object will be deleted.
     */
    @Getter private final Long lifeTime;
    /**
     * The serialized value stored in the cached object that can be
     * converted back to its original form.
     *
     * @return the raw json form of the cached object.
     */
    @Getter private final Object value;
    /**
     * Get the deletion time for an object if it has not been modified
     * or has not been used under a certain amount of time.
     *
     * @return the time the object will be deleted.
     */
    @Getter private long updateTime;
    @Getter(AccessLevel.PROTECTED) protected Long updateTimeExact;

    /**
     * Cached object stores a wide range of data
     * that is utilized by the {@link EasyCache} class
     * for processes such as memory management, cache
     * deletion and so forth.
     *
     * @param value serialized value of the object
     * @param lifeTime time the object will be deleted
     */
    public CachedObject(Object value, Long lifeTime, Long updateTime) {
        this.lifeTime = lifeTime;
        this.updateTime = updateTime;
        this.value = value;
        update();
    }

    @Override
    public int compareTo(CachedObject o) {
        return getLower().compareTo(o.getLower());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CachedObject that = (CachedObject) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    /**
     * Tells that the object has been updated preventing deletion
     * in the EasyCache class until the next deadline is hit.
     */
    protected void update() {
        updateTimeExact = updateTime + System.currentTimeMillis();
    }

    /**
     * Gets the lower time of both the updateTime and the the lifetime
     * whichever value is closer to real time is chosen.
     *
     * @return lower time
     */
    protected Long getLower() {
        return (lifeTime > updateTimeExact ? updateTimeExact : lifeTime);
    }
}

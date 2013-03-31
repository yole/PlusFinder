package ru.yole.plusfinder.model;

/**
 * @author yole
 */
public abstract class BaseEntity {
    private String myName;
    private long myId = -1;

    public String getName() {
        return myName;
    }

    public void setName(String name) {
        myName = name;
    }

    public long getId() {
        return myId;
    }

    public void setId(long id) {
        myId = id;
    }

    @Override
    public String toString() {
        return myName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        if (myId != that.myId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (myId ^ (myId >>> 32));
    }
}

package com.loopperfect.buckaroo;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.nio.file.Path;

public final class TouchFileEvent implements Event {

    public final Path path;

    private TouchFileEvent(final Path path) {
        Preconditions.checkNotNull(path);
        this.path = path;
    }

    // TODO: equals, hashCode


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("path", path)
            .toString();
    }

    public static TouchFileEvent of(final Path path) {
        return new TouchFileEvent(path);
    }
}

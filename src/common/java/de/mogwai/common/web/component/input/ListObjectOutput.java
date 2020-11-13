package de.mogwai.common.web.component.input;

import java.io.IOException;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class ListObjectOutput implements ObjectOutput {

    private final List<Object> objects;

    public ListObjectOutput() {
        objects = new ArrayList<>();
    }

    @Override
    public void writeObject(final Object obj) throws IOException {
        objects.add(obj);
    }

    @Override
    public void write(final int b) throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void write(final byte[] b) throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void writeBoolean(final boolean v) throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void writeByte(final int v) throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void writeShort(final int v) throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void writeChar(final int v) throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void writeInt(final int v) throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void writeLong(final long v) throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void writeFloat(final float v) throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void writeDouble(final double v) throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void writeBytes(final String s) throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void writeChars(final String s) throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void writeUTF(final String s) throws IOException {
        objects.add(s);
    }

    public List<Object> getObjects() {
        return objects;
    }
}

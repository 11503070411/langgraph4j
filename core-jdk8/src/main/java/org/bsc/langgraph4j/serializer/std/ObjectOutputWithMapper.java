package org.bsc.langgraph4j.serializer.std;

import lombok.NonNull;
import org.bsc.langgraph4j.serializer.Serializer;

import java.io.IOException;
import java.io.ObjectOutput;
import java.util.Optional;

class ObjectOutputWithMapper implements ObjectOutput {

    private final ObjectOutput out;
    private final SerializerMapper mapper;

    public ObjectOutputWithMapper(@NonNull ObjectOutput out, @NonNull SerializerMapper mapper) {
        this.out = out;
        this.mapper = mapper;
    }

    @Override
    public void writeObject(Object obj) throws IOException {
        Optional<Serializer<Object>> serializer = (obj != null) ?
                mapper.getSerializer(obj.getClass()) :
                Optional.empty();
        // check if written by serializer
        if (serializer.isPresent()) {
            out.writeObject(obj.getClass());
            serializer.get().write(obj, out);
        }
        else {
            out.writeObject(obj);
        }
        out.flush();
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        out.writeBoolean(v);
    }

    @Override
    public void writeByte(int v) throws IOException {
        out.writeByte(v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        out.writeShort(v);
    }

    @Override
    public void writeChar(int v) throws IOException {
        out.writeChar(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        out.writeInt(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        out.writeLong(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        out.writeFloat(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        out.writeDouble(v);
    }

    @Override
    public void writeBytes(String s) throws IOException {
        out.writeBytes(s);
    }

    @Override
    public void writeChars(String s) throws IOException {
        out.writeChars(s);
    }

    @Override
    public void writeUTF(String s) throws IOException {
        out.writeUTF(s);
    }
}

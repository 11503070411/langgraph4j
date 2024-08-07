package org.bsc.langgraph4j.checkpoint;

import java.io.Externalizable;
import java.util.Collection;
import java.util.Optional;

public interface BaseCheckpointSaver {


    Collection<Checkpoint> list();
    Optional<Checkpoint> getLast();
    void put( Checkpoint checkpoint ) throws Exception;
}

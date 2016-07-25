package org.neo4j.ogm.session.delegates;

import org.neo4j.ogm.session.Capability;
import org.neo4j.ogm.session.Neo4jSession;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManageDelegate extends SaveDelegate implements Capability.Manage, Capability.Flush {

    private List<Object> defered = new ArrayList<>();

    public ManageDelegate(Neo4jSession neo4jSession) {
        super(neo4jSession);
    }

    @Override
    public <T> void manage(T object) {
        defered.add(object);
    }

    @Override
    public void flush() {
        super.save(defered);
        defered = new ArrayList<>();
    }
}

package org.neo4j.ogm.integration.social;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.ogm.domain.social.Person;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.testutil.Neo4jIntegrationTestRule;

import java.io.IOException;

import static org.neo4j.ogm.testutil.GraphTestUtils.assertSameGraph;

public class SocialFollowsRelationshipsIntegrationTest {

    @ClassRule
    public static Neo4jIntegrationTestRule neo4jRule = new Neo4jIntegrationTestRule();

    private Session session;

    @Before
    public void init() throws IOException {
        SessionFactory sessionFactory = new SessionFactory("org.neo4j.ogm.domain.social");
        session = sessionFactory.openSession(neo4jRule.url());
    }

    @After
    public void clearDatabase() {
        neo4jRule.clearDatabase();
    }

    private static GraphDatabaseService getDatabase() {
        return neo4jRule.getGraphDatabaseService();
    }

    @Test
    public void testFollowsGraphShouldBePersisted() {
        Person mark = createPerson("Mark");
        Person jasper = createPerson("Jasper");
        Person luanne = createPerson("Luanne");
        Person chris = createPerson("Chris");

        mark.addPersonToFollow(jasper);
        mark.addPersonToFollow(luanne);
        luanne.addPersonToFollow(chris);
        chris.addPersonToFollow(jasper);
        session.save(mark);
        session.save(luanne);
        session.save(jasper);
        session.save(chris);

        assertSameGraph(getDatabase(), "CREATE (a:Person {name:'Luanne'})-[:FOLLOWS]->(b:Person {name:'Chris'})-[:FOLLOWS]->(c:Person {name:'Jasper'}), (m:Person {name:'Mark'})-[:FOLLOWS]->(c), (m)-[:FOLLOWS]->(a)");
    }

    @Test
    public void testFollowsWithExplicitBiderectionalReference() {
        Person mark = createPerson("Mark");
        Person jasper = createPerson("Jasper");
        Person luanne = createPerson("Luanne");
        Person chris = createPerson("Chris");

        mark.addPersonFollowBiDirectional(jasper);
        mark.addPersonFollowBiDirectional(luanne);
        luanne.addPersonFollowBiDirectional(chris);
        chris.addPersonFollowBiDirectional(jasper);
        session.save(mark);
        session.save(luanne);
        session.save(jasper);
        session.save(chris);

        assertSameGraph(getDatabase(), "CREATE (a:Person {name:'Luanne'})-[:FOLLOWS]->(b:Person {name:'Chris'})-[:FOLLOWS]->(c:Person {name:'Jasper'}), (m:Person {name:'Mark'})-[:FOLLOWS]->(c), (m)-[:FOLLOWS]->(a)");
    }

    private Person createPerson(String name) {
        Person person = new Person();
        person.setName(name);

        return person;
    }
}

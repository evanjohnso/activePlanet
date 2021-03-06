package dao;

import dataModels.Adventure;
import dataModels.Destination;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oDestinationDaoTest {
    private Sql2oDestinationDao destinationDao;
    private Connection con;

    @Before
    public void setUp() throws Exception {
        final String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        destinationDao = new Sql2oDestinationDao(sql2o);
        con = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        con.close();
    }

    // helpers
    private Destination createDest() {
        return new Destination("North America");
    }

    private Adventure createAdventure() {
        return new Adventure("America", "Pacific Crest Trail", "hiking trail", "6 months", "summer", 1);
    }

    @Test
    public void add() throws Exception {
        Destination testDestination = createDest();
        destinationDao.add(testDestination);
        assertEquals(1, destinationDao.getAllDestinations().size());
    }

    @Test
    public void getAllDestinations() throws Exception {
        Destination testDestination = createDest();
        Destination testDestinationOne = createDest();
        destinationDao.add(testDestination);
        destinationDao.add(testDestinationOne);
        assertEquals(2, destinationDao.getAllDestinations().size());
    }

    @Test
    public void findById() throws Exception {
        Destination testDestination = createDest();
        Destination testDestination2 = createDest();
        Destination testDestination3 = createDest();
        destinationDao.add(testDestination);
        destinationDao.add(testDestination2);
        destinationDao.add(testDestination3);
        Destination foundDest = destinationDao.findById(testDestination3.getId());
        String name = foundDest.getLocation();

        assertEquals(3, foundDest.getId());
        assertEquals("North America", name);
    }

    @Test
    public void updateLocation() throws Exception {
        Destination testDestination = createDest();
        destinationDao.add(testDestination);

        destinationDao.updateLocation(testDestination.getId(), "Africa");

        Destination updatedDestination = destinationDao.findById(testDestination.getId());
        assertEquals("Africa", updatedDestination.getLocation());
    }

    @Test
    public void removeById() throws Exception {
        Destination testDestination = createDest();
        Destination testDestination2 = createDest();
        destinationDao.add(testDestination);
        destinationDao.add(testDestination2);
        destinationDao.removeById(testDestination.getId());
        assertEquals(1, destinationDao.getAllDestinations().size());
    }

    @Test
    public void locationAlreadyExists() throws Exception {
        Destination testDestination = createDest();
        destinationDao.add(testDestination);
        assertEquals(testDestination,  destinationDao.locationAlreadyExists("North America"));
    }
}
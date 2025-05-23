package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;


import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Hello world!
 *
 */
public class App 
{
	
	private static SimpleServer server;

    private static Session session;
    private static SessionFactory sessionFactory = null;

    static SessionFactory getSessionFactory() throws HibernateException {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost/flowers_db");
            configuration.setProperty("hibernate.connection.username", "root");
            configuration.setProperty("hibernate.connection.password", "Asaf8323");
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.hbm2ddl.auto", "create");
            configuration.addAnnotatedClass(Flower.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            return configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }

    public static void main( String[] args ) throws IOException
    {

        try {

            SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        Flower flower_1 = new Flower("Romance Royalty",11.99,"Rose");
        session.save(flower_1);
        session.flush();
        Flower flower_2 = new Flower("Sunny Smiler",6.99,"Sunflower");
        session.save(flower_2);
        session.flush();
        Flower flower_3 = new Flower("Spring's Prince",3.99,"Tulip");
        session.save(flower_3);
        session.flush();
        Flower flower_4 = new Flower("Purple Cloud",8.99,"Jacarande");
        session.save(flower_4);
        session.flush();
        Flower flower_5 = new Flower("Exotic Queenn",9.99,"Orchid");
        session.save(flower_5);
        session.flush();
        session.getTransaction().commit(); // Save everything.

    } catch (Exception exception) {
    if (session != null) {
        session.getTransaction().rollback();
    }
    System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
} finally {
    session.close();
}
        server = new SimpleServer(3000);
        server.listen();
}


    }


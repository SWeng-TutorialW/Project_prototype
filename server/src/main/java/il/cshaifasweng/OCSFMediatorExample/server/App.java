package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;


import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.CartItem;
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
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.addAnnotatedClass(Flower.class);
            configuration.addAnnotatedClass(LoginRegCheck.class);
            configuration.addAnnotatedClass(Complain.class);
            configuration.addAnnotatedClass(Order.class);
            configuration.addAnnotatedClass(CartItem.class);
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
        Flower flower_5 = new Flower("Exotic Queen",9.99,"Orchid");
        session.save(flower_5);
        session.flush();
        Flower flower_6 = new Flower("White Snowflake", 6.49, "Lily");
        session.save(flower_6);
        session.flush();
        Flower flower_7 = new Flower("Golden Breeze", 7.99, "Daffodil");
        session.save(flower_7);
        session.flush();
        Flower flower_8 = new Flower("Blue Whisper", 5.49, "Hyacinth");
        session.save(flower_8);
        session.flush();
        LoginRegCheck Nissim = new  LoginRegCheck("Nissim","123","Nissim@",0,true);
        session.save(Nissim);
        session.flush();
        LoginRegCheck Yarden = new  LoginRegCheck("Yarden","123","Yarden@",0,true);
        session.save(Yarden);
        session.flush();
        LoginRegCheck Itay = new  LoginRegCheck("Itay","123","Itay@",0,true);
        session.save(Itay);
        session.flush();
        LoginRegCheck Diana = new  LoginRegCheck("Diana","123","Diana@",0,true);
        session.save(Diana);
        session.flush();
        LoginRegCheck Dor = new  LoginRegCheck("Dor","123","Dor@",0,true);
        session.save(Dor);
        session.flush();
        LoginRegCheck Asaf = new  LoginRegCheck("Asaf","123","Asaf@",0,true);
        session.save(Asaf);
        session.flush();
        LoginRegCheck tamar = new  LoginRegCheck("tamar","123","Asaf@",0,false);
        session.save(tamar);
        session.flush();




        session.getTransaction().commit();// Save everything.



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


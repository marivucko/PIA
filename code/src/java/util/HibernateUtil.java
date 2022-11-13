/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author user
 */
public class HibernateUtil {
    
        private static  SessionFactory sessionFactory;
    
        static{
            try{
                Configuration cfg=new Configuration().configure("hibernate.cfg.xml");
                StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
                sb.applySettings(cfg.getProperties());
                StandardServiceRegistry sr = sb.build();
                sessionFactory = cfg.buildSessionFactory(sr);

            }catch(HibernateException he){
                System.out.println("HibernateUtil.java: "+he);

            }
        }
        
        public static SessionFactory getSessionFactory() {
        return sessionFactory;
        }

    }
//package hibernate;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//
//import org.junit.jupiter.api.Test;
//
//class JPAConnectionTDD {
//
//	@Test
//	void connect_to_database() {
//		EntityManagerFactory emf = Persistence.createEntityManagerFactory("kryptoczat");
//		EntityManager em = emf.createEntityManager();	
//		assertTrue(em != null);
//	}
//
//}

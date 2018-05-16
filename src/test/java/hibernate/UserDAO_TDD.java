//package hibernate;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import javax.persistence.EntityManager;
//import javax.persistence.NoResultException;
//import javax.persistence.TypedQuery;
//
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import hibernate.dao.JPAConnection;
//import hibernate.entities.User;
//
//class UserDAO_TDD {
//	
//	@Mock
//	private UserService userService;
//
//	@InjectMocks
//	private User user = new User("test user", "test password");
//
//	@Disabled("trzeba to zmockowac")
//	@Test
//	void create_new_user() {
//		JPAConnection.createJPAConnection();
//		EntityManager em = JPAConnection.getJPAEntityManager();
//		em.getTransaction().begin();
//		user = new User("testUser1", "testPassword1");
//
//		em.persist(user);
//		em.getTransaction().commit();
//		em.close();
//	}
//	
//	@Test
//	void check_if_user_does_not_exist() {
//		JPAConnection.createJPAConnection();
//		EntityManager em = JPAConnection.getJPAEntityManager();
//		String userName = "jwdaw123waf532dfa this user should not exist";
//		String jpql = "SELECT u.userName FROM User u WHERE userName = :name";
//		em.getTransaction().begin();
//		TypedQuery<String> query = em.createQuery(jpql, String.class);
//		boolean check = false;
//		query.setParameter("name", userName);
//		try {
//			query.getSingleResult();
//		} catch (NoResultException exc) {
//			check = true;
//		}
//		assertTrue(check);
//	}
//
//	@Test
//	void checkCredentials() {
//		JPAConnection.createJPAConnection();
//		EntityManager em = JPAConnection.getJPAEntityManager();
//		String name = "test";
//		String pass = "pass";
//		String jpql = "SELECT u FROM User u WHERE userName = :name AND password = :password";
//
//		TypedQuery<User> q = em.createQuery(jpql, User.class);
//		q.setParameter("name", name);
//		q.setParameter("password", pass);
//
//		boolean check = false;
//		try {
//			q.getSingleResult();
//		} catch (NoResultException exc) {
//			check = true;
//		}
//		assertTrue(check);
//	}
//
//}

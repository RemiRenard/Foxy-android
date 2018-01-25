package org.foxy.data;

import org.foxy.data.model.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserEntityTest {

    @Test
    public void user_isCorrect() throws Exception {
        User user = new User("myUsername");
        assertEquals(null, user.getEmail());
        assertEquals(null, user.getAvatar());
        assertEquals(null, user.getBirthday());
        assertEquals(null, user.getEmailVerified());
        assertEquals(null, user.getFirstName());
        assertEquals(null, user.getLastName());
        assertEquals(null, user.getId());
        assertEquals("myUsername", user.getUsername());
    }
}
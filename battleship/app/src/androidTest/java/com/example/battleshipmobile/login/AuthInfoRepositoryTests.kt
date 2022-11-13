import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.battleshipmobile.battleship.login.AuthInfoRepositorySharedPrefs
import com.example.battleshipmobile.battleship.service.user.AuthInfo
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthInfoRepositoryTests {

    private val repo by lazy {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        AuthInfoRepositorySharedPrefs(context)
    }

    @Test
    fun setting_to_null_clears_userInfo() {
        repo.authInfo = AuthInfo(1, "testToken")

        assertNotNull(repo.authInfo)

        repo.authInfo = null

        assertNull(repo.authInfo)

    }

}
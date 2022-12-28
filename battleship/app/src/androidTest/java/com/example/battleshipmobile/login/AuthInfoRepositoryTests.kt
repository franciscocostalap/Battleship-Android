import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.battleshipmobile.battleship.auth.AuthInfoService
import com.example.battleshipmobile.battleship.http.SharedPrefsCookieStore
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import okhttp3.Cookie
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthInfoRepositoryTests {

    private val cookieStore = SharedPrefsCookieStore(InstrumentationRegistry.getInstrumentation().targetContext)

    private val repo by lazy {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        AuthInfoService(context,cookieStore)
    }

    @Test
    fun setting_to_null_clears_userInfo() {
        repo.uid = 1

        assertNotNull(repo.uid)

        repo.uid = null

        assertNull(repo.uid)
    }

    @Test
    fun clearValues_clears_cookies_and_uid() {
        repo.uid = 1
        cookieStore["test".toHttpUrl()] = listOf(Cookie.Builder().name("test").value("test").build())
        assert(cookieStore.isNotEmpty())
        assert(repo.uid != null)
        repo.clearAuthInfo()

        assertNull(repo.uid)
        assert(cookieStore.isEmpty())
    }
}
package com.centr.pro.dp.sensor

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume

suspend fun hasf(context: Context): String =
    suspendCancellableCoroutine { continuation ->
        val client = InstallReferrerClient.newBuilder(context).build()
        val isResumed = AtomicBoolean(false)

        continuation.invokeOnCancellation {
            client.endConnection()
        }

        client.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                try {
                    if (!isResumed.compareAndSet(false, true)) return

                    val result =
                        if (responseCode == InstallReferrerClient.InstallReferrerResponse.OK) {
                            client.installReferrer.installReferrer
                        } else {
                            "null"
                        }

                continuation.resume(result)
//                    continuation.resume("utm_source=apps.facebook.com&utm_campaign=fb4a_content=%7B%22app%22%3A%2223623634262346%22%2C%22t%22%3A1777977048%2C%22source%22%3A%7B%22data%22%3A%2282571ce3f38c55b7af498c94f17dae5b88dd1632a46f818d6503c568a769813d31de8fccd3aaa21f933f765ff440b795aaea851f90eb276a2b7cf053110f35dab96c7fca1e7c998a509cb281defb3ea397571a719ea0ba3a0ad440ab2b6e0bdcab7f721a9a3d97f238482a8dc2d62682abd2865bb0a6e6b52c3a6910e87aa59fdd8adbe4b9e1f46629e3d3727dbbef2c576ec1f0e037eda79bd0ce7fc5cac367dc7cafdd645b1fedc87c0a8baa4fbe37e29e16bf3486027cf42dee4d4c6f797f1b5645ddd5c5b597b362f427114bacc4740f40687b7b0245cc830bf20fdaa43b431efb307b63eb449e0b9f8e587b3c01b8132c9c636db3e779171b04fe70b5c87d1b0a1c9d3860e5f8bfdc210e8c8a0a5b6eb83ecc7d5059d8899bbc7afb10f580f19e3f03732c3ff825efe04951da959c67c67a202a76c7a7d1479370008d6615f9b935f12fe206235735027856317a53cad72e15699188cb43c7b7ad02ec3aa7a97d29758c6f5df816539ceae875f2ee929b426f72edcca6fc6d0371539ffb8f31abae7c80fbb9ffcfc6a51a0951320aec379a8ac45e8aedf43babfbba78b30678994e9d90e7ea2e99a695dec9e14a94e926c0c6c6b3f6dec349991b771d3381360b7a13d8c13ff4a22d6d9273b084c2049fe42f9bb52dc62a3f4bd9599016463659c34faa52fbbdd9bbaf54f51eb6544378cf097b497e8ffc4fbbfc7f368559b8c9a48b77c3d5b8c59ea3af62edc3fde757c51374e15ec1f967378fa6fe358c281e71274072ff8d927ba05564c088%22%2C%22nonce%22%3A%22635ebc9285c2236aa87fd649%22%7D%7D")
//                    continuation.resume("cmpgn=aaaa_TEST-Deeplink_bbbb_cccc_dddd")
//                continuation.resume("cmpgn=aaaa_MA-TEST_bbbb_cccc_dddd")

                } catch (_: Exception) {
                    if (isResumed.compareAndSet(false, true)) {
                        continuation.resume("null")
                    }
                } finally {
                    client.endConnection()
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                if (isResumed.compareAndSet(false, true)) {
                    continuation.resume("null")
                }
            }
        })
    }

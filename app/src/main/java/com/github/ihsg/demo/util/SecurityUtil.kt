package com.github.ihsg.demo.util

import java.io.UnsupportedEncodingException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * AES encrypt/decrypt utility
 * Created by hsg on 14/10/2017.
 */
object SecurityUtil {
    private const val CIPHER_MODE = "AES/ECB/PKCS5Padding"
    private const val MASTER_PASSWORD = "Test123454321"

    private fun createKey(password: String?): SecretKeySpec {
        var data: ByteArray? = null
        val sb = StringBuffer(32)
        sb.append(password ?: "")
        while (sb.length < 32) {
            sb.append("0")
        }
        if (sb.length > 32) {
            sb.setLength(32)
        }
        try {
            data = sb.toString().toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return SecretKeySpec(data, "AES")
    }

    private fun encrypt(content: ByteArray, password: String): ByteArray {
        val key = createKey(password)
        val cipher = Cipher.getInstance(CIPHER_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher.doFinal(content)
    }

    fun encrypt(content: String, password: String = MASTER_PASSWORD): String {
        var data = content.toByteArray(charset("UTF-8"))
        data = encrypt(data, password)
        return byte2hex(data)
    }

    private fun decrypt(content: ByteArray, password: String): ByteArray {
        val key = createKey(password)
        val cipher = Cipher.getInstance(CIPHER_MODE)
        cipher.init(Cipher.DECRYPT_MODE, key)
        return cipher.doFinal(content)
    }

    fun decrypt(content: String, password: String = MASTER_PASSWORD): String? {
        var data = hex2byte(content)
        data = decrypt(data, password)
        return data.toString(charset("UTF-8"))
    }

    private fun byte2hex(b: ByteArray): String { // 一个字节的数，
        val sb = StringBuffer(b.size * 2)
        var tmp: String
        for (n in b.indices) { // 整数转成十六进制表示
            tmp = Integer.toHexString(b[n].toInt() and 0XFF)
            if (tmp.length == 1) {
                sb.append("0")
            }
            sb.append(tmp)
        }
        return sb.toString().toUpperCase(Locale.ROOT) // 转成大写
    }

    private fun hex2byte(inputString: String): ByteArray {
        if (inputString.length < 2) {
            return ByteArray(0)
        }
        val str = inputString.toLowerCase(Locale.ROOT)
        val l = inputString.length / 2
        val result = ByteArray(l)
        for (i in 0 until l) {
            val tmp = str.substring(2 * i, 2 * i + 2)
            result[i] = (tmp.toInt(16) and 0xFF).toByte()
        }
        return result
    }
}
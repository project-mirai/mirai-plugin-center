/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.services

import net.mamoe.mirai.plugincenter.utils.setonly
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import java.util.*

@Component
class MailService {

    @Autowired
    lateinit var template: SpringTemplateEngine

    @Autowired
    lateinit var sender: JavaMailSender

    @Value("\${mail.fromMail.addr}")
    lateinit var from: String

    inner class NewMail(
        private val msg: MimeMessageHelper
    ) {
        var subject by setonly<String> { msg.setSubject(it) }
        var to by setonly<String> { msg.setTo(it) }
        var text by setonly<String> { msg.setText(it, false) }
        var html by setonly<String> { msg.setText(it, true) }

        fun send() {
            sender.send(msg.mimeMessage)
        }
    }

    class MailTemplate(
        val variables: MutableMap<String, Any>
    ) {
        fun greetingWithName(value: String) {
            variables["greeting_with_name"] = value
        }

        fun subject(value: String) {
            variables["header"] = value
        }

        fun header(value: String) {
            variables["line0"] = value
        }

        fun content(value: String) {
            variables["line1"] = value
        }

        fun extContent(value: String) {
            variables["line2"] = value
        }

        fun footer(value: String) {
            variables["footer"] = value
        }
    }

    @Value("\${mail.footer}")
    var stdFooter = "Footer"

    fun mailTemplate(action: MailTemplate.() -> Unit): String {
        val variables = mutableMapOf<String, Any>(
            "greeting_with_name" to "",
            "header" to "",
            "line0" to "",
            "line1" to "",
            "line2" to "",
            "footer" to stdFooter,
            "site_title" to "Mirai Plugin Center",
        )
        MailTemplate(variables).action()
        val context = Context(Locale.getDefault(), variables)
        return template.process("mail.html", context)
    }

    fun newMsg() = NewMail(sender.createMimeMessage().let { msg ->
        msg.setFrom(from)
        MimeMessageHelper(msg)
    })

    fun sendMsg(action: NewMail.() -> Unit) {
        newMsg().also(action).send()
    }
}

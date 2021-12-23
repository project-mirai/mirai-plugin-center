/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.utils.validate

import net.mamoe.mirai.plugincenter.model.PermissionEntity
import net.mamoe.mirai.plugincenter.model.UserEntity
import org.intellij.lang.annotations.Language

private typealias Validator = () -> ValidatorBuilder.Result

open class ValidatorBuilder(val validationName: String = "Validation") {
    companion object {
        fun Boolean.toResult(message: String): Result {
            return if (this) {
                Result.Success
            } else {
                Result.Failed(message)
            }
        }
    }

    sealed class Result {
        object Success : Result()
        data class Failed(val message: String) : Result()

        fun throwIfFailed() {
            when (this) {
                is Failed -> throw IllegalArgumentException(this.message)
                Success -> Unit
            }
        }
    }

    class StringValidatorBuilder(val string: String) : ValidatorBuilder("String Validation") {
        fun match(regex: Regex): Validator = +{
            regex.matches(string).toResult("the string '$this' doesn't match the regex: ${regex.pattern}")
        }

        fun match(@Language("RegExp") regexStr: String): Validator = match(Regex(regexStr))
    }

    private val requirements: MutableList<Validator> = mutableListOf()

    /**
     * A simple usage for `+requires(str) { match(regex) }`
     */
    infix fun String.matches(regex: Regex): Validator = +StringValidatorBuilder(this).match(regex)

    /**
     * A simple usage for `+requires(str) { match(regexStr) }`
     */
    infix fun String.matches(@Language("RegExp") regexStr: String): Validator = +StringValidatorBuilder(this).match(regexStr)

    /**
     * Short Circuit OR for Validators
     */
    infix fun Validator.or(that: Validator): Validator {
        val last1 = this@ValidatorBuilder.requirements.removeLast()  // should equals `that`
        val last2 = this@ValidatorBuilder.requirements.removeLast()  // should equals `this`

        requires {
            assert(last1 === that)
            assert(last2 === this@or)
        }

        return +{
            when (val result = last2.invoke()) {
                is Result.Failed -> last1.invoke()
                Result.Success -> result
            }
        }
    }

    fun assert(condition: Boolean): Validator = +{
        condition.toResult("assertion failed: $condition === true")
    }

    /**
     * Add a Validator to this builder
     */
    operator fun Validator.unaryPlus(): Validator {
        requirements.add(this@unaryPlus)

        return this@unaryPlus
    }

    infix fun UserEntity.can(permit: PermissionEntity): Validator = assert(this@can.hasPermit(permit))

    /**
     * Make a subrequirement for string.
     */
    inline fun requires(string: String, block: StringValidatorBuilder.() -> Unit): Validator {
        return +StringValidatorBuilder(string).apply {
            block()
        }.build()
    }

    fun build(): Validator = {
        val errors = requirements.mapNotNull {
            when (val result = it.invoke()) {
                is Result.Failed -> result.message
                Result.Success -> null
            }
        }

        if (errors.isEmpty()) {
            Result.Success
        } else {
            val errMsg = errors.joinToString(separator = "\n")

            Result.Failed("$validationName Failed:\n$errMsg")
        }
    }
}

inline fun requires(block: ValidatorBuilder.() -> Unit) {
    return ValidatorBuilder().apply {
        block()
    }.build().invoke().throwIfFailed()
}
/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import net.mamoe.mirai.plugincenter.dto.ApiResp
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Api
@RequestMapping("/v1")
class APITest {
    // api 文档地址 http://localhost:8080/api/swagger-ui/index.html
    @GetMapping("/aaa")
    @ApiOperation(value = "获取数据")
    suspend fun test(@RequestParam @ApiParam("这是说明信息",required = true) param :Int):ApiResp<Int>{
       return ApiResp(param)
    }
}

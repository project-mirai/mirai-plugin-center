package net.mamoe.mirai.plugincenter.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Api
@RequestMapping("/api/v1")
class APITest {
    // api 文档地址 http://localhost:8080/api/swagger-ui/index.html
    @GetMapping("/aaa")
    @ApiOperation(value = "获取数据")
    @ApiResponse(code = 200, message = "正常")
    suspend fun test(@RequestParam @ApiParam("这是说明信息",required = true) param :Int):String{
        return "success"
    }
}

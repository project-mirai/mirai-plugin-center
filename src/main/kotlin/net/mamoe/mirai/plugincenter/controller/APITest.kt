package net.mamoe.mirai.plugincenter.controller

import io.swagger.annotations.*
import org.springframework.web.bind.annotation.*

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

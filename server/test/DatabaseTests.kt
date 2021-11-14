package net.mamoe.mirai.plugincenter

import net.mamoe.mirai.plugincenter.repo.RolePermissionRepo
import net.mamoe.mirai.plugincenter.repo.RoleRepo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class DatabaseTests {
    @Autowired
    lateinit var rolePermissionRepo: RolePermissionRepo

    @Autowired
    lateinit var roleRepo: RoleRepo

    @Test
    @Transactional
    fun roleRepoTest() {
        val roles = roleRepo.findAll()

        for (role in roles) {
            println("${role.id} ${role.name} ${role.owner.uid} ${role.permissionSet} ${role.log?.id}")
        }
    }
}
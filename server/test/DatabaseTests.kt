package net.mamoe.mirai.plugincenter

import net.mamoe.mirai.plugincenter.repo.RoleRepo
import net.mamoe.mirai.plugincenter.repo.UserRepo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DatabaseTests {
    @Autowired
    lateinit var userRepo: UserRepo

    @Autowired
    lateinit var roleRepo: RoleRepo

    @Test
    fun roleRepoTest() {
        val roles = roleRepo.findAll()

        for (role in roles) {
            println("${role.id} ${role.name} ${role.owner.uid} ${role.permissionSet} ${role.log?.id}")
        }
    }

    @Test
    fun userRolesTest() {
        val user = userRepo.findById(31).get()

        for (role in user.rolesByUid) {
            println("${role.role.id} ${role.role.name}")
        }
    }
}
<template>
  <div>
    <user-info v-if="logon"></user-info>
    <need-login v-else></need-login>
  </div>
</template>

<script>
import NeedLogin from "./UserInfo/NeedLogin";
import UserInfo from "./UserInfo";
export default {
  name: "Drawer",
  components: {UserInfo, NeedLogin},
  data() {
    return {
      logon: false,
    }
  }
}
</script>

<style scoped>

</style>

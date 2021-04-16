<template>
  <div>
    <user-info v-if="logon"></user-info>
    <need-login v-else></need-login>
    <router-list></router-list>
  </div>
</template>

<script>
import NeedLogin from "./UserInfo/NeedLogin";
import UserInfo from "./UserInfo/UserInfo";
import RouterList from './RouterList.vue';
export default {
  name: "Drawer",
  components: {NeedLogin, RouterList, UserInfo},
  data() {
    return {
      logon: false,
    }
  }
}
</script>

<style scoped>

</style>

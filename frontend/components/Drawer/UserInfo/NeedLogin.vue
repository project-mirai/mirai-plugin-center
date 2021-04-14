<template>
  <v-list>
    <v-list-item>
      <v-list-item-content>
        <v-list-item-title class="title">
          未登录
        </v-list-item-title>
        <v-list-item-subtitle>登陆后访问更多功能</v-list-item-subtitle>
      </v-list-item-content>

      <v-list-item-action>
        <!--
        <authorization-dialog @click="$emit('click')"></authorization-dialog>
        -->
        <auth-dialog></auth-dialog>
      </v-list-item-action>
    </v-list-item>
  </v-list>
</template>

<script>
import AuthDialog from "./AuthDialog";
export default {
  name: "NeedLogin",
  components: {AuthDialog}
}
</script>

<style scoped>

</style>
